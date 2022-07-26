package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_MetalDevice implements IStringSerializable, BlockIEBase.IBlockEnum
{
	METAL_CRATE,
	AMMUNITION_CRATE,
	SMALL_DATA_BUFFER,
	TIMED_BUFFER,
	REDSTONE_BUFFER,
	PUNCHTAPE_READER,
	DATA_ROUTER,
	DATA_MERGER,
	MEDIC_CRATE,
	REPAIR_CRATE,
	LATEX_COLLECTOR;

	/**
	 * @author Pabilo8
	 * @since 2019-05-17
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
