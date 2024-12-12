package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * One to control them all...<br>
 * A utility class for convenient and easy {@link NBTTagCompound} construction<br>
 * Also features various other NBT related methods.
 *
 * @author Pabilo8
 * @since 03.09.2022
 */
@SuppressWarnings("unused")
//REFACTOR: 05.04.2024 EasyNBT checkSet to ifPresent
public class EasyNBT extends Constants.NBT
{
	private final NBTTagCompound wrapped;

	private EasyNBT(@Nullable NBTTagCompound wrapped)
	{
		this.wrapped = wrapped==null?new NBTTagCompound(): wrapped;
	}

	//--- Wrapping ---//

	public static EasyNBT newNBT()
	{
		return new EasyNBT(new NBTTagCompound());
	}

	public static EasyNBT wrapNBT(NBTTagCompound nbt)
	{
		return new EasyNBT(nbt);
	}

	public static EasyNBT wrapNBT(JsonObject json)
	{
		return parseEasyNBT(json.toString());
	}

	public static EasyNBT wrapNBT(ItemStack nbt)
	{
		return new EasyNBT(ItemNBTHelper.getTag(nbt));
	}

	public static EasyNBT parseEasyNBT(String format, Object... arguments)
	{
		return new EasyNBT(parseNBT(format, arguments));
	}

	//--- With ---//
	//TODO: 18.07.2024 use one "with" method

	/**
	 * Appends a serializable object
	 *
	 * @param key   name of this tag
	 * @param value value to be appended
	 * @param <T>   type of the value
	 * @return this
	 */
	public <T extends NBTBase> EasyNBT withSerializable(String key, INBTSerializable<T> value)
	{
		wrapped.setTag(key, value.serializeNBT());
		return this;
	}

	/**
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withInt(String key, int value)
	{
		wrapped.setInteger(key, value);
		return this;
	}

	/**
	 * Appends a byte
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withByte(String key, int value)
	{
		wrapped.setInteger(key, value);
		return this;
	}

	/**
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withFloat(String key, float value)
	{
		wrapped.setFloat(key, value);
		return this;
	}

	/**
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withDouble(String key, double value)
	{
		wrapped.setDouble(key, value);
		return this;
	}

	/**
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withBoolean(String key, boolean value)
	{
		wrapped.setBoolean(key, value);
		return this;
	}

	/**
	 * Appends a string
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withString(String key, String value)
	{
		wrapped.setString(key, value);
		return this;
	}

	/**
	 * Appends an enum value in form of a string
	 *
	 * @param key name of this tag
	 */
	public <E extends Enum<E> & ISerializableEnum> EasyNBT withEnum(String key, E value)
	{
		wrapped.setString(key, value.getName());
		return this;
	}

	/**
	 * Appends a list
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withList(String key, Object... value)
	{
		wrapped.setTag(key, listOf(value));
		return this;
	}

	/**
	 * Appends a list
	 *
	 * @param key name of this tag
	 */
	public <T, E extends NBTBase> EasyNBT withList(String key, Function<T, E> conversion, T... objects)
	{
		wrapped.setTag(key, listOf((Object[])Arrays.stream(objects).filter(Objects::nonNull).map(conversion).toArray(NBTBase[]::new)));
		return this;
	}

	/**
	 * Appends a list
	 *
	 * @param key name of this tag
	 */
	public <T, E extends NBTBase> EasyNBT withList(String key, Function<T, E> conversion, Collection<T> objects)
	{
		wrapped.setTag(key, listOf((Object[])objects.stream().filter(Objects::nonNull).map(conversion).toArray(NBTBase[]::new)));
		return this;
	}

	/**
	 * Appends an element to a new or existing list
	 *
	 * @param key name of this tag
	 */
	public EasyNBT appendList(String key, int type, NBTBase object)
	{
		NBTTagList tagList = wrapped.getTagList(key, type);
		if(!wrapped.hasKey(key))
			wrapped.setTag(key, tagList);
		tagList.appendTag(object);
		return this;
	}

