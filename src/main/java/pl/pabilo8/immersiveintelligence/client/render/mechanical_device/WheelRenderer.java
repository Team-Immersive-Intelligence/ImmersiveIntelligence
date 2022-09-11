package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.model.connector.ModelWheel;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.render.direction_transformer.DirectionTransformerWheel;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalWheel;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class WheelRenderer extends TileEntitySpecialRenderer<TileEntityMechanicalWheel> implements IReloadableModelContainer<WheelRenderer>
{
	private static ModelWheel model;
	private static final DirectionTransformerWheel t = new DirectionTransformerWheel();
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/mechanical_device/wheel.png");

	@Override
	public void render(@Nullable TileEntityMechanicalWheel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			t.startTileModel(x, y, z, TEXTURE);
			t.transformTileDirection(te.facing);

			model.render();

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0.5f, 0f);

			//(te.getFacing().getAxisDirection()==AxisDirection.NEGATIVE?-1f:1)*
			double world_rpm = (te.getConnectorWorld().getTotalWorldTime()%RotaryUtils.getRPMMax()+partialTicks)/RotaryUtils.getRPMMax();
			GlStateManager.rotate((float)(world_rpm*(te.facing.getAxisDirection()==AxisDirection.NEGATIVE?-360f:360f)*te.getOutputRPM()), 0, 0, 1);

			for(ModelRendererTurbo mod : model.wheel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();


			t.endModel();
			ClientProxy.mech_con_renderer.render(te, x, y, z, partialTicks, destroyStage, alpha);

		}
		else
		{
			t.startItemModel(x, y, z+1, TEXTURE);
			t.transformTileDirection(EnumFacing.NORTH);
			GlStateManager.translate(-0.5f, -0.25f, 0.85f);
			GlStateManager.scale(1.65f, 1.65f, 1.65f);
			model.render();

			GlStateManager.translate(0.5f, 0.5f, 0);

			for(ModelRendererTurbo mod : model.wheel)
				mod.render(0.0625f);

			t.endModel();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelWheel();
	}
}
