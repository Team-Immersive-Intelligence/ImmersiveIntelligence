package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term.DiplomaticAgreement;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents the identity of an owner (single or group of owners) of a tile entity or entity.
 * <p>
 * Members are stored by UUID and assigned a {@link PermissionRole}. Ownership is determined
 * solely by the role's {@link PermissionRole#isOwner()} flag – there is no separate owner list.
 * </p>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 22.07.2026
 * @since 03.09.2025
 */
public class OwnerIdentity implements INBTSerializable<NBTTagCompound>
{
	private final UUID uuid;
	private String displayName;
	private final Map<UUID, String> memberRoles = new HashMap<>();
	private final Map<String, PermissionRole> availableRoles = new LinkedHashMap<>();
	private final Map<UUID, DiplomaticStatus> relations = new HashMap<>();
	private String startingMemberRole = LawForm.DEFAULT.getStartingRole();
	private LawForm lawForm = LawForm.DEFAULT;
	private IIColor color = IIColor.ALPHA;
	private ItemStack banner = new ItemStack(Items.BANNER);
	/**
	 * Total world time at which this identity was founded; legacy identities use tick zero.
	 */
	private long foundingDate;
	private boolean invalid;

	//--- Diplomacy (agreements) ---//
	private final Map<UUID, DiplomaticAgreement> activeAgreementsAsGrantor = new HashMap<>();
	private final Map<UUID, DiplomaticAgreement> activeAgreementsAsTarget = new HashMap<>();
	private final Map<UUID, DiplomaticAgreement> pendingOutgoingProposals = new HashMap<>();
	private final Map<UUID, DiplomaticAgreement> pendingIncomingProposals = new HashMap<>();

	//--- Invitations ---//
	private final Set<UUID> invitedPlayers = new HashSet<>();

	//--- Constructors ---//
	protected OwnerIdentity(@Nonnull UUID uuid)
	{
		this(uuid, "placeholder", 0);
	}

	protected OwnerIdentity(@Nonnull UUID uuid, @Nonnull String displayName)
	{
		this(uuid, displayName, 0);
	}

	protected OwnerIdentity(@Nonnull UUID uuid, @Nonnull String displayName, long foundingDate)
	{
		this.uuid = uuid;
		this.displayName = displayName;
		this.foundingDate = Math.max(0, foundingDate);
		this.invalid = true;
		initAvailableRoles();
	}

	public OwnerIdentity(EntityLivingBase player)
	{
		this(UUID.randomUUID(), player.getName(), player.world.getTotalWorldTime());
		this.withMember(player.getUniqueID(), LawForm.DEFAULT.getOwnerRole());
		this.color = IIColor.fromHSV(player.getRNG().nextFloat(), 0.35f, 0.85f);
		this.banner = new ItemStack(Items.BANNER, 1, color.getDyeColor().getMetadata());
	}

	public OwnerIdentity(OwnerIdentity a, OwnerIdentity b)
	{
		this(a, b, Math.max(a.foundingDate, b.foundingDate));
	}

	public OwnerIdentity(OwnerIdentity a, OwnerIdentity b, long foundingDate)
	{
		this(UUID.randomUUID(), a.getDisplayName()+"-"+b.getDisplayName(), foundingDate);
		this.invalid = false;
		//Mix colors and make a banner
		this.color = a.color.mixedWith(b.color, 0.5f);
		this.banner = new ItemStack(Items.BANNER, 1, color.getDyeColor().getMetadata());
		initAvailableRoles();

		//Merge members
		for(UUID uuid : a.memberRoles.keySet())
			this.memberRoles.put(uuid, getStartingMemberRole());
		for(UUID uuid : b.memberRoles.keySet())
			this.memberRoles.put(uuid, getStartingMemberRole());
		a.invalid = true;
		b.invalid = true;
	}

	public OwnerIdentity(EasyNBT tag)
	{
		UUID loadedUUID = tag.getUUID("uuid");
		if(this.invalid = tag.getBoolean("invalid")||loadedUUID==null)
		{
			this.uuid = loadedUUID==null?UUID.randomUUID(): loadedUUID;
			this.displayName = "invalid";
			this.foundingDate = Math.max(0, tag.getLong("foundingDate"));
			initAvailableRoles();
		}
		else
		{
			this.uuid = loadedUUID;
			this.displayName = tag.getString("displayName");
			deserializeNBT(tag.unwrap());
		}
	}

	private void initAvailableRoles()
	{
		this.availableRoles.clear();
		for(PermissionRole role : lawForm.getDefaultRoles())
			this.availableRoles.put(role.getId(), role);
		this.startingMemberRole = lawForm.getStartingRole();
	}

