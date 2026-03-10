package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * A container for 36 {@link DataType Data Variables} used to exchange information between {@link pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice Data Devices}, central component of the data system.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 28.10.2024
 * @ii-approved 0.3.1
 * @since 31.05.2019
 */
public class DataPacket implements Iterable<DataVariable>, INBTSerializable<NBTTagCompound>
{
	/**
	 * A list of all valid variable names, used to identify variables in the packet.
	 */
	public static final char[] VARIABLE_NAMES = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	/**
	 * A list of all variables in this packet, used for iteration and serialization.
	 */
	private List<DataVariable> variableList = new ArrayList<>();
	/**
	 * A map of all variables in this packet, used for quick access by name.
	 */
	private Map<Character, DataType> variableMap = new HashMap<>();
	/**
	 * The color of the packet, used to identify the packet connector sub-network.
	 * Defaults to {@link EnumDyeColor#WHITE} if not set.
	 */
	private EnumDyeColor packetColor = EnumDyeColor.WHITE;
	/**
	 * The destination address of the packet, used in advanced data networks.
	 */
	private int packetAddress = -1;


	public DataPacket()
	{

	}

	public DataPacket(NBTTagCompound tag)
	{
		deserializeNBT(tag);
	}

	public DataPacket(List<DataVariable> entries)
	{
		entries.forEach(this::set);
	}

	/**
	 * Checks if the given variable name is valid.
	 *
	 * @param variableName the name of the variable to check, must be a single character
	 * @return true if the variable name is valid, false otherwise
	 */
	public static boolean isValidVariable(char variableName)
	{
		for(char name : VARIABLE_NAMES)
			if(name==variableName)
				return true;
		return false;
	}

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

	/**
	 * Evaluates a variable:
	 * <ul>
	 * <li>if it is an {@link DataTypeAccessor} it will access amd retirm the real value</li>
	 * <li>if it is a {@link DataTypeExpression} and allowExpressions is true, it will evaluate the expression and return the result</li>
	 * <li>otherwise it will return the actual value as is</li>
	 * </ul>
	 *
	 * @param actual           the actual value of the variable, can be null
	 * @param allowExpressions if true, expressions will be evaluated, if false, only the real value will be returned
	 * @return the evaluated value of the variable
	 */
	public DataType evaluateVariable(@Nullable DataType actual, boolean allowExpressions)
	{
		if(actual instanceof DataTypeAccessor)
			return ((DataTypeAccessor)actual).getRealValue(this);
		else if(actual instanceof DataTypeExpression&&allowExpressions)
			return ((DataTypeExpression)actual).getValue(this);
		else
			return actual;
	}

	/**
	 * Checks if this packet contains any variables.
	 *
	 * @return true if the packet is empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return variableMap.isEmpty();
	}

	/**
	 * Checks if this packet contains a variable with the given name.
	 *
	 * @param c the name of the variable
	 * @return true if the packet contains the variable, false otherwise
	 */
	public boolean has(Character c)
	{
		return variableMap.containsKey(c);
	}

	/**
	 * Checks if this packet contains all the given variables.
	 *
	 * @param names the names of the variables to check
	 * @return true if the packet contains all the variables, false otherwise
	 */
	public boolean has(Character... names)
	{
		for(Character c : names)
			if(!variableMap.containsKey(c))
				return false;
		return true;
	}

	/**
	 * Gets the variable with the given name.
	 *
	 * @param name the name of the variable
	 * @return the variable, or a {@link DataTypeNull} if it does not exist
	 */
	@Nonnull
	public DataType get(Character name)
	{
		return variableMap.getOrDefault(name, new DataTypeNull());
	}

	/**
	 * Sets the variable with the given name to the given type.
	 * If the variable already exists, it will be updated.
	 * If the variable does not exist, it will be added.
	 *
	 * @param name  the name of the variable
	 * @param value the value to set the variable
	 * @return true if the variable was set or added, false if the character is not valid
	 */
	public boolean set(Character name, DataType value)
	{
		if(ArrayUtils.contains(VARIABLE_NAMES, name))
		{
			variableMap.put(name, value);
			for(int i = 0; i < variableList.size(); i++)
				if(variableList.get(i).name==name)
				{
					variableList.set(i, new DataVariable(name, value));
					return true;
				}
			variableList.add(new DataVariable(name, value));
			return true;
		}
		return false;
	}

	/**
	 * Sets the variable with the given name to the given type.
	 *
	 * @param dataVariable the variable to set, must not be null
	 */
	public void set(@Nonnull DataVariable dataVariable)
	{
		set(dataVariable.name, dataVariable.value);
	}

