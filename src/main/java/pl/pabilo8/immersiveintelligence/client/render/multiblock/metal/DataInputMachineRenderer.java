package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelDataInputMachine;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class DataInputMachineRenderer extends TileEntitySpecialRenderer<TileEntityDataInputMachine> implements IReloadableModelContainer<DataInputMachineRenderer>
{
	private static ModelDataInputMachine model;

	@Override
	public void render(TileEntityDataInputMachine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/data_input_machine.png";
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

			model.getBlockRotation(te.facing, false);

			if(te.mirrored)
			{
				GlStateManager.scale(-1,1,1);
				GlStateManager.translate(-1,0,0);
				GlStateManager.cullFace(CullFace.FRONT);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, (Math.min(5f, Math.max(te.drawerAngle+(te.isDrawerOpened?0.4f*partialTicks: -0.5f*partialTicks), 0f)))*0.0625f);
			for(ModelRendererTurbo mod : model.drawerModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(1f, 0f, -0.96875f);
			GlStateManager.rotate(-Math.min(135f, Math.max(te.doorAngle+(te.isDoorOpened?3f*partialTicks: -5f*partialTicks), 0f)), 0, 1, 0);
			for(ModelRendererTurbo mod : model.lidModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			model.render();

			if(te.mirrored)
				GlStateManager.cullFace(CullFace.BACK);

			GlStateManager.popMatrix();

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelDataInputMachine();
	}
}
