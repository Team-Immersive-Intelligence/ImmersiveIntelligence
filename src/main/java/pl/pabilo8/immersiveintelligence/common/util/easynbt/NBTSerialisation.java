package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidTank;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class is used to sync fields in any class using NBT<br>
 * To use it, add the {@link SyncNBT} annotation to the fields you want to be synced<br>
 * Any non-static field, declared or inherited with the annotation will be synced automatically<br>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 25.12.2023
 */
@SuppressWarnings({"rawtypes", "unused"})
public class NBTSerialisation
{
	/**
	 * Registry of all serializers for field types
	 */
	private static final HashMap<Class<?>, BiFunction<Field, SyncNBT, FieldSerializer<?, ?>>> serializerRegistry = new HashMap<>();
	/**
	 * Registry of all serializers for a class
	 */
	private static final HashMap<Class<?>, NBTSerializer<?>> serializers = new HashMap<>();

	static
	{
		//Register serializers for all primitive types
		registerSerializer(String.class, NBTTagString.class, NBTTagString::new, NBTTagString::getString);
		registerSerializer(int.class, NBTTagInt.class, NBTTagInt::new, NBTTagInt::getInt);
		registerSerializer(boolean.class, NBTTagByte.class, i -> new NBTTagByte((byte)(i?1: 0)), nbt -> nbt.getByte()==1);
		registerSerializer(float.class, NBTTagFloat.class, NBTTagFloat::new, NBTTagFloat::getFloat);
		registerSerializer(double.class, NBTTagDouble.class, NBTTagDouble::new, NBTTagDouble::getDouble);
		registerSerializer(long.class, NBTTagLong.class, NBTTagLong::new, NBTTagLong::getLong);
		registerSerializer(char.class, NBTTagString.class,
				c -> new NBTTagString(String.valueOf(c)),
				nbt -> nbt.getString().isEmpty()?'\0': nbt.getString().charAt(0)
		);
		registerSerializer(Enum.class, NBTTagString.class,
				e -> new NBTTagString(e.name()),
				(nbt, en) -> en.valueOf(en.getDeclaringClass(), nbt.getString())
		);

		//Register serializers for all primitive array types
		registerSerializer(int[].class, NBTTagIntArray.class, NBTTagIntArray::new, NBTTagIntArray::getIntArray);
//		registerSerializer(boolean[].class, NBTTagByteArray.class, NBTTagByteArray::new, NBTTagByteArray::getByteArray);

		//Register serializers for vanilla types
		registerSerializer(Vec3d.class, NBTTagList.class,
				vec -> new NBTTagList()
				{
					{
						appendTag(new NBTTagDouble(vec.x));
						appendTag(new NBTTagDouble(vec.y));
						appendTag(new NBTTagDouble(vec.z));
					}
				},
				nbt ->
				{
					double x = ((NBTTagDouble)nbt.get(0)).getDouble();
					double y = ((NBTTagDouble)nbt.get(1)).getDouble();
					double z = ((NBTTagDouble)nbt.get(2)).getDouble();
					return new Vec3d(x, y, z);
				}
		);

		//Register serializers for IE types
		//FluxStorage
		registerSerializer(FluxStorage.class, NBTTagInt.class, i -> new NBTTagInt(i.getEnergyStored()), (nbt, fluxStorage) ->
		{
			fluxStorage.setEnergy(nbt.getInt());
			return fluxStorage;
		});

		//FluidTank
		registerSerializer(FluidTank.class, NBTTagCompound.class,
				tank -> tank.writeToNBT(new NBTTagCompound()),
				(nbt, tank) -> tank.readFromNBT(nbt)
		);
		registerSerializer(MultiFluidTank.class, NBTTagCompound.class,
				tank -> tank.writeToNBT(new NBTTagCompound()),
				(nbt, tank) -> tank.readFromNBT(nbt)
		);

		//NonNullList<ItemStack> (used for inventories)
		registerSerializer(NonNullList.class, NBTTagList.class,
				list ->
				{
					NBTTagList nbt = new NBTTagList();
					for(ItemStack stack : (NonNullList<ItemStack>)list)
						nbt.appendTag(stack.serializeNBT());
					return nbt;
				},
				(nbt, list) ->
				{
					for(int i = 0; i < nbt.tagCount(); i++)
						list.set(i, new ItemStack(nbt.getCompoundTagAt(i)));
					return list;
				}
		);

		registerSerializer(
				INBTSerializable.class,
				NBTBase.class,
				INBTSerializable::serializeNBT,
				(nbt, field) -> {
					field.deserializeNBT(nbt);
					return field;
				}
		);

		registerSerializer(IIColor.class, NBTTagInt.class, color -> new NBTTagInt(color.getPackedRGB()), nbt -> IIColor.fromPackedRGB(nbt.getInt()));

		registerSerializer(
				DataVariable.class,
				NBTTagCompound.class,
				dataVariable -> {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					nbtTagCompound.setString("name", String.valueOf(dataVariable.getName()));
					nbtTagCompound.setTag("value", dataVariable.getValue().valueToNBT());
					return nbtTagCompound;
				},
				nbtTagCompound -> {
					char name = nbtTagCompound.getString("name").isEmpty()?'a': nbtTagCompound.getString("name").charAt(0);
					NBTTagCompound valueTag = nbtTagCompound.getCompoundTag("value");
					return new DataVariable(name, IIDataTypeUtils.getVarFromNBT(valueTag));
				}
		);
	}

