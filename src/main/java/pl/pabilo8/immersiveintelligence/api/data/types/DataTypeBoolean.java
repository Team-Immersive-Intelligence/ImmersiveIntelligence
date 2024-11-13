package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IComparableDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeBoolean extends DataType implements IComparableDataType<DataTypeBoolean>
{
	public boolean value = false;

	public DataTypeBoolean(boolean i)
	{
		this.value = i;
	}

	public DataTypeBoolean()
	{
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getBoolean("Value");
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setBoolean("Value", value);
		return nbt;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeBoolean&&((DataTypeBoolean)obj).value==value;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}

	@Override
	public boolean canCompareWith(DataType other)
	{
		return other instanceof DataTypeBoolean;
	}

	@Override
	public int compareTo(DataTypeBoolean other)
	{
		return Boolean.compare(this.value, other.value);
	}
}
