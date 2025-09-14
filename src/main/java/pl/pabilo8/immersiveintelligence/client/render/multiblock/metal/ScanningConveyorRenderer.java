package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIBooleanAnimation;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityScanningConveyor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 28.10.2023
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/scanning_conveyor", clazz = TileEntityScanningConveyor.class)
public class ScanningConveyorRenderer extends IIMultiblockRenderer<TileEntityScanningConveyor>
{
	AMTModel model;
	private IIBooleanAnimation active;
	private IIAnimationCompiledMap photo;

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);
		active = new IIBooleanAnimation(
				this.model.getPart("conveyor_on"),
				this.model.getPart("conveyor_off")
		);
		photo = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "scanning_conveyor/photo"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = AMTUtils.disposeOf(model);
	}

	@Override
	public void drawAnimated(TileEntityScanningConveyor te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardRotation(te.facing.getOpposite());

		active.apply(!te.getRedstoneAtPos(0));
		if(te.lastScanned.isEmpty())
			photo.apply(0);
		else
			for(Integer value : te.lastScanned.values())
				if(value > 2)
				{
					photo.apply(Math.max(value-2-partialTicks, 0)/3f);
					break;
				}

		model.render(tes, buf);


	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		active.apply(false);
		photo.apply(0);
		model.render(tes, buf);
	}
}