	private static <FIELD, NBT extends NBTBase> void registerSerializer(Class<FIELD> dataClass, Class<NBT> nbtClass,
																		Function<FIELD, NBT> serialize, Function<NBT, FIELD> deserialize)
	{
		serializerRegistry.put(dataClass, (field, annotation) -> new FieldSerializer<FIELD, NBT>(field, annotation)
		{
			@Override
			protected FIELD fromNBT(NBT nbt)
			{
				return deserialize.apply(nbt);
			}

			@Override
			protected NBT toNBT(FIELD field)
			{
				return serialize.apply(field);
			}
		});
	}

	private static <FIELD, NBT extends NBTBase> void registerSerializer(Class<FIELD> dataClass, Class<NBT> nbtClass,
																		Function<FIELD, NBT> serialize,
																		BiFunction<NBT, FIELD, FIELD> deserialize)
	{
		serializerRegistry.put(dataClass, (field, annotation) -> new FieldSerializer<FIELD, NBT>(field, annotation)
		{
			@Override
			protected FIELD fromNBT(Object obj, NBT nbt)
			{
				FIELD invoke = null;
				try
				{
					invoke = (FIELD)getter.invoke(obj);
					return deserialize.apply(nbt, invoke);
				} catch(Throwable ignored)
				{

				}
				return invoke;
			}

			@Override
			protected FIELD fromNBT(NBT nbt)
			{
				return null;
			}

			@Override
			protected NBT toNBT(FIELD field)
			{
				return serialize.apply(field);
			}
		});
	}

	public static <T> void synchroniseFor(T obj, BiConsumer<NBTSerializer, T> action)
	{
		NBTSerializer<?> serializer = serializers.computeIfAbsent(obj.getClass(), NBTSerializer::new);
		action.accept(serializer, obj);
	}

	/**
	 * Serializes the given Object
	 */
	public static class NBTSerializer<T>
	{
		private final List<FieldSerializer<?, ?>> fields;
		private final HashMap<Integer, List<FieldSerializer<?, ?>>> timeFields;
		private final HashMap<SyncEvents, List<FieldSerializer<?, ?>>> eventFields;

		NBTSerializer(Class<T> clazz)
		{
			serializers.put(clazz, this);
			fields = new ArrayList<>();
			timeFields = new HashMap<>();
			eventFields = new HashMap<>();

			Field[] fields = clazz.getFields();

			//Get the max time value
			int maxTime = Arrays.stream(fields)
					.map(f -> f.getAnnotation(SyncNBT.class))
					.filter(Objects::nonNull)
					.mapToInt(SyncNBT::time)
					.max().orElse(1);

			for(Field field : fields)
				if(field.isAnnotationPresent(SyncNBT.class))
				{
					SyncNBT annotation = field.getAnnotation(SyncNBT.class);
					FieldSerializer<?, ?> serializer = getSerializerFor(field, annotation);

					//Field is not serializable
					if(serializer==null)
					{
						IILogger.error("Field "+field.getName()+" in "+clazz.getName()+" is not serializable!");
						continue;
					}

					//Add to all matching lists

					//All fields
					this.fields.add(serializer);
					//Fields synced on event
					for(SyncEvents event : annotation.events())
						eventFields.compute(event, (e, list) ->
						{
							if(list==null)
								list = new ArrayList<>();
							list.add(serializer);
							return list;
						});

					//Fields synced on time (modulus)
					if(annotation.time() > 0)
						for(int i = 1; i <= maxTime; i++)
							if(i%annotation.time()==0)
								timeFields.compute(i, (t, list) ->
								{
									if(list==null)
										list = new ArrayList<>();
									list.add(serializer);
									return list;
								});

				}

			//Check for fields that are not public, either the annotation or access level is incorrect
			Arrays.stream(clazz.getDeclaredFields())
					.filter(field -> field.isAnnotationPresent(SyncNBT.class))
					.filter(field -> !Modifier.isPublic(field.getModifiers()))
					.forEach(field -> IILogger.warn("SyncNBT field "+field.getName()+" in "+clazz.getName()+" is not public!"));
		}

