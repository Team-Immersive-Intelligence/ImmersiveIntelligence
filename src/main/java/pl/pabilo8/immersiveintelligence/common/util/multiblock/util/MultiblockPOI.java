package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents common multiblock Points-of-Interest types
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.08.2026
 * @since 26.10.2023
 */
public enum MultiblockPOI
{
	//Power
	POWER,
	//Electric Energy
	ENERGY(POWER),
	ENERGY_INPUT(ENERGY),
	ENERGY_OUTPUT(ENERGY),
	//Rotary Power
	ROTARY(POWER),
	ROTARY_INPUT(ROTARY),
	ROTARY_OUTPUT(ROTARY),
	//Heat
	HEAT(POWER),
	HEAT_INPUT(HEAT),
	HEAT_OUTPUT(HEAT),

	//Fluids
	FLUID,
	FLUID_INPUT(FLUID),
	FLUID_OUTPUT(FLUID),

	//Items
	ITEM,
	ITEM_INPUT(ITEM),
	ITEM_OUTPUT(ITEM),

	//Redstone
	REDSTONE,
	REDSTONE_INPUT(REDSTONE),
	REDSTONE_OUTPUT(REDSTONE),

	//Data
	DATA,
	DATA_INPUT(DATA),
	DATA_OUTPUT(DATA),

	//Wires
	WIRE_MOUNT,
	REDSTONE_CABLE_MOUNT(WIRE_MOUNT),
	DATA_CABLE_MOUNT(WIRE_MOUNT),
	SKYCRATE_WIRE_MOUNT(WIRE_MOUNT),
	SKYGONDOLA_WIRE_MOUNT(WIRE_MOUNT),

	//Misc
	MISC_FLAGPOLE,
	MISC_HATCH,
	MISC_DOOR,
	MISC_CONTROL_PANEL,
	MISC_SWITCH,
	MISC_DETECTOR,
	MISC_CRATE,
	MISC_WEAPON;

	@Getter
	@Nullable
	private final MultiblockPOI parent;
	@Getter
	private final List<MultiblockPOI> children;

	MultiblockPOI()
	{
		this.parent = null;
		this.children = new ArrayList<>();
	}

	@SuppressWarnings("IncompleteCopyConstructor")
	MultiblockPOI(@Nonnull MultiblockPOI parent)
	{
		this.parent = parent;
		this.children = new ArrayList<>();
		parent.children.add(this);
	}

	/**
	 * @return true if this Point-of-Interest has children, false otherwise.
	 */
	public boolean hasChildren()
	{
		return !children.isEmpty();
	}

	/**
	 * Checks if this Point-of-Interest is a parent of another type.
	 *
	 * @param poi type to check
	 * @return true if this type is an ancestor of the specified type
	 */
	public boolean isParentOf(@Nonnull MultiblockPOI poi)
	{
		MultiblockPOI current = poi.parent;
		while(current!=null)
		{
			if(current==this)
				return true;
			current = current.parent;
		}
		return false;
	}

	/**
	 * Checks if a declared POI type can satisfy a query for another type.
	 *
	 * @param poi queried type
	 * @return true if the types are equal or one is a parent of the other
	 */
	public boolean matches(@Nonnull MultiblockPOI poi)
	{
		return this==poi||isParentOf(poi)||poi.isParentOf(this);
	}
}
