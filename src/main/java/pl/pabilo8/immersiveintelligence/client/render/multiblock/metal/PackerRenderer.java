package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIMachineUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;

/**
 * @author Pabilo8
 * @since 20.08.2022
 */
@RegisteredTileRenderer(name = "multiblock/packer", clazz = TileEntityPacker.class)
public class PackerRenderer extends IIMultiblockRenderer<TileEntityPacker>
{
	AMT[] model, upgradeParts;

	IIBooleanAnimation conveyor;
	IIMachineUpgradeModel fluidUpgrade, energyUpgrade, railwayUpgrade, namingUpgrade;
	private IIAnimationCompiledMap animationWork, animationDefault;
	private AMTItem itemModel;

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		for(AMT mod : model)
			mod.defaultize();
		IIAnimationUtils.setModelVisibility(upgradeParts, false);

		//Render
		for(AMT mod : model)
			mod.render(tes, buf);

		//Render container
		animationDefault.apply(0);
		for(AMT mod : upgradeParts)
			mod.render(tes, buf);
	}

	@Override
	public void drawAnimated(TileEntityPacker te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		for(AMT mod : model)
			mod.defaultize();
		IIAnimationUtils.setModelVisibility(upgradeParts, false);

		//loading progress
		boolean active = !te.getRedstoneAtPos(0);
		float animationProgress = IIAnimationUtils.getAnimationProgress(te.processTime, Packer.actionTime,
				te.processTime > 0&&active, false, 1f, 0f, partialTicks);

		//set conveyor item
		itemModel.setStack(te.inventory.get(0));

		//apply loading progress
		animationWork.apply(animationProgress);

		//apply rotation and mirroring
		applyStandardMirroring(te, true);

		//conveyor
		conveyor.apply(active);

		//render upgrades
		UpgradeStage railway = railwayUpgrade.renderConstruction(te, tes, buf, partialTicks);
		if(railway!=UpgradeStage.NOT_INSTALLED)
			conveyor.applyVisibility(false);
		if(railway==UpgradeStage.INSTALLED)
		{
			railwayUpgrade.defaultize();
			railwayUpgrade.render(tes, buf);
		}

		namingUpgrade.renderConstruction(te, tes, buf, partialTicks);

		if(fluidUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
		{
			fluidUpgrade.defaultize();
			fluidUpgrade.render(tes, buf);
		}
		else if(energyUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
		{
			energyUpgrade.defaultize();
			energyUpgrade.render(tes, buf);
		}
		else if(te.getCurrentlyInstalled()!=IIContent.UPGRADE_PACKER_FLUID&&te.getCurrentlyInstalled()!=IIContent.UPGRADE_PACKER_ENERGY)
		{
			//show item packer (default mode) elements
			animationDefault.apply(0);
			for(AMT mod : upgradeParts)
				mod.render(tes, buf);
		}

		//render
		for(AMT mod : model)
			mod.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		//model loading
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), header ->
				new AMT[]{
						itemModel = new AMTItem("conveyor_item", header)
				}
		);
		conveyor = new IIBooleanAnimation(
				IIAnimationUtils.getPart(model, "conveyor_active"),
				IIAnimationUtils.getPart(model, "conveyor")
		);

		//progress animation
		animationWork = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "packer/work"));

		//upgrade models
		upgradeParts = new AMT[4];
		AMT[] modelUpgrades = IIAnimationUtils.getAMTFromRes(
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/packer_construction.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/packer_construction.obj.amt"),
				header -> new AMT[]{
						upgradeParts[0] = new AMTLocator("item", header),
						upgradeParts[1] = new AMTLocator("fluid", header),
						upgradeParts[2] = new AMTLocator("energy", header),
						upgradeParts[3] = new AMTLocator("railway", header)
				}
		);
		animationDefault = IIAnimationCompiledMap.create(modelUpgrades, new ResourceLocation(ImmersiveIntelligence.MODID, "packer/default"));

		railwayUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_PACKER_RAILWAY, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_railway"));
		namingUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_PACKER_NAMING, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_naming"));

		fluidUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_PACKER_FLUID, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_fluid"));
		energyUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_PACKER_ENERGY, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_energy"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		IIAnimationUtils.disposeOf(model);

		IIAnimationUtils.disposeOf(railwayUpgrade);
		IIAnimationUtils.disposeOf(namingUpgrade);
		IIAnimationUtils.disposeOf(fluidUpgrade);
		IIAnimationUtils.disposeOf(energyUpgrade);
	}
}
