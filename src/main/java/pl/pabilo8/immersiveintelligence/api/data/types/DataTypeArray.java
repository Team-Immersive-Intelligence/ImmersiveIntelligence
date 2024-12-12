package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IterableDataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeArray extends IterableDataType
{
	public DataType[] value = new DataType[0];

	public DataTypeArray(DataType... i)
	{
		this.value = new DataType[Math.min(i.length, 255)];
		System.arraycopy(i, 0, this.value, 0, this.value.length);
	}

	public DataTypeArray(Collection<DataType> collection)
	{
		this(collection.toArray(new DataType[0]));
	}

	public DataTypeArray()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		NBTTagList l = n.getTagList("Values", 10);
		ArrayList<DataType> dataTypes = new ArrayList<>();
		for(NBTBase b : l)
		{
			if(b instanceof NBTTagCompound)
			{
				NBTTagCompound c = (NBTTagCompound)b;
				if(c.hasKey("Type"))
					dataTypes.add(IIDataTypeUtils.getVarFromNBT(c));
			}
		}
		this.value = dataTypes.toArray(new DataType[]{});
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		NBTTagList list = new NBTTagList();
		for(DataType type : value)
			list.appendTag(type.valueToNBT());
		nbt.setTag("Values", list);
		return nbt;
	}

	@Override
	public String toString()
	{
		if(value==null||value.length==0)
			return "[]";

		StringBuilder s = new StringBuilder("[");
		for(DataType type : value)
			s.append(type.toString()).append(", ");
		return s.delete(s.length()-2, s.length()-1).append("]").toString();
	}
}