package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTFluid;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityElectrolyzer;

/**
 * @author Pabilo8
 * @updated 23.10.2023
 * @since 28-06-2019
 */
@RegisteredTileRenderer(name = "multiblock/electrolyzer", clazz = TileEntityElectrolyzer.class)
public class ElectrolyzerRenderer extends IIMultiblockRenderer<TileEntityElectrolyzer>
{
	AMTFluid fluid;

	@Override
	public void drawAnimated(TileEntityElectrolyzer te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);

		fluid.setFluid(te.tanks[0].getFluid());
		float tankAmount = te.tanks[0].getFluidAmount();
		if(te.currentProcess!=null)
			tankAmount -= te.getProductionProgress(te.currentProcess, partialTicks)*te.currentProcess.recipe.fluidInput.amount;
		fluid.setLevel(tankAmount/(float)te.tanks[0].getCapacity());

		fluid.setFlowing(false);
		fluid.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		fluid = new AMTFluid("fluid", new Vec3d(-3, -6, 19.9), new Vec3d(10/16f, 14/16f, 25/16f));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		if(fluid!=null)
			fluid.disposeOf();
	}
}
