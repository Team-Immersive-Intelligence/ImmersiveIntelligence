package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IterableDataType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeMap extends IterableDataType implements Map<DataType, DataType>
{
	private final HashMap<DataType, DataType> values = new HashMap<>();

	public DataTypeMap(DataType key, DataType value)
	{
		values.put(key, value);
	}

	public DataTypeMap()
	{

	}

	@Override
	public int size()
	{
		return values.size();
	}

	@Override
	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return values.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return values.containsValue(value);
	}

	@Override
	public DataType get(Object key)
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

	public DataTypeMap put(DataType key, DataType value)
	{
		if(values.size() < 255)
			values.put(key, value);
		return this;
	}

	@Override
	public DataType remove(Object key)
	{
		return values.remove(key);
	}

	@Override
	public void putAll(Map<? extends DataType, ? extends DataType> m)
	{
		values.putAll(m);
	}

	@Override
	public void clear()
	{
		values.clear();
	}

	@Override
	public Set<DataType> keySet()
	{
		return values.keySet();
	}

	@Override
	public Collection<DataType> values()
	{
		return values.values();
	}

	@Override
	public Set<Entry<DataType, DataType>> entrySet()
	{
		return values.entrySet();
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
					values.put(IIDataTypeUtils.getVarFromNBT(c.getCompoundTag("Key")), IIDataTypeUtils.getVarFromNBT(c.getCompoundTag("Value")));
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

	@Override
	public String toString()
	{
		return values.toString();
	}
}