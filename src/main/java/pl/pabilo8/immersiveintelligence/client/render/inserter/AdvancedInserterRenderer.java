package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.animation.AMT;
import pl.pabilo8.immersiveintelligence.client.animation.AMTItem;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.client.animation.IIModelHeader;
import pl.pabilo8.immersiveintelligence.client.render.inserter.InserterBaseRenderer;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityAdvancedInserter;

import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
public class AdvancedInserterRenderer extends InserterBaseRenderer<TileEntityAdvancedInserter>
{
	//reference to model parts
	private AMTItem item;

	@Override
	protected void doAdditionalTransforms(TileEntityAdvancedInserter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//set held stack
		item.setStack(te.insertionHandler.getStackInSlot(0));
		IIAnimationUtils.setModelRotation(item, 90, 0, 0);
	}

	@Override
	protected Function<IIModelHeader, AMT[]> getAdditionalParts()
	{
		return header -> new AMT[]{
				item = new AMTItem("held", header.getOffset("held"))
		};
	}
}
