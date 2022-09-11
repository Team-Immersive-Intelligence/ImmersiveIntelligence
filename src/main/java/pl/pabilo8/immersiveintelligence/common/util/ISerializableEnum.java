package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Default II serializable enum, returns its name in lower case
 *
 * @author Pabilo8
 * @since 03.09.2022
 */
public interface ISerializableEnum extends IStringSerializable
{
	@Override
	@Nonnull
	default String getName()
	{
		return this.toString().toLowerCase(Locale.ENGLISH);
	}
}
