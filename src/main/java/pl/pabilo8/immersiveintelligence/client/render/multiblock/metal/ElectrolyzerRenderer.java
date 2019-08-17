package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityElectrolyzer;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class ElectrolyzerRenderer extends TileEntitySpecialRenderer<TileEntityElectrolyzer>
{
	private static ModelElectrolyzer model = new ModelElectrolyzer();
	private static final ModelElectrolyzer model_default = new ModelElectrolyzer();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/electrolyzer.png";

	@Override
	public void render(TileEntityElectrolyzer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			//GlStateManager.disableLighting();
			//RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, model);
			model.render();

			GlStateManager.popMatrix();

		}
	}
}
