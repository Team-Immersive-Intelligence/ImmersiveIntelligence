package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeFloat implements IDataTypeNumeric
{
	public float value;

	public DataTypeFloat(float i)
	{
		this.value = i;
	}

	public DataTypeFloat()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "float";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{
				{"ie.manual.entry.def_value", "0.0"},
				{"ie.manual.entry.min_value", String.valueOf(Float.MIN_VALUE)},
				{"ie.manual.entry.max_value", String.valueOf(Float.MAX_VALUE)}
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
	public int getTypeColour()
	{
		return 0x0d6b68;
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
	public boolean equals(Object obj)
	{
		return obj instanceof IDataTypeNumeric&&((IDataTypeNumeric)obj).floatValue()==value;
	}
}
