package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
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
	private AMTModel model;
	private IIAnimationCompiledMap animationDrawer, animationHatch, animationProgrammingStart;

	@Override
	public void drawAnimated(TileEntityDataInputMachine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		model.defaultize();
		animationDrawer.apply(te.drawer.getProgress(partialTicks));
		animationHatch.apply(te.hatch.getProgress(partialTicks));

		//Show item programming animation for items like radio explosives
		if(te.currentProcess!=null&&te.currentProcess.recipe.showItem)
			animationProgrammingStart.apply(1f);
		else
			animationProgrammingStart.apply(0);

		//Draw
		applyStandardMirroring(te, true);
		//Render
		model.render(tes, buf);
		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		model.defaultize();
		//Render
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		//model loading
		this.model = new AMTModel(state, model);

		//animations
		animationDrawer = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "data_input_machine/drawer"));
		animationHatch = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "data_input_machine/hatch"));
		animationProgrammingStart = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "data_input_machine/programming_start"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		IIAnimationUtils.disposeOf(model);
	}
}
