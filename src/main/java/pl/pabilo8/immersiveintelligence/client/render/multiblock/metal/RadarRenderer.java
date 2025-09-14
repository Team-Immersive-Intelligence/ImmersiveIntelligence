package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTConstructionModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2025
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/radar", clazz = TileEntityRadar.class)
public class RadarRenderer extends IIMultiblockRenderer<TileEntityRadar>
{
	private AMTModel model;
	private AMTConstructionModel constructionModel;
	private IIAnimationCompiledMap animationDish;

	@Override
	public void drawAnimated(TileEntityRadar te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state + apply rotation and mirroring
		model.defaultize();
		applyStandardMirroring(te, true);

		//Draw construction model or finished one
		if(constructionModel.renderProgress(te, tes, buf, partialTicks)==AMTConstructionModel.ConstructionStage.FINISHED)
		{
			animationDish.apply(AMTUtils.getAnimationProgress(te.dishRotation, 360, te.active, false, 1, 0, partialTicks));
			model.render(tes, buf);
		}
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state and render
		model.defaultize();
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);
		this.constructionModel = new AMTConstructionModel(
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/radar/radar_construction.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "radar/construction")
		);

		this.animationDish = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("radar/dish"));
	}
}
