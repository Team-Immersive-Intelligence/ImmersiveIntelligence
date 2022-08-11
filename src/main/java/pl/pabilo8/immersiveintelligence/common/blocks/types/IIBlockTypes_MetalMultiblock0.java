package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_MetalMultiblock0 implements IStringSerializable, BlockIEBase.IBlockEnum
{
	RADIO_STATION(false),
	PRINTING_PRESS(true),
	DATA_INPUT_MACHINE(false),
	ARITHMETIC_LOGIC_MACHINE(false),
	CHEMICAL_BATH(true),
	ELECTROLYZER(true),
	PRECISSION_ASSEMBLER(true),
	BALLISTIC_COMPUTER(true),
	ARTILLERY_HOWITZER(true),
	PERISCOPE(false),
	CONVEYOR_SCANNER(false),
	AMMUNITION_FACTORY(false), //deprecated
	PACKER_OLD(true), //deprecated
	PACKER(true),
	RAILWAY_PACKER(false);

	/**
	 * @author Pabilo8
	 * @since 20-06-2019
	 */
	private boolean needsCustomState;

	IIBlockTypes_MetalMultiblock0(boolean needsCustomState)
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