	/**
	 * Sets the variable with the given name to the given type.
	 *
	 * @param name  the name of the variable, must be a single character from {@link #VARIABLE_NAMES}
	 * @param value the value to set the variable, must not be null
	 * @return this packet instance, for chaining
	 */
	@Nonnull
	public DataPacket with(Character name, DataType value)
	{
		set(name, value);
		return this;
	}

	/**
	 * Sets the variable with the given name to the given type.
	 *
	 * @param dataVariable the variable to set, must not be null
	 * @return this packet instance, for chaining
	 */
	@Nonnull
	public DataPacket with(@Nonnull DataVariable dataVariable)
	{
		set(dataVariable);
		return this;
	}

	/**
	 * Sets the packet color, used to identify the packet connector sub-network.
	 *
	 * @param color the color of the packet, if null, it will result to {@link EnumDyeColor#WHITE}
	 * @return this packet instance, for chaining
	 */
	public DataPacket withPacketColor(EnumDyeColor color)
	{
		this.packetColor = color;
		return this;
	}

	public DataPacket withPacketAddress(int address)
	{
		if(address >= -1)
			this.packetAddress = address;
		return this;
	}

	public boolean matchesConnector(EnumDyeColor connColor, int connAddress)
	{
		return (packetAddress==-1||packetAddress==connAddress)&&(packetColor==EnumDyeColor.WHITE||packetColor==connColor);
	}

	public void clear()
	{
		for(char c : VARIABLE_NAMES)
			remove(c);
	}

	public void remove(Character... names)
	{
		for(Character c : names)
			if(ArrayUtils.contains(VARIABLE_NAMES, c))
			{
				variableMap.remove(c);
				variableList.removeIf(variable -> variable.name==c);
			}
	}

	public boolean remove(Character c)
	{
		if(ArrayUtils.contains(VARIABLE_NAMES, c))
		{
			variableMap.remove(c);
			variableList.removeIf(variable -> variable.name==c);
			return true;
		}
		return false;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		for(DataVariable entry : variableList)
			nbt.setTag(String.valueOf(entry.name), entry.value.valueToNBT());

		if(packetColor!=EnumDyeColor.WHITE)
			nbt.setInteger("color", packetColor.getMetadata());
		if(packetAddress!=-1)
			nbt.setInteger("address", packetAddress);


		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		variableMap.clear();
		variableList.clear();
		for(Character c : VARIABLE_NAMES)
			if(nbt.hasKey(String.valueOf(c)))
			{
				NBTTagCompound n = nbt.getCompoundTag(String.valueOf(c));
				String type = n.getString("Type");
				if(IIDataTypeUtils.metaTypesByName.containsKey(type))
				{
					DataType data = IIDataTypeUtils.metaTypesByName.get(type).supplier.get();
					data.valueFromNBT(n);
					variableMap.put(c, data);
					variableList.add(new DataVariable(c, data));
				}
			}
		if(nbt.hasKey("color"))
			this.packetColor = EnumDyeColor.byMetadata(nbt.getInteger("color"));
		if(nbt.hasKey("address"))
			this.packetAddress = nbt.getInteger("address");
	}

	/**
	 * @return a list of all variables in this packet
	 */
	public List<DataVariable> getAllVariables()
	{
		return variableList;
	}

	/**
	 * @return an iterator over the variables in this packet
	 */
	@Override
	public Iterator<DataVariable> iterator()
	{
		return variableList.iterator();
	}

	/**
	 * Applies the given consumer to each variable in this packet.
	 *
	 * @param consumer the consumer to apply
	 */
	public void forEach(BiConsumer<Character, DataType> consumer)
	{
		variableMap.forEach(consumer);
	}

	/**
	 * @return a stream of all variables in this packet, useful for functional programming
	 */
	@Nonnull
	public Stream<DataVariable> stream()
	{
		return variableList.stream();
	}

	/**
	 * @return the count of variables in this packet
	 */
	public int size()
	{
		return variableList.size();
	}

	/**
	 * @return a deep copy of this packet
	 */
	@Override
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	public DataPacket clone()
	{
		DataPacket packet = new DataPacket();
		this.variableMap.forEach((character, dataType) -> {
			packet.variableMap.put(character, dataType.clone());
			packet.variableList.add(new DataVariable(character, dataType));
		});
		packet.packetColor = this.packetColor;
		packet.packetAddress = this.packetAddress;
		return packet;
	}

	@Override
	public String toString()
	{
		return this.serializeNBT().toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this==obj)
			return true;
		if(obj instanceof DataPacket)
		{
			DataPacket other = (DataPacket)obj;

			if(!variableMap.keySet().equals(other.variableMap.keySet()))
				return false;
			if(!matchesConnector(other.packetColor, other.packetAddress))
				return false;
			for(Entry<Character, DataType> entry : variableMap.entrySet())
				if(!other.get(entry.getKey()).toString().equals(entry.getValue().toString()))
					return false;

			return true;
		}
		return false;
	}
}
