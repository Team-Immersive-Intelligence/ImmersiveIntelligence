package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.model.connector.ModelDataCallbackConnector;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataCallbackConnector;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataCallbackConnectorRenderer extends TileEntitySpecialRenderer<TileEntityDataCallbackConnector>
{
	private static ModelDataCallbackConnector model = new ModelDataCallbackConnector();

	@Override
	public void render(TileEntityDataCallbackConnector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/data_connector.png";
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z+1);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();

			float[] colors_in = Utils.rgbIntToRGB(EnumDyeColor.byMetadata(te.colorIn).getColorValue());
			float[] colors_out = Utils.rgbIntToRGB(EnumDyeColor.byMetadata(te.colorOut).getColorValue());

			ModelBlockBase.getCommonConnectorModelRotation(te.facing, model);
			model.baseModel[1].render(0.0625f);
			model.baseModel[3].render(0.0625f);
			model.baseModel[4].render(0.0625f);
			model.baseModel[5].render(0.0625f);

			GlStateManager.color(colors_in[0], colors_in[1], colors_in[2], 1.0f);
			model.baseModel[0].render(0.0625f);

			GlStateManager.color(colors_out[0], colors_out[1], colors_out[2], 1.0f);
			model.baseModel[2].render(0.0625f);

			GlStateManager.popMatrix();

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z+1);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ModelBlockBase.getCommonConnectorModelRotation(EnumFacing.DOWN, model);
			//model.getBlockRotation(EnumFacing.DOWN,model);

			model.render();

			GlStateManager.popMatrix();
		}
	}
}
