package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType.IGenericDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class DataPacket implements Iterable<DataType>
{
	public Map<Character, DataType> variables = new HashMap<>();
	private EnumDyeColor packetColor = EnumDyeColor.WHITE;
	private int packetAddress = -1;

	public static final char[] varCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	public static final Map<String, Class<? extends DataType>> varTypes = new LinkedHashMap<>();

	public static final Map<Class<? extends DataType>, TypeMetaInfo<?>> metaTypesByClass = new LinkedHashMap<>();
	public static final Map<String, TypeMetaInfo<?>> metaTypesByName = new LinkedHashMap<>();

	static
	{
		//null
		registerType("null", DataTypeNull.class, DataTypeNull::new, IIColor.fromPackedRGB(0x8f2fb3));

		//logic types
		registerType("boolean", DataTypeBoolean.class, DataTypeBoolean::new, IIColor.fromPackedRGB(0x922020));

		//number types
		registerType("integer", DataTypeInteger.class, DataTypeInteger::new, IIColor.fromPackedRGB(0x26732e));
		registerType("float", DataTypeFloat.class, DataTypeFloat::new, IIColor.fromPackedRGB(0x0d6b68));
		registerType("vector", DataTypeVector.class, DataTypeVector::new, IIColor.fromPackedRGB(0x9d8900));
		registerGenericType("number", NumericDataType.class);

		//text types
		registerType("string", DataTypeString.class, DataTypeString::new, IIColor.fromPackedRGB(0xb86300));

		//statement types
		registerType("accessor", DataTypeAccessor.class, DataTypeAccessor::new, IIColor.fromPackedRGB(0x161c26));
		registerType("expression", DataTypeExpression.class, DataTypeExpression::new, IIColor.fromPackedRGB(0x2a4db4));

		//collection types
		registerType("array", DataTypeArray.class, DataTypeArray::new, IIColor.fromPackedRGB(0x520c2b));
		registerType("map", DataTypeMap.class, DataTypeMap::new, IIColor.fromPackedRGB(0x4d5914));
		registerGenericType("iterable", IterableDataType.class);

		//in-world types
		registerType("itemstack", DataTypeItemStack.class, DataTypeItemStack::new, IIColor.fromPackedRGB(0x121031));
		registerType("fluidstack", DataTypeFluidStack.class, DataTypeFluidStack::new, IIColor.fromPackedRGB(0x082730));
		registerType("entity", DataTypeEntity.class, DataTypeEntity::new, IIColor.fromPackedRGB(0x435e46));

		//cryptographic types
		registerType("encrypted", DataTypeEncrypted.class, DataTypeEncrypted::new, IIColor.fromPackedRGB(0x5a0d75));
	}

	private static <T extends DataType> void registerGenericType(String name, Class<T> klass)
	{
		//Find the fallback type of the generic type
		IGenericDataType generic = klass.getAnnotation(IGenericDataType.class);
		if(generic==null)
		{
			IILogger.error("Tried to register a generic type without the IGenericDataType annotation! ("+klass.getName()+")");
			return;
		}

		//Assign its meta-info to the generic type
		TypeMetaInfo<? extends DataType> metaInfo = IIDataHandlingUtils.getTypeMeta(generic.defaultType());
		metaTypesByName.put(name, metaInfo);
		metaTypesByClass.put(klass, metaInfo);
	}

	public static <T extends DataType> void registerType(String name, Class<T> klass, Supplier<T> supplier, IIColor color)
	{
		TypeMetaInfo<T> metaInfo = new TypeMetaInfo<>(name, klass, supplier, color);
		varTypes.put(name, klass);
		metaTypesByName.put(name, metaInfo);
		metaTypesByClass.put(klass, metaInfo);
	}

	@Nonnull
	public static DataType getVarFromNBT(NBTTagCompound nbt)
	{
		DataType data = getVarInstance(DataPacket.varTypes.get(nbt.getString("Type")));
		data.valueFromNBT(nbt);
		return data;
	}

	@Nonnull
	public static DataType getVarInstance(Class<? extends DataType> type)
	{
		if(type.isAnnotationPresent(IGenericDataType.class))
			type = type.getAnnotation(IGenericDataType.class).defaultType();

		TypeMetaInfo<?> meta = metaTypesByClass.get(type);
		return meta==null?new DataTypeNull(): meta.supplier.get();
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

			DataType type;
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
					DataType p = preferred.newInstance();
					return preferred.cast(p);
				} catch(InstantiationException|IllegalAccessException ignored)
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
				if(varTypes.containsKey(type))
				{
					DataType data = null;
					try
					{
						data = varTypes.get(type).newInstance();
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
				if(!other.getPacketVariable(entry.getKey()).valueToString().equals(entry.getValue().valueToString()))
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
