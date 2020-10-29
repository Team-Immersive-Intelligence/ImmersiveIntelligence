package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataPacketTypeArray implements IDataType
{
	public IDataType[] value;

	public DataPacketTypeArray(IDataType... i)
	{
		this.value = i;
	}

	public DataPacketTypeArray()
	{

	}

	@Override
	public String getName()
	{
		return "array";
	}

	@Override
	public String valueToString()
	{
		return Arrays.toString(value);
	}

	@Override
	public void setDefaultValue()
	{
		this.value = new IDataType[]{new DataPacketTypeNull()};
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		NBTTagList l = n.getTagList("Values", 10);
		ArrayList<IDataType> dataTypes = new ArrayList<>();
		Iterator<NBTBase> iterator = l.iterator();
		while(iterator.hasNext())
		{
			NBTBase b = iterator.next();
			if(b instanceof NBTTagCompound)
			{
				NBTTagCompound c = (NBTTagCompound)b;
				if(c.hasKey("Type"))
				{
					String type = c.getString("Type");
					IDataType data = null;
					try
					{
						data = (IDataType)DataPacket.varTypes.get(type).newInstance();
					} catch(InstantiationException|IllegalAccessException e)
					{
						e.printStackTrace();
					}
					if(data!=null)
					{
						data.valueFromNBT(c);
						dataTypes.add(data);
					}

				}
			}
		}
		this.value = dataTypes.toArray(new IDataType[]{});
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Type", "array");
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

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 7;
	}
}