package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.MechanicalPump;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalPump;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "mechanical/pump", clazz = TileEntityMechanicalPump.class)
public class MechanicalPumpRenderer extends IITileRenderer<TileEntityMechanicalPump>
{
	private static IIAnimationCompiledMap rotation, pumping;
	private static AMT[] models = null;

	@Override
	public void draw(TileEntityMechanicalPump te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		if(te.dummy)
			return;

		//pumping calculation
		boolean b = te.getWorld().isBlockIndirectlyGettingPowered(te.getPos()) > 0;
		int a = (int)(30-(25*(te.rotation.getRotationSpeed()/(float)MechanicalPump.rpmBreakingMax)));
		float pumpingProgress = 0;
		if(te.rotation.getRotationSpeed() > 0)
			pumpingProgress = ((te.getWorld().getTotalWorldTime()%a)+partialTicks)/(float)a*(b?1f: 0f);

		//apply animation
		rotation.apply(IIRotaryUtils.getDisplayRotation(te, te.rotation, partialTicks));
		pumping.apply(pumpingProgress);

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
		models = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));

		rotation = IIAnimationCompiledMap.create(models, ResLoc.of(IIReference.RES_II, "mechanical_pump/rotation"));
		pumping = IIAnimationCompiledMap.create(models, ResLoc.of(IIReference.RES_II, "mechanical_pump/pumping"));
	}

	@Override
	protected void nullifyModels()
	{
		rotation = pumping = null;
		models = IIAnimationUtils.disposeOf(models);
	}
}
