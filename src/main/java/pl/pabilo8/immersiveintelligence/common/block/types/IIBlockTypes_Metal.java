package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_Metal implements IStringSerializable, BlockIEBase.IBlockEnum
{
	PLATINUM,
	ZINC,
	TUNGSTEN,
	BRASS,
	DURALUMINIUM;

	/**
	 * @author Pabilo8
	 * @since 2019-05-12
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