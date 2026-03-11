package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Represents the identity of an owner (single or group of owners) of a tile entity or entity.
 * An owner can be a single player, a group of player, a team, a group of teams and any combination of it.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public class OwnerIdentity
{
	private static final EnumMap<PermissionCategory, PermissionLevel> DEFAULT_LEVELS = new EnumMap<>(PermissionCategory.class);

	static
	{
		DEFAULT_LEVELS.put(PermissionCategory.DISBAND, PermissionLevel.OWNER_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.MERGE, PermissionLevel.OWNER_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.ADD_MEMBERS, PermissionLevel.OWNER_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.REMOVE_MEMBERS, PermissionLevel.OWNER_ALLOW);

		DEFAULT_LEVELS.put(PermissionCategory.MILITARY_AID, PermissionLevel.ALLIES_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.TRANSIT, PermissionLevel.OTHERS_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.TRADE, PermissionLevel.OTHERS_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.RESEARCH, PermissionLevel.MEMBER_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.LOGISTICS, PermissionLevel.MEMBER_ALLOW);
		DEFAULT_LEVELS.put(PermissionCategory.CONTAINER_ACCESS, PermissionLevel.OTHERS_ALLOW);
	}

	//Publicly displayed name
	private final String displayName;
	//Members
	private final List<String> owners = new ArrayList<>();
	private final List<String> players = new ArrayList<>();
	//Permissions
	private final EnumMap<PermissionCategory, PermissionLevel> permissions = new EnumMap<>(PermissionCategory.class);
	private final HashMap<OwnerIdentity, DiplomaticStatus> relations = new HashMap<>();
	private LawForm lawForm = LawForm.DEFAULT;
	//Presentation
	private IIColor color = IIColor.ALPHA;
	private ItemStack banner = new ItemStack(Items.BANNER);
	private boolean invalid;

	protected OwnerIdentity(String displayName)
	{
		this.displayName = displayName;
		this.invalid = true;
	}

	public OwnerIdentity(EntityLivingBase player)
	{
		this(player.getName());
		this.withMember(player, true);
		this.color = IIColor.fromHSV(player.getRNG().nextFloat(), 0.35f, 0.85f);
	}

	public OwnerIdentity(OwnerIdentity a, OwnerIdentity b)
	{
		this(a.getDisplayName()+"-"+b.getDisplayName());
		this.invalid = false;
		this.players.addAll(a.players);
		this.players.addAll(b.players);
		this.owners.addAll(a.owners);
		this.owners.addAll(b.owners);
		a.invalid = true;
		b.invalid = true;
	}

	public OwnerIdentity(NBTTagCompound tag)
	{
		this(tag.getString("displayName"));
		this.invalid = false;
		loadFromNBT(EasyNBT.wrapNBT(tag));
	}

	//--- With ---//

	public OwnerIdentity withMember(EntityLivingBase member, boolean owner)
	{
		return withMember(member.getName(), owner);
	}

	public OwnerIdentity withMember(String memberName, boolean owner)
	{
		invalid = false;
		if(owner&&!owners.contains(memberName))
			this.owners.add(memberName);
		if(!players.contains(memberName))
			this.players.add(memberName);
		return this;
	}

	public OwnerIdentity withLawForm(LawForm lawForm)
	{
		this.lawForm = lawForm;
		return this;
	}

	public OwnerIdentity withColor(IIColor color)
	{
		this.color = color;
		return this;
	}

	public OwnerIdentity withBanner()
	{
		return this;
	}


	//--- Getters ---//

	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Returns the color associated with this owner (team or player).
	 * For teams, this could be a team color; for players, a default or skin-based color.
	 */
	public IIColor getColor()
	{
		return color;
	}

	public LawForm getLawForm()
	{
		return lawForm;
	}

	//--- Permission Methods ---//

	public OwnerIdentity withPermission(PermissionCategory category, PermissionLevel level)
	{
		permissions.put(category, level);
		return this;
	}

	public PermissionLevel getPermission(PermissionCategory category)
	{
		return permissions.computeIfAbsent(category, DEFAULT_LEVELS::get);
	}

	public OwnerIdentity withAllPermissions(PermissionLevel permissionLevel)
	{
		for(PermissionCategory value : PermissionCategory.values())
			permissions.put(value, permissionLevel);
		return this;
	}

	public boolean isPermitted(@Nonnull EntityLivingBase entity, PermissionCategory category)
	{
		OwnerIdentity otherIdentity = DiplomacyUtils.getOwnerIdentityForEntity(entity);
		PermissionLevel permission = getPermission(category);
		String name = entity.getName();

		//Direct owner check
		if(permission.atLeast(PermissionLevel.OWNER_ALLOW)&&owners.contains(name))
			return true;

		//Member check (also covers owners implicitly if you want redundancy)
		if(permission.atLeast(PermissionLevel.MEMBER_ALLOW)&&players.contains(name))
			return true;

		//Diplomatic status checks
		DiplomaticStatus status = relations.getOrDefault(otherIdentity, DiplomaticStatus.NEUTRAL);

		if(permission.atLeast(PermissionLevel.ALLIES_ALLOW)&&status.atLeast(DiplomaticStatus.ALLIED))
			return true;
		if(permission.atLeast(PermissionLevel.ENEMY_ALLOW)&&status.atLeast(DiplomaticStatus.ENEMY))
			return true;

		//OTHERS_ALLOW: anyone passes
		return permission.atLeast(PermissionLevel.OTHERS_ALLOW);
	}

	//--- Relation Methods ---//

	public boolean isMember(EntityLivingBase entityLivingBase)
	{
		String name = entityLivingBase.getName();
		if(!name.isEmpty())
		{
			//Check if the entity is the owner
			if(owners.contains(name))
				return true;
			//Check if the entity is one of the players
			return players.contains(name);
		}
		//return false otherwise
		return false;
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
		OwnerIdentity other = DiplomacyUtils.getOwnerIdentityForEntity(entity);
		return getRelationTowards(other);
	}

	@Nonnull
	public DiplomaticStatus getRelationTowards(@Nonnull OwnerIdentity other)
	{
		if(other.equals(this))
			return DiplomaticStatus.MEMBER;
		return relations.getOrDefault(other, DiplomaticStatus.NEUTRAL);
	}

	@Nullable
	public EntityLivingBase getFirstResponsibleMember(World world)
	{
		//TODO: 03.01.2026 factions without a player entity?
		//First check for owners
		for(String owner : owners)
		{
			EntityPlayer player = world.getPlayerEntityByName(owner);
			if(player!=null)
				return player;
		}
		//Then for normal members
		for(String playerName : players)
		{
			EntityPlayer player = world.getPlayerEntityByName(playerName);
			if(player!=null)
				return player;
		}
		return null;
	}


	//--- NBT ---//

	public EasyNBT toNBT()
	{
		//Return an invalid tag if the identity is invalid
		if(invalid)
			return EasyNBT.newNBT().withBoolean("invalid", true);

		//Save permissions
		EasyNBT permissionsTag = EasyNBT.newNBT();
		for(Entry<PermissionCategory, PermissionLevel> entry : permissions.entrySet())
			permissionsTag.withEnum(entry.getKey().getName(), entry.getValue());

		//Save relations with other identities
		EasyNBT relationsTag = EasyNBT.newNBT();
		for(Entry<OwnerIdentity, DiplomaticStatus> entry : relations.entrySet())
			permissionsTag.withEnum(entry.getKey().getDisplayName(), entry.getValue());

		//Save all data, nesting the compounds above into it
		return EasyNBT.newNBT()
				.withString("displayName", displayName)
				.withList("owners", owners.toArray())
				.withList("players", players.toArray())
				.withTag("permissions", permissionsTag)
				.withTag("relations", relationsTag)
				.withEnum("lawForm", lawForm)
				.withColor("color", color)
				.withItemStack("banner", banner);
	}

	public void loadFromNBT(EasyNBT nbt)
	{
		owners.clear();
		players.clear();
		permissions.clear();
		relations.clear();

		//Check for validity
		if(invalid = nbt.getBoolean("invalid"))
			return;

		//Load owners, players and teams
		nbt.streamList(NBTTagString.class, "owners").map(NBTTagString::getString).forEach(owners::add);
		nbt.streamList(NBTTagString.class, "players").map(NBTTagString::getString).forEach(players::add);

		//Load permissions
		EasyNBT permissionsTag = nbt.getEasyCompound("permissions");
		for(PermissionCategory category : PermissionCategory.values())
			if(permissionsTag.hasKey(category.getName()))
				withPermission(category, permissionsTag.getEnum(category.getName(), PermissionLevel.class));

		//Load relations
		EasyNBT relationsTag = nbt.getEasyCompound("relations");
		for(String key : relationsTag.asMap().keySet())
		{
			//Only save relations with existing factions
			OwnerIdentity identity = DiplomacyUtils.getIdentityByName(key);
			if(identity!=DiplomacyUtils.NEUTRAL)
				this.relations.put(identity, relationsTag.getEnum(key, DiplomaticStatus.class));
		}

		this.lawForm = nbt.getEnum("lawForm", LawForm.class);
		this.color = nbt.getColor("color");
		this.banner = nbt.getItemStack("banner");
	}

	//--- Equals and HashCode ---//

	@Override
	public boolean equals(@Nullable Object o)
	{
		if(this==o) return true;
		if(o==null||getClass()!=o.getClass()) return false;
		OwnerIdentity that = (OwnerIdentity)o;
		return Objects.equals(displayName, that.displayName);

	}

	@Override
	public int hashCode()
	{
		return Objects.hash(displayName);
	}

	@Override
	public String toString()
	{
		return "OwnerIdentity:"+displayName;
	}

	public boolean hasAnyPlayers()
	{
		return !players.isEmpty();
	}

	public boolean isInvalid()
	{
		return invalid;
	}
}
