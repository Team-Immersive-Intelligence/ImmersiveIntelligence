package pl.pabilo8.immersiveintelligence.common.util;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.12.2025
 */
public interface ILocalizedEnum extends ISerializableEnum, ILocalizedString
{
	/**
	 * @return a language key prefix for this enum
	 */
	String geLocaleKey();

	@Override
	default String getFullLocaleKey()
	{
		return geLocaleKey()+getName();
	}
}
