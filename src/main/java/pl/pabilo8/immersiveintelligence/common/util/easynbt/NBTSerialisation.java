package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityFluidInserter.InserterTaskFluid;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityFluidInserter.InserterTaskLatexCollectorDrain;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityFluidInserter.InserterTaskMilkCow;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter.InserterTaskFromMinecart;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter.InserterTaskIntoMinecart;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter.InserterTaskItem;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter.InserterTaskPlaceBlock;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
	private static final HashMap<Class<?>, BiFunction<Field, SyncNBT, FieldSerializer<?, ?>>> serializerRegistry = new LinkedHashMap<>();
	/**
	 * Registry of all serializers for a class
	 */
	private static final ConcurrentHashMap<Class<?>, NBTSerializer<?>> serializers = new ConcurrentHashMap<>();
	/**
	 * Polymorphic type registry for ITypeNBTSerializable implementations.
	 */
	private static final ConcurrentHashMap<Class<? extends ITypeNBTSerializable>, TypeSerializationData> typeSerializers = new ConcurrentHashMap<>();
	/**
	 * Reference to polymorphic type serialization data by subtype names.
	 */
	private static final ConcurrentHashMap<String, TypeSerializationData> nameToSerializers = new ConcurrentHashMap<>();

	/**
	 * Basic, implementation-independent functionality.
	 */
	public static void preInit()
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
		//noinspection unchecked,AccessStaticViaInstance
		registerSerializer(Enum.class, NBTTagString.class,
				e -> new NBTTagString(e.name().toLowerCase()),
				(nbt, en) -> en.valueOf(en.getDeclaringClass(), nbt.getString().toUpperCase())
		);

		//Register serializers for all primitive array types
		registerSerializer(int[].class, NBTTagIntArray.class, NBTTagIntArray::new, NBTTagIntArray::getIntArray);

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

		registerSerializer(EnumFacing.class, NBTTagInt.class,
				facing -> new NBTTagInt(facing.getIndex()),
				nbt -> EnumFacing.getFront(nbt.getInt())
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
					//noinspection unchecked
					for(ItemStack stack : (NonNullList<ItemStack>)list)
						nbt.appendTag(stack.serializeNBT());
					return nbt;
				},
				(nbt, list) ->
				{
					for(int i = 0; i < nbt.tagCount(); i++)
						//noinspection unchecked
						list.set(i, new ItemStack(nbt.getCompoundTagAt(i)));
					return list;
				}
		);

		registerSerializer(ItemStack.class, NBTTagCompound.class, ItemStack::serializeNBT, nbt -> new ItemStack(nbt));
		registerSerializer(FluidStack.class, NBTTagCompound.class, fluidStack -> {
			NBTTagCompound nbt = new NBTTagCompound();
			fluidStack.writeToNBT(nbt);
			return nbt;
		}, FluidStack::loadFluidStackFromNBT);

		registerSerializer(UUID.class, NBTTagString.class,
				uuid -> new NBTTagString(uuid.toString()),
				nbt -> nbt.getString().isEmpty()?null: UUID.fromString(nbt.getString())
		);

		registerSerializer(
				OwnerIdentity.class, NBTTagString.class,
				ownerIdentity -> new NBTTagString(ownerIdentity==null?"00000000-0000-0000-0000-000000000000": ownerIdentity.getStringUUID()),
				nbt -> DiplomacyHandler.getIdentityByUUIDStatic(nbt.getString())
		);

		registerSerializer(ITypeNBTSerializable.class, NBTTagCompound.class,
				type -> {
					NBTTagCompound nbt = new NBTTagCompound();
					if(type==null)
					{
						nbt.setString("type", "null");
						return nbt;
					}
					TypeSerializationData data = typeSerializers.get(type.getClass());
					//noinspection unchecked
					return data!=null?data.serialize(type): nbt;
				},
				(nbtTagCompound, type) -> {
					TypeSerializationData data = nameToSerializers.get(nbtTagCompound.getString("type"));
					if(data==null)
						return null;
					return data.deserialize(nbtTagCompound, type);
				});

		registerSerializer(
				INBTSerializable.class, NBTBase.class,
				INBTSerializable::serializeNBT,
				(nbt, field) -> {
					//noinspection unchecked
					field.deserializeNBT(nbt);
					return field;
				}
		);
	}

	/**
	 * Additional, mod-specific functionality.
	 */
	public static void postInit()
	{
		registerSerializer(IIColor.class, NBTTagInt.class, color -> new NBTTagInt(color.getPackedRGB()), nbt -> IIColor.fromPackedRGB(nbt.getInt()));

		registerSerializer(
				DataVariable.class, NBTTagCompound.class,
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

		registerSerializer(WireType.class, NBTTagString.class,
				wireType -> new NBTTagString(wireType==null?"": wireType.getUniqueName()),
				nbt -> {
					String string = nbt.getString();
					return string.isEmpty()?null: WireType.getValue(string);
				}
		);

		//Inserter tasks
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskItem.class);
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskPlaceBlock.class);
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskFromMinecart.class);
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskIntoMinecart.class);
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskFluid.class);
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskLatexCollectorDrain.class);
		NBTSerialisation.registerPolimorphicTypeClass(InserterTaskMilkCow.class);


	}

	public static <FIELD, NBT extends NBTBase> void registerSerializer(Class<FIELD> dataClass, Class<NBT> nbtClass,
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
		IILogger.debug("Registered NBT serializer for "+dataClass.getName());
	}

	public static <FIELD, NBT extends NBTBase> void registerSerializer(Class<FIELD> dataClass, Class<NBT> nbtClass,
	                                                                   Function<FIELD, NBT> serialize,
	                                                                   BiFunction<NBT, FIELD, FIELD> deserialize)
	{
		serializerRegistry.put(dataClass, (field, annotation) -> new FieldSerializer<FIELD, NBT>(field, annotation)
		{
			@Override
			protected FIELD fromNBT(@Nonnull Object obj, NBT nbt)
			{
				FIELD invoke = null;
				try
				{
					//noinspection unchecked
					invoke = (FIELD)getter.invoke(obj);
					return deserialize.apply(nbt, invoke);
				} catch(Throwable e)
				{
					IILogger.error("NBT Deserialization error for field "+field.getName()+": "+e+", NBT: "+nbt.toString());
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
		IILogger.debug("Registered NBT serializer for "+dataClass.getName());
	}

	/**
	 * Registers an ITypeNBTSerializable implementation for polymorphic serialization.
	 *
	 * @param clazz The class to register. It and its extending classes have a public no-arg constructor.
	 */
	public static <T extends ITypeNBTSerializable> void registerPolimorphicTypeClass(@Nonnull Class<T> clazz)
	{
		Class<? extends ITypeNBTSerializable> parent = clazz;
		//Find the topmost parent class that implements ITypeNBTSerializable, to ensure the type is registered under the correct name
		for(Class<?> c = clazz; c!=null&&ITypeNBTSerializable.class.isAssignableFrom(c); c = c.getSuperclass())
			//noinspection unchecked
			parent = (Class<? extends ITypeNBTSerializable>)c;

		//Get serializer data for parent type (stores data for this and other sub-types)
		TypeSerializationData data = typeSerializers.computeIfAbsent(parent, TypeSerializationData::new);
		//Add link to serializer for this exact type
		typeSerializers.put(clazz, data);
		//Add the class and a supplier to the registry
		//noinspection unchecked
		data.addSubType(clazz);
	}

	public static <T> void synchroniseFor(T obj, BiConsumer<NBTSerializer, T> action)
	{
		NBTSerializer<?> serializer = serializers.computeIfAbsent(obj.getClass(), NBTSerializer::new);
		action.accept(serializer, obj);
	}

	public static ITypeNBTSerializable deserializePolymorphic(@Nonnull NBTTagCompound current)
	{
		return nameToSerializers.get(current.getString("type")).deserialize(current);
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
				{
					into.setTag(nbtName, new NBTTagCompound());
					return;
				}
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
					if(canBeNull&&from.getTag(nbtName).hasNoTags())
						setter.invoke(obj, null);
					else
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
				IILogger.error("Error deserializing field "+fieldName+" in "+obj.getClass().getName()+", "+e);
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

	public static class TypeSerializationData<T extends ITypeNBTSerializable>
	{
		private final Class<T> baseType;
		private HashMap<String, Supplier<T>> suppliersFromName = new HashMap<>();
		private HashMap<Class<? extends T>, String> nameFromClass = new HashMap<>();

		public TypeSerializationData(Class<T> baseType)
		{
			this.baseType = baseType;
			nameToSerializers.put(baseType.getSimpleName(), this);
		}

		@SuppressWarnings("unchecked")
		public void addSubType(Class<T> clazz)
		{
			try
			{
				//Get the constructor
				Class<ITypeNBTSerializable> type = (Class<ITypeNBTSerializable>)clazz;
				Constructor<ITypeNBTSerializable> constructor = type.getDeclaredConstructor();
				constructor.setAccessible(true);

				//Turn the public no-arg constructor into a Supplier
				Supplier<ITypeNBTSerializable> supplier = () -> {
					try
					{
						return constructor.newInstance();
					} catch(Exception ignored)
					{
						return null;
					}
				};
				try
				{
					//Ensure it works
					supplier.get();

					//Register the type
					String name = clazz.getSimpleName();
					nameFromClass.put(clazz, name);
					suppliersFromName.put(name, (Supplier<T>)supplier);
					nameToSerializers.put(name, this);
					IILogger.debug("Registered NBT polymorphic type \""+name+"\" -> "+clazz.getName());
				} catch(Throwable e)
				{
					throw new RuntimeException("Could not instantiate type \""+clazz.getName()+"\"");
				}
			} catch(Exception e)
			{
				IILogger.error("Could not find no-arg constructor for type \""+clazz.getName()+"\"", e);
			}
		}

		@Nonnull
		public NBTTagCompound serialize(T type)
		{
			if(type==null)
				return new NBTTagCompound();

			NBTTagCompound nbt = new NBTTagCompound();
			String typeId = nameFromClass.get(type.getClass());
			if(typeId==null)
			{
				IILogger.error("Unrecognized NBT polymorphic type \""+type.getClass().getName()+"\"");
				return nbt;
			}

			nbt.setString("type", typeId);
			nbt.setTag("value", type.serializeNBT());
			return nbt;
		}

		@Nullable
		public T deserialize(NBTTagCompound from)
		{
			return deserialize(from, null);
		}

		/**
		 * Deserializes a polymorphic value and reuses the current instance when its subtype matches.
		 */
		@Nullable
		public T deserialize(NBTTagCompound from, @Nullable T current)
		{
			String typeId = from.getString("type");
			if(typeId.isEmpty()||"null".equals(typeId))
				return null;
			Supplier<T> supplier = suppliersFromName.get(typeId);
			if(supplier==null)
			{
				IILogger.error("Unknown ITypeNBTSerializable type \""+typeId+"\".");
				return null;
			}

			T instance = current;
			if(instance==null||!typeId.equals(nameFromClass.get(instance.getClass())))
				instance = supplier.get();
			if(instance==null)
			{
				IILogger.error("Failed to instantiate ITypeNBTSerializable type \""+typeId+"\".");
				return null;
			}

			NBTTagCompound value = from.getCompoundTag("value");
			try
			{
				instance.deserializeNBT(value);
			} catch(Exception e)
			{
				IILogger.error("Error deserializing ITypeNBTSerializable type \""+typeId+"\".", e);
			}
			return instance;
		}
	}
}
