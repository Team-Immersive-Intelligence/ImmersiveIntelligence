package pl.pabilo8.immersiveintelligence.client.tmt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumFacing;

/**
 * Created by Pabilo8 on 04-01-2020.
 */
public abstract class DirectionTransformer
{
	public abstract void transformTileDirection(EnumFacing facing);

	public void startTileModel(double x, double y, double z, String texture)
	{
		ClientUtils.bindTexture(texture);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		//GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();

	}

	public void startItemModel(double x, double y, double z, String texture)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();

		ClientUtils.bindTexture(texture);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

	}

	public void endModel()
	{
		GlStateManager.popMatrix();
	}

	public void changeTexture(String texture)
	{
		ClientUtils.bindTexture(texture);
	}
}
