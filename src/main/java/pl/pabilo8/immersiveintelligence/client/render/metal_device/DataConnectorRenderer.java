package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.model.connector.ModelDataConnector;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataConnector;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class DataConnectorRenderer extends TileEntitySpecialRenderer<TileEntityDataConnector>
{
	private static ModelDataConnector model = new ModelDataConnector();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/data_connector.png";

	@Override
	public void render(TileEntityDataConnector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z+1);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();

			//model.getBlockRotation(te.getFacing(),model);
			//I didn't have a standardised model making system, so i used rotation and offset in this one
			//And that's why it doesn't use getBlockRotation
			BaseBlockModel.getCommonConnectorModelRotation(te.facing, model);
			model.render();

			GlStateManager.popMatrix();
			return;

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z+1);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			BaseBlockModel.getCommonConnectorModelRotation(EnumFacing.DOWN, model);
			//model.getBlockRotation(EnumFacing.DOWN,model);

			model.render();

			GlStateManager.popMatrix();
			return;
		}
	}
}
