package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeNull extends DataType
{
	public DataTypeNull()
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
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeNull;
	}

	@Override
	public String toString()
	{
		return "null";
	}
}