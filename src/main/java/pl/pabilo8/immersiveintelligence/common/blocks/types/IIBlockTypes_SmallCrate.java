package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 16-07-2019.
 */
public enum IIBlockTypes_SmallCrate implements IStringSerializable, BlockIEBase.IBlockEnum
{
	WOODEN_CRATE_BOX,
	WOODEN_CRATE_CUBE,
	WOODEN_CRATE_WIDE,
	METAL_CRATE_BOX,
	METAL_CRATE_CUBE,
	METAL_CRATE_WIDE;

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