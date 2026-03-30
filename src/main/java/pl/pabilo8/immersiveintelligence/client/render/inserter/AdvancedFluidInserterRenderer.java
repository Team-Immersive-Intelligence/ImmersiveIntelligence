package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
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
	@Override
	protected void doAdditionalTransforms(TileEntityAdvancedFluidInserter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	protected Function<AMTModelHeader, AMT[]> getAdditionalParts()
	{
		return header -> new AMT[]{};
	}
}
