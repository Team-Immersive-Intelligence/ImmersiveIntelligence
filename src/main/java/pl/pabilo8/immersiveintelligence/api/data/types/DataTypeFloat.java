package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
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

	@Override
	@Nonnull
	public DataType clone()
	{
		return new DataTypeFloat(value);
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

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}
}
