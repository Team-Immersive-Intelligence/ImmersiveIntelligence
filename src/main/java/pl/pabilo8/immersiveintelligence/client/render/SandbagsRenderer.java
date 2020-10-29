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
 * @author Pabilo8
 * @since 15-06-2019
 */

@SideOnly(Side.CLIENT)
public class SandbagsRenderer extends TileEntitySpecialRenderer<TileEntitySandbags>
{
	private static ModelSandbagsStraight model = new ModelSandbagsStraight();
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/fortification/sandbags.png";

	@Override
	public void render(TileEntitySandbags te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();

			model.getBlockRotation(te.getFacing().getOpposite(), false);
			if(te.getCornerFacing()==1)
			{
				GlStateManager.rotate(45f, 0f, 1f, 0f);
				GlStateManager.pushMatrix();

				for(ModelRendererTurbo mod : model.leftFullModel)
					mod.render(0.0625f);

				GlStateManager.popMatrix();
			}
			else if(te.getCornerFacing()==2)
			{
				GlStateManager.translate(-0f, 0f, -0.75f);
				GlStateManager.rotate(-45f, 0f, 1f, 0f);

			}


			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);

			if(te.hasNeighbour())
			{
				for(ModelRendererTurbo mod : model.rightFullModel)
					mod.render(0.0625f);
			}
			else
				for(ModelRendererTurbo mod : model.rightDotModel)
					mod.render(0.0625f);

			GlStateManager.popMatrix();

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(EnumFacing.NORTH, false);

			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.leftDotModel)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.rightDotModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}
}
