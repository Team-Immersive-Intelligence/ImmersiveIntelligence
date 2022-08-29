package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeInteger implements IDataTypeNumeric
{
	public int value;

	public DataTypeInteger(int i)
	{
		this.value = i;
	}

	public DataTypeInteger()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "integer";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{
				{"ie.manual.entry.def_value", "0"},
				{"ie.manual.entry.min_value", String.valueOf(Integer.MIN_VALUE)},
				{"ie.manual.entry.max_value", String.valueOf(Integer.MAX_VALUE)}
		};
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
		value = 0;
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
	public int getTypeColour()
	{
		return 0x26732e;
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
		return obj instanceof IDataTypeNumeric&&((IDataTypeNumeric)obj).intValue()==value;
	}
}
