package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTellermine;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTellermine;

/**
 * @author Pabilo8
 * @since 02.02.2021
 */
public class TellermineRenderer extends TileEntitySpecialRenderer<TileEntityTellermine> implements IReloadableModelContainer<TellermineRenderer>
{
	private static ModelTellermine model;

	@Override
	public void render(TileEntityTellermine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+0.5, y, z+0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.translate(0, 0.0325f-(te.digLevel*0.0625f), 0);
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
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelTellermine();
		AmmoRegistry.INSTANCE.registeredModels.remove("tellermine");
		AmmoRegistry.INSTANCE.registeredModels.put("tellermine", model);
	}

	public static class TellermineItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack itemStackIn, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.25, 0.5);
			GlStateManager.scale(1.5,1.5,1.5f);

			assert itemStackIn.getItem() instanceof IAmmo;
			IAmmo bullet = (IAmmo)itemStackIn.getItem();

			model.renderCasing(0, bullet.getPaintColor(itemStackIn));
			model.renderCore(bullet.getCore(itemStackIn).getColour(), EnumCoreTypes.CANISTER);

			GlStateManager.popMatrix();
		}
	}
}