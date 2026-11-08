package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCratePost;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;

/**
 * Renders the Skycrate Post with its retained TMT model.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.10.2026
 * @since 01.06.2019
 */
public class SkyCratePostRenderer extends TileEntitySpecialRenderer<TileEntitySkyCratePost> implements IReloadableModelContainer<SkyCratePostRenderer>
{
	private static ModelSkyCratePost model;

	@Override
	public void render(TileEntitySkyCratePost te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			if(model==null)
				reloadModels();
			boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
			boolean light0 = GL11.glIsEnabled(GL11.GL_LIGHT0), light1 = GL11.glIsEnabled(GL11.GL_LIGHT1);
			String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/skycrate_post.png";
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-1, (float)z);
			GlStateManager.disableLighting();
			if(te.hasWorld())
				RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(te.facing, false);
			model.render();

			GlStateManager.popMatrix();
			if(lighting) GlStateManager.enableLighting();
			else GlStateManager.disableLighting();
			if(light0) GlStateManager.enableLight(0);
			else GlStateManager.disableLight(0);
			if(light1) GlStateManager.enableLight(1);
			else GlStateManager.disableLight(1);
			GlStateManager.color(1, 1, 1, 1);

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelSkyCratePost();
	}
}
