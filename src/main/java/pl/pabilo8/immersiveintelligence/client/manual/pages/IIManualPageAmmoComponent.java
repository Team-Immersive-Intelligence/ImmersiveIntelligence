package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;

import java.util.Arrays;
import java.util.List;

/**
 * Displays an {@link AmmoComponent} and its properties in the Engineer's Manual.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.09.2026
 * @ii-approved 0.3.1
 * @since 07.08.2021
 */
public class IIManualPageAmmoComponent extends IIManualPageAmmoPart<AmmoComponent>
{
	public IIManualPageAmmoComponent(ManualInstance manual, AmmoComponent component)
	{
		super(manual, component, "bullet_component");
	}

	@Override
	protected String getLocalizedPartName()
	{
		return part.getTranslatedName();
	}

	@Override
	protected List<String> getInfoLines()
	{
		return Arrays.asList(
				I18n.format("ie.manual.entry.bullet_components.type", part.getRole().getLocalizedName()),
				I18n.format("ie.manual.entry.bullet_components.density", part.getDensity()),
				I18n.format("ie.manual.entry.bullet_components.slots_taken", part.getSlotsTaken())
		);
	}
}
