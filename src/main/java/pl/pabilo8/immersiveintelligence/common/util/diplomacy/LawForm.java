package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public enum LawForm implements ISerializableEnum
{
	/**
	 * No special law form. Does not have a development tree. Default permissions apply.
	 */
	DEFAULT(
			new PermissionRole("owner", "Owner", true)
					.withAllPermissions(true),

			new PermissionRole("trusted", "Trusted")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("member", "Member")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true)
	),
	/**
	 * A company is owned by private individuals or shareholders. It operates for profit and is managed by a board of directors.<br>
	 * Rank in a company ladder depends on the number of shares you own.
	 */
	COMPANY(
			new PermissionRole("ceo", "CEO", true)
					.withAllPermissions(true),

			new PermissionRole("cfo", "CFO")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("shareholder", "Shareholder")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("manager", "Manager")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("employee", "Employee")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true)
	),
	/**
	 * A republic is governed by elected officials who represent its members. Leadership rotates based on scheduled elections. <br>
	 * Rank is earned through election results.
	 */
	REPUBLIC(
			new PermissionRole("president", "President", true)
					.withAllPermissions(true),

			new PermissionRole("minister", "Minister", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("representative", "Representative")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true),

			new PermissionRole("citizen", "Citizen")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true)
	),
	/**
	 * A commune is a collective organization where all members share ownership equally. Decisions are made by consensus or direct vote.<br>
	 * Rank is nonexistent - authority rests in collective agreement.
	 */
	COMMUNE(
			new PermissionRole("member", "Member", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false)
	),
	/**
	 * A commissariat is an administrative structure ruled by appointed officials, often backed by ideology or military power.<br>
	 * Rank is assigned by the central authority, not by consent.
	 */
	COMMISARIAT("citizen", "citizen",
			new PermissionRole("supreme_commissar", "Supreme Commissar", true)
					.withAllPermissions(true),

			new PermissionRole("comissar", "Commissar")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("officer", "Officer")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("enforcer", "Enforcer")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true),

			new PermissionRole("citizen", "Citizen")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true),

			new PermissionRole("sub_citizen", "Second-Class Citizen")
					.withAllPermissions(false)
	),
	/**
	 * A free city is governed by a city council, each councillor represents a specialization: industry, agriculture, trade, logistics and military.<br>
	 * Rank is based on influence within the city’s institutions.
	 */
	FREE_CITY(
			new PermissionRole("mayor", "Mayor", true)
					.withAllPermissions(true),

			new PermissionRole("councilor_industry", "Councilor of Industry", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("councilor_agriculture", "Councilor of Agriculture", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("councilor_trade", "Councilor of Trade", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("councilor_logistics", "Councilor of Logistics", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("councilor_military", "Councilor of Military Affairs", true)
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("citizen", "Citizen")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true)
	),
	/**
	 * Authority is inherited through bloodline. Ownership passes to heirs upon death (despite respawn).
	 */
	DYNASTY(
			new PermissionRole("ruler", "Ruler", true)
					.withAllPermissions(true),

			new PermissionRole("heir", "Heir")
					.withAllPermissions(true)
					.withPermission(PermissionCategory.DISBAND, false)
					.withPermission(PermissionCategory.MERGE, false),

			new PermissionRole("noble", "Noble")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true),

			new PermissionRole("commoner", "Commoner")
					.withAllPermissions(false)
					.withPermission(PermissionCategory.MILITARY_AID, true)
					.withPermission(PermissionCategory.TRANSIT, true)
					.withPermission(PermissionCategory.TRADE, true)
					.withPermission(PermissionCategory.RESEARCH, true)
					.withPermission(PermissionCategory.LOGISTICS, true)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, true)
	);

	private final String startingRole, ownerRole;
	private final Supplier<List<PermissionRole>> roles;

	LawForm(String startingRole, String ownerRole, PermissionRole... roles)
	{
		this.startingRole = startingRole;
		this.ownerRole = ownerRole;
		this.roles = () -> Arrays.asList(roles);
	}

	LawForm(PermissionRole... roles)
	{
		//Last role is the default starting role
		this(roles[roles.length-1].getId(), roles[0].getId(), roles);
	}

	public List<PermissionRole> getDefaultRoles()
	{
		return this.roles.get();
	}

	public String getStartingRole()
	{
		return startingRole;
	}

	public String getOwnerRole()
	{
		return ownerRole;
	}
}
