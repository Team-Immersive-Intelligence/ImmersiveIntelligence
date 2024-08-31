package pl.pabilo8.immersiveintelligence.common.util.item;

import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.09.2022
 */
public interface IIItemEnum extends ISerializableEnum
{
	/**
	 * @return this SubItem's ID - the metadata number
	 */
	default int getMeta()
	{
		return ((Enum<?>)this).ordinal();
	}

	default boolean isHidden()
	{
		IIItemProperties tp = getProperties();
		return tp!=null&&tp.hidden();
	}

	default int getStackSize()
	{
		IIItemProperties tp = getProperties();
		if(tp!=null)
			return tp.stackSize();
		return -1;
	}

	default IICategory getCategory()
	{
		IIItemProperties tp = getProperties();
		if(tp==null)
			return IICategory.NULL;
		return tp.category();
	}

	@Nonnull
	default String[] getOreDict()
	{
		IIItemProperties tp = getProperties();
		if(tp!=null)
			return tp.oreDict();
		return new String[0];
	}

	@Nullable
	default IIItemProperties getProperties()
	{
		return IIUtils.getEnumAnnotation(IIItemProperties.class, this);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.TYPE})
	@interface IIItemProperties
	{
		boolean hidden() default false;

		String[] oreDict() default {};

		int stackSize() default -1;

		IICategory category() default IICategory.NULL;
	}
}
