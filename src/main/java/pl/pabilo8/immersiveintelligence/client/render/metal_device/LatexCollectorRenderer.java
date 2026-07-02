package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
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
	private AMTParticle particle;

	@Override
	public void draw(TileEntityLatexCollector te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Bucket place animation
		float bucketProgress = 0f;
		if(!te.bucket.isEmpty())
			bucketProgress = AMTUtils.getAnimationProgress(te.bucketTime, 10f, true, partialTicks);
		this.placeBucket.apply(bucketProgress);

		//Chain and extractor visibility
		boolean nextToTree = te.isNextToTree();
		this.extractorVisibility.apply(nextToTree?1f: 0f);

		//Apply latex level and drip animation
		this.particle.setProperty(0);
		if(FluidUtil.getFluidContained(te.bucket)!=null)
			this.latex.withLevel(MathHelper.clamp(1f, 0f, 1f));
		else
		{
			float soonCollected = 0;
			if(!te.bucket.isEmpty())
				soonCollected = (Math.max(0, te.collectionTimer-(LatexCollector.dropTimer-14)+partialTicks)/14f)
						*te.getIncomeModifier()*LatexCollector.dropAmount;
			this.latex.withLevel(MathHelper.clamp((te.collectedLatex+soonCollected)/(float)1000, 0f, 1f));

			//Apply drip particle animation
			this.particle.setProperty(AMTUtils.getAnimationProgress(Math.max(0, te.collectionTimer-(LatexCollector.dropTimer-35)+partialTicks)
							+MathHelper.clamp((te.collectionTimer+partialTicks)/10f, 0, 7),
					35, false, 0));
		}

		//Render
		applyStandardRotation(te.facing);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model, header -> new AMT[]{
				this.latex = new AMTFluid("latex", header)
						.withFluid(new FluidStack(IIContent.fluidLatex, 1000)),
				this.particle = new AMTParticle("particle", header)
						.setParticle("machine/latex_drip")
		});
		this.placeBucket = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("latex_collector/place_bucket"));
		this.extractorVisibility = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("latex_collector/extractor"));
	}

	@Override
	protected void nullifyModels()
	{
		this.model = AMTUtils.disposeOf(this.model);
		this.latex = null;
		this.particle = null;
	}
}
