package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCokeBattery;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 25.05.2026
 * @since 25.05.2026
 */
@RegisteredTileRenderer(name = "multiblock/coke_battery", clazz = TileEntityCokeBattery.class)
public class CokeBatteryRenderer extends IIMultiblockRenderer<TileEntityCokeBattery>
{
	private AMTModel model;
	private AMTFluid fluid;

	@Override
	public void drawAnimated(TileEntityCokeBattery te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.defaultize();

		fluid.withFluid(te.tankInput.getFluid());
		float tankAmount = te.tankInput.getFluidAmount();
		if(te.currentProcess!=null)
			tankAmount -= te.getProductionProgress(te.currentProcess, partialTicks)*te.currentProcess.recipe.fluidInput.amount;
		fluid.withLevel(tankAmount/(float)te.tankInput.getCapacity());
		fluid.withFlowing(false);

		applyStandardMirroring(te, true);
		model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.defaultize();
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model, header -> new AMT[]{
				fluid = new AMTFluid("fluid", header)
		});
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		AMTUtils.disposeOf(this.model);
	}
}
