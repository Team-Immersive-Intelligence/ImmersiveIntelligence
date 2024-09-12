package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeNull implements IDataType
{
	public DataTypeNull()
	{
	}

	@Override
	public String getName()
	{
		return "null";
	}

	@Override
	public String valueToString()
	{
		return "null";
	}

	@Override
	public void setDefaultValue()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{

	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		return getHeaderTag();
	}

	@Override
	public IIColor getTypeColour()
	{
		return IIColor.fromPackedRGB(0x8f2fb3);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeNull;
	}
}