package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A container for 36 {@link DataType Data Variables} used to exchange information between {@link pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice Data Devices}, central component of the data system.
 *
 * @author Pabilo8
 * @updated 28.10.2024
 * @ii-approved 0.3.1
 * @since 2019-05-31
 */
public class DataPacket implements Iterable<DataType>
{
	public Map<Character, DataType> variables = new HashMap<>();
	private EnumDyeColor packetColor = EnumDyeColor.WHITE;
	private int packetAddress = -1;

	public static final char[] varCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

	/**
	 * @param preferred type, can be {@link DataType} for *any* type and an interface annotated with {@link IGenericDataType} a generic/bridging type
	 * @param actual    actual tag in packet, if null a default value of preferred type will be returned
	 * @param <T>       the returned datatype
	 * @return datatype provided in preferred class or a new INSTANCE of it
	 */
	@Nonnull
	public <T extends DataType> T getVarInType(Class<T> preferred, @Nullable DataType actual)
	{
		if(actual!=null)
		{
			if(preferred==DataType.class)
				return (T)actual;
			DataType type = evaluateVariable(actual, preferred!=DataTypeExpression.class);

			if(preferred.isInstance(type))
				return (T)type;
			else if(!preferred.isAnnotationPresent(IGenericDataType.class))
			{
				try
				{
					DataType p = preferred.newInstance();
					return preferred.cast(p);
				} catch(InstantiationException|IllegalAccessException ignored)
				{
				}
			}
		}

		return (T)IIDataTypeUtils.getVarInstance(preferred);
	}

	public DataType evaluateVariable(@Nullable DataType actual, boolean allowExpressions)
	{
		if(actual instanceof DataTypeAccessor)
			return ((DataTypeAccessor)actual).getRealValue(this);
		else if(actual instanceof DataTypeExpression&&allowExpressions)
			return ((DataTypeExpression)actual).getValue(this);
		else
			return actual;
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

	public DataType getPacketVariable(Character name)
	{
		if(variables.containsKey(name))
			return variables.get(name);
		return new DataTypeNull();
	}

	public boolean setVariable(Character c, DataType type)
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

		for(Map.Entry<Character, DataType> entry : variables.entrySet())
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
				if(IIDataTypeUtils.metaTypesByName.containsKey(type))
				{
					DataType data = IIDataTypeUtils.metaTypesByName.get(type).supplier.get();
					data.valueFromNBT(n);
					variables.put(c, data);
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
			for(Entry<Character, DataType> entry : variables.entrySet())
				if(!other.getPacketVariable(entry.getKey()).toString().equals(entry.getValue().toString()))
					return false;

			return true;
		}
		return false;
	}

	@Override
	public Iterator<DataType> iterator()
	{
		return variables.values().iterator();
	}

	public int size()
	{
		return variables.size();
	}

	public void trimNulls()
	{
		variables.entrySet().removeIf(entry -> entry.getValue() instanceof DataTypeNull);
	}
}
