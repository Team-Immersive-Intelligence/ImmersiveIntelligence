package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelDataInputMachine;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityDataInputMachine;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class DataInputMachineRenderer extends TileEntitySpecialRenderer<TileEntityDataInputMachine>
{
	private static ModelDataInputMachine model = new ModelDataInputMachine();
	private static final ModelDataInputMachine model_default = new ModelDataInputMachine();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/data_input_machine.png";

	@Override
	public void render(TileEntityDataInputMachine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
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

			for(int j = 0; j < model.lidModel.length; j++)
			{
				ModelRendererTurbo mod = model.lidModel[j];
				ModelRendererTurbo mod2 = model_default.lidModel[j];
				mod.rotateAngleX = mod2.rotateAngleX;
				mod.rotateAngleY = mod2.rotateAngleY;
				mod.rotateAngleZ = mod2.rotateAngleZ;
			}

			for(int j = 0; j < model.drawerModel.length; j++)
			{
				ModelRendererTurbo mod = model.drawerModel[j];
				ModelRendererTurbo mod2 = model_default.drawerModel[j];
				mod.rotationPointX = mod2.rotationPointX;
				mod.rotationPointY = mod2.rotationPointY;
				mod.rotationPointZ = mod2.rotationPointZ;
			}

			model.getBlockRotation(te.facing, model);

			model.translate(model.drawerModel, 0, 0, (Math.min(5f, Math.max(te.drawerAngle+(te.isDrawerOpened?0.4f*partialTicks: -0.5f*partialTicks), 0f))));

			model.addRotation(model.lidModel, 0, -TmtUtil.AngleToTMT(Math.min(135f, Math.max(te.doorAngle+(te.isDoorOpened?3f*partialTicks: -5f*partialTicks), 0f))), 0);
			model.render();

			GlStateManager.popMatrix();

		}
	}
}
