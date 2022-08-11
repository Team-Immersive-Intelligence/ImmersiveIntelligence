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
public class DataTypeMap implements IDataType
{
	public HashMap<IDataType, IDataType> values;

	public DataTypeMap(IDataType key, IDataType value)
	{
		setDefaultValue();
		values.put(key, value);
	}

	public DataTypeMap()
	{

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
		return new String[][]{{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
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
			tag.setTag("Key",entry.getKey().valueToNBT());
			tag.setTag("Value",entry.getValue().valueToNBT());
			list.appendTag(tag);
		}

		nbt.setTag("Entries", list);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x829d00;
	}
}