package pl.pabilo8.immersiveintelligence.common.compat;

import lombok.experimental.UtilityClass;
import net.minecraft.nbt.*;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converts II data packets to Lua values and preserves their data types.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.10.2026
 * @since 01.10.2026
 */
@UtilityClass
public class LuaDataConverter
{
	private static final String NBT_TYPES = "__nbtTypes";
	private static final String LUA_TYPES = "__iiTypes";
	private static final int MAX_DEPTH = 64;
	private static final int MAX_COLLECTION_SIZE = 255;

	/**
	 * Converts packet variables to Lua values. Arrays and maps contain their entries directly.
	 */
	public static Map<String, Object> toLua(DataPacket packet)
	{
		Map<String, Object> result = new LinkedHashMap<>();
		Map<Object, Object> types = new LinkedHashMap<>();
		packet.forEach((name, value) -> {
			String key = String.valueOf(name);
			result.put(key, toLua(value, types, key, 0));
		});
		if(!types.isEmpty())
			result.put(LUA_TYPES, types);
		return result;
	}

	/**
	 * Reads packet variables from a Lua table. Rejects invalid values before delivery.
	 */
	public static DataPacket fromLua(Map<?, ?> table)
	{
		DataPacket packet = new DataPacket();
		Map<?, ?> types = optionalTable(member(table, LUA_TYPES));
		for(Map.Entry<?, ?> entry : table.entrySet())
		{
			String name = string(entry.getKey());
			if(name!=null&&name.length()==1&&DataPacket.isValidVariable(name.charAt(0)))
				try
				{
					packet.set(name.charAt(0), fromLua(entry.getValue(), lookup(types, name), 0));
				} catch(IllegalArgumentException exception)
				{
					throw new IllegalArgumentException("Invalid packet variable '"+name+"': "+exception.getMessage(), exception);
				}
		}
		return packet;
	}

	private static Object toLua(DataType value, Map<Object, Object> types, Object key, int depth)
	{
		checkDepth(depth);
		if(value instanceof DataTypeString)
			return ((DataTypeString)value).value;
		if(value instanceof DataTypeBoolean)
			return ((DataTypeBoolean)value).value;
		if(value instanceof DataTypeInteger)
			return ((DataTypeInteger)value).value;
		if(value instanceof DataTypeFloat)
		{
			types.put(key, "float");
			return ((DataTypeFloat)value).value;
		}
		if(value instanceof DataTypeArray)
		{
			DataType[] entries = ((DataTypeArray)value).value;
			checkCollectionSize(entries.length);
			Map<Object, Object> result = new LinkedHashMap<>();
			Map<Object, Object> children = new LinkedHashMap<>();
			for(int i = 0; i < entries.length; i++)
				result.put(i+1, toLua(entries[i], children, i+1, depth+1));
			types.put(key, collectionType("array", "Values", children));
			return result;
		}
		if(value instanceof DataTypeMap)
		{
			DataTypeMap entries = (DataTypeMap)value;
			checkCollectionSize(entries.size());
			Map<Object, Object> result = new LinkedHashMap<>();
			Map<Object, Object> children = new LinkedHashMap<>();
			for(Map.Entry<DataType, DataType> entry : entries.entrySet())
			{
				Map<Object, Object> entryTypes = new LinkedHashMap<>();
				Object luaKey = toLua(entry.getKey(), entryTypes, "Key", depth+1);
				if(luaKey instanceof Number)
				{
					double number = finiteNumber(luaKey);
					luaKey = number==0?0D: number;
				}
				if(result.containsKey(luaKey))
					throw new IllegalArgumentException("II map keys collide after Lua conversion");
				result.put(luaKey, toLua(entry.getValue(), entryTypes, "Value", depth+1));
				if(!entryTypes.isEmpty())
					children.put(luaKey, entryTypes);
			}
			types.put(key, collectionType("map", "Entries", children));
			return result;
		}

		Map<String, Integer> nbtTypes = new LinkedHashMap<>();
		Map<String, Object> result = compoundToLua(value.valueToNBT(), "", nbtTypes, depth);
		// Keep null as a typed table. Lua nil would remove the packet variable.
		result.put(NBT_TYPES, nbtTypes);
		return result;
	}

