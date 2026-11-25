package pl.pabilo8.immersiveintelligence.client.render.armor;

import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer.RegisteredArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIRadioPack;

/**
 * Renders the radio backpack and headset.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.11.2026
 */
@RegisteredArmorRenderer(name = "armor/backpack/radio_backpack")
public class RadioPackRenderer extends IIArmorRenderer<ItemIIRadioPack>
{
	public RadioPackRenderer()
	{
		super(IIContent.itemRadioPack, ARMOR_MODELS.with("backpack/radio_backpack"));
	}
}
