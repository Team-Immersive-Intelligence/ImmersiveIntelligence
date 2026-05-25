package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileCokeBattery;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 25.05.2026
 * @since 25.05.2026
 */

@RegisteredTileRenderer(name = "multiblock/coke_battery", clazz = TileCokeBattery.class)
public class CokeBatteryRenderer extends IIMultiblockRenderer<TileCokeBattery>

{
	AMTFluid fluid;

	@Override
	public void drawAnimated(TileCokeBattery te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);

		fluid.withFluid(te.tankInput.getFluid());
		float tankAmount = te.tankInput.getFluidAmount();
		if(te.currentProcess!=null)
			tankAmount -= te.getProductionProgress(te.currentProcess, partialTicks)*te.currentProcess.recipe.fluidInput.amount;
		fluid.withLevel(tankAmount/(float)te.tankInput.getCapacity());

		fluid.withFlowing(false);
		fluid.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		fluid = new AMTFluid("fluid", new Vec3d(3, -6, 19.9).scale(0.0625))
				.withSize(new Vec3d(10, 14, 25));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		if(fluid!=null)
			fluid.disposeOf();
	}
}
