package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 20-06-2019.
 * The beginning of a new definition of what an Immersive Engineering addon is!
 */
public enum IIBlockTypes_MetalMultiblock1 implements IStringSerializable, BlockIEBase.IBlockEnum
{
	REDSTONE_DATA_INTERFACE(false),
	AMMUNITION_CASING_FILLER(false),
	AMMUNITION_CORE_FILLER(false),
	AMMUNITION_ASSEMBLER(false),
	AMMUNITION_WORKSHOP(false),
	FUEL_STATION(false),
	VEHICLE_WORKSHOP(false),
	FLAGPOLE(false),
	RADAR(false),
	EMPLACEMENT(false);


	private boolean needsCustomState;

	IIBlockTypes_MetalMultiblock1(boolean needsCustomState)
	{
		this.needsCustomState = needsCustomState;
	}

	@Override
	public String getName()
	{
		return this.toString().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public int getMeta()
	{
		return ordinal();
	}

	@Override
	public boolean listForCreative()
	{
		return false;
	}

	public boolean needsCustomState()
	{
		return this.needsCustomState;
	}

	public String getCustomState()
	{
		return getName().toLowerCase();
	}
}
