package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.TripmineRenderer.TripmineItemStackRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripMine;

/**
 * @author Pabilo8
 * @since 02.02.2021
 */
@RegisteredTileRenderer(name = "tripmine", clazz = TileEntityTripMine.class, teisrClazz = TripmineItemStackRenderer.class)
public class TripmineRenderer extends TileEntitySpecialRenderer<TileEntityTripMine>
{
	@Override
	public void render(TileEntityTripMine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z+1);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		//TODO: 05.03.2024 reimplement rendering

		/*GlStateManager.translate(0, 0.03125f*(15-te.digLevel), 0);
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
		GlStateManager.color(c[0], c[1], c[2], te.grass?0.125f: 1f);
		for(ModelRendererTurbo mod : model.coreModel)
			mod.render();
		GlStateManager.disableBlend();
		GlStateManager.color(1f, 1f, 1f, 1f);*/

		GlStateManager.popMatrix();
	}

	public static class TripmineItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.25, 0.75, 2);
			GlStateManager.scale(1.75, 1.75, 1.75);

			ItemBlockMineBase ammo = (ItemBlockMineBase)IIContent.blockTripmine.itemBlock;
			assert ammo!=null;
			if(ammo.isBulletCore(stack))
				AmmoRegistry.getModel(ammo).renderCore(stack);
			else
				AmmoRegistry.getModel(ammo).renderAmmoComplete(false, stack);

			GlStateManager.popMatrix();
		}
	}
}