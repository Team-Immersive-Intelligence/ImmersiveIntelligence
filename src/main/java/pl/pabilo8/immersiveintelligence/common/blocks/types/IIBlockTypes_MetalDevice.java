package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public enum IIBlockTypes_MetalDevice implements IStringSerializable, BlockIEBase.IBlockEnum
{
	METAL_CRATE,
	AMMUNITION_CRATE,
	SMALL_DATA_BUFFER,
	TIMED_BUFFER,
	REDSTONE_BUFFER,
	DATA_DEBUGGER;

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