		/**
		 * Gets the serializer for the given field based on its type
		 */
		@Nullable
		private FieldSerializer getSerializerFor(Field field, SyncNBT annotation)
		{
			for(Class<?> clazz : serializerRegistry.keySet())
				if(clazz.isAssignableFrom(field.getType()))
					return serializerRegistry.get(clazz).apply(field, annotation);
			return null;
		}

		/**
		 * Serializes all fields
		 *
		 * @param obj  The object
		 * @param into The NBT to serialize into
		 */
		public void serializeAll(T obj, NBTTagCompound into)
		{
			for(FieldSerializer<?, ?> field : fields)
				field.serializeField(obj, into);
		}

		/**
		 * Serializes all fields for the given time
		 *
		 * @param obj  The object
		 * @param into The NBT to serialize into
		 * @param time The time to serialize for
		 */
		public void serializeForTime(T obj, NBTTagCompound into, int time)
		{
			List<FieldSerializer<?, ?>> fields = timeFields.get(time);
			if(fields!=null)
				for(FieldSerializer<?, ?> field : fields)
					field.serializeField(obj, into);
		}

		/**
		 * Serializes all fields for the given event
		 *
		 * @param obj   The object
		 * @param into  The NBT to serialize into
		 * @param event The event to serialize for
		 */
		public void serializeForEvent(T obj, NBTTagCompound into, SyncEvents event)
		{
			List<FieldSerializer<?, ?>> fields = eventFields.get(event);
			if(fields!=null)
				for(FieldSerializer<?, ?> field : fields)
					field.serializeField(obj, into);
		}

		/**
		 * Deserializes all fields
		 */
		public void deserializeAll(T obj, NBTTagCompound from, boolean canSkip)
		{
			for(FieldSerializer<?, ?> field : fields)
				field.deserializeField(obj, from, canSkip);
		}
	}

	/**
	 * Syncs a specific field
	 */
	private static abstract class FieldSerializer<FIELD, NBT extends NBTBase>
	{
		private final String nbtName, fieldName;
		private final Field field;
		private final boolean canBeNull;
		protected MethodHandle getter, setter;

		FieldSerializer(@Nonnull Field field, SyncNBT annotation)
		{
			this.field = field;
			this.nbtName = !annotation.name().isEmpty()?annotation.name(): IIStringUtil.toSnakeCase(field.getName());
			this.fieldName = field.getName();
			this.canBeNull = annotation.nullable();

			field.setAccessible(true);

			try
			{
				//Get the getter method using LambdaMetaFactory
				MethodHandles.Lookup lookup = MethodHandles.lookup();
				getter = lookup.findGetter(field.getDeclaringClass(), fieldName, field.getType());
				setter = lookup.findSetter(field.getDeclaringClass(), fieldName, field.getType());

			} catch(NoSuchFieldException|IllegalAccessException e)
			{
				IILogger.error("Error serializing field "+fieldName+" in "+field.getDeclaringClass().getName());
			}
		}

		@SuppressWarnings("unchecked")
		void serializeField(@Nonnull Object obj, @Nonnull NBTTagCompound into)
		{
			try
			{
				FIELD value = (FIELD)getter.invoke(obj);
				if(value==null&&canBeNull)
					return;
				NBT nbt = toNBT(value);
				into.setTag(nbtName, nbt);
			} catch(Throwable e)
			{
				IILogger.error("Error serializing field "+fieldName+" in "+obj.getClass().getName());
			}
		}

		@SuppressWarnings("unchecked")
		void deserializeField(@Nonnull Object obj, @Nonnull NBTTagCompound from, boolean canSkip)
		{
			try
			{
				if(from.hasKey(nbtName))
					setter.invoke(obj, fromNBT(obj, (NBT)from.getTag(nbtName)));
				else
				{
					if(canSkip)
						return;
					if(canBeNull)
						setter.invoke(obj, null);
				}
			} catch(Throwable e)
			{
				IILogger.error("Error deserializing field "+fieldName+" in "+obj.getClass().getName());
			}
		}

		/**
		 * Override if you want to get the current value from the object
		 *
		 * @param obj The object
		 * @param nbt The nbt to deserialize from
		 * @return The deserialized value
		 */
		protected FIELD fromNBT(@Nonnull Object obj, NBT nbt)
		{
			return fromNBT(nbt);
		}

		protected abstract FIELD fromNBT(NBT nbt);

		protected abstract NBT toNBT(FIELD field);
	}
}
