package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityAdvancedInserter;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.05.2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "device/inserter/inserter_advanced", clazz = TileEntityAdvancedInserter.class)
public class AdvancedInserterRenderer extends InserterBaseRenderer<TileEntityAdvancedInserter>
{
	//reference to model parts
	private AMTItem item;

	@Override
	protected void doAdditionalTransforms(TileEntityAdvancedInserter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//set held stack
		item.setStack(te.insertionHandler.getStackInSlot(0));
	}

	@Override
	protected Function<AMTModelHeader, AMT[]> getAdditionalParts()
	{
		return header -> new AMT[]{
				item = new AMTItem("held", header.getOffset("held"))
		};
	}
}
