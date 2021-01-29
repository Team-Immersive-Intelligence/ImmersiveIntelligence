package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.model.connector.ModelDataConnector;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataConnector;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataConnectorRenderer extends TileEntitySpecialRenderer<TileEntityDataConnector>
{
	private static ModelDataConnector model = new ModelDataConnector();

	@Override
	public void render(TileEntityDataConnector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/data_connector.png";
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z+1);
			float[] colors = Utils.rgbIntToRGB(EnumDyeColor.byMetadata(te.color).getColorValue());
			GlStateManager.color(colors[0], colors[1], colors[2], 1.0f);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();

			//model.getBlockRotation(te.getFacing(),model);
			//I didn't have a standardised model making system, so i used rotation and offset in this one
			//And that's why it doesn't use getBlockRotation
			ModelIIBase.getCommonConnectorModelRotation(te.facing, model);
			model.baseModel[0].render(0.0625f);
			model.baseModel[2].render(0.0625f);

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.baseModel[1].render(0.0625f);
			model.baseModel[3].render(0.0625f);

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

			ModelIIBase.getCommonConnectorModelRotation(EnumFacing.DOWN, model);
			//model.getBlockRotation(EnumFacing.DOWN,model);

			model.render();

			GlStateManager.popMatrix();
		}
	}
}
