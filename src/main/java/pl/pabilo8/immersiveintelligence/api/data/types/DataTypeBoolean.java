package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeBoolean implements IDataType
{
	public boolean value;

	public DataTypeBoolean(boolean i)
	{
		this.value = i;
	}

	public DataTypeBoolean()
	{
	}


	@Nonnull
	@Override
	public String getName()
	{
		return "boolean";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{{"ie.manual.entry.def_value", "ie.manual.entry.false"}, {"ie.manual.entry.accepted_values", "ie.manual.entry.tf"}};
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		return String.valueOf(value);
	}

	@Override
	public void setDefaultValue()
	{
		value = false;
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
	public int getTypeColour()
	{
		return 0x922020;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeBoolean&&((DataTypeBoolean)obj).value==value;
	}
}
