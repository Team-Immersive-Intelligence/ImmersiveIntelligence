package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeMap implements IDataTypeIterable
{
	private HashMap<IDataType, IDataType> values;

	public DataTypeMap(IDataType key, IDataType value)
	{
		setDefaultValue();
		values.put(key, value);
	}

	public DataTypeMap()
	{

	}

	public DataTypeMap put(IDataType key, IDataType value)
	{
		if(values.size() < 255)
			values.put(key, value);

		return this;
	}

	@Nonnull
	public IDataType getValue(IDataType key)
	{
		return values.getOrDefault(key, new DataTypeNull());
	}

	@Nonnull
	public IDataType getKey(IDataType value)
	{
		//ah, yes, the lambdas
		return values.entrySet()
				.stream()
				.filter(e -> e.getValue().equals(value))
				.map(Entry::getKey)
				.findFirst()
				.orElse(new DataTypeNull());
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "map";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{{"ie.manual.entry.def_value", "ie.manual.entry.empty"}, {"ie.manual.entry.max_length", "255"}};
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		return values.toString();
	}

	@Override
	public void setDefaultValue()
	{
		this.values = new HashMap<>();
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		setDefaultValue();
		NBTTagList l = n.getTagList("Entries", 10);

		for(NBTBase b : l)
		{
			if(b instanceof NBTTagCompound)
			{
				NBTTagCompound c = (NBTTagCompound)b;
				if(c.hasKey("Key")&&c.hasKey("Value"))
					values.put(DataPacket.getVarFromNBT(c.getCompoundTag("Key")), DataPacket.getVarFromNBT(c.getCompoundTag("Value")));
			}
		}
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		NBTTagList list = new NBTTagList();

		for(Entry<IDataType, IDataType> entry : values.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("Key", entry.getKey().valueToNBT());
			tag.setTag("Value", entry.getValue().valueToNBT());
			list.appendTag(tag);
		}

		nbt.setTag("Entries", list);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x4d5914;
	}
}