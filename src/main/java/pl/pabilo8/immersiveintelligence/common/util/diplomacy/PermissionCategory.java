package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public enum PermissionCategory implements ISerializableEnum
{
	/**
	 * Allows disbanding the identity
	 */
	DISBAND,
	/**
	 * Allows merging two permitting identities
	 */
	MERGE,
	/**
	 * Allows adding members (players or teams) to the identity
	 */
	ADD_MEMBERS,
	/**
	 * Allows removing members (players or teams) from the identity
	 */
	REMOVE_MEMBERS,
	/**
	 * Determines if the identity's units and military devices should help those having this permission
	 */
	MILITARY_AID,
	/**
	 * Allows moving through this identity's territory.
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
	CONTAINER_ACCESS
}
