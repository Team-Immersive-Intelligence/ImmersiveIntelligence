package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityAdvancedFluidInserter;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 19.01.2026
 * @ii-approved 0.3.1
 * @since 15.06.2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "device/inserter/advanced_fluid_inserter", clazz = TileEntityAdvancedFluidInserter.class)
public class AdvancedFluidInserterRenderer extends InserterBaseRenderer<TileEntityAdvancedFluidInserter>
{
	AMTFluid fluid;

	@Override
	protected void doAdditionalTransforms(TileEntityAdvancedFluidInserter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//set held stack
		//fluid.setFluid();
	}

	@Override
	protected Function<AMTModelHeader, AMT[]> getAdditionalParts()
	{
		return header -> new AMT[]{
				fluid = new AMTFluid("fluid_tank", header, new Vec3d(4, 4, 4))
		};
	}
}
