package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelPunchtapeReader;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityPunchtapeReader;

/**
 * @author Pabilo8
 * @since 30.08.2020
 */
public class PunchtapeReaderRenderer extends TileEntitySpecialRenderer<TileEntityPunchtapeReader> implements IReloadableModelContainer<PunchtapeReaderRenderer>
{
	private static ModelPunchtapeReader model;

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/punchtape_reader.png";

	@Override
	public void render(TileEntityPunchtapeReader te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(te.getFacing(), false);
			model.render();

			GlStateManager.popMatrix();
			return;

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(EnumFacing.NORTH, false);
			model.render();

			GlStateManager.popMatrix();
			return;
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelPunchtapeReader();
	}
}
