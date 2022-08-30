package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_SmallCrate implements IStringSerializable, BlockIEBase.IBlockEnum
{
	WOODEN_CRATE_BOX,
	WOODEN_CRATE_CUBE,
	WOODEN_CRATE_WIDE,
	METAL_CRATE_BOX,
	METAL_CRATE_CUBE,
	METAL_CRATE_WIDE;

	/**
	 * @author Pabilo8
	 * @since 16-07-2019
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