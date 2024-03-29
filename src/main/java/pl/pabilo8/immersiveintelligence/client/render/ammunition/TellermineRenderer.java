package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTellermine;

/**
 * Handles rendering of a landmine entity
 *
 * @author Pabilo8
 * @updated 05.03.2024
 * @ii-approved 0.3.1
 * @since 02.02.2021
 */
@RegisteredTileRenderer(name = "tellermine", clazz = TileEntityTellermine.class, teisrClazz = TellermineRenderer.TellermineItemStackRenderer.class)
public class TellermineRenderer extends TileEntitySpecialRenderer<TileEntityTellermine>
{
	@Override
	public void render(TileEntityTellermine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+0.5, y, z+0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		/*GlStateManager.translate(0, 0.0325f-(te.digLevel*0.0625f), 0);
		model.renderCasing(0f, 0xffffff);
		model.renderCore(te.coreColor, EnumCoreTypes.CANISTER);

		if(te.grass)
		{
			ClientUtils.bindAtlas();

			GlStateManager.translate(-0.5f, 0, -0.5f);
			int color = getWorld().getBiome(te.getPos()).getGrassColorAtPos(te.getPos())&0x7FFFFFFF;
			float[] colors = IIUtils.rgbIntToRGB(color);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();

			ClientUtils.mc().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(
					ClientUtils.mc().getBlockRendererDispatcher().getModelForState(Blocks.TALLGRASS.getStateFromMeta(1)), 1f,
					colors[0], colors[1], colors[2]);
			GlStateManager.enableLighting();
		}*/

		GlStateManager.popMatrix();
	}

	public static class TellermineItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.25, 0.5);
			GlStateManager.scale(1.5, 1.5, 1.5f);

			ItemBlockMineBase ammo = (ItemBlockMineBase)IIContent.blockTellermine.itemBlock;
			assert ammo!=null;
			if(ammo.isBulletCore(stack))
				AmmoRegistry.getModel(ammo).renderCore(stack);
			else
				AmmoRegistry.getModel(ammo).renderAmmoComplete(false, stack);

			GlStateManager.popMatrix();
		}
	}
}