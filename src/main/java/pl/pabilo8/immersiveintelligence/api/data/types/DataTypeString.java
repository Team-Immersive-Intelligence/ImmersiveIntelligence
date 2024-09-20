package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeString implements IDataType
{
	public String value;

	public DataTypeString(String i)
	{
		this.value = i;
	}

	public DataTypeString()
	{

	}

	@Override
	public String getName()
	{
		return "string";
	}

	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{{"ie.manual.entry.def_value", "''"}, {"ie.manual.entry.max_length", "512"}};
	}

	@Override
	public String valueToString()
	{
		return value;
	}

	@Override
	public void setDefaultValue()
	{
		this.value = "";
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
	public IIColor getTypeColor()
	{
		return IIColor.fromPackedRGB(0xb86300);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeString&&((DataTypeString)obj).value.equals(value);
	}
}