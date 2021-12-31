package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase.IBlockEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * @author Pabilo8
 * @since 08.12.2021
 */
public enum IIBlockTypesCharredLog implements IStringSerializable, IBlockEnum
{
	MAIN;

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
