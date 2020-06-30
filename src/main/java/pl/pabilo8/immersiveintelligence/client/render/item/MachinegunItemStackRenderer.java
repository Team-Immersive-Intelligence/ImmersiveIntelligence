package pl.pabilo8.immersiveintelligence.client.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;

/**
 * @author Pabilo8
 * @since 13-10-2019
 */
public class MachinegunItemStackRenderer extends TileEntityItemStackRenderer
{
	public static MachinegunItemStackRenderer instance = new MachinegunItemStackRenderer();

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks)
	{
		GlStateManager.pushMatrix();

		RenderHelper.enableStandardItemLighting();
		MachinegunRenderer.renderMachinegun(itemStackIn, null);

		GlStateManager.popMatrix();
	}
}
