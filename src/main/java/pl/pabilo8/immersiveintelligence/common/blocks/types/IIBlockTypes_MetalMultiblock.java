package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public enum IIBlockTypes_MetalMultiblock implements IStringSerializable, BlockIEBase.IBlockEnum
{
	RADIO_STATION,
	PRINTING_PRESS,
	DATA_INPUT_MACHINE,
	ARITHMETIC_LOGIC_MACHINE,
	CHEMICAL_BATH,
	ELECTROLYZER,
	PRECISSION_ASSEMBLER,
	BALLISTIC_COMPUTER,
	ARTILLERY_HOWITZER,
	PERISCOPE,
	CONVEYOR_SCANNER,
	AMMUNITION_FACTORY;

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
		return true;
	}
}
