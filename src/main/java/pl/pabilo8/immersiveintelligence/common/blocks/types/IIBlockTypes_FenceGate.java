package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public enum IIBlockTypes_FenceGate implements IStringSerializable, BlockIEBase.IBlockEnum
{
	WOODEN(false),
	WOODEN_CHAIN(false),
	STEEL(false),
	STEEL_CHAIN(false),
	ALUMINIUM(false),
	ALUMINIUM_CHAIN(false);

	private final boolean needsCustomState;

	IIBlockTypes_FenceGate(boolean needsCustomState)
	{
		this.needsCustomState = needsCustomState;
	}

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
		return false;
	}

	public boolean needsCustomState()
	{
		return this.needsCustomState;
	}

	public String getCustomState()
	{
		return getName().toLowerCase();
	}
}
