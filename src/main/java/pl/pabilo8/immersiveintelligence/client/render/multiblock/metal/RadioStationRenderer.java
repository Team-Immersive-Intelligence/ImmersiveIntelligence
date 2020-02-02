package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelRadioStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityRadioStation;

/**
 * Created by Pabilo8 on 21-06-2019.
 */
public class RadioStationRenderer extends TileEntitySpecialRenderer<TileEntityRadioStation>
{
	private static ModelRadioStation model = new ModelRadioStation();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/radio_station.png";

	@Override
	public void render(TileEntityRadioStation te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+3, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, model);

			if(te.isConstructionFinished())
			{
				model.render();
			}
			else
			{
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				float progress = Math.max(Math.min(te.getCurrentConstruction()/(float)te.getConstructionCost(), 1f), 0f);
				GlStateManager.scale(0.98f, 0.98f, 0.98f);
				GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
				float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);
				ShaderUtil.blueprint_static(0.55f-(progress*0.35f), ClientUtils.mc().player.ticksExisted+partialTicks);
				for(int i = 50; i >= 50*progress; i--)
					model.baseModel[i].render(0.0625f);
				ShaderUtil.releaseShader();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				GlStateManager.popMatrix();

				for(int i = 0; i < 50*progress; i++)
					model.baseModel[i].render(0.0625f);


			}

			GlStateManager.popMatrix();

		}
	}
}
