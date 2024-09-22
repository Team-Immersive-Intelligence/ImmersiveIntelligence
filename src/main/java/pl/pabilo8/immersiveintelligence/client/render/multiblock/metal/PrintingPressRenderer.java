package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @updated 13.12.2023
 * @since 10.07.2019
 */
@RegisteredTileRenderer(name = "multiblock/printing_press", clazz = TileEntityPrintingPress.class)
public class PrintingPressRenderer extends IIMultiblockRenderer<TileEntityPrintingPress>
{
	private AMT[] model;
	private IIAnimationCompiledMap animationWork, animationDefault;
	//For falling paper page and paper stack
	private AMTItem itemModel, stackModel;

	@Override
	public void drawAnimated(TileEntityPrintingPress te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		for(AMT mod : model)
			mod.defaultize();
		animationDefault.apply(0);

		itemModel.setStack(te.inventory.get(TileEntityPrintingPress.SLOT_PAPER));
		stackModel.setStack(te.inventory.get(TileEntityPrintingPress.SLOT_OUTPUT));

		if(!te.processQueue.isEmpty())
		{
			if(te.processQueue.size() > 1&&te.getProductionProgress(te.processQueue.get(1), partialTicks) > 0.06666)
				animationWork.apply(te.getProductionProgress(te.processQueue.get(1), partialTicks));
			else
				animationWork.apply(te.getProductionProgress(te.processQueue.get(0), partialTicks));
		}

		applyStandardMirroring(te, true);

		//Render
		for(AMT mod : model)
			mod.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		animationWork.apply(0);
		itemModel.setStack(ItemStack.EMPTY);
		stackModel.setStack(ItemStack.EMPTY);

		//Render
		for(AMT mod : model)
			mod.render(tes, buf);
	}


	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		//model loading
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), header ->
				new AMT[]{
						itemModel = new AMTItem("paper", header),
						stackModel = new AMTItem("stack", header).setStacking(true)
				}
		);

		//animations
		animationDefault = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "printing_press/default"));
		animationWork = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "printing_press/work"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		IIAnimationUtils.disposeOf(model);
	}
}
