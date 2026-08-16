package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityFluidInserter;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 19.01.2026
 * @ii-approved 0.3.1
 * @since 15.06.2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "device/inserter/fluid_inserter", clazz = TileEntityFluidInserter.class)
public class FluidInserterRenderer extends InserterBaseRenderer<TileEntityFluidInserter>
{
	@Override
	protected void doAdditionalTransforms(TileEntityFluidInserter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	protected Function<AMTModelHeader, AMT[]> getAdditionalParts()
	{
		return header -> new AMT[]{};
	}
}