	/**
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withIntArray(String key, int... value)
	{
		wrapped.setIntArray(key, value);
		return this;
	}

	/**
	 * Appends any value extending {@link NBTBase}, f.e. an {@link NBTTagInt}, {@link NBTTagCompound}, {@link NBTTagList}, etc.
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withTag(String key, NBTBase value)
	{
		wrapped.setTag(key, value);
		return this;
	}

	/**
	 * Appends an {@link EasyNBT} converted to {@link NBTTagCompound}
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withTag(String key, EasyNBT value)
	{
		wrapped.setTag(key, value.unwrap());
		return this;
	}

	/**
	 * Appends a {@link NBTTagCompound} consumer, like {@link net.minecraftforge.fluids.FluidTank#writeToNBT(NBTTagCompound)}
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withTag(String key, Consumer<NBTTagCompound> accepted)
	{
		NBTTagCompound tag = new NBTTagCompound();
		accepted.accept(tag);
		wrapped.setTag(key, tag);
		return this;
	}

	/**
	 * Appends a BlockPos
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withPos(String key, BlockPos value)
	{
		return withIntArray(key, value.getX(), value.getY(), value.getZ());
	}

	/**
	 * Appends a DimensionBlockPos
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withDimPos(String key, DimensionBlockPos value)
	{
		return withIntArray(key, value.getX(), value.getY(), value.getZ(), value.dimension);
	}

	/**
	 * Appends a Vec3d
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withVec3d(String key, Vec3d value)
	{
		return withList(key, value.x, value.y, value.z);
	}

	/**
	 * Appends a Vec3d
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withVec3d(String key, double x, double y, double z)
	{
		return withList(key, x, y, z);
	}

	/**
	 * Appends an ItemStack
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withItemStack(String key, ItemStack value)
	{
		return withTag(key, value.serializeNBT());
	}

	/**
	 * Appends an IngredientStack
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withIngredientStack(String key, IngredientStack value)
	{
		return withTag(key, value.writeToNBT(new NBTTagCompound()));
	}

	/**
	 * Appends a FluidStack
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withFluidStack(String key, FluidStack value)
	{
		return withTag(key, value.writeToNBT(new NBTTagCompound()));
	}

	//with color
	public EasyNBT withColor(String key, IIColor color)
	{
		return withInt(key, color.getPackedARGB());
	}


	/**
	 * Appends any value extending {@link NBTBase}, as well as common types such as int, double, String, etc.
	 *
	 * @param name  tag name
	 * @param value tag value
	 * @return this
	 */
	public EasyNBT withAny(String name, @Nullable Object value)
	{
		if(value==null)
			return this;
		if(value instanceof NBTBase)
			return withTag(name, (NBTBase)value);

		if(value instanceof Integer)
			return withInt(name, (Integer)value);
		if(value instanceof Byte)
			return withInt(name, (Byte)value);
		if(value instanceof Float)
			return withFloat(name, (Float)value);
		if(value instanceof Double)
			return withDouble(name, (Double)value);
		if(value instanceof Boolean)
			return withBoolean(name, (Boolean)value);
		if(value instanceof String)
			return withString(name, (String)value);

		if(value instanceof EasyNBT)
			return withTag(name, (EasyNBT)value);

		if(value instanceof DimensionBlockPos)
			return withDimPos(name, (DimensionBlockPos)value);
		if(value instanceof BlockPos)
			return withPos(name, (BlockPos)value);
		if(value instanceof Vec3d)
			return withVec3d(name, (Vec3d)value);
		if(value instanceof IStringSerializable)
			return withString(name, ((IStringSerializable)value).getName());
		if(value instanceof IIColor)
			return withColor(name, (IIColor)value);

		if(value instanceof ItemStack)
			return withItemStack(name, (ItemStack)value);
		if(value instanceof FluidStack)
			return withFluidStack(name, (FluidStack)value);

		if(value instanceof Object[])
			return withList(name, (Object[])value);
		if(value instanceof Collection)
			return withList(name, value);

		return this;
	}

