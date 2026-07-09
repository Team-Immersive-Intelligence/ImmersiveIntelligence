package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrintingPress;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.12.2023
 * @since 10.07.2019
 */
@RegisteredTileRenderer(name = "multiblock/printing_press", clazz = TileEntityPrintingPress.class)
public class PrintingPressRenderer extends IIMultiblockRenderer<TileEntityPrintingPress>
{
	private AMTModel model;
	private IIAnimationCompiledMap animationWork, animationDefault;
	//For falling paper page and paper stack
	private AMTItem itemModel, stackModel;

	@Override
	public void drawAnimated(TileEntityPrintingPress te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		model.defaultize();
		animationDefault.apply(0);

		itemModel.setStack(te.inventory.get(MultiblockPrintingPress.SLOT_PAPER));
		stackModel.setStack(te.inventory.get(MultiblockPrintingPress.SLOT_OUTPUT));

		if(!te.processQueue.isEmpty())
		{
			if(te.processQueue.size() > 1&&te.getProductionProgress(te.processQueue.get(1), partialTicks) > 0.06666)
				animationWork.apply(te.getProductionProgress(te.processQueue.get(1), partialTicks));
			else
				animationWork.apply(te.getProductionProgress(te.processQueue.get(0), partialTicks));
		}

		applyStandardMirroring(te, true);

		//Render
		model.render(tes, buf);

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
		model.render(tes, buf);
	}


	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		//model loading
		this.model = new AMTModel(state, model, header ->
				new AMT[]{
						itemModel = new AMTItem("paper", header),
						stackModel = new AMTItem("stack", header).setStacking(true)
				}
		);

		//animations
		animationDefault = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "printing_press/default"));
		animationWork = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "printing_press/work"));

		UpgradeTechTree.getTreeFor(TileEntityPrintingPress.class)
				.withBaseModelLocation(IIReference.RES_BLOCK_MODEL.with("multiblock/printing_press/printing_press_inv.obj"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		AMTUtils.disposeOf(model);
	}
}
