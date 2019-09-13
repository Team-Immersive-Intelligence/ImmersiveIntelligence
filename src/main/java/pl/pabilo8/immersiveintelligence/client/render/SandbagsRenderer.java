package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelSandbagsStraight;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.TileEntitySandbags;

/**
 * Created by Pabilo8 on 15-06-2019.
 */

@SideOnly(Side.CLIENT)
public class SandbagsRenderer extends TileEntitySpecialRenderer<TileEntitySandbags>
{
	private static ModelSandbagsStraight model = new ModelSandbagsStraight();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/sandbags.png";

	@Override
	public void render(TileEntitySandbags te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();

			model.getBlockRotation(te.getFacing().getOpposite(), model);
			if(te.getCornerFacing()==1)
			{
				GlStateManager.rotate(45f, 0f, 1f, 0f);
				for(ModelRendererTurbo mod : model.leftModel)
					mod.render(0.0625f);
			}
			else if(te.getCornerFacing()==2)
			{
				GlStateManager.translate(-0f, 0f, -0.75f);
				GlStateManager.rotate(-45f, 0f, 1f, 0f);

			}
			model.render();

			if(te.hasNeighbour())
			{
				for(ModelRendererTurbo mod : model.rightModel)
					mod.render(0.0625f);
			}

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

			model.getBlockRotation(EnumFacing.NORTH, model);

			model.render();

			for(ModelRendererTurbo mod : model.rightModel)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.leftModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
			return;
		}
	}
}
