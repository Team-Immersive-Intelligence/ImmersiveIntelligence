package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author GabrielV
 * Data type used for transferring encrypted data from Cryptographer
 */
public class DataTypeEncrypted implements IDataTypeIterable
{
	public byte[] value;

	public DataTypeEncrypted(byte[] data)
	{
		value = new byte[data.length];
		System.arraycopy(data, 0, value, 0, data.length);
	}

	public DataTypeEncrypted() {}

	@Override
	public String getName()
	{
		return "encrypted";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
	}

	@Override
	public String valueToString()
	{
		if (value==null||value.length==0) return "00";
		StringBuilder s = new StringBuilder();
		for (byte b : value)
			s.append(String.format("%02X", b));
		return s.delete(s.length()-2, s.length()-1).toString();
	}

	@Override
	public void setDefaultValue()
	{
		this.value = new byte[0];
	}

	@Override
	public void valueFromNBT(NBTTagCompound nbt)
	{
		this.value = nbt.getByteArray("Values");
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setByteArray("Values", value);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x5a0d75;
	}
}
