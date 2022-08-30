package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase.IBlockEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public enum IIBlockTypes_WoodenFortification implements IBlockEnum, IStringSerializable
{
	WOODEN_STEEL_CHAIN_FENCE,
	WOODEN_BRASS_CHAIN_FENCE;

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
