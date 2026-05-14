package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.LatexCollector;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityLatexCollector;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.05.2026
 * @ii-approved 0.3.1
 * @since 30.08.2020
 */
@RegisteredTileRenderer(name = "latex_collector", clazz = TileEntityLatexCollector.class)
public class LatexCollectorRenderer extends IITileRenderer<TileEntityLatexCollector>
{
	private IIAnimationCompiledMap placeBucket, extractorVisibility;
	private AMTModel model;
	private AMTFluid latex;

	@Override
	public void draw(TileEntityLatexCollector te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Bucket place animation
		float bucketProgress = 0f;
		if(!te.bucket.isEmpty())
			bucketProgress = AMTUtils.getAnimationProgress(te.bucketTime, 10f, true, partialTicks);
		placeBucket.apply(bucketProgress);
		//Chain and extractor visibility
		boolean nextToTree = te.isNextToTree();
		extractorVisibility.apply(nextToTree?1f: 0f);

		//Apply latex level
		float atime = te.timer;
		latex.withLevel(Math.min((atime+(!te.bucket.isEmpty()&&nextToTree?(partialTicks*te.getIncomeModifier()): 0))/(float)LatexCollector.collectTime, 1f));

		//Render
		applyStandardRotation(te.facing);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model, header -> new AMT[]{
				this.latex = new AMTFluid("latex", header)
						.withFluid(new FluidStack(IIContent.fluidLatex, 1000))
		});
		this.placeBucket = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("latex_collector/place_bucket"));
		this.extractorVisibility = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("latex_collector/extractor"));
	}

	@Override
	protected void nullifyModels()
	{
		this.model = AMTUtils.disposeOf(this.model);
		this.latex = null;
	}
}
