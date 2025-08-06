package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/arithmetic_logic_machine", clazz = TileEntityArithmeticLogicMachine.class)
public class ArithmeticLogicMachineRenderer extends IIMultiblockRenderer<TileEntityArithmeticLogicMachine>
{
	private AMT[] model;
	private IIMachineUpgradeModel upgradeCircuitRacks, upgradeMemory;
	private IIAnimationCompiledMap animationDrawer, animationDoor, animationKeyboard;

	@Override
	public void drawAnimated(TileEntityArithmeticLogicMachine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		for(AMT amt : model)
			amt.defaultize();

		animationDrawer.apply(te.drawer.getProgress(partialTicks));
		animationDoor.apply(te.door.getProgress(partialTicks));
		animationKeyboard.apply(te.keyboard.getProgress(partialTicks));

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

		upgradeCircuitRacks = new IIMachineUpgradeModel(IIContent.UPGRADE_CIRCUIT_RACKS,
				ResLoc.of(IIReference.RES_II, "block/metal_multiblock0/arithmetic_logic_machine/upgrade/circuit_racks.obj.ie"),
				ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/upgrade_circuit_racks")
		);
		upgradeMemory = new IIMachineUpgradeModel(IIContent.UPGRADE_MEMORY,
				ResLoc.of(IIReference.RES_II, "block/metal_multiblock0/arithmetic_logic_machine/memory.obj.ie"),
				ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/upgrade_memory")
		);

		//animations
		animationDrawer = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/open_drawer"));
		animationDoor = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/open_door"));
		animationKeyboard = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/open_keyboard"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		IIAnimationUtils.disposeOf(model);
	}
}
