package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.model.connector.ModelWheel;
import pl.pabilo8.immersiveintelligence.client.render.direction_transformer.DirectionTransformerWheel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalWheel;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class WheelRenderer extends TileEntitySpecialRenderer<TileEntityMechanicalWheel>
{
	private static ModelWheel model = new ModelWheel();
	private static DirectionTransformerWheel t = new DirectionTransformerWheel();

	@Override
	public void render(TileEntityMechanicalWheel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/mechanical_device/wooden_wheel.png";
		if(te!=null)
		{
			t.startTileModel(x, y, z, texture);
			t.transformTileDirection(te.facing);

			model.render();

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0.5f, 0f);

			//(te.getFacing().getAxisDirection()==AxisDirection.NEGATIVE?-1f:1)*
			double world_rpm = (te.getConnectorWorld().getTotalWorldTime()%RotaryUtils.getRPMMax()+partialTicks)/RotaryUtils.getRPMMax();
			GlStateManager.rotate((float)(world_rpm*360f*te.getOutputRPM()), 0, 0, 1);

			for(ModelRendererTurbo mod : model.wheel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();


			GlStateManager.popMatrix();
			ClientProxy.mech_con_renderer.render(te, x, y, z, partialTicks, destroyStage, alpha);

		}
		else
		{
			t.startItemModel(x, y, z+1, texture);
			t.transformTileDirection(EnumFacing.NORTH);
			GlStateManager.translate(-0.5f, -0.25f, 0.85f);
			GlStateManager.scale(1.65f, 1.65f, 1.65f);
			model.render();

			GlStateManager.translate(0.5f, 0.5f, 0);

			for(ModelRendererTurbo mod : model.wheel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}
}
