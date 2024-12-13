package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IterableDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A utility class for registering and operating on {@link DataType data types}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.10.2024
 **/
public class IIDataTypeUtils
{
	public static final Map<Class<? extends DataType>, TypeMetaInfo<?>> metaTypesByClass = new LinkedHashMap<>();
	public static final Map<String, TypeMetaInfo<?>> metaTypesByName = new LinkedHashMap<>();

	public static void registerDataTypes()
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
		metaTypesByName.put(name, metaInfo);
		metaTypesByClass.put(klass, metaInfo);
	}

	@Nonnull
	public static DataType getVarFromNBT(NBTTagCompound nbt)
	{
		TypeMetaInfo<?> type = metaTypesByName.get(nbt.getString("Type"));
		if(type==null)
			return new DataTypeNull();

		DataType data = type.supplier.get();
		data.valueFromNBT(nbt);
		return data;
	}

	@Nonnull
	public static DataType getVarInstance(String type)
	{
		TypeMetaInfo<?> meta = metaTypesByName.get(type);
		return meta==null?new DataTypeNull(): meta.supplier.get();
	}

	@Nonnull
	public static DataType getVarInstance(Class<? extends DataType> type)
	{
		if(type.isAnnotationPresent(IGenericDataType.class))
			type = type.getAnnotation(IGenericDataType.class).defaultType();

		TypeMetaInfo<?> meta = metaTypesByClass.get(type);
		return meta==null?new DataTypeNull(): meta.supplier.get();
	}
}
