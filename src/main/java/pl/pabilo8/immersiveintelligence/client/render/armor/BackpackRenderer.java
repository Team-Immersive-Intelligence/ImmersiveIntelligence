package pl.pabilo8.immersiveintelligence.client.render.armor;

import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer.RegisteredArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackPack;

/**
 * Renders the storage backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.11.2026
 * @since 04.11.2026
 */
@RegisteredArmorRenderer(name = "armor/backpack/backpack")
public class BackpackRenderer extends IIArmorRenderer<ItemIIBackPack>
{
	public BackpackRenderer()
	{
		super(IIContent.itemBackpack, ARMOR_MODELS.with("backpack/backpack"));
	}
}
