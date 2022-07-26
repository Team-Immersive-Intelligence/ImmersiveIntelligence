package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class ArithmeticLogicMachineRenderer extends TileEntitySpecialRenderer<TileEntityArithmeticLogicMachine> implements IReloadableModelContainer<ArithmeticLogicMachineRenderer>
{
	private static ModelArithmeticLogicMachine model;
	private static ModelArithmeticLogicMachine modelFlipped;

	@Override
	public void render(TileEntityArithmeticLogicMachine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/arithmetic_logic_machine.png";
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

			float door = MathHelper.clamp(te.doorAngle+(partialTicks*(te.isDoorOpened?5f: -6.5f)), 0, 135f)*flipMod;

			GlStateManager.pushMatrix();
			GlStateManager.translate(1.9375f, 3f, -0.96875f*flipMod);
			GlStateManager.rotate(door, 0, 1, 0);
			for(ModelRendererTurbo model : modelCurrent.doorLeftModel)
				model.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0625f, 3.0f, -0.96875f*flipMod);
			GlStateManager.rotate(-door, 0, 1, 0);
			for(ModelRendererTurbo model : modelCurrent.doorRightModel)
				model.render(0.0625f);
			GlStateManager.popMatrix();

			modelCurrent.render();

			ModelRendererTurbo[][] circuits = new ModelRendererTurbo[][]{modelCurrent.chip1Model, modelCurrent.chip2Model, modelCurrent.chip3Model, modelCurrent.chip4Model};

			for(int i = 0; i < te.renderCircuit.length; i++)
				if(!te.renderCircuit[i].isEmpty())
				{
					ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/alm_circuits/"+te.renderCircuit[i]+".png");
					for(ModelRendererTurbo model : circuits[i])
						model.render();
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
