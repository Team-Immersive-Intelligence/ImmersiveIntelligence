package pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.DiplomaticAction;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.*;

/**
 * A diplomatic proposal or active pact between two factions.
 * <ul>
 * <li>Type: {@link DiplomaticAction#SEND_OFFER} or {@link DiplomaticAction#SEND_ULTIMATUM}.</li>
 * <li>Terms: relation change, granted permissions, chunk/property transfers.</li>
 * <li>Status: PENDING, ACCEPTED, REJECTED.</li>
 * </ul>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.04.2026
 */
public class DiplomaticAgreement
{
	private UUID id;
	private final UUID source;
	private final UUID target;
	//Offer or Ultimatum
	private DiplomaticAction type;
	private long creationTime;
	// -1 = permanent after acceptance; otherwise proposal expires
	private long expirationTime;
	private AgreementStatus status = AgreementStatus.PENDING;

	// Terms that will be applied when the agreement becomes active
	private final List<ITerm> terms = new ArrayList<>();

	// After acceptance, the active agreement holds these resolved values for quick lookup
	@Nullable
	private DiplomaticStatus activeRelationChange;
	private final Map<PermissionCategory, Boolean> grantedPermissions = new EnumMap<>(PermissionCategory.class);
	private final List<UUID> transferredProperties = new ArrayList<>();

	public DiplomaticAgreement(UUID source, UUID target, DiplomaticAction type, long creationTime, long expirationTime)
	{
		this.id = UUID.randomUUID();
		this.source = source;
		this.target = target;
		this.type = type;
		this.creationTime = creationTime;
		this.expirationTime = expirationTime;
	}

	public DiplomaticAgreement(EasyNBT tag)
	{
		this.id = UUID.fromString(tag.getString("id"));
		this.status = AgreementStatus.valueOf(tag.getString("status"));
		this.source = tag.getUUID("source");
		this.target = tag.getUUID("target");
		this.type = tag.getEnum("type", DiplomaticAction.class);
		this.creationTime = tag.getLong("creationTime");
		this.expirationTime = tag.getLong("expirationTime");

		EasyNBT termsTag = tag.getEasyCompound("terms");
		for(String key : termsTag.asMap().keySet())
		{
			EasyNBT termNBT = termsTag.getEasyCompound(key);
			this.terms.add(TermFactory.fromNBT(termNBT));
		}

		//Resolve data if active
		if(tag.getBoolean("active"))
		{
			this.status = AgreementStatus.ACCEPTED;
			if(tag.hasKey("relationChange"))
				this.activeRelationChange = tag.getEnum("relationChange", DiplomaticStatus.class);
			EasyNBT perms = tag.getEasyCompound("grantedPermissions");
			for(PermissionCategory cat : PermissionCategory.values())
				if(perms.hasKey(cat.getName()))
					this.grantedPermissions.put(cat, perms.getBoolean(cat.getName()));
			this.transferredProperties.clear();
			tag.streamList(NBTTagCompound.class, "transferredProperties")
					.map(EasyNBT::wrapNBT)
					.map(t -> UUID.fromString(t.getString("uuid")))
					.forEach(this.transferredProperties::add);
		}
	}

	// --- Term handling ---
	public void addTerm(ITerm term)
	{
		terms.add(term);
	}

	public List<ITerm> getTerms()
	{
		return Collections.unmodifiableList(terms);
	}

	// --- Acceptance ---
	public void accept()
	{
		this.status = AgreementStatus.ACCEPTED;
		// Resolve all terms
		for(ITerm term : terms)
			term.apply(this);
	}

	public void reject()
	{
		this.status = AgreementStatus.REJECTED;
	}

	public boolean isActive()
	{
		return status==AgreementStatus.ACCEPTED;
	}

	public boolean isPending()
	{
		return status==AgreementStatus.PENDING;
	}

	// --- Getters ---
	public UUID getId()
	{
		return id;
	}

	public UUID getSourceFaction()
	{
		return source;
	}

	public UUID getTargetFaction()
	{
		return target;
	}

	public DiplomaticAction getType()
	{
		return type;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public long getExpirationTime()
	{
		return expirationTime;
	}

	public boolean isExpired(long currentTime)
	{
		return expirationTime!=-1&&currentTime > expirationTime;
	}

	@Nullable
	public DiplomaticStatus getActiveRelationChange()
	{
		return activeRelationChange;
	}

	public Map<PermissionCategory, Boolean> getGrantedPermissions()
	{
		return Collections.unmodifiableMap(grantedPermissions);
	}

	public List<UUID> getTransferredProperties()
	{
		return Collections.unmodifiableList(transferredProperties);
	}

	public void setActiveRelationChange(@Nullable DiplomaticStatus rel)
	{
		this.activeRelationChange = rel;
	}

	void addGrantedPermission(PermissionCategory cat, boolean val)
	{
		grantedPermissions.put(cat, val);
	}

	void addTransferredProperty(UUID propId)
	{
		transferredProperties.add(propId);
	}

	// --- NBT ---
	public EasyNBT toNBT()
	{
		EasyNBT tag = EasyNBT.newNBT();
		tag.withString("id", id.toString());
		tag.withUUID("source", source);
		tag.withUUID("target", target);
		tag.withEnum("type", type);
		tag.withLong("creationTime", creationTime);
		tag.withLong("expirationTime", expirationTime);
		tag.withString("status", status.name());

		// Terms
		EasyNBT termsList = EasyNBT.newNBT();
		int i = 0;
		for(ITerm term : terms)
			termsList.withTag(String.valueOf(i++), term.toNBT());
		tag.withTag("terms", termsList);

		// Resolved data (only saved when active to keep NBT consistent)
		tag.withBoolean("active", isActive());
		if(isActive())
		{
			if(activeRelationChange!=null)
				tag.withEnum("relationChange", activeRelationChange);
			EasyNBT perms = EasyNBT.newNBT();
			grantedPermissions.forEach((cat, val) -> perms.withBoolean(cat.getName(), val));
			tag.withTag("grantedPermissions", perms);
			tag.withList("transferredProperties", uuid -> EasyNBT.newNBT().withString("uuid", uuid.toString()).unwrap(), transferredProperties);
		}
		return tag;
	}

	// --- Term interface ---
	public interface ITerm
	{
		void apply(DiplomaticAgreement agreement);

		EasyNBT toNBT();
	}

	public enum AgreementStatus
	{
		PENDING,
		ACCEPTED,
		REJECTED
	}
}
