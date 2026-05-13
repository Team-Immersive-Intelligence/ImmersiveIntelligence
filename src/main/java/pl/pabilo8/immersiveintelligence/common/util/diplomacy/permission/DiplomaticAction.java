package pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission;

import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.09.2025
 */
public enum DiplomaticAction implements ILocalizedEnum
{
	/**
	 * Invite a player to the identity's members list
	 */
	ADD_MEMBER(PermissionCategory.INVITE_MEMBERS),
	/**
	 * Remove a player from the identity's members list
	 */
	REMOVE_MEMBER(PermissionCategory.REMOVE_MEMBERS),
	/**
	 * Merge the identity with another one
	 */
	MERGE(PermissionCategory.MERGE),
	/**
	 * Disband the identity, removing all members and deleting all claims
	 */
	DISBAND(PermissionCategory.DISBAND),

	/**
	 * Change the identity's name
	 */
	RENAME(PermissionCategory.MODIFY_INSIGNIA),
	/**
	 * Change the identity's law form
	 */
	CHANGE_LAW_FORM(PermissionCategory.MODIFY_INSIGNIA),
	/**
	 * Change the identity's color
	 */
	CHANGE_COLOR(PermissionCategory.MODIFY_INSIGNIA),
	/**
	 * Change the identity's banner
	 */
	CHANGE_BANNER(PermissionCategory.MODIFY_INSIGNIA),
	/**
	 * Change a permission for a lower role
	 */
	CHANGE_PERMISSION,

	/**
	 * Claim a property without an owner
	 */
	CLAIM,
	/**
	 * Seize another identity's claim, making it your own. May result in a diplomatic incident.
	 */
	START_SEIZING(PermissionCategory.FOREIGN_AFFAIRS),

	/**
	 * Send a diplomatic offer to another identity, which may be accepted or rejected without any consequences.
	 */
	SEND_OFFER(PermissionCategory.FOREIGN_AFFAIRS),
	/**
	 * Send a diplomatic ultimatum to another identity, which may be accepted or rejected. May result in a diplomatic incident if the ultimatum is rejected.
	 */
	SEND_ULTIMATUM(PermissionCategory.FOREIGN_AFFAIRS);

	@Nullable
	private PermissionCategory requiredPermission;

	DiplomaticAction()
	{
		this(null);
	}

	DiplomaticAction(@Nullable PermissionCategory requiredPermission)
	{
		this.requiredPermission = requiredPermission;
	}

	@Nullable
	public PermissionCategory getRequiredPermission()
	{
		return requiredPermission;
	}

	@Override
	public String geLocaleKey()
	{
		return IIReference.DESCRIPTION_KEY+"diplomacy.action.";
	}
}