	private static DataType fromLua(Object value, Object typeHint, int depth)
	{
		checkDepth(depth);
		if("float".equals(string(typeHint)))
		{
			double number = finiteNumber(value);
			if(Math.abs(number) > Float.MAX_VALUE)
				throw new IllegalArgumentException("Number exceeds the II float range");
			return new DataTypeFloat((float)number);
		}
		Map<?, ?> collection = optionalTable(typeHint);
		String collectionName = string(member(collection, "Type"));
		if(collectionName!=null)
		{
			if(!(value instanceof Map))
				throw new IllegalArgumentException("Expected a collection table");
			if(!"array".equals(collectionName)&&!"map".equals(collectionName))
				throw new IllegalArgumentException("Unknown collection type: "+collectionName);
			return collectionFromLua((Map<?, ?>)value, collection, "array".equals(collectionName), depth);
		}
		if(value==null)
			return new DataTypeNull();
		if(value instanceof Boolean)
			return new DataTypeBoolean((Boolean)value);
		if(value instanceof Number)
		{
			double number = finiteNumber(value);
			if(number==Math.rint(number)&&number >= Integer.MIN_VALUE&&number <= Integer.MAX_VALUE)
				return new DataTypeInteger((int)number);
			if(Math.abs(number) > Float.MAX_VALUE)
				throw new IllegalArgumentException("Number exceeds the II float range");
			return new DataTypeFloat((float)number);
		}
		String text = string(value);
		if(text!=null)
			return new DataTypeString(text);
		if(!(value instanceof Map))
			throw new IllegalArgumentException("Expected a string, number, boolean or table");

		Map<?, ?> table = (Map<?, ?>)value;
		Object typeName = member(table, "Type");
		if(typeName!=null)
		{
			String name = string(typeName);
			if(name==null||!IIDataTypeUtils.metaTypesByName.containsKey(name))
				throw new IllegalArgumentException("Unknown II data Type: "+name);
			DataType data = IIDataTypeUtils.getVarInstance(name);
			Map<String, Integer> types = new LinkedHashMap<>();
			// Default fields also permit tables made directly in Lua, without metadata.
			compoundToLua(data.valueToNBT(), "", types, depth);
			Object metadata = member(table, NBT_TYPES);
			if(metadata!=null)
			{
				if(!(metadata instanceof Map))
					throw new IllegalArgumentException(NBT_TYPES+" must be a table");
				for(Map.Entry<?, ?> entry : ((Map<?, ?>)metadata).entrySet())
				{
					String path = string(entry.getKey());
					int id = (int)integer(entry.getValue(), 1, 12);
					if(path==null)
						throw new IllegalArgumentException("NBT type paths must be strings");
					types.put(path, id);
				}
			}
			NBTTagCompound nbt = compoundFromLua(table, "", types, depth);
			if(!name.equals(nbt.getString("Type")))
				throw new IllegalArgumentException("Type must be an NBT string");
			data.valueFromNBT(nbt);
			return data;
		}

		Object[] sequence = sequence(table);
		return collectionFromLua(table, collection, sequence!=null&&sequence.length > 0, depth);
	}

	private static Map<String, Object> collectionType(String type, String field, Map<Object, Object> children)
	{
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("Type", type);
		if(!children.isEmpty())
			result.put(field, children);
		return result;
	}

	private static DataType collectionFromLua(Map<?, ?> table, Map<?, ?> type, boolean array, int depth)
	{
		checkCollectionSize(table.size());
		if(array)
		{
			Object[] sequence = sequence(table);
			if(sequence==null)
				throw new IllegalArgumentException("II arrays require consecutive indices starting at 1");
			Map<?, ?> types = optionalTable(member(type, "Values"));
			DataType[] entries = new DataType[sequence.length];
			for(int i = 0; i < sequence.length; i++)
				entries[i] = fromLua(sequence[i], lookup(types, i+1), depth+1);
			return new DataTypeArray(entries);
		}
		Map<?, ?> types = optionalTable(member(type, "Entries"));
		DataTypeMap result = new DataTypeMap();
		for(Map.Entry<?, ?> entry : table.entrySet())
		{
			Map<?, ?> entryTypes = optionalTable(lookup(types, entry.getKey()));
			result.put(fromLua(entry.getKey(), member(entryTypes, "Key"), depth+1),
					fromLua(entry.getValue(), member(entryTypes, "Value"), depth+1));
		}
		return result;
	}

	private static Map<?, ?> optionalTable(Object value)
	{
		if(value==null)
			return Collections.emptyMap();
		if(!(value instanceof Map))
			throw new IllegalArgumentException("Type metadata must be a table");
		return (Map<?, ?>)value;
	}

	private static Object lookup(Map<?, ?> table, Object key)
	{
		Object value = table.get(key);
		if(value!=null)
			return value;
		String text = string(key);
		if(text!=null)
			return member(table, text);
		for(Map.Entry<?, ?> entry : table.entrySet())
			if(key instanceof Number&&entry.getKey() instanceof Number&&
					((Number)key).doubleValue()==((Number)entry.getKey()).doubleValue())
				return entry.getValue();
		return null;
	}

	private static void checkCollectionSize(int size)
	{
		if(size > MAX_COLLECTION_SIZE)
			throw new IllegalArgumentException("II collections can contain at most 255 entries");
	}

