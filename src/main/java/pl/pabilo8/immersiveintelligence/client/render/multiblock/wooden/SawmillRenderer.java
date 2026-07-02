package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeCachedModel.MachineCachedUpgradeModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIISawBlade.SawBlades;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.06.2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "multiblock/sawmill", clazz = TileEntitySawmill.class)
public class SawmillRenderer extends IIMultiblockRenderer<TileEntitySawmill>
{
	private AMTCachedModel<TileEntitySawmill> model;
	private AMTUpgradeCachedModel<TileEntitySawmill> upgradeSawUnregulator, upgradeGearbox;
	private IIAnimationCachedMap animationRotate, animationDustPile, animationInteract;
	private IIAnimationCachedMap animationProductionStart, animationProductionLoop, animationProductionReach;

	private AMTCrossVariantReference<AMTQuads> partSawblade;
	private AMTCrossVariantReference<AMTItem> partItemInput, partItemOutput, partItemInserter;

	@Override
	protected boolean shouldNotRender(TileEntitySawmill te)
	{
		return te==null||te.isDummy();
	}

	@Override
	public void drawAnimated(TileEntitySawmill te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Prepare variables
		ItemStack sawBlade = te.inventory.get(MultiblockSawmill.SLOT_SAWBLADE);
		ItemStack sawDust = te.getInventory().get(MultiblockSawmill.SLOT_SAWDUST);
		float progress = te.getProductionProgress(te.currentProcess, partialTicks);

		//Get model variant, defaultize
		applyStandardRotation(te.facing);
		String sawbladeName = sawBlade.isEmpty()?"": ((ISawblade)sawBlade.getItem()).getToolID(sawBlade);
		model.getVariant(te, te.upgradeManager, sawbladeName);
		model.defaultize();

		//Set item display
		partItemInput.get().setStack(te.inventory.get(MultiblockSawmill.SLOT_INPUT));
		partItemOutput.get().setStack(te.inventory.get(MultiblockSawmill.SLOT_OUTPUT));
		partItemInserter.get().setStack(ItemStack.EMPTY);

		//Dust pile size
		animationDustPile.apply(sawDust.isEmpty()?0f: sawDust.getCount()/(float)sawDust.getMaxStackSize());

		//Saw Blade model visibility
		partSawblade.get().setVisible(!te.getInventory().get(MultiblockSawmill.SLOT_SAWBLADE).isEmpty());

		//Rotation
		animationRotate.apply(IIRotaryUtils.getDisplayRotation(te, te.rotation, partialTicks));

		//Production animation
		if(progress > 0)
		{
			assert te.currentProcess!=null;
			SawmillRecipe recipe = te.currentProcess.recipe;

			//Beginning "grab" animation
			if(progress < 0.1f)
				animationProductionStart.apply(progress/0.1f);
			else
			{
				progress -= 0.1f;
				//Individual animation of each plank being cut
				double individual = ((progress*recipe.itemOutput.getCount())%0.9)/0.9f;

				animationProductionLoop.apply((float)individual);
				animationProductionReach.apply(progress);
			}

			//Currently held item
			ItemStack displayInput = te.currentProcess.processData.getItemStack("displayInput");
			partItemInserter.get().setStack(displayInput.isEmpty()?recipe.itemInput.getExampleStack(): displayInput);
			//Used to match output texture to input woodtype
			ItemStack displayOutput = te.currentProcess.processData.getItemStack("correctOutput");
			partItemOutput.get().setStack(displayOutput.isEmpty()?recipe.itemOutput: displayOutput);
		}
		else
			animationProductionReach.apply(0f);

		//GUI/Interaction animation
		animationInteract.apply(te.vise.getProgress(partialTicks));

		//Flip
		if(!te.mirrored)
			mirrorRender();

		//Display upgrade construction
		upgradeGearbox.apply(te, tes, buf, partialTicks);
		upgradeSawUnregulator.apply(te, tes, buf, partialTicks);

		//Render
		model.render(tes, buf);
		//Revert
		if(!te.mirrored)
			unMirrorRender();
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.getVariant(null);
		model.defaultize();
		animationRotate.apply(0f);
		animationInteract.apply(0f);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		ResLoc resFolder = IIReference.RES_BLOCK_MODEL.with("multiblock/sawmill/");

		//model loading start
		AMTCachedModelBuilder<TileEntitySawmill> modelBuilder = AMTCachedModelBuilder.startTileEntityModel(TileEntitySawmill.class)
				.withModel(model)
				.withModel(resFolder.with("sawblade.obj"))
				.withModel(resFolder.with("sawdust.obj"))
				.withHeader(AMTLoader.loadHeader(model))
				.withHeader(resFolder.with("sawmill_unregulator.obj.amt"))
				.withHeader(resFolder.with("sawmill_gearbox.obj.amt"))
				.withModelProvider((te, header) -> new AMT[]{
						new AMTItem("item_input", header),
						new AMTItem("item_output", header),
						new AMTItem("item_inserter", header),
						new AMTLocator("cardan1", header),
						new AMTLocator("cardan3", header),
						new AMTParticle("particle_sawdust", header)
				})
				.withTextureProvider((res, te) -> {
					//Default
					if(te==null||!te.hasWorld())
						return ClientUtils.getSprite(res);
					ItemStack sawblade = te.inventory.get(MultiblockSawmill.SLOT_SAWBLADE);

					//Sawblade
					if(res.getResourcePath().endsWith("iron")&&sawblade.getItem() instanceof ISawblade)
						return ClientUtils.getSprite(((ISawblade)sawblade.getItem()).getSawbladeTexture(sawblade));

					return ClientUtils.getSprite(res);
				});

		//upgrade models
		this.upgradeSawUnregulator = new MachineCachedUpgradeModelBuilder<>(modelBuilder)
				.withUpgrade(IIContent.UPGRADE_SAW_UNREGULATOR)
				.withConstructionModel(resFolder.with("sawmill_unregulator.obj"))
				.withAnimation(ResLoc.of(IIReference.RES_II, "sawmill/upgrade_saw_unregulator"))
				.build();
		this.upgradeGearbox = new MachineCachedUpgradeModelBuilder<>(modelBuilder)
				.withUpgrade(IIContent.UPGRADE_IMPROVED_GEARBOX)
				.withConstructionModel(resFolder.with("sawmill_gearbox.obj"))
				.withAnimation(ResLoc.of(IIReference.RES_II, "sawmill/upgrade_gearbox"))
				.build();

		this.model = modelBuilder.build();

		this.partSawblade = new AMTCrossVariantReference<>("sawblade", this.model);
		this.partItemInput = new AMTCrossVariantReference<>("item_input", this.model);
		this.partItemOutput = new AMTCrossVariantReference<>("item_output", this.model);
		this.partItemInserter = new AMTCrossVariantReference<>("item_inserter", this.model);

		this.animationInteract = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "sawmill/interact"));
		this.animationRotate = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "sawmill/rotate"));
		this.animationDustPile = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "sawmill/sawdust"));

		this.animationProductionStart = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "sawmill/production_start"));
		this.animationProductionLoop = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "sawmill/production_loop"));
		this.animationProductionReach = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "sawmill/production_reach"));

		UpgradeTechTree.getTreeFor(TileEntitySawmill.class)
				.withBaseModelLocation(resFolder.with("sawmill_base.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_IMPROVED_GEARBOX, resFolder.with("sawmill_gearbox.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_SAW_UNREGULATOR, resFolder.with("sawmill_unregulator.obj"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
		//In case of other mods that want II compat, register your sprites separately
		for(SawBlades value : SawBlades.values())
			map.registerSprite(IIContent.itemSawblade.getSawbladeTexture(IIContent.itemSawblade.getStack(value)));
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}
}
