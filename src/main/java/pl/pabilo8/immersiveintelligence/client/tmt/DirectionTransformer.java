package pl.pabilo8.immersiveintelligence.client.tmt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.Utils;

/**
 * @author Pabilo8
 * @since 04-01-2020
 */
public abstract class DirectionTransformer
{
	public abstract void transformTileDirection(EnumFacing facing);

	public void startTileModel(double x, double y, double z, ResourceLocation texture)
	{
		Utils.bindTexture(texture);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		//GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();

	}

	@Deprecated
	public void startTileModel(double x, double y, double z, String texture)
	{
		startTileModel(x, y, z, new ResourceLocation(texture));
	}

	public void startItemModel(double x, double y, double z, ResourceLocation texture)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();

		Utils.bindTexture(texture);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

	}

	@Deprecated
	public void startItemModel(double x, double y, double z, String texture)
	{
		startItemModel(x, y, z, new ResourceLocation(texture));
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
