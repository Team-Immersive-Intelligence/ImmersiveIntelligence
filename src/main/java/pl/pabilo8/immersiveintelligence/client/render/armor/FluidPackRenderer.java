package pl.pabilo8.immersiveintelligence.client.render.armor;

import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer.RegisteredArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIFluidPack;

/**
 * Renders the canister backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.11.2026
 */
@RegisteredArmorRenderer(name = "armor/backpack/fluid_backpack")
public class FluidPackRenderer extends IIArmorRenderer<ItemIIFluidPack>
{
	public FluidPackRenderer()
	{
		super(IIContent.itemFluidPack, ARMOR_MODELS.with("backpack/fluid_backpack"));
	}
}
