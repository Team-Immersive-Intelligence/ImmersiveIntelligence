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
public class DataTypeMap extends IterableDataType
{
	private final HashMap<DataType, DataType> values = new HashMap<>();

	public DataTypeMap(DataType key, DataType value)
	{
		values.put(key, value);
	}

	public DataTypeMap()
	{

	}

	public DataTypeMap put(DataType key, DataType value)
	{
		if(values.size() < 255)
			values.put(key, value);

		return this;
	}

	@Nonnull
	public DataType getValue(DataType key)
	{
		return values.getOrDefault(key, new DataTypeNull());
	}

	@Nonnull
	public DataType getKey(DataType value)
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
	public String valueToString()
	{
		return values.toString();
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		values.clear();
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

		for(Entry<DataType, DataType> entry : values.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("Key", entry.getKey().valueToNBT());
			tag.setTag("Value", entry.getValue().valueToNBT());
			list.appendTag(tag);
		}

		nbt.setTag("Entries", list);
		return nbt;
	}
}