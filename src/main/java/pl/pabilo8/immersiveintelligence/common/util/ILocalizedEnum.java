package pl.pabilo8.immersiveintelligence.common.util;


import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.12.2025
 */
public interface ILocalizedEnum extends ISerializableEnum
{
	/**
	 * @return a language key prefix for this enum
	 */
	String geLocaleKey();

	/**
	 * @return a localized name for this enum value
	 */
	@SideOnly(Side.CLIENT)
	default String getLocalizedName()
	{
		return I18n.format(geLocaleKey()+getName());
	}
}
