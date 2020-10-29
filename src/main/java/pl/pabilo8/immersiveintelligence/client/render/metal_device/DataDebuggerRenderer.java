package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelDebugger;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataDebugger;

/**
 * @author Pabilo8
 * @since 30.08.2020
 */
public class DataDebuggerRenderer extends TileEntitySpecialRenderer<TileEntityDataDebugger> implements IReloadableModelContainer<DataDebuggerRenderer>
{
	private static ModelDebugger model;

	@Override
	public void render(TileEntityDataDebugger te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/debugger.png";
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(te.getFacing().getOpposite(), false);
			if(te.setupTime > 0)
			{
				float progress = MathHelper.clamp(1f-((te.setupTime-partialTicks)/25f), 0, 1);
				float machineBox = MathHelper.clamp(progress/0.35f, 0, 1);
				float projector = MathHelper.clamp((progress-0.35f)/0.35f, 0, 1);
				float deskStuff = MathHelper.clamp((progress-0.7f)/0.3f, 0, 1);
				for(ModelRendererTurbo mod : model.baseModel)
					mod.render(0.0625f);


				GlStateManager.enableBlend();
				GlStateManager.pushMatrix();
				GlStateManager.color(1f, 1f, 1f, Math.min(machineBox, 1));
				GlStateManager.translate(0, 1f-machineBox, 0);
				for(ModelRendererTurbo mod : model.machineBoxModel)
					mod.render(0.0625f);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.color(1f, 1f, 1f, Math.min(projector, 1));
				GlStateManager.translate(0, 0, 1f-projector);
				for(ModelRendererTurbo mod : model.projectorModel)
					mod.render(0.0625f);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.color(1f, 1f, 1f, Math.min(deskStuff, 1));
				GlStateManager.translate(-1+deskStuff, 0, 0);
				for(ModelRendererTurbo mod : model.deskStuffModel)
					mod.render(0.0625f);
				GlStateManager.popMatrix();
				GlStateManager.disableBlend();
			}
			else
			{
				for(ModelRendererTurbo mod : model.baseModel)
					mod.render(0.0625f);
				for(ModelRendererTurbo mod : model.machineBoxModel)
					mod.render(0.0625f);
				for(ModelRendererTurbo mod : model.projectorModel)
					mod.render(0.0625f);
				for(ModelRendererTurbo mod : model.deskStuffModel)
					mod.render(0.0625f);
			}

			GlStateManager.popMatrix();

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(EnumFacing.SOUTH, false);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.machineBoxModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.projectorModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.deskStuffModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelDebugger();
	}
}
