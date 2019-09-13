package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 2019-05-12.
 */
public enum IIBlockTypes_Metal implements IStringSerializable, BlockIEBase.IBlockEnum
{
	PLATINUM,
	ZINC,
	TUNGSTEN,
	BRASS;

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