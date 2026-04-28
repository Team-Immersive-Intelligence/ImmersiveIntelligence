package pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission;

import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a role within a faction, defining a set of permissions and whether it's the owner role.
 * Each role has a unique ID, a display name, and a map of permissions for different categories.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.04.2026
 */
public class PermissionRole
{
	private final String id;
	private final String displayName;
	private final boolean isOwner;
	private final Map<PermissionCategory, Boolean> permissions = new EnumMap<>(PermissionCategory.class);

	public PermissionRole(String id, String displayName, boolean isOwner)
	{
		this.id = id;
		this.displayName = displayName;
		this.isOwner = isOwner;
	}

	/**
	 * Convenience constructor – most roles are not owners.
	 */
	public PermissionRole(String id, String displayName)
	{
		this(id, displayName, false);
	}

	public PermissionRole withAllPermissions(boolean allowed)
	{
		for(PermissionCategory cat : PermissionCategory.values())
			permissions.put(cat, allowed);
		return this;
	}

	public PermissionRole withPermission(PermissionCategory category, boolean allowed)
	{
		permissions.put(category, allowed);
		return this;
	}

	public boolean isAllowed(PermissionCategory category)
	{
		return permissions.getOrDefault(category, false);
	}

	public boolean isOwner()
	{
		return isOwner;
	}

	public String getId()
	{
		return id;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	/* ---------- NBT ---------- */
	public EasyNBT toNBT()
	{
		EasyNBT tag = EasyNBT.newNBT();
		tag.withString("id", id);
		tag.withString("displayName", displayName);
		tag.withBoolean("isOwner", isOwner);
		EasyNBT perms = EasyNBT.newNBT();
		for(PermissionCategory cat : PermissionCategory.values())
		{
			if(permissions.containsKey(cat))
				perms.withBoolean(cat.getName(), permissions.get(cat));
		}
		tag.withTag("permissions", perms);
		return tag;
	}

	public static PermissionRole fromNBT(EasyNBT tag)
	{
		boolean owner = tag.getBoolean("isOwner");
		PermissionRole role = new PermissionRole(tag.getString("id"), tag.getString("displayName"), owner);
		EasyNBT perms = tag.getEasyCompound("permissions");
		for(PermissionCategory cat : PermissionCategory.values())
		{
			if(perms.hasKey(cat.getName()))
				role.permissions.put(cat, perms.getBoolean(cat.getName()));
		}
		return role;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this==o) return true;
		if(!(o instanceof PermissionRole)) return false;
		return id.equals(((PermissionRole)o).id);
	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
}
