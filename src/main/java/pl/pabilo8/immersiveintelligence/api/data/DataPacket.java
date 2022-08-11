package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType.IGenericDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class DataPacket implements Iterable<IDataType>
{
	public Map<Character, IDataType> variables = new HashMap<>();
	private EnumDyeColor packetColor = EnumDyeColor.WHITE;
	private int packetAddress = -1;

	public static final char[] varCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	public static final Map<String, Class<? extends IDataType>> varTypes = new LinkedHashMap<>();

	static
	{
		varTypes.put("null", DataTypeNull.class);

		varTypes.put("boolean", DataTypeBoolean.class);

		varTypes.put("integer", DataTypeInteger.class);
		varTypes.put("float", DataTypeFloat.class);
		//varTypes.put("vector", DataTypeVector.class);
		//varTypes.put("range", DataTypeRange.class);

		varTypes.put("string", DataTypeString.class);

		varTypes.put("accessor", DataTypeAccessor.class);
		varTypes.put("expression", DataTypeExpression.class);

		varTypes.put("itemstack", DataTypeItemStack.class);
		varTypes.put("entity", DataTypeEntity.class);

		varTypes.put("array", DataTypeArray.class);
		varTypes.put("map", DataTypeMap.class);

	}

	@Nonnull
	public static IDataType getVarFromNBT(NBTTagCompound nbt)
	{
		IDataType data = getVarInstance(DataPacket.varTypes.get(nbt.getString("Type")));
		data.valueFromNBT(nbt);
		return data;
	}

	@Nonnull
	public static IDataType getVarInstance(Class<? extends IDataType> type)
	{
		try
		{
			IDataType data = type.isAnnotationPresent(IGenericDataType.class)?(type.getAnnotation(IGenericDataType.class).defaultType().newInstance()): type.newInstance();
			data.setDefaultValue();
			return data;
		}
		catch(InstantiationException|IllegalAccessException ignored)
		{
		}
		return new DataTypeNull();
	}

	/**
	 * @param preferred type, can be {@link IDataType} for *any* type and an interface annotated with {@link IGenericDataType} a generic/bridging type
	 * @param actual    actual tag in packet, if null a default value of preferred type will be returned
	 * @param <T>       the returned datatype
	 * @return datatype provided in preferred class or a new instance of it
	 */
	@Nonnull
	public <T extends IDataType> T getVarInType(Class<T> preferred, @Nullable IDataType actual)
	{
		if(actual!=null)
		{
			if(preferred==IDataType.class)
				return (T)actual;

			IDataType type;
			if(actual instanceof DataTypeAccessor)
				type = ((DataTypeAccessor)actual).getRealValue(this);
			else if(actual instanceof DataTypeExpression&&preferred!=DataTypeExpression.class)
				type = ((DataTypeExpression)actual).getValue(this);
			else
				type = actual;

			if(preferred.isInstance(type))
				return (T)type;
			else if(!preferred.isAnnotationPresent(IGenericDataType.class))
			{
				try
				{
					IDataType p = preferred.newInstance();
					p.setDefaultValue();
					return preferred.cast(p);
				}
				catch(InstantiationException|IllegalAccessException ignored)
				{
				}
			}
		}

		return (T)getVarInstance(preferred);
	}

	public boolean hasAnyVariables()
	{
		return variables.size() > 0;
	}

	public boolean hasVariable(Character c)
	{
		return variables.containsKey(c);
	}

	public boolean hasAnyVariables(Character... names)
	{
		for(Character c : names)
			if(!variables.containsKey(c))
				return false;
		return true;
	}

	public IDataType getPacketVariable(Character name)
	{
		if(variables.containsKey(name))
			return variables.get(name);
		return new DataTypeNull();
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
		return (packetAddress==-1||packetAddress==connAddress)&&(packetColor==EnumDyeColor.WHITE||packetColor==connColor);
	}

	public void removeAllVariables()
	{
		for(char c : varCharacters)
			removeVariable(c);
	}

	public void removeVariables(Character... names)
	{
		for(Character c : names)
			if(ArrayUtils.contains(varCharacters, c))
				variables.remove(c);
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
			nbt.setTag(String.valueOf(entry.getKey()), entry.getValue().valueToNBT());

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
			if(nbt.hasKey(String.valueOf(c)))
			{
				NBTTagCompound n = nbt.getCompoundTag(String.valueOf(c));
				String type = n.getString("Type");
				if(varTypes.containsKey(type))
				{
					IDataType data = null;
					try
					{
						data = varTypes.get(type).newInstance();
					}
					catch(InstantiationException|IllegalAccessException e)
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

	@Override
	public Iterator<IDataType> iterator()
	{
		return variables.values().iterator();
	}
}
