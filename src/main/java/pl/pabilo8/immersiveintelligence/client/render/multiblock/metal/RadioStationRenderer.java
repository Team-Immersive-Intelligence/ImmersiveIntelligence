package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTConstructionModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTConstructionModel.ConstructionStage;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityRadioStation;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/radio_station", clazz = TileEntityRadioStation.class)
public class RadioStationRenderer extends IIMultiblockRenderer<TileEntityRadioStation>
{
	private AMTModel model;
	private AMTConstructionModel constructionModel;

	@Override
	public void drawAnimated(TileEntityRadioStation te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state + apply rotation and mirroring
		model.defaultize();
		applyStandardMirroring(te, true);
		//Draw construction model or finished one
		if(constructionModel.renderProgress(te, tes, buf, partialTicks)==ConstructionStage.FINISHED)
			model.render(tes, buf);
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
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/radio_station/radio_station_construction.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "radio_station/construction")
		);
	}
}