	private static Map<String, Object> compoundToLua(NBTTagCompound tag, String path, Map<String, Integer> types, int depth)
	{
		checkDepth(depth);
		Map<String, Object> result = new LinkedHashMap<>();
		for(String key : tag.getKeySet())
			result.put(key, nbtToLua(tag.getTag(key), childPath(path, key), types, depth+1));
		return result;
	}

	private static Object nbtToLua(NBTBase tag, String path, Map<String, Integer> types, int depth)
	{
		checkDepth(depth);
		types.put(path, (int)tag.getId());
		if(tag instanceof NBTTagCompound)
			return compoundToLua((NBTTagCompound)tag, path, types, depth);
		if(tag instanceof NBTTagString)
			return ((NBTTagString)tag).getString();
		if(tag instanceof NBTPrimitive)
		{
			NBTPrimitive number = (NBTPrimitive)tag;
			// Lua numbers cannot store all 64-bit integers exactly.
			if(tag.getId()==4)
				return Long.toString(number.getLong());
			return number.getDouble();
		}
		Map<Integer, Object> result = new LinkedHashMap<>();
		if(tag instanceof NBTTagList)
		{
			NBTTagList list = (NBTTagList)tag;
			for(int i = 0; i < list.tagCount(); i++)
				result.put(i+1, nbtToLua(list.get(i), childPath(path, Integer.toString(i+1)), types, depth+1));
		}
		else if(tag instanceof NBTTagByteArray)
		{
			byte[] array = ((NBTTagByteArray)tag).getByteArray();
			for(int i = 0; i < array.length; i++)
				result.put(i+1, (int)array[i]);
		}
		else if(tag instanceof NBTTagIntArray)
		{
			int[] array = ((NBTTagIntArray)tag).getIntArray();
			for(int i = 0; i < array.length; i++)
				result.put(i+1, array[i]);
		}
		else if(tag instanceof NBTTagLongArray)
		{
			// Minecraft 1.12 does not expose the long array through a getter.
			String literal = tag.toString();
			String body = literal.substring(3, literal.length()-1);
			if(!body.isEmpty())
			{
				String[] entries = body.split(",");
				for(int i = 0; i < entries.length; i++)
					result.put(i+1, entries[i].trim().replace("L", ""));
			}
		}
		else
			throw new IllegalArgumentException("Unsupported NBT type: "+tag.getId());
		return result;
	}

	private static NBTTagCompound compoundFromLua(Map<?, ?> table, String path, Map<String, Integer> types, int depth)
	{
		checkDepth(depth);
		NBTTagCompound result = new NBTTagCompound();
		for(Map.Entry<?, ?> entry : table.entrySet())
		{
			String key = string(entry.getKey());
			if(key==null)
				throw new IllegalArgumentException("NBT object keys must be strings");
			if(path.isEmpty()&&NBT_TYPES.equals(key))
				continue;
			result.setTag(key, nbtFromLua(entry.getValue(), childPath(path, key), types, depth+1));
		}
		String type = result.getString("Type");
		String entries = "array".equals(type)?"Values": "map".equals(type)?"Entries": "";
		if(!entries.isEmpty()&&result.getTagList(entries, 10).tagCount() > MAX_COLLECTION_SIZE)
			throw new IllegalArgumentException("II collections can contain at most 255 entries");
		return result;
	}

	private static NBTBase nbtFromLua(Object value, String path, Map<String, Integer> types, int depth)
	{
		checkDepth(depth);
		int id = types.getOrDefault(path, 0);
		if(id==0)
		{
			if(value instanceof Boolean)
				id = 1;
			else if(value instanceof Number)
			{
				double number = finiteNumber(value);
				id = number==Math.rint(number)&&number >= Integer.MIN_VALUE&&number <= Integer.MAX_VALUE?3: 6;
			}
			else if(string(value)!=null)
				id = 8;
			else if(value instanceof Map)
				id = !((Map<?, ?>)value).isEmpty()&&sequence((Map<?, ?>)value)!=null?9: 10;
		}
		switch(id)
		{
			case 1:
				return new NBTTagByte(value instanceof Boolean?(byte)((Boolean)value?1: 0): (byte)integer(value, Byte.MIN_VALUE, Byte.MAX_VALUE));
			case 2:
				return new NBTTagShort((short)integer(value, Short.MIN_VALUE, Short.MAX_VALUE));
			case 3:
				return new NBTTagInt((int)integer(value, Integer.MIN_VALUE, Integer.MAX_VALUE));
			case 4:
				return new NBTTagLong(longInteger(value));
			case 5:
			{
				double number = finiteNumber(value);
				if(Math.abs(number) > Float.MAX_VALUE)
					throw new IllegalArgumentException("NBT float is out of range");
				return new NBTTagFloat((float)number);
			}
			case 6:
				return new NBTTagDouble(finiteNumber(value));
			case 8:
				if(string(value)==null)
					throw new IllegalArgumentException("Expected an NBT string at "+path);
				return new NBTTagString(string(value));
			case 10:
				if(!(value instanceof Map))
					throw new IllegalArgumentException("Expected an NBT object at "+path);
				return compoundFromLua((Map<?, ?>)value, path, types, depth);
			case 7:
			case 9:
			case 11:
			case 12:
				return sequenceFromLua(value, path, types, depth, id);
			default:
				throw new IllegalArgumentException("Invalid NBT value at "+path);
		}
	}

