package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

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

	public static EasyNBT wrapNBT(NBTTagCompound nbt)
	{
		return new EasyNBT(nbt);
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
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withCompound(String key, NBTTagCompound value)
	{
		wrapped.setTag(key, value);
		return this;
	}

	/**
	 * Appends an integer
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withCompound(String key, EasyNBT value)
	{
		wrapped.setTag(key, value.unwrap());
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
		return withCompound(key, value.serializeNBT());
	}

	/**
	 * Appends a FluidStack
	 *
	 * @param key name of this tag
	 */
	public EasyNBT withFluidStack(String key, FluidStack value)
	{
		return withCompound(key, value.writeToNBT(new NBTTagCompound()));
	}

	//--- Merging ---//

	public EasyNBT mergeWith(NBTTagCompound other)
	{
		wrapped.merge(other);
		return this;
	}

	public EasyNBT mergeWith(EasyNBT other)
	{
		wrapped.merge(other.wrapped);
		return this;
	}

	//--- Getting ---//


	//--- Check-Action ---//

	public void checkSetInt(String key, Consumer<Integer> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getInteger(key));
	}

	public void checkSetFloat(String key, Consumer<Float> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getFloat(key));
	}

	public void checkSetDouble(String key, Consumer<Double> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getDouble(key));
	}

	public void checkSetBoolean(String key, Consumer<Boolean> ifPresent)
	{
		if(wrapped.hasKey(key))
			ifPresent.accept(wrapped.getBoolean(key));
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
