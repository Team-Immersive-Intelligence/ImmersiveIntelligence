package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class ArithmeticLogicMachineRenderer extends TileEntitySpecialRenderer<TileEntityArithmeticLogicMachine> implements IReloadableModelContainer<ArithmeticLogicMachineRenderer>
{
	private static ModelArithmeticLogicMachine model;
	private static ModelArithmeticLogicMachine modelFlipped;
	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/arithmetic_logic_machine.png";

	@Override
	public void render(TileEntityArithmeticLogicMachine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
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

			ModelArithmeticLogicMachine modelCurrent = te.mirrored?modelFlipped: model;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			int flipMod = te.mirrored?-1: 1;

			GlStateManager.pushMatrix();
			GlStateManager.translate(1.9375f, 3f, -0.96875f*flipMod);
			GlStateManager.rotate(Math.min(135f, Math.max(te.doorAngle+(te.isDoorOpened?6.5f*partialTicks: -3f*partialTicks), 0f))*flipMod, 0, 1, 0);
			for(ModelRendererTurbo model : modelCurrent.doorLeftModel)
				model.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0625f, 3.0f, -0.96875f*flipMod);
			GlStateManager.rotate(-Math.min(135f, Math.max(te.doorAngle+(te.isDoorOpened?6.5f*partialTicks: -3f*partialTicks), 0f))*flipMod, 0, 1, 0);
			for(ModelRendererTurbo model : modelCurrent.doorRightModel)
				model.render(0.0625f);
			GlStateManager.popMatrix();

			modelCurrent.render();

			//I didn't know there even is something like String.isEmpty()
			if(!te.renderCircuit1.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit1+".png");
				for(ModelRendererTurbo model : modelCurrent.chip1Model)
					model.render(0.0625f);
			}
			if(!te.renderCircuit2.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit2+".png");
				for(ModelRendererTurbo model : modelCurrent.chip2Model)
					model.render(0.0625f);
			}
			if(!te.renderCircuit3.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit3+".png");
				for(ModelRendererTurbo model : modelCurrent.chip3Model)
					model.render(0.0625f);
			}
			if(!te.renderCircuit4.isEmpty())
			{
				ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit4+".png");
				for(ModelRendererTurbo model : modelCurrent.chip4Model)
					model.render(0.0625f);
			}

			GlStateManager.popMatrix();

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelArithmeticLogicMachine();
		modelFlipped = new ModelArithmeticLogicMachine();
		modelFlipped.flipAllZ();
	}
}
