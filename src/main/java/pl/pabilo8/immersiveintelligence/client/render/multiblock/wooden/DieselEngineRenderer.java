package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntityDieselEngine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * Renderer for the Diesel Engine multiblock
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.2
 * @updated 15.10.2026
 * @since 14.10.2026
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "multiblock/diesel_engine", clazz = TileEntityDieselEngine.class)
public class DieselEngineRenderer extends IIMultiblockRenderer<TileEntityDieselEngine>
{
	private AMTModel model;
	private IIAnimationCompiledMap animationOpen, animationWork;

	@Override
	public void drawAnimated(TileEntityDieselEngine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		this.animationOpen.apply(te.maintenanceHatch.getProgress(partialTicks));
		this.animationWork.apply(te.workAnimator.getAnimationProgress(partialTicks));

		this.applyStandardMirroring(te, true);
		this.model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		this.animationOpen.apply(0);
		this.animationWork.apply(0);
		this.model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel objModel)
	{
		this.model = new AMTModel(state, objModel);
		this.animationOpen = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "diesel_engine/open"));
		this.animationWork = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "diesel_engine/work"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		this.model = AMTUtils.disposeOf(model);
		this.animationOpen = animationWork = null;
	}

}
