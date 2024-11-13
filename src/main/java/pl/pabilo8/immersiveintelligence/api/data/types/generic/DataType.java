package pl.pabilo8.immersiveintelligence.api.data.types.generic;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
@MethodsReturnNonnullByDefault
public abstract class DataType
{
	/**
	 * @param nbt to load values from
	 */
	public abstract void valueFromNBT(NBTTagCompound nbt);

	/**
	 * @return NBTTagCompound with variable's values
	 */
	public abstract NBTTagCompound valueToNBT();

	/**
	 * Sets header (type name) while saving the variable to NBT
	 *
	 * @return NBTTagCompound with header tag
	 */
	public NBTTagCompound getHeaderTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Type", getName());
		return tag;
	}

	/**
	 * @return cached type metadata that is valid for all instances of this data type
	 */
	public TypeMetaInfo<?> getTypeMeta()
	{
		return IIDataHandlingUtils.getTypeMeta(this.getClass());
	}

	/**
	 * @return this data type's name in snake_case
	 */
	public String getName()
	{
		return getTypeMeta().name;
	}

	/**
	 * @return type color used by GUIs to represent this data type
	 */
	public IIColor getTypeColor()
	{
		return getTypeMeta().color;
	}

	/**
	 * @return icon texture location for this data type
	 */
	public ResourceLocation getTextureLocation()
	{
		return getTypeMeta().getTextureLocation();
	}

	/**
	 * Used by interfaces extending IDataType to provide a generic "bridge" between data types
	 *
	 * @see NumericDataType
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface IGenericDataType
	{
		Class<? extends DataType> defaultType();
	}

	public static class TypeMetaInfo<T extends DataType>
	{
		public final String name;
		public final Class<T> type;
		public final Supplier<T> supplier;
		public final IIColor color;
		private final ResourceLocation textureLocation;

		public TypeMetaInfo(String name, Class<T> type, Supplier<T> supplier, IIColor color)
		{
			this.name = name;
			this.type = type;
			this.supplier = supplier;
			this.color = color;
			this.textureLocation = ResLoc.of(IIReference.RES_TEXTURES_GUI, "data_types/"+name).withExtension(ResLoc.EXT_PNG);
		}

		public ResourceLocation getTextureLocation()
		{
			return textureLocation;
		}
	}
}