	private static NBTBase sequenceFromLua(Object value, String path, Map<String, Integer> types, int depth, int id)
	{
		Object[] entries = value instanceof Map?sequence((Map<?, ?>)value): null;
		if(entries==null)
			throw new IllegalArgumentException("Expected consecutive table indices starting at 1 at "+path);
		if(id==7)
		{
			byte[] array = new byte[entries.length];
			for(int i = 0; i < entries.length; i++)
				array[i] = (byte)integer(entries[i], Byte.MIN_VALUE, Byte.MAX_VALUE);
			return new NBTTagByteArray(array);
		}
		if(id==11)
		{
			int[] array = new int[entries.length];
			for(int i = 0; i < entries.length; i++)
				array[i] = (int)integer(entries[i], Integer.MIN_VALUE, Integer.MAX_VALUE);
			return new NBTTagIntArray(array);
		}
		if(id==12)
		{
			long[] array = new long[entries.length];
			for(int i = 0; i < entries.length; i++)
				array[i] = longInteger(entries[i]);
			return new NBTTagLongArray(array);
		}
		NBTTagList list = new NBTTagList();
		boolean numeric = Arrays.stream(entries).allMatch(entry -> entry instanceof Number);
		for(int i = 0; i < entries.length&&numeric; i++)
			numeric = !types.containsKey(childPath(path, Integer.toString(i+1)));
		for(int i = 0; i < entries.length; i++)
		{
			NBTBase entry = numeric?new NBTTagDouble(finiteNumber(entries[i])):
					nbtFromLua(entries[i], childPath(path, Integer.toString(i+1)), types, depth+1);
			if(i > 0&&entry.getId()!=list.getTagType())
				throw new IllegalArgumentException("NBT lists must contain one tag type at "+path);
			list.appendTag(entry);
		}
		return list;
	}

	private static Object[] sequence(Map<?, ?> table)
	{
		Object[] ordered = new Object[table.size()];
		boolean[] seen = new boolean[ordered.length];
		for(Map.Entry<?, ?> entry : table.entrySet())
		{
			if(!(entry.getKey() instanceof Number))
				return null;
			double index = ((Number)entry.getKey()).doubleValue();
			if(index!=Math.rint(index)||index < 1||index > table.size())
				return null;
			int offset = (int)index-1;
			if(seen[offset])
				return null;
			seen[offset] = true;
			ordered[offset] = entry.getValue();
		}
		return ordered;
	}

	private static long integer(Object value, long minimum, long maximum)
	{
		double number = finiteNumber(value);
		if(number!=Math.rint(number)||number < minimum||number > maximum)
			throw new IllegalArgumentException("Integer is out of range");
		return (long)number;
	}

	private static long longInteger(Object value)
	{
		String text = string(value);
		if(text!=null)
			try
			{
				return Long.parseLong(text);
			} catch(NumberFormatException exception)
			{
				throw new IllegalArgumentException("Expected a decimal 64-bit integer", exception);
			}
		// Accept Lua numbers only in the range where every integer is exact.
		return integer(value, -9007199254740991L, 9007199254740991L);
	}

	private static double finiteNumber(Object value)
	{
		if(!(value instanceof Number))
			throw new IllegalArgumentException("Expected a number");
		double number = ((Number)value).doubleValue();
		if(Double.isNaN(number)||Double.isInfinite(number))
			throw new IllegalArgumentException("Numbers must be finite");
		return number;
	}

	private static String string(Object value)
	{
		if(value instanceof String)
			return (String)value;
		// OpenComputers can supply Lua strings as UTF-8 byte arrays.
		return value instanceof byte[]?new String((byte[])value, StandardCharsets.UTF_8): null;
	}

	private static Object member(Map<?, ?> table, String name)
	{
		for(Map.Entry<?, ?> entry : table.entrySet())
			if(name.equals(string(entry.getKey())))
				return entry.getValue();
		return null;
	}

	private static String childPath(String path, String key)
	{
		return path+"/"+key.replace("~", "~0").replace("/", "~1");
	}

	private static void checkDepth(int depth)
	{
		if(depth > MAX_DEPTH)
			throw new IllegalArgumentException("Tables are cyclic or exceed 64 nested levels");
	}
}
