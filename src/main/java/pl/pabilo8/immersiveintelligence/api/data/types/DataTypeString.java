package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeString extends DataType
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
	public String valueToString()
	{
		return value;
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
}