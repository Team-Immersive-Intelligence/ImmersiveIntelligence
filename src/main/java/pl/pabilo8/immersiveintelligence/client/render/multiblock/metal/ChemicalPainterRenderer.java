package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 29.08.2025
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/chemical_painter", clazz = TileEntityChemicalPainter.class)
public class ChemicalPainterRenderer extends IIMultiblockRenderer<TileEntityChemicalPainter>
{
	private AMTModel model;
	private IIAnimationCompiledMap animationProduction;
	private AMTItem item, itemPaint;
	private AMT paintSmall, paintBig, paintAtomizer;

	@Override
	public void drawAnimated(TileEntityChemicalPainter te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		model.defaultize();
		//Apply animations
		if(te.currentProcess!=null)
		{
			item.setStack(te.recipeStack, te.resultStack);
			itemPaint.setStack(te.resultStack);

			float productionProgress = te.getProductionProgress(te.currentProcess, partialTicks);
			animationProduction.apply(productionProgress);

			Float[] colors = new Float[]{te.color.red/255f, te.color.green/255f, te.color.blue/255f};
			paintSmall.setShader(Shaders.COLOR, colors);
			paintBig.setShader(Shaders.COLOR, colors);
			paintAtomizer.setShader(Shaders.COLOR, colors);
			/*if(productionProgress > 0.583f)
				IIAnimationUtils.setModelShader(item, Shaders.COLOR, colors);
			else if(productionProgress > 0.33)
			{
				IIColor mixed = IIColor.WHITE.mixedWith(te.color, (productionProgress-0.33f)/.253f);
				colors = new Float[]{mixed.red/255f, mixed.green/255f, mixed.blue/255f};
				IIAnimationUtils.setModelShader(item, Shaders.COLOR, colors);
			}*/
		}
		else
			animationProduction.apply(0f);
		applyStandardMirroring(te, true);
		//Render
		model.render(tes, buf);
		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		model.defaultize();
		//Render
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		//model loading
		this.model = new AMTModel(state, model, header -> new AMT[]{
				item = new AMTItem("item", header),
				itemPaint = new AMTItem("item_paint", header)
		});

		this.paintSmall = this.model.getPartRecursive("paint_small");
		this.paintBig = this.model.getPartRecursive("paint_big");
		this.paintAtomizer = this.model.getPartRecursive("atomizer_paint");

		//animations
		animationProduction = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "chemical_painter/production"));
	}
}
