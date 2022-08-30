package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_MetalDecoration implements IStringSerializable, BlockIEBase.IBlockEnum
{
	COIL_DATA,
	ELECTRONIC_ENGINEERING,
	ADVANCED_ELECTRONIC_ENGINEERING,
	MECHANICAL_ENGINEERING,
	HEAVY_MECHANICAL_ENGINEERING,
	COIL_STEEL_MOTOR_BELT,
	COMPUTER_ENGINEERING;

	/**
	 * @author Pabilo8
	 * @since 2019-05-15
	 */
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