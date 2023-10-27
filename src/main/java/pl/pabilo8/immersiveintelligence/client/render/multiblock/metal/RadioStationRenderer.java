package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelRadioStation;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityRadioStation;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class RadioStationRenderer extends TileEntitySpecialRenderer<TileEntityRadioStation> implements IReloadableModelContainer<RadioStationRenderer>
{
	private static ModelRadioStation model;
	private static ModelRadioStation modelFlipped;

	@Override
	public void render(TileEntityRadioStation te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/radio_station.png";
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			ModelRadioStation modelCurrent = te.mirrored?modelFlipped: model;

			modelCurrent.getBlockRotation(te.facing, te.mirrored);

			if(te.isConstructionFinished())
			{
				modelCurrent.render();
			}
			else
			{
				double cc = (int)Math.min(te.clientConstruction+((partialTicks*(Tools.electricHammerEnergyPerUseConstruction/4.25f))), IIUtils.getMaxClientProgress(te.construction, te.getConstructionCost(), TileEntityRadioStation.PART_AMOUNT));
				double progress = MathHelper.clamp(cc/(float)te.getConstructionCost(), 0f, 1f);

				for(int i = 0; i < TileEntityRadioStation.PART_AMOUNT*progress; i++)
				{
					if(1+i > Math.round(TileEntityRadioStation.PART_AMOUNT*progress))
					{
						GlStateManager.pushMatrix();
						double scale = 1f-(((progress*TileEntityRadioStation.PART_AMOUNT)%1f));
						GlStateManager.enableBlend();
						GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale*2, 1));
						GlStateManager.translate(0, scale*1.5f, 0);

						modelCurrent.baseModel[i].render(0.0625f);
						GlStateManager.color(1f, 1f, 1f, 1f);
						GlStateManager.popMatrix();
					}
					else
						modelCurrent.baseModel[i].render(0.0625f);
				}

				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				GlStateManager.scale(0.98f, 0.98f, 0.98f);
				GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
				//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

				ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
				for(int i = 50; i >= Math.max((50*progress)-1, 0); i--)
				{
					modelCurrent.baseModel[i].render(0.0625f);
				}
				ShaderUtil.releaseShader();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				GlStateManager.popMatrix();

			}

			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+2.5, (float)y+1, (float)z+1.75);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.scale(0.25, 0.25, 0.25);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			model.render();
			GlStateManager.popMatrix();

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelRadioStation();
		modelFlipped = new ModelRadioStation();
		modelFlipped.flipAllZ();

		TileEntityRadioStation.PART_AMOUNT = model.baseModel.length;
	}
}
