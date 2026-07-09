package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeCachedModel.MachineCachedUpgradeModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
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
	private AMTCachedModel<TileEntityDataInputMachine> model;
	private AMTUpgradeCachedModel<TileEntityDataInputMachine> upgradeAdvancedData;
	private IIAnimationCachedMap animationDrawer, animationHatch, animationProgrammingStart;

	@Override
	public void drawAnimated(TileEntityDataInputMachine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		this.model.getVariant(te, te.upgradeManager);
		this.model.defaultize();
		animationDrawer.apply(te.drawer.getProgress(partialTicks));
		animationHatch.apply(te.hatch.getProgress(partialTicks));

		//Show item programming animation for items like radio explosives
		if(te.currentProcess!=null&&te.currentProcess.recipe.showItem)
			animationProgrammingStart.apply(1f);
		else
			animationProgrammingStart.apply(0);

		//Draw
		applyStandardMirroring(te, true);

		//Display upgrade construction
		upgradeAdvancedData.apply(te, tes, buf, partialTicks);

		//Render
		model.render(tes, buf);
		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		model.getBase();
		model.defaultize();
		//Render
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		//model loading
		ResLoc resFolder = IIReference.RES_II.with("models/block/multiblock/data_input_machine/");
		AMTCachedModelBuilder<TileEntityDataInputMachine> modelBuilder =
				AMTCachedModelBuilder.startTileEntityModel(TileEntityDataInputMachine.class)
						.withModel(model)
						.withHeader(resFolder.with("data_input_machine.obj.amt"));

		//upgrade models
		this.upgradeAdvancedData = new MachineCachedUpgradeModelBuilder<>(modelBuilder)
				.withUpgrade(IIContent.UPGRADE_ADVANCED_DATA)
				.withConstructionModel(resFolder.with("upgrades/advanced_data.obj"))
				.withAnimation(ResLoc.of(IIReference.RES_II, "data_input_machine/upgrade_advanced_data"))
				.build();

		//finish main model
		this.model = modelBuilder.build();

		//animations
		animationDrawer = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "data_input_machine/drawer"));
		animationHatch = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "data_input_machine/hatch"));
		animationProgrammingStart = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "data_input_machine/programming_start"));

		UpgradeTechTree.getTreeFor(TileEntityDataInputMachine.class)
				.withBaseModelLocation(IIReference.RES_BLOCK_MODEL.with("multiblock/data_input_machine/data_input_machine_preview.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_ADVANCED_DATA, IIReference.RES_BLOCK_MODEL.with(
						"multiblock/data_input_machine/upgrades/advanced_data.obj"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		AMTUtils.disposeOf(model);
	}
}