	//--- Remove ---//

	/**
	 * Removes a Tag from the Compound
	 *
	 * @param key name of this tag
	 */
	public EasyNBT without(String key)
	{
		wrapped.removeTag(key);
		return this;
	}

	/**
	 * Removes multiple Tags from the Compound
	 *
	 * @param keys keys to be removed
	 */
	public EasyNBT without(String... keys)
	{
		for(String key : keys)
			wrapped.removeTag(key);
		return this;
	}

	//--- Lambda Expessions ---//

	/**
	 * Performs an action when a condition is met. Allows branching.<br>
	 * For example:<br>
	 * <code>
	 * nbt.conditionally(obj!=null, obj.toNBT()) //will not add the object if it's null
	 * </code>
	 *
	 * @param condition condition to be met
	 * @param whenTrue  action taken
	 */
	public EasyNBT conditionally(boolean condition, Consumer<EasyNBT> whenTrue)
	{
		if(condition)
			whenTrue.accept(this);
		return this;
	}

	/**
	 * Merges tags of this Compound into tags of another one
	 *
	 * @param accepted other Compound
	 * @return this
	 */
	public EasyNBT accept(Consumer<NBTTagCompound> accepted)
	{
		accepted.accept(wrapped);
		return this;
	}

	/**
	 * Filters tags of this Compound by removing those that are not present in the remaining array
	 *
	 * @param remaining array of tags to keep
	 * @return this
	 */
	public EasyNBT filter(String[] remaining)
	{
		ArrayList<String> keySet = new ArrayList<>(wrapped.getKeySet());
		Arrays.asList(remaining).forEach(keySet::remove);
		keySet.forEach(wrapped::removeTag);
		return this;
	}

	//--- Merging ---//

	/**
	 * Merges Tags of another Compound into this one
	 *
	 * @param other other Compound
	 * @return this
	 */
	public EasyNBT mergeWith(@Nullable NBTTagCompound other)
	{
		if(other!=null)
			wrapped.merge(other);
		return this;
	}

	/**
	 * Merges Tags of another Compound into this one
	 *
	 * @param other other Compound
	 * @return this
	 */
	public EasyNBT mergeWith(@Nullable EasyNBT other)
	{
		if(other!=null)
			wrapped.merge(other.wrapped);
		return this;
	}

	//--- Getting ---//

	/**
	 * Checks if a tag exists
	 *
	 * @param key name of this tag
	 */
	public boolean hasKey(String key)
	{
		return wrapped.hasKey(key);
	}

	/**
	 * Checks if a tag exists
	 *
	 * @param key name of this tag
	 */
	public boolean hasKey(IStringSerializable key)
	{
		return wrapped.hasKey(key.getName());
	}

	/**
	 * Checks if a tag exists
	 *
	 * @param key  name of this tag
	 * @param type of this tag
	 */
	public boolean hasKey(String key, int type)
	{
		return wrapped.hasKey(key)&&wrapped.getTag(key).getId()==type;
	}

	/**
	 * Gets an integer
	 *
	 * @param key name of this tag
	 */
	public int getInt(String key)
	{
		return wrapped.getInteger(key);
	}

	/**
	 * Gets a byte
	 *
	 * @param key name of this tag
	 */
	public byte getByte(String key)
	{
		return wrapped.getByte(key);
	}

	/**
	 * Gets a float
	 *
	 * @param key name of this tag
	 */
	public float getFloat(String key)
	{
		return wrapped.getFloat(key);
	}

	/**
	 * Gets a double
	 *
	 * @param key name of this tag
	 */
	public double getDouble(String key)
	{
		return wrapped.getDouble(key);
	}

	/**
	 * Gets a boolean
	 *
	 * @param key name of this tag
	 */
	public boolean getBoolean(String key)
	{
		return wrapped.getBoolean(key);
	}

