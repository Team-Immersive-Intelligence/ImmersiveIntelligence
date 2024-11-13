package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IComparableDataType;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeString extends DataType implements IComparableDataType<DataTypeString>
{
	public String value = "";

	public DataTypeString(String i)
	{
		this.value = i;
	}

	public DataTypeString()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getString("Value");
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setString("Value", value);
		return nbt;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeString&&((DataTypeString)obj).value.equals(value);
	}

	@Override
	public String toString()
	{
		return value;
	}

	@Override
	public boolean canCompareWith(DataType other)
	{
		return other instanceof DataTypeString;
	}

	@Override
	public int compareTo(DataTypeString other)
	{
		return Integer.compare(value.length(), other.value.length());
	}
}