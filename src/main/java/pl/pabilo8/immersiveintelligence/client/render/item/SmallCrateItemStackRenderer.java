package pl.pabilo8.immersiveintelligence.client.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.SmallCrateRenderer;

/**
 * @author Pabilo8
 * @since 13-10-2019
 */
public class SmallCrateItemStackRenderer extends TileEntityItemStackRenderer
{
	public static SmallCrateItemStackRenderer instance = new SmallCrateItemStackRenderer();

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(1f, 0f, 0f);
		SmallCrateRenderer.renderWithMeta(itemStackIn.getMetadata(), EnumFacing.EAST);
		GlStateManager.popMatrix();
	}
}
