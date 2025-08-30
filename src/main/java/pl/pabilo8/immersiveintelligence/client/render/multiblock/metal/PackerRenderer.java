package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.MachineUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.08.2022
 */
@RegisteredTileRenderer(name = "multiblock/packer", clazz = TileEntityPacker.class)
public class PackerRenderer extends IIMultiblockRenderer<TileEntityPacker>
{
	AMTModel model, upgradeParts;

	IIBooleanAnimation conveyor;
	MachineUpgradeModel fluidUpgrade, energyUpgrade, railwayUpgrade, namingUpgrade;
	private IIAnimationCompiledMap animationWork, animationDefault;
	private AMTItem itemModel;

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		model.defaultize();
		IIAnimationUtils.setModelVisibility(upgradeParts, false);

		//Render
		model.render(tes, buf);

		//Render container
		animationDefault.apply(0);
		upgradeParts.render(tes, buf);
	}

	@Override
	public void drawAnimated(TileEntityPacker te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		model.defaultize();
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
		else if(te.getCurrentUpgrade()!=IIContent.UPGRADE_PACKER_FLUID&&te.getCurrentUpgrade()!=IIContent.UPGRADE_PACKER_ENERGY)
		{
			//show item packer (default mode) elements
			animationDefault.apply(0);
			upgradeParts.render(tes, buf);
		}

		//render
		model.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		//model loading
		this.model = new AMTModel(state, model, header ->
				new AMT[]{
						itemModel = new AMTItem("conveyor_item", header)
				}
		);
		conveyor = new IIBooleanAnimation(
				this.model.getPart("conveyor_active"),
				this.model.getPart("conveyor")
		);

		//progress animation
		animationWork = IIAnimationCompiledMap.create(this.model, new ResourceLocation(ImmersiveIntelligence.MODID, "packer/work"));

		//upgrade models
		this.upgradeParts = new AMTModel(DefaultVertexFormats.BLOCK,
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/packer_construction.obj.ie"),
				header -> new AMT[]{
						new AMTLocator("item", header),
						new AMTLocator("fluid", header),
						new AMTLocator("energy", header),
						new AMTLocator("railway", header)
				}
		);
		animationDefault = IIAnimationCompiledMap.create(upgradeParts, new ResourceLocation(ImmersiveIntelligence.MODID, "packer/default"));

		railwayUpgrade = new MachineUpgradeModel(IIContent.UPGRADE_PACKER_RAILWAY, upgradeParts,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_railway"));
		namingUpgrade = new MachineUpgradeModel(IIContent.UPGRADE_PACKER_NAMING, upgradeParts,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_naming"));

		fluidUpgrade = new MachineUpgradeModel(IIContent.UPGRADE_PACKER_FLUID, upgradeParts,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_fluid"));
		energyUpgrade = new MachineUpgradeModel(IIContent.UPGRADE_PACKER_ENERGY, upgradeParts,
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
