package pl.pabilo8.immersiveintelligence.common.block.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum IIBlockTypes_ClothDecoration implements IStringSerializable, BlockIEBase.IBlockEnum
{
	COIL_ROPE,
	COIL_CLOTH_MOTOR_BELT,
	COIL_RUBBER_MOTOR_BELT;

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