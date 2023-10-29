package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityScanningConveyor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8
 * @updated 28.10.2023
 * @since 21-06-2019
 */
@RegisteredTileRenderer(name = "scanning_conveyor", clazz = TileEntityScanningConveyor.class)
public class ScanningConveyorRenderer extends IIMultiblockRenderer<TileEntityScanningConveyor>
{
	AMT[] model;
	private IIBooleanAnimation active;
	private IIAnimationCompiledMap photo;

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
		active = new IIBooleanAnimation(
				IIAnimationUtils.getPart(model, "conveyor_on"),
				IIAnimationUtils.getPart(model, "conveyor_off")
		);
		photo = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "scanning_conveyor/photo"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = IIAnimationUtils.disposeOf(model);
	}

	@Override
	public void drawAnimated(TileEntityScanningConveyor te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		active.apply(!te.getRedstoneAtPos(te.getPOI(MultiblockPOI.REDSTONE_INPUT)[0]));
		if(te.lastScanned.isEmpty())
			photo.apply(0);
		else
			for(Integer value : te.lastScanned.values())
				if(value > 2)
				{
					photo.apply(Math.max(value-2-partialTicks, 0)/3f);
					break;
				}

		for(AMT amt : model)
			amt.render(tes, buf);


	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}
}