	//--- Builder-style setters ---//

	public OwnerIdentity withDisplayName(String displayName)
	{
		this.displayName = displayName;
		return this;
	}

	public OwnerIdentity withStartingMemberRole(String startingMemberRole)
	{
		this.startingMemberRole = startingMemberRole;
		return this;
	}

	/**
	 * Adds a member with the given role. If the member is already present, updates their role.
	 *
	 * @param uuid   player UUID
	 * @param roleId id of the role (must exist in availableRoles)
	 */
	public OwnerIdentity withMember(UUID uuid, String roleId)
	{
		if(availableRoles.containsKey(roleId))
		{
			memberRoles.put(uuid, roleId);
			invalid = false;
		}
		return this;
	}

	public OwnerIdentity removeMember(UUID uuid)
	{
		memberRoles.remove(uuid);
		//Remove
		if(memberRoles.isEmpty())
			this.disband();
		return this;
	}

	private void disband()
	{
		disband(true);
	}

	private void disband(boolean sync)
	{
		this.invalid = true;
		this.displayName = "invalid";

		this.memberRoles.clear();
		this.availableRoles.clear();
		this.relations.clear();
		this.activeAgreementsAsGrantor.clear();
		this.activeAgreementsAsTarget.clear();
		this.pendingOutgoingProposals.clear();
		this.pendingIncomingProposals.clear();
		this.invitedPlayers.clear();

		if(sync)
			IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(this));
	}

	boolean removeMemberForIntegrityCheck(UUID uuid)
	{
		return memberRoles.remove(uuid)!=null;
	}

	void invalidateForIntegrityCheck()
	{
		disband(false);
	}

	public OwnerIdentity withLawForm(LawForm lawForm)
	{
		this.lawForm = lawForm;
		initAvailableRoles();
		return this;
	}

	public OwnerIdentity withColor(IIColor color)
	{
		this.color = color;
		return this;
	}

	public OwnerIdentity withBanner(@Nonnull ItemStack banner)
	{
		if(banner.getItem() instanceof ItemBanner)
			this.banner = banner;
		return this;
	}

	//--- Getters ---//

	public String getStringUUID()
	{
		return uuid.toString();
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public long getFoundingDate()
	{
		return foundingDate;
	}

	public Map<UUID, String> getMemberRolesMap()
	{
		return Collections.unmodifiableMap(memberRoles);
	}

	public Set<UUID> getMembers()
	{
		return Collections.unmodifiableSet(memberRoles.keySet());
	}

	public boolean isMember(UUID uuid)
	{
		return memberRoles.containsKey(uuid);
	}

	public String getStartingMemberRole()
	{
		return startingMemberRole;
	}

	public ItemStack getBanner()
	{
		return banner;
	}

	public IIColor getColor()
	{
		return color;
	}

	public LawForm getLawForm()
	{
		return lawForm;
	}

	public boolean isInvalid()
	{
		return invalid;
	}

	public boolean hasAnyPlayers()
	{
		return !memberRoles.isEmpty();
	}

	//--- Role access ---//

	@Nullable
	public PermissionRole getRoleOf(UUID playerUUID)
	{
		String roleId = memberRoles.get(playerUUID);
		return roleId!=null?availableRoles.get(roleId): null;
	}

	public Map<String, PermissionRole> getAvailableRoles()
	{
		return Collections.unmodifiableMap(availableRoles);
	}

	/**
	 * Returns true if the player holds a role marked as owner.
	 */
	public boolean isOwner(UUID playerUUID)
	{
		PermissionRole role = getRoleOf(playerUUID);
		return role!=null&&role.isOwner();
	}

	//--- Permission check (role‑based) ---//

	/**
	 * Permission logic:
	 * <ol>
	 *   <li>Always allow actions done to the neutral faction and ones not requiring any permission.</li>
	 *   <li>If the entity is an owner (role.isOwner() == true) → allow everything.</li>
	 *   <li>If the entity is a member and their role explicitly allows the category → allow.</li>
	 *   <li>If this faction has granted the category to the entity's faction via an active agreement → allow.</li>
	 *   <li>Otherwise deny.</li>
	 * </ol>
	 */
	public boolean isPermitted(@Nonnull EntityLivingBase entity, @Nullable PermissionCategory category)
	{
		if(category==null||this.uuid==DiplomacyHandler.NEUTRAL_UUID)
			return true;

		UUID uuid = entity.getUniqueID();
		OwnerIdentity otherIdentity = DiplomacyHandler.getInstance(entity.world.isRemote).getOwnerIdentityForEntity(entity);

		if(isOwner(uuid))
			return true;

		PermissionRole role = getRoleOf(uuid);
		if(role!=null&&role.isAllowed(category))
			return true;

		DiplomaticAgreement grantorAgreement = activeAgreementsAsGrantor.get(otherIdentity.getUUID());
		return grantorAgreement!=null&&grantorAgreement.getGrantedPermissions().getOrDefault(category, false);
	}

	//--- Agreement management ---//

	public void addGrantorAgreement(DiplomaticAgreement agreement)
	{
		if(agreement.isActive())
			activeAgreementsAsGrantor.put(agreement.getTargetFaction(), agreement);
		else
			pendingOutgoingProposals.put(agreement.getTargetFaction(), agreement);
	}

	public void addTargetAgreement(DiplomaticAgreement agreement)
	{
		if(agreement.isActive())
			activeAgreementsAsTarget.put(agreement.getSourceFaction(), agreement);
		else
			pendingIncomingProposals.put(agreement.getSourceFaction(), agreement);
	}

	public void removeAgreement(DiplomaticAgreement agreement)
	{
		activeAgreementsAsGrantor.remove(agreement.getTargetFaction());
		activeAgreementsAsTarget.remove(agreement.getSourceFaction());
		pendingOutgoingProposals.remove(agreement.getTargetFaction());
		pendingIncomingProposals.remove(agreement.getSourceFaction());
	}

	//--- Invitations (for external players) ---//
	public void invitePlayer(UUID playerUUID)
	{
		invitedPlayers.add(playerUUID);
	}

	public boolean isInvited(UUID playerUUID)
	{
		return invitedPlayers.contains(playerUUID);
	}

	public void removeInvitation(UUID playerUUID)
	{
		invitedPlayers.remove(playerUUID);
	}

	public Set<UUID> getInvitedPlayers()
	{
		return Collections.unmodifiableSet(invitedPlayers);
	}

	//--- Relations ---//
	public boolean isMember(EntityLivingBase entityLivingBase)
	{
		return isMember(entityLivingBase.getUniqueID());
	}

	public boolean isHostile(@Nonnull EntityLivingBase entity)
	{
		return getRelationTowards(entity)==DiplomaticStatus.ENEMY;
	}

	public boolean isAllied(@Nonnull EntityLivingBase entity)
	{
		return getRelationTowards(entity).atLeast(DiplomaticStatus.ALLIED);
	}

	@Nonnull
	public DiplomaticStatus getRelationTowards(@Nonnull EntityLivingBase entity)
	{
		OwnerIdentity other = DiplomacyHandler.getInstance(entity.world.isRemote).getOwnerIdentityForEntity(entity);
		return getRelationTowards(other);
	}

	@Nonnull
	public DiplomaticStatus getRelationTowards(@Nonnull OwnerIdentity other)
	{
		if(other.getUUID().equals(this.uuid))
			return DiplomaticStatus.MEMBER;
		return relations.getOrDefault(other.getUUID(), DiplomaticStatus.NEUTRAL);
	}

	public void setRelation(OwnerIdentity other, DiplomaticStatus status)
	{
		relations.put(other.getUUID(), status);
	}

	@Nullable
	public EntityLivingBase getFirstResponsibleMember(World world)
	{
		//The so called "Odpowiedzialność zbiorowa"...
		for(UUID uuid : memberRoles.keySet())
		{
			EntityPlayer player = world.getPlayerEntityByUUID(uuid);
			if(player!=null) return player;
		}
		return null;
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		if(invalid)
			return EasyNBT.newNBT()
					.withUUID("uuid", uuid)
					.withString("displayName", displayName)
					.withLong("foundingDate", foundingDate)
					.withBoolean("invalid", true)
					.unwrap();

		EasyNBT rolesTag = EasyNBT.newNBT();
		memberRoles.forEach((uuid, roleId) -> rolesTag.withString(uuid.toString(), roleId));

		EasyNBT availRolesTag = EasyNBT.newNBT();
		availableRoles.forEach((id, role) -> availRolesTag.withTag(id, role.toNBT()));

		EasyNBT relationsTag = EasyNBT.newNBT();
		relations.forEach((otherUuid, status) -> relationsTag.withEnum(otherUuid.toString(), status));

		EasyNBT grantorTag = saveAgreementMap(activeAgreementsAsGrantor);
		EasyNBT targetTag = saveAgreementMap(activeAgreementsAsTarget);
		EasyNBT outTag = saveAgreementMap(pendingOutgoingProposals);
		EasyNBT inTag = saveAgreementMap(pendingIncomingProposals);

		List<String> invitedList = new ArrayList<>();
		invitedPlayers.forEach(uuid -> invitedList.add(uuid.toString()));

		return EasyNBT.newNBT()
				.withUUID("uuid", uuid)
				.withString("displayName", displayName)
				.withLong("foundingDate", foundingDate)
				.withTag("memberRoles", rolesTag)
				.withTag("availableRoles", availRolesTag)
				.withEnum("lawForm", lawForm)
				.withColor("color", color)
				.withItemStack("banner", banner)
				.withTag("relations", relationsTag)
				.withTag("activeGrantorAgreements", grantorTag)
				.withTag("activeTargetAgreements", targetTag)
				.withTag("pendingOutgoing", outTag)
				.withTag("pendingIncoming", inTag)
				.withList("invitedPlayers", invitedList)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);
		memberRoles.clear();
		availableRoles.clear();
		relations.clear();
		activeAgreementsAsGrantor.clear();
		activeAgreementsAsTarget.clear();
		pendingOutgoingProposals.clear();
		pendingIncomingProposals.clear();
		invitedPlayers.clear();
		foundingDate = Math.max(0, enbt.getLong("foundingDate"));

		if(enbt.getBoolean("invalid"))
		{
			invalid = true;
			return;
		}
		invalid = false;

		EasyNBT rolesTag = enbt.getEasyCompound("memberRoles");
		for(String key : rolesTag.asMap().keySet())
		{
			UUID uuid = UUID.fromString(key);
			String roleId = ((NBTTagString)rolesTag.asMap().get(key)).getString();
			memberRoles.put(uuid, roleId);
		}

		lawForm = enbt.getEnum("lawForm", LawForm.class);
		initAvailableRoles();
		EasyNBT availTag = enbt.getEasyCompound("availableRoles");
		for(String roleId : availTag.asMap().keySet())
		{
			PermissionRole role = PermissionRole.fromNBT(availTag.getEasyCompound(roleId));
			availableRoles.put(role.getId(), role);
		}

		color = enbt.getColor("color");
		banner = enbt.getItemStack("banner");
		displayName = enbt.getString("displayName");

		EasyNBT relationsTag = enbt.getEasyCompound("relations");
		for(String key : relationsTag.asMap().keySet())
		{
			UUID otherUuid = UUID.fromString(key);
			DiplomaticStatus status = relationsTag.getEnum(key, DiplomaticStatus.class);
			relations.put(otherUuid, status);
		}

		loadAgreementMap(enbt.getEasyCompound("activeGrantorAgreements"), activeAgreementsAsGrantor);
		loadAgreementMap(enbt.getEasyCompound("activeTargetAgreements"), activeAgreementsAsTarget);
		loadAgreementMap(enbt.getEasyCompound("pendingOutgoing"), pendingOutgoingProposals);
		loadAgreementMap(enbt.getEasyCompound("pendingIncoming"), pendingIncomingProposals);

		enbt.streamList(NBTTagString.class, "invitedPlayers")
				.map(NBTTagString::getString)
				.map(UUID::fromString)
				.forEach(invitedPlayers::add);
	}

	private EasyNBT saveAgreementMap(Map<UUID, DiplomaticAgreement> map)
	{
		EasyNBT tag = EasyNBT.newNBT();
		int i = 0;
		for(DiplomaticAgreement ag : map.values())
			tag.withTag(String.valueOf(i++), ag.toNBT());
		return tag;
	}

	private void loadAgreementMap(EasyNBT tag, Map<UUID, DiplomaticAgreement> target)
	{
		if(tag==null) return;
		for(Object obj : tag.asMap().values())
		{
			NBTTagCompound comp = (NBTTagCompound)obj;
			DiplomaticAgreement ag = new DiplomaticAgreement(EasyNBT.wrapNBT(comp));
			// Use the agreement's source/target UUID to key
			if(ag.getSourceFaction().equals(this.uuid))
				target.put(ag.getTargetFaction(), ag);
			else
				target.put(ag.getSourceFaction(), ag);
		}
	}


	//--- Equals & Hashcode ---//
	@Override
	public boolean equals(@Nullable Object o)
	{
		if(this==o)
			return true;
		if(o==null||getClass()!=o.getClass())
			return false;
		return uuid.equals(((OwnerIdentity)o).uuid);
	}

	@Override
	public int hashCode()
	{
		return uuid.hashCode();
	}

	@Override
	public String toString()
	{
		return "OwnerIdentity:"+displayName+"("+uuid+")";
	}
}
