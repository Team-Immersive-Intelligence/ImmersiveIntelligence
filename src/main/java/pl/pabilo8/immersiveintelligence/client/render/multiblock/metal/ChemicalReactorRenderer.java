package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalReactor;

@RegisteredTileRenderer(name = "multiblock/chemical_reactor", clazz = TileEntityChemicalReactor.class)
public class ChemicalReactorRenderer extends IIMultiblockRenderer<TileEntityChemicalReactor>
{
	private AMTModel model;

	@Override
	public void drawAnimated(TileEntityChemicalReactor te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);
		if(model!=null)
			model.render(tes, buf);
		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		// The base model is rendered by IIMultiblockRenderer for manual previews.
	}

	@Override
	public void compileModels(IBlockState state, OBJModel objModel)
	{
		model = null;
	}

	@Override
	protected void nullifyModels()
	{
		model = AMTUtils.disposeOf(model);
		super.nullifyModels();
	}
}
