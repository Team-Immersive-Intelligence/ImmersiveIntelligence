package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeBoolean extends DataType
{
	public boolean value = false;

	public DataTypeBoolean(boolean i)
	{
		this.value = i;
	}

	public DataTypeBoolean()
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
}
