package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTItem;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "device/inserter/inserter", clazz = TileEntityInserter.class)
public class InserterRenderer extends InserterBaseRenderer<TileEntityInserter>
{
	//reference to model parts
	private AMTItem item;

	@Override
	protected void doAdditionalTransforms(TileEntityInserter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//set held stack
		item.setStack(te.insertionHandler.getStackInSlot(0));
	}

	@Override
	protected Function<IIModelHeader, AMT[]> getAdditionalParts()
	{
		return header -> new AMT[]{
				item = new AMTItem("held", header.getOffset("held"))
		};
	}
}
