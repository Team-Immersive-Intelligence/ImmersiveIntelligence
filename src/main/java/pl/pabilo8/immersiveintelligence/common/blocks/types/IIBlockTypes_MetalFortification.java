package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase.IBlockEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public enum IIBlockTypes_MetalFortification implements IBlockEnum, IStringSerializable
{
	STEEL_CHAIN_FENCE,
	BRASS_CHAIN_FENCE,
	ALUMINIUM_CHAIN_FENCE;

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
