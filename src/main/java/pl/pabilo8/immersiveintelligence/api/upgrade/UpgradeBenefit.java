package pl.pabilo8.immersiveintelligence.api.upgrade;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represent a positive or negative effect of installing an upgrade, listed in the upgrade's tooltip;
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.09.2025
 */
public class UpgradeBenefit
{
	private final String translationKey;
	private final int amount;
	private final boolean positive;

	public UpgradeBenefit(String benefitName, int amount, boolean positive)
	{
		this.translationKey = "machineupgrade."+benefitName;
		this.amount = amount;
		this.positive = positive;
	}

	@SideOnly(Side.CLIENT)
	public String getLocalizedName()
	{
		//+ 1000000 Additional Energy Storage
		return (positive?TextFormatting.GREEN+"+": TextFormatting.RED+"-")+(amount==0?" ": (amount+" "))+I18n.format(translationKey)+TextFormatting.RESET;
	}
}
