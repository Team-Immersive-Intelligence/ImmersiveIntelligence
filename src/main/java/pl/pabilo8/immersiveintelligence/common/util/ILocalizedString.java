package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Provides a translation key and a localised display name for a value.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.10.2026
 */
public interface ILocalizedString
{
	/**
	 * @return the complete translation key for this value
	 */
	String getFullLocaleKey();

	/**
	 * @return the localised display name for this value
	 */
	@SideOnly(Side.CLIENT)
	default String getLocalizedName()
	{
		return I18n.format(getFullLocaleKey());
	}
}
