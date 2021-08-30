package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class DataPacket
{
	public Map<Character, IDataType> variables = new HashMap<>();
	private EnumDyeColor packetColor = EnumDyeColor.WHITE;
	private int packetAddress = -1;

	public static final char[] varCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	public static final Map<String, Class> varTypes = new HashMap<>();

	static
	{
		varTypes.put("null", DataPacketTypeNull.class);

		varTypes.put("integer", DataPacketTypeInteger.class);
		//varTypes.put("float", DataPacketTypeFloat.class);
		//varTypes.put("vector", DataPacketTypeVector.class);
		//varTypes.put("range", DataPacketTypeRange.class);

		varTypes.put("string", DataPacketTypeString.class);
		//varTypes.put("character", DataPacketTypeCharacter.class);

		varTypes.put("boolean", DataPacketTypeBoolean.class);

		varTypes.put("accessor", DataPacketTypeAccessor.class);
		varTypes.put("expression", DataPacketTypeExpression.class);

		varTypes.put("itemstack", DataPacketTypeItemStack.class);
		//varTypes.put("fluidstack", DataPacketTypeFluidStack.class);
		//varTypes.put("crafting_recipe", DataPacketTypeCraftingRecipe.class);

		varTypes.put("array", DataPacketTypeArray.class);

		varTypes.put("entity", DataPacketTypeEntity.class);

		//varTypes.put("pair", DataPacketTypePair.class);
	}

	public boolean hasVariables()
	{
		return variables.size()>0;
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
			variables.remove(c);
			variables.put(c, type);
			return true;
		}
		return false;
	}

	public DataPacket setPacketColor(EnumDyeColor color)
	{
		this.packetColor = color;
		return this;
	}

	public DataPacket setPacketAddress(int address)
	{
		if(address >= -1)
			this.packetAddress = address;
		return this;
	}

	public boolean matchesConnector(EnumDyeColor connColor, int connAddress)
	{
		return (packetAddress==-1||packetAddress==connAddress)&&(packetColor==EnumDyeColor.WHITE||connColor==packetColor);
	}

	//If you really need to
	public void removeAllVariables()
	{
		for(char c : varCharacters)
		{
			removeVariable(c);
		}
	}

	public boolean removeVariable(Character c)
	{
		if(ArrayUtils.contains(varCharacters, c))
		{
			variables.remove(c);
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

		if(packetColor!=EnumDyeColor.WHITE)
			nbt.setInteger("color", packetColor.getMetadata());
		if(packetAddress!=-1)
			nbt.setInteger("address", packetAddress);


		return nbt;
	}

	public DataPacket fromNBT(NBTTagCompound nbt)
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
					try
					{
						data = (IDataType)varTypes.get(type).newInstance();
					} catch(InstantiationException|IllegalAccessException e)
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
		if(nbt.hasKey("color"))
			this.packetColor = EnumDyeColor.byMetadata(nbt.getInteger("color"));
		if(nbt.hasKey("address"))
			this.packetAddress = nbt.getInteger("address");
		return this;
	}

	@Override
	public DataPacket clone()
	{
		DataPacket packet = new DataPacket();
		packet.fromNBT(this.toNBT());
		return packet;
	}

	@Override
	public String toString()
	{
		return this.toNBT().toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this==obj)
			return true;
		if(obj instanceof DataPacket)
		{
			DataPacket other = (DataPacket)obj;

			if(!variables.keySet().equals(other.variables.keySet()))
				return false;
			if(!matchesConnector(other.packetColor, other.packetAddress))
				return false;
			for(Entry<Character, IDataType> entry : variables.entrySet())
				if(!other.getPacketVariable(entry.getKey()).valueToString().equals(entry.getValue().valueToString()))
					return false;

			return true;
		}
		return false;
	}
}
