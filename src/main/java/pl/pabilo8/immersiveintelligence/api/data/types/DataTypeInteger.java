package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeInteger extends NumericDataType
{
	public int value = 0;

	public DataTypeInteger(int i)
	{
		this.value = i;
	}

	public DataTypeInteger()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getInteger("Value");
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setInteger("Value", value);
		return nbt;
	}

	@Override
	public DataTypeInteger asInt()
	{
		return this;
	}

	public DataTypeFloat asFloat()
	{
		return new DataTypeFloat((float)this.value);
	}

	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj)&&((NumericDataType)obj).intValue()==value;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}
}
