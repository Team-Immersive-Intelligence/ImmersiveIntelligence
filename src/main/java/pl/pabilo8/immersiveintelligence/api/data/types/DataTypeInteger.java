package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
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
	@Nonnull
	public DataType clone()
	{
		return new DataTypeInteger(value);
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
