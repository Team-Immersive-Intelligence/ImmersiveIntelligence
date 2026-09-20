package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;

import java.util.Arrays;
import java.util.List;

/**
 * Displays an {@link AmmoCore} and its properties in the Engineer's Manual.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.09.2026
 * @since 07.08.2021
 */
public class IIManualPageAmmoCore extends IIManualPageAmmoPart<AmmoCore>
{
	public IIManualPageAmmoCore(ManualInstance manual, AmmoCore core)
	{
		super(manual, core, "bullet_core");
	}

	@Override
	protected String getLocalizedPartName()
	{
		return manual.formatText(text);
	}

	@Override
	protected List<String> getInfoLines()
	{
		return Arrays.asList(
				I18n.format("ie.manual.entry.bullet_components.density", part.getDensity()),
				I18n.format("ie.manual.entry.bullet_components.damage_modifier", part.getDamageModifier()),
				I18n.format("ie.manual.entry.bullet_components.component_efficiency_modifier", part.getExplosionModifier()),
				I18n.format("ie.manual.entry.bullet_components.penetration_hardness", part.getPenetrationHardness().getLocalizedName())
		);
	}

	@Override
	protected int getMinimumDescriptionY()
	{
		return 100;
	}
}
