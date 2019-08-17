package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityArithmeticLogicMachine;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class ArithmeticLogicMachineRenderer extends TileEntitySpecialRenderer<TileEntityArithmeticLogicMachine>
{
	private static ModelArithmeticLogicMachine model = new ModelArithmeticLogicMachine();
	private static final ModelArithmeticLogicMachine model_default = new ModelArithmeticLogicMachine();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/arithmetic_logic_machine.png";

	@Override
	public void render(TileEntityArithmeticLogicMachine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
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

			for(int j = 0; j < model.doorLeftModel.length; j++)
			{
				ModelRendererTurbo mod = model.doorLeftModel[j];
				ModelRendererTurbo mod2 = model_default.doorLeftModel[j];
				mod.rotateAngleX = mod2.rotateAngleX;
				mod.rotateAngleY = mod2.rotateAngleY;
				mod.rotateAngleZ = mod2.rotateAngleZ;
			}

			for(int j = 0; j < model.doorRightModel.length; j++)
			{
				ModelRendererTurbo mod = model.doorRightModel[j];
				ModelRendererTurbo mod2 = model_default.doorRightModel[j];
				mod.rotateAngleX = mod2.rotateAngleX;
				mod.rotateAngleY = mod2.rotateAngleY;
				mod.rotateAngleZ = mod2.rotateAngleZ;
			}

			model.getBlockRotation(te.facing, model);

			model.addRotation(model.doorLeftModel, 0, TmtUtil.AngleToTMT(Math.min(135f, Math.max(te.doorAngle+(te.isDoorOpened?6.5f*partialTicks: -3f*partialTicks), 0f))), 0);

			model.addRotation(model.doorRightModel, 0, -TmtUtil.AngleToTMT(Math.min(135f, Math.max(te.doorAngle+(te.isDoorOpened?6.5f*partialTicks: -3f*partialTicks), 0f))), 0);

			model.render();

			//I didn't know there even is something like String.isEmpty()
			if(!te.renderCircuit1.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit1+".png");
				for(ModelRendererTurbo model : model.chip1Model)
					model.render(1F/16F);
			}
			if(!te.renderCircuit2.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit2+".png");
				for(ModelRendererTurbo model : model.chip2Model)
					model.render(1F/16F);
			}
			if(!te.renderCircuit3.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit3+".png");
				for(ModelRendererTurbo model : model.chip3Model)
					model.render(1F/16F);
			}
			if(!te.renderCircuit4.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit4+".png");
				for(ModelRendererTurbo model : model.chip4Model)
					model.render(1F/16F);
			}

			GlStateManager.popMatrix();

		}
	}
}
