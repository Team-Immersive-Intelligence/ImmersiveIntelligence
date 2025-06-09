package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents common multiblock Points-of-Interest types
 *
 * @author Pabilo8 (pabilo@iiteam.net)
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
	MISC_HATCH,
	MISC_DOOR,
	MISC_CONTROL_PANEL,
	MISC_SWITCH,
	MISC_DETECTOR,
	MISC_CRATE,
	MISC_WEAPON;

	private final List<MultiblockPOI> children;

	MultiblockPOI()
	{
		children = new ArrayList<>();
	}

	@SuppressWarnings("IncompleteCopyConstructor")
	MultiblockPOI(@Nonnull MultiblockPOI parent)
	{
		this();
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
	 * @return all children of this Point-of-Interest
	 */
	public List<MultiblockPOI> getChildren()
	{
		return children;
	}
}
