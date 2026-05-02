package pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission;

import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public enum PermissionCategory implements ILocalizedEnum
{
	/**
	 * Allows disbanding the identity, releasing all claims and members.
	 */
	DISBAND,
	/**
	 * Allows merging two permitting identities
	 */
	MERGE,
	/**
	 * Allows seizing land (claiming is still allowed without this permission), and proposing
	 * {@link pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term.DiplomaticAgreement Agreements} to other identities
	 */
	FOREIGN_AFFAIRS,
	/**
	 * Allows modifying the identity's name, color, banner, and other visual aspects
	 */
	MODIFY_INSIGNIA,
	/**
	 * Allows inviting members to the identity
	 */
	INVITE_MEMBERS,
	/**
	 * Allows removing members from the identity
	 */
	REMOVE_MEMBERS,
	/**
	 * Determines if the identity's units and military devices should help those having this permission
	 */
	MILITARY_AID,
	/**
	 * Allows moving through this identity's territory without instigating a diplomatic incident
	 */
	TRANSIT,
	/**
	 * Allows trading with this identity's facilities
	 */
	TRADE,
	/**
	 * Allows using this identity's research
	 */
	RESEARCH,
	/**
	 * Allows using this identity's logistics network
	 */
	LOGISTICS,
	/**
	 * Allows opening containers owned by this identity
	 */
	CONTAINER_ACCESS,
	/**
	 * Allows breaking blocks that are marked as owned by this identity
	 */
	BREAKING_STRUCTURES;


	@Override
	public String geLocaleKey()
	{
		return IIReference.DESCRIPTION_KEY+"diplomacy.permission.";
	}
}
