package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeArray implements IDataTypeIterable
{
	public IDataType[] value;

	public DataTypeArray(IDataType... i)
	{
		this.value = new IDataType[Math.min(i.length, 255)];
		System.arraycopy(i, 0, this.value, 0, this.value.length);
	}

	public DataTypeArray()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "array";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{{"ie.manual.entry.def_value", "ie.manual.entry.empty"}, {"ie.manual.entry.min_index", "0"}, {"ie.manual.entry.max_index", "255"}};
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		if(value==null||value.length==0)
			return "[]";

		StringBuilder s = new StringBuilder("[");
		for(IDataType type : value)
			s.append(type.valueToString()).append(", ");
		return s.delete(s.length()-2, s.length()-1).append("]").toString();
	}

	@Override
	public void setDefaultValue()
	{
		this.value = new IDataType[0];
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		NBTTagList l = n.getTagList("Values", 10);
		ArrayList<IDataType> dataTypes = new ArrayList<>();
		for(NBTBase b : l)
		{
			if(b instanceof NBTTagCompound)
			{
				NBTTagCompound c = (NBTTagCompound)b;
				if(c.hasKey("Type"))
					dataTypes.add(DataPacket.getVarFromNBT(c));
			}
		}
		this.value = dataTypes.toArray(new IDataType[]{});
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		NBTTagList list = new NBTTagList();
		for(IDataType type : value)
			list.appendTag(type.valueToNBT());
		nbt.setTag("Values", list);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x520c2b;
	}
}