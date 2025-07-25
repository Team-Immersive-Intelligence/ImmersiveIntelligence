package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/data_input_machine", clazz = TileEntityDataInputMachine.class)
public class DataInputMachineRenderer extends IIMultiblockRenderer<TileEntityDataInputMachine>
{
	private AMT[] model;
	IIMachineUpgradeModel upgradeModel;
	private IIAnimationCompiledMap animationDrawer, animationHatch, animationProgrammingStart;

	@Override
	public void drawAnimated(TileEntityDataInputMachine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		for(AMT amt : model)
			amt.defaultize();
		animationDrawer.apply(te.drawer.getProgress(partialTicks));
		animationHatch.apply(te.hatch.getProgress(partialTicks));

		if(te.currentProcess!=null&&te.currentProcess.recipe.showItem)
		{
			animationProgrammingStart.apply(1f);
		}
		else
			animationProgrammingStart.apply(0);

		//Draw
		applyStandardMirroring(te, true);

		//Render
		for(AMT amt : model)
			amt.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		for(AMT amt : model)
			amt.defaultize();

		//Render
		for(AMT amt : model)
			amt.render(tes, buf);
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		//model loading
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), header ->
				new AMT[]{}
		);

		//animations
		animationDrawer = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "data_input_machine/drawer"));
		animationHatch = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "data_input_machine/hatch"));
		animationProgrammingStart = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "data_input_machine/programming_start"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		IIAnimationUtils.disposeOf(model);
	}
}
