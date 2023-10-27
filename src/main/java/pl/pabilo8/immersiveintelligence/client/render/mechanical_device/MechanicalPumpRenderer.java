package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.MechanicalPump;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalPump;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "mechanical_pump", clazz = TileEntityMechanicalPump.class)
public class MechanicalPumpRenderer extends IITileRenderer<TileEntityMechanicalPump>
{
	private static IIAnimation rotation, pumping;
	private static IIAnimationCompiledMap rotationMap, pumpingMap;
	private static AMT[] models = null;

	@Override
	public void draw(TileEntityMechanicalPump te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		if(te.dummy)
			return;

		// TODO: 06.04.2022 improve calculation

		//rotation calculation
		double world_rpm = RotaryUtils.getWorldRPM(te.getWorld(), partialTicks);
		float rotation_progress = (float)(world_rpm*te.rotation.getRotationSpeed());

		//pumping calculation
		boolean b = te.getWorld().isBlockIndirectlyGettingPowered(te.getPos()) > 0;
		int a = (int)(30-(25*(te.rotation.getRotationSpeed()/(float)MechanicalPump.rpmBreakingMax)));
		float pumping_progress = 0;
		if(te.rotation.getRotationSpeed() > 0)
			pumping_progress = ((te.getWorld().getTotalWorldTime()%a)+partialTicks)/(float)a*(b?1f: 0f);

		//apply animation
		rotationMap.apply(rotation_progress%1f);
		pumpingMap.apply(pumping_progress);

		GlStateManager.translate(0, 1, 0);

		//apply rotation for block facing
		applyStandardRotation(te.getFacing());

		//render
		for(AMT mod : models)
			mod.render(tes, buf);

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		rotation = IIAnimationLoader.loadAnimation(new ResourceLocation(ImmersiveIntelligence.MODID, "mechanical_pump/rotation"));
		pumping = IIAnimationLoader.loadAnimation(new ResourceLocation(ImmersiveIntelligence.MODID, "mechanical_pump/pumping"));

		models = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));

		rotationMap = IIAnimationCompiledMap.create(models, rotation);
		pumpingMap = IIAnimationCompiledMap.create(models, pumping);


	}

	@Override
	protected void nullifyModels()
	{
		rotationMap = pumpingMap = null;
		rotation = pumping = null;

		models = IIAnimationUtils.disposeOf(models);
	}
}
