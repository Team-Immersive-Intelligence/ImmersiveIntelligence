package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.01.2026
 */
public class DataTypeLogisticTag extends DataType
{
	public LogisticTag value = new LogisticTag();

	public DataTypeLogisticTag(LogisticTag value)
	{
		this.value = value;
	}

	public DataTypeLogisticTag()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = new LogisticTag(n.getCompoundTag("Value"));
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setTag("Value", value.serializeNBT());
		return nbt;
	}

	@Override
	@Nonnull
	public DataType clone()
	{
		return new DataTypeLogisticTag(value.clone());
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeLogisticTag&&((DataTypeLogisticTag)obj).value.equals(value);
	}

	@Override
	public String toString()
	{
		return value.toString();
	}
}