	/**
	 * Gets a string
	 *
	 * @param key name of this tag
	 */
	public String getString(String key)
	{
		return wrapped.getString(key);
	}

	/**
	 * Gets an {@link NBTTagList}
	 *
	 * @param key  name of this tag
	 * @param type of the contained tags, i.e. {@link #TAG_INT}, {@link #TAG_STRING}
	 */
	public NBTTagList getList(String key, int type)
	{
		return wrapped.getTagList(key, type);
	}

	/**
	 * Streams an {@link NBTTagList}
	 *
	 * @param clazz class of the tags, must to extend {@link NBTBase}
	 * @param key   name of this tag
	 * @param type  of the contained tags, i.e. {@link #TAG_INT}, {@link #TAG_STRING}
	 */
	public <T extends NBTBase> Stream<T> streamList(Class<T> clazz, String key, int type)
	{
		if(!wrapped.hasKey(key))
			return Stream.empty();
		return wrapped.getTagList(key, type).tagList.stream().map(n -> (T)n);
	}

	/**
	 * Streams an {@link NBTTagList}, gets an automatic
	 *
	 * @param key   name of this tag
	 * @param clazz class of the tags, must to extend {@link NBTBase}
	 */
	public <T extends NBTBase> Stream<T> streamList(Class<T> clazz, String key)
	{
		return streamList(clazz, key, getTagIDByClass(clazz));
	}

	/**
	 * Gets an integer array
	 *
	 * @param key name of this tag
	 */
	public int[] getIntArray(String key)
	{
		return wrapped.getIntArray(key);
	}

	/**
	 * Gets an {@link NBTTagCompound}
	 *
	 * @param key name of this tag
	 */
	public NBTTagCompound getCompound(String key)
	{
		return wrapped.getCompoundTag(key);
	}

	/**
	 * Gets an {@link NBTTagCompound} wrapped by {@link EasyNBT}
	 *
	 * @param key name of this tag
	 */
	public EasyNBT getEasyCompound(String key)
	{
		return new EasyNBT(wrapped.getCompoundTag(key));
	}

	/**
	 * Gets a {@link BlockPos}
	 *
	 * @param key name of this tag
	 */
	public BlockPos getPos(String key)
	{
		int[] pos = getIntArray(key);
		if(pos.length > 2)
			return new BlockPos(pos[0], pos[1], pos[2]);

		//only if key is present
		if(wrapped.hasKey(key))
			IILogger.error("Malformed BlockPos tag for \""+key+"\" in"+Arrays.toString(new Throwable().getStackTrace()));

		return BlockPos.ORIGIN;
	}

	/**
	 * Gets a {@link DimensionBlockPos}
	 *
	 * @param key name of this tag
	 */
	public DimensionBlockPos getDimPos(String key)
	{
		int[] pos = getIntArray(key);
		if(pos.length > 3)
			return new DimensionBlockPos(pos[0], pos[1], pos[2], pos[3]);

		//only if key is present
		if(wrapped.hasKey(key))
			IILogger.error("Malformed DimensionBlockPos tag for \""+key+"\" in"+Arrays.toString(new Throwable().getStackTrace()));

		return new DimensionBlockPos(BlockPos.ORIGIN, 0);
	}

	/**
	 * Gets a Vec3d
	 *
	 * @param key name of this tag
	 */
	public Vec3d getVec3d(String key)
	{
		NBTTagList pos = getList(key, TAG_DOUBLE);

		if(pos.tagCount() > 2)
		{
			NBTTagDouble x = (NBTTagDouble)pos.get(0);
			NBTTagDouble y = (NBTTagDouble)pos.get(1);
			NBTTagDouble z = (NBTTagDouble)pos.get(2);

			return new Vec3d(x.getDouble(), y.getDouble(), z.getDouble());
		}

		//only if key is present
		if(wrapped.hasKey(key))
			IILogger.error("Malformed Vec3D tag for \""+key+"\" in"+Arrays.toString(new Throwable().getStackTrace()));

		return Vec3d.ZERO;
	}

