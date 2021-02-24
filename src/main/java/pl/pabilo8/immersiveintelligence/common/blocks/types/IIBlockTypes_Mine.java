package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_Mine implements IStringSerializable, BlockIEBase.IBlockEnum
{
	MAIN;

	/**
	 * @author Pabilo8
	 * @since 31.01.2021
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
