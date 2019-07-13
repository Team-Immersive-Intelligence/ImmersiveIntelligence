package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pabilo8 on 2019-05-31.
 */
public class DataPacket
{
	public Map<Character, IDataType> variables = new HashMap<>();

	public static final char[] varCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	public static final Map<String, Class> varTypes = new HashMap<>();

	static
	{
		varTypes.put("null", DataPacketTypeNull.class);
		varTypes.put("integer", DataPacketTypeInteger.class);
		varTypes.put("string", DataPacketTypeString.class);
		varTypes.put("boolean", DataPacketTypeBoolean.class);
		varTypes.put("accessor", DataPacketTypeAccessor.class);
		varTypes.put("expression", DataPacketTypeExpression.class);
	}

	public IDataType getPacketVariable(Character name)
	{
		if(variables.containsKey(name))
			return variables.get(name);
		return new DataPacketTypeNull();
	}

	public boolean setVariable(Character c, IDataType type)
	{
		if(ArrayUtils.contains(varCharacters, c))
		{
			if(variables.containsKey(c))
			{
				variables.remove(c);
			}
			variables.put(c, type);
			return true;
		}
		return false;
	}

	public boolean removeVariable(Character c)
	{
		if(ArrayUtils.contains(varCharacters, c))
		{
			if(variables.containsKey(c))
			{
				variables.remove(c);
			}
			return true;
		}
		return false;
	}

	public NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		for(Map.Entry<Character, IDataType> entry : variables.entrySet())
		{
			nbt.setTag(String.valueOf(entry.getKey()), entry.getValue().valueToNBT());
		}

		return nbt;
	}

	public void fromNBT(NBTTagCompound nbt)
	{
		variables.clear();
		for(Character c : varCharacters)
		{
			if(nbt.hasKey(String.valueOf(c)))
			{
				NBTTagCompound n = nbt.getCompoundTag(String.valueOf(c));
				String type = n.getString("Type");
				if(varTypes.containsKey(type))
				{
					IDataType data = null;
					//ImmersiveIntelligence.logger.info("A "+type+" named " + "\" "+c+"\" has been added to a device");
					try
					{
						data = (IDataType)varTypes.get(type).newInstance();
					} catch(InstantiationException e)
					{
						e.printStackTrace();
					} catch(IllegalAccessException e)
					{
						e.printStackTrace();
					}
					if(data!=null)
					{
						data.valueFromNBT(n);
						variables.put(c, data);
					}
				}
			}
		}
	}
}