	/**
	 * Gets an ItemStack
	 *
	 * @param key name of this tag
	 */
	public ItemStack getItemStack(String key)
	{
		return new ItemStack(getCompound(key));
	}

	/**
	 * Gets an IngredientStack
	 *
	 * @param key name of this tag
	 */
	public IngredientStack getIngredientStack(String key)
	{
		return IngredientStack.readFromNBT(getCompound(key));
	}

	/**
	 * Gets a FluidStack
	 *
	 * @param key name of this tag
	 */
	public FluidStack getFluidStack(String key)
	{
		return FluidStack.loadFluidStackFromNBT(getCompound(key));
	}

	/**
	 * Gets an enum value from a string
	 *
	 * @param key  name of this tag
	 * @param type enum class
	 */
	public <E extends Enum<E> & ISerializableEnum> E getEnum(String key, Class<E> type)
	{
		return IIUtils.enumValue(type, getString(key));
	}

	public IIColor getColor(String key)
	{
		return IIColor.fromPackedARGB(getInt(key));
	}


	//--- Check-Action ---//

	public EasyNBT checkSetInt(String key, Consumer<Integer> ifPresent, int ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getInteger(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetInt(String key, Consumer<Integer> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getInteger(key));
		return this;
	}

	public EasyNBT checkSetByte(String key, Consumer<Byte> ifPresent, byte ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getByte(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetByte(String key, Consumer<Byte> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getByte(key));
		return this;
	}

	public EasyNBT checkSetFloat(String key, Consumer<Float> ifPresent, float ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getFloat(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetFloat(String key, Consumer<Float> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getFloat(key));
		return this;
	}

	public EasyNBT checkSetDouble(String key, Consumer<Double> ifPresent, double ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getDouble(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetDouble(String key, Consumer<Double> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getDouble(key));
		return this;
	}

	public EasyNBT checkSetBoolean(String key, Consumer<Boolean> ifPresent, boolean ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getBoolean(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetBoolean(String key, Consumer<Boolean> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getBoolean(key));
		return this;
	}

	public EasyNBT checkSetString(String key, Consumer<String> ifPresent, String ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getString(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetString(String key, Consumer<String> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getString(key));
		return this;
	}

	public EasyNBT checkSetCompound(String key, Consumer<NBTTagCompound> ifPresent, NBTTagCompound ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getCompoundTag(key));
		else
			ifPresent.accept(ifNot);
		return this;
	}

	public EasyNBT checkSetCompound(String key, Consumer<NBTTagCompound> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getCompoundTag(key));
		return this;
	}

	public <T extends NBTBase> EasyNBT checkSetTag(String key, Class<T> clazz, Consumer<T> ifPresent)
	{
		if(wrapped.hasKey(key))
		{
			NBTBase tag = wrapped.getTag(key);
			if(clazz.isInstance(tag))
				ifPresent.accept(clazz.cast(tag));
		}
		return this;
	}

	public EasyNBT checkSetVec3D(String key, Consumer<Vec3d> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(getVec3d(key));
		return this;
	}

	public EasyNBT checkSetColor(String key, Consumer<IIColor> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(getColor(key));
		return this;
	}

	public <E extends Enum<E> & ISerializableEnum> EasyNBT checkSetEnum(String key, Class<E> type, Consumer<E> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(getEnum(key, type));
		return this;
	}

	//--- Pseudo - Map ---//

	public int size()
	{
		return wrapped.getSize();
	}

	public boolean isEmpty()
	{
		return wrapped.hasNoTags();
	}

	public boolean containsKey(Object key)
	{
		return key instanceof String&&wrapped.hasKey(((String)key));
	}

	//--- Unwrapping ---//

	/**
	 * @return the NBT created using this wrapper
	 */
	public NBTTagCompound unwrap()
	{
		return wrapped;
	}

	/**
	 * @return the NBT Compound as Map
	 */
	@SuppressWarnings("deprecation")
	public Map<String, NBTBase> asMap()
	{
		return ReflectionHelper.getPrivateValue(NBTTagCompound.class, wrapped.copy(), "tagMap");
	}

	/**
	 * @return a json string representation of the NBT
	 */
	@Override
	public String toString()
	{
		return wrapped.toString();
	}

	//--- General Utils ---//

	public static NBTTagCompound parseNBT(String format, Object... arguments)
	{
		String json = String.format(format, arguments);
		//Error-Proof(tm)
		try {return JsonToNBT.getTagFromJson(json);} catch(NBTException ignored)
		{
			//well, at least I think so
			return new NBTTagCompound();
		}
	}

	public <T extends NBTBase> byte getTagIDByClass(Class<T> clazz)
	{
		try
		{
			return clazz.newInstance().getId();
		} catch(InstantiationException|IllegalAccessException ignored)
		{
			return 0;
		}
	}

	/**
	 * Thus spoke EasyNBT - a parser for all and none
	 *
	 * @param elements valid objects such as {@link net.minecraft.nbt.NBTBase}, int, float, double, boolean,
	 *                 {@link EasyNBT}, {@link BlockPos}, {@link net.minecraft.util.math.Vec3d}, {@link net.minecraft.item.ItemStack},
	 *                 {@link net.minecraftforge.fluids.FluidStack} and {@link java.util.Collection} or array of the above
	 * @implNote Accepts only a single type of object, will not work if multiple types are passed
	 */
	public static NBTTagList listOf(Object... elements)
	{
		NBTTagList list = new NBTTagList();

		for(Object element : elements)
		{
			if(element==null)
				continue;
			if(element instanceof NBTBase)
				list.appendTag(((NBTBase)element));

			else if(element instanceof Integer)
				list.appendTag(new NBTTagInt(((Integer)element)));
			else if(element instanceof Long)
				list.appendTag(new NBTTagLong(((Long)element)));
			else if(element instanceof Short)
				list.appendTag(new NBTTagShort(((Short)element)));
			else if(element instanceof Byte)
				list.appendTag(new NBTTagByte(((Byte)element)));
			else if(element instanceof Float)
				list.appendTag(new NBTTagFloat(((Float)element)));
			else if(element instanceof Double)
				list.appendTag(new NBTTagDouble(((Double)element)));
			else if(element instanceof Boolean)
				list.appendTag(new NBTTagByte((byte)(((Boolean)element)?1: 0)));

			else if(element instanceof EasyNBT)
				list.appendTag(((EasyNBT)element).wrapped);

			else if(element instanceof DimensionBlockPos)
			{
				DimensionBlockPos pos = (DimensionBlockPos)element;
				list.appendTag(listOf(pos.getX(), pos.getY(), pos.getZ(), pos.dimension));
			}
			else if(element instanceof BlockPos)
			{
				BlockPos pos = (BlockPos)element;
				list.appendTag(listOf(pos.getX(), pos.getY(), pos.getZ()));
			}
			else if(element instanceof Vec3d)
			{
				Vec3d pos = (Vec3d)element;
				list.appendTag(listOf(pos.x, pos.y, pos.z));
			}

			else if(element instanceof ItemStack)
				list.appendTag(((ItemStack)element).serializeNBT());
			else if(element instanceof FluidStack)
				list.appendTag(((FluidStack)element).writeToNBT(new NBTTagCompound()));

			else if(element instanceof int[])
				list.appendTag(new NBTTagIntArray(((int[])element)));
			else if(element instanceof Object[])
				list.appendTag(listOf(element));
			else if(element instanceof Collection)
				list.appendTag(listOf(((Collection<?>)element).toArray(new Object[0])));

		}

		return list;
	}

	public static NBTTagIntArray intArrayOf(int... ints)
	{
		return new NBTTagIntArray(ints);
	}
}
