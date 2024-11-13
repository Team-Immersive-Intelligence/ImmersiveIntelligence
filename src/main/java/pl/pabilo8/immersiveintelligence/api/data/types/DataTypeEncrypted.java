package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IterableDataType;

/**
 * @author GabrielV
 * Data type used for transferring encrypted data from Cryptographer
 */
public class DataTypeEncrypted extends IterableDataType
{
	public byte[] value = new byte[0];

	public DataTypeEncrypted(byte[] data)
	{
		value = new byte[data.length];
		System.arraycopy(data, 0, value, 0, data.length);
	}

	public DataTypeEncrypted()
	{

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
	public String toString()
	{
		if(value==null||value.length==0) return "00";
		StringBuilder s = new StringBuilder();
		for(byte b : value)
			s.append(String.format("%02X", b));
		return s.delete(s.length()-2, s.length()-1).toString();
	}
}
