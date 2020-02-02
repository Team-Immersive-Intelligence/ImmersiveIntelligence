package pl.pabilo8.immersiveintelligence.common.blocks.types;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by Pabilo8 on 2019-05-31.
 */
public enum IIBlockTypes_Connector implements IStringSerializable, BlockIEBase.IBlockEnum
{
	DATA_CONNECTOR,
	DATA_RELAY,
	ALARM_SIREN,
	INSERTER,
	FLUID_INSERTER,
	ADVANCED_INSERTER,
	ADVANCED_FLUID_INSERTER,
	CHEMICAL_DISPENSER;

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
