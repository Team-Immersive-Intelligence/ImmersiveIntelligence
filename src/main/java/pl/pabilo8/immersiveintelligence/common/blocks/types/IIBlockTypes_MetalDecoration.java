package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 2019-05-15.
 */
public enum IIBlockTypes_MetalDecoration implements IStringSerializable, BlockIEBase.IBlockEnum
{
	COIL_DATA,
	ELECTRONIC_ENGINEERING,
	ADVANCED_ELECTRONIC_ENGINEERING;

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