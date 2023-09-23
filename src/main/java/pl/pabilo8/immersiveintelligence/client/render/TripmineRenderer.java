package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTripMine;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripMine;

/**
 * @author Pabilo8
 * @since 02.02.2021
 */
public class TripmineRenderer extends TileEntitySpecialRenderer<TileEntityTripMine> implements IReloadableModelContainer<TripmineRenderer>
{
	private static ModelTripMine model;

	@Override
	public void render(TileEntityTripMine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z+1);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.translate(0, 0.03125f*(15-te.digLevel), 0);
		model.renderCasing(0f, 0xffffff);

		if(te.grass)
		{
			ClientUtils.bindAtlas();

			GlStateManager.translate(0f, 0, -1f);
			int color = getWorld().getBiome(te.getPos()).getGrassColorAtPos(te.getPos())&0x7FFFFFFF;
			float[] colors = IIUtils.rgbIntToRGB(color);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);

			ClientUtils.mc().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(
					ClientUtils.mc().getBlockRendererDispatcher().getModelForState(Blocks.TALLGRASS.getStateFromMeta(1)), 1f,
					colors[0], colors[1], colors[2]);
			GlStateManager.enableLighting();
			GlStateManager.translate(0f, 0, 1f);
			IIClientUtils.bindTexture(ModelTripMine.TEXTURES[Mines.tripmineColor]);

		}

		GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float[] c = IIUtils.rgbIntToRGB(te.coreColor);
		GlStateManager.color(c[0], c[1], c[2],te.grass?0.125f:1f);
		for(ModelRendererTurbo mod : model.coreModel)
			mod.render();
		GlStateManager.disableBlend();
		GlStateManager.color(1f,1f,1f,1f);

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelTripMine();
		model.translateAll(7.5f,0,-8.5f );
	}

	public static class TripmineItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack itemStackIn, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.25, 0.75, 2);
			GlStateManager.scale(1.75,1.75,1.75);

			assert itemStackIn.getItem() instanceof IAmmo;
			IAmmo bullet = (IAmmo)itemStackIn.getItem();

			model.renderCasing(0, bullet.getPaintColor(itemStackIn));
			model.renderCore(bullet.getCore(itemStackIn).getColour(), EnumCoreTypes.CANISTER);

			GlStateManager.popMatrix();
		}
	}
}