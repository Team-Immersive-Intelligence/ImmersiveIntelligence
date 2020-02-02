package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCratePost;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCratePost;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class SkyCratePostRenderer extends TileEntitySpecialRenderer<TileEntitySkyCratePost>
{
	private static ModelSkyCratePost model = new ModelSkyCratePost();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/skycrate_post.png";

	@Override
	public void render(TileEntitySkyCratePost te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-1, (float)z);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(te.facing, model);
			model.render();

			GlStateManager.popMatrix();

		}
	}
}
