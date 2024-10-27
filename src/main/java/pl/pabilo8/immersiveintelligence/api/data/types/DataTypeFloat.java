package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeFloat extends NumericDataType
{
	public float value = 0;

	public DataTypeFloat(float i)
	{
		this.value = i;
	}

	public DataTypeFloat()
	{

	}

	@Nonnull
	@Override
	public String valueToString()
	{
		return String.valueOf(value);
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getFloat("Value");
	}


	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setFloat("Value", value);
		return nbt;
	}


	public DataTypeInteger asInt()
	{
		return new DataTypeInteger(((int)this.value));
	}

	@Override
	public DataTypeFloat asFloat()
	{
		return this;
	}
}
