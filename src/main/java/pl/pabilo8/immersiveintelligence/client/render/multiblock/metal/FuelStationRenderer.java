package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.03.2026
 * @ii-approved 0.3.1
 * @since 07.04.2021
 */
@RegisteredTileRenderer(name = "multiblock/fuel_station", clazz = TileEntityFuelStation.class)
public class FuelStationRenderer extends IIMultiblockRenderer<TileEntityFuelStation>
{
	private AMTCachedModel<TileEntityFuelStation> model;
	private IIAnimationCachedMap inserterYaw, inserterPitch;

	@Override
	public void drawAnimated(TileEntityFuelStation te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		this.model.getVariant(te, te.style);
		this.inserterYaw.apply(getTransformedYaw(te, te.calculateInserterAngle(partialTicks))/360f);
		this.inserterPitch.apply(te.calculateDistance(partialTicks));

		applyStandardMirroring(te, true);
		this.model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		this.model.getVariant(null);
		this.inserterPitch.apply(25/180f);
		this.inserterYaw.apply(0.5f);
		this.model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		ResLoc modelDir = IIReference.RES_BLOCK_MODEL.with("multiblock/fuel_station/");
		this.model = AMTCachedModelBuilder.startTileEntityModel(TileEntityFuelStation.class)
				.withModel(model)
				.withHeader(modelDir.with("fuel_station").withExtension(ResLoc.EXT_OBJAMT))
				.withModel(te -> te==null||te.style.getStyle().equals("wooden"),
						modelDir.with("variant_wooden.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("steel"),
						modelDir.with("variant_steel.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("naval"),
						modelDir.with("variant_naval.obj"))
				.build();

		this.inserterYaw = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "fuel_station/inserter_yaw"));
		this.inserterPitch = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "fuel_station/inserter_pitch"));
	}
}
