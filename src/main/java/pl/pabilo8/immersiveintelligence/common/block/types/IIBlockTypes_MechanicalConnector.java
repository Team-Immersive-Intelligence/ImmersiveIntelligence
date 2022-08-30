package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_MechanicalConnector implements IStringSerializable, BlockIEBase.IBlockEnum
{
	WOODEN_WHEEL;

	/**
	 * @author Pabilo8
	 * @since 2019-05-31
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
