package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public enum IIBlockTypes_ConcreteDecoration implements IStringSerializable, BlockIEBase.IBlockEnum
{
	CONCRETE_BRICKS,
	STURDY_CONCRETE_BRICKS,
	UBERCONCRETE;

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
