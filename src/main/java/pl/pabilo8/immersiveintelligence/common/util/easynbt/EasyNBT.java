package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
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
public class EasyNBT extends Constants.NBT
{
	private final NBTTagCompound wrapped;

	private EasyNBT(NBTTagCompound wrapped)
	{
		this.wrapped = wrapped;
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

	public static EasyNBT wrapNBT(ItemStack nbt)
	{
		return new EasyNBT(ItemNBTHelper.getTag(nbt));
	}

	public static EasyNBT parseEasyNBT(String format, Object... arguments)
	{
		return new EasyNBT(parseNBT(format, arguments));
	}

	//--- With ---//

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
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withList(String key, Object... value)
	{
		wrapped.setTag(key, listOf(value));
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
	 * Appends an ItemStack
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withItemStack(String key, ItemStack value)
	{
		return withTag(key, value.serializeNBT());
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
	 * Appends a Vec3d
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
	 * Appends an ItemStack
	 *
	 * @param key name of this tag
	 */
	public ItemStack getItemStack(String key)
	{
		return new ItemStack(getCompound(key));
	}

	/**
	 * Appends a FluidStack
	 *
	 * @param key name of this tag
	 */
	public FluidStack getFluidStack(String key)
	{
		return FluidStack.loadFluidStackFromNBT(getCompound(key));
	}

	//--- Check-Action ---//

	public void checkSetInt(String key, Consumer<Integer> ifPresent, int ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getInteger(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetInt(String key, Consumer<Integer> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getInteger(key));
	}

	public void checkSetByte(String key, Consumer<Byte> ifPresent, byte ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getByte(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetByte(String key, Consumer<Byte> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getByte(key));
	}

	public void checkSetFloat(String key, Consumer<Float> ifPresent, float ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getFloat(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetFloat(String key, Consumer<Float> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getFloat(key));
	}

	public void checkSetDouble(String key, Consumer<Double> ifPresent, double ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getDouble(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetDouble(String key, Consumer<Double> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getDouble(key));
	}

	public void checkSetBoolean(String key, Consumer<Boolean> ifPresent, boolean ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getBoolean(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetBoolean(String key, Consumer<Boolean> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getBoolean(key));
	}

	public void checkSetString(String key, Consumer<String> ifPresent, String ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getString(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetString(String key, Consumer<String> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getString(key));
	}

	public void checkSetCompound(String key, Consumer<NBTTagCompound> ifPresent, NBTTagCompound ifNot)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getCompoundTag(key));
		else
			ifPresent.accept(ifNot);
	}

	public void checkSetCompound(String key, Consumer<NBTTagCompound> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getCompoundTag(key));
	}

	public <T extends NBTBase> void checkSetTag(String key, Class<T> clazz, Consumer<T> ifPresent)
	{
		if(wrapped.hasKey(key))
		{
			NBTBase tag = wrapped.getTag(key);
			if(clazz.isInstance(tag))
				ifPresent.accept(clazz.cast(tag));
		}
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

		if(elements.length==0)
			return list;

		for(Object element : elements)
		{
			if(element instanceof NBTBase)
				list.appendTag(((NBTBase)element));

			else if(element instanceof Integer)
				list.appendTag(new NBTTagInt(((Integer)element)));
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
			{
				list.appendTag(((ItemStack)element).serializeNBT());
			}
			else if(element instanceof FluidStack)
			{
				list.appendTag(((FluidStack)element).writeToNBT(new NBTTagCompound()));
			}

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
