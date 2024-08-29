package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIISawBlade.SawBlades;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "sawmill", clazz = TileEntitySawmill.class)
public class SawmillRenderer extends IITileRenderer<TileEntitySawmill>
{
	private AMTModelCache<TileEntity> model;
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
	public void draw(TileEntitySawmill te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Prepare variables
		ItemStack sawBlade = te.inventory.get(TileEntitySawmill.SLOT_SAWBLADE);
		ItemStack sawDust = te.getInventory().get(TileEntitySawmill.SLOT_SAWDUST);
		float progress = te.getProductionProgress(te.currentProcess, partialTicks);

		//Get model variant, defaultize
		applyStandardRotation(te.facing);
		model.getVariant(sawBlade.isEmpty()?"": ((ISawblade)sawBlade.getItem()).getMaterialName(sawBlade), te);
		for(AMT mod : model)
			mod.defaultize();

		//Set item display
		partItemInput.get().setStack(te.inventory.get(TileEntitySawmill.SLOT_INPUT));
		//TODO: 30.07.2023 different output
		partItemOutput.get().setStack(te.inventory.get(TileEntitySawmill.SLOT_OUTPUT));
		partItemInserter.get().setStack(ItemStack.EMPTY);

		//Dust pile size
		animationDustPile.apply(sawDust.isEmpty()?0f: sawDust.getCount()/(float)sawDust.getMaxStackSize());

		//Saw Blade model visibility
		IIAnimationUtils.setModelVisibility(partSawblade.get(), !te.getInventory().get(TileEntitySawmill.SLOT_SAWBLADE).isEmpty());

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
			partItemInserter.get().setStack(recipe.itemInput.getExampleStack());
			partItemOutput.get().setStack(recipe.itemOutput);
		}
		else
			animationProductionReach.apply(0f);

		//GUI/Interaction animation
		animationInteract.apply(te.vise.getProgress(partialTicks));

		//Flip
		if(!te.mirrored) mirrorRender();

		//Render
		for(AMT mod : model)
			mod.render(tes, buf);

		//Revert
		if(!te.mirrored) unMirrorRender();
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = AMTModelCacheBuilder.startTileEntityModel()
				.withModel(((IESmartObjModel)sModel.getSecond()).getModel())
				.withModel(ResLoc.of(IIReference.RES_BLOCK_MODEL, "multiblock/sawmill/sawblade.obj"))
				.withModel(ResLoc.of(IIReference.RES_BLOCK_MODEL, "multiblock/sawmill/sawdust.obj"))
				.withHeader(IIAnimationLoader.loadHeader(sModel.getSecond()))
				.withModelProvider((tile, header) -> new AMT[]{
						new AMTItem("item_input", header),
						new AMTItem("item_output", header),
						new AMTItem("item_inserter", header),
						new AMTLocator("cardan1", header),
						new AMTLocator("cardan3", header)
				})
				//TODO: 18.07.2023 dust colors
				.withTextureProvider((res, tile) -> {
					//Default
					if(!(tile instanceof TileEntitySawmill)||!tile.hasWorld()) return ClientUtils.getSprite(res);

					TileEntitySawmill sawmill = (TileEntitySawmill)tile;
					ItemStack sawblade = sawmill.inventory.get(TileEntitySawmill.SLOT_SAWBLADE);

					//Sawblade
					if(res.getResourcePath().endsWith("iron")&&sawblade.getItem() instanceof ISawblade)
						return ClientUtils.getSprite(((ISawblade)sawblade.getItem()).getSawbladeTexture(sawblade));

					//Dust

					return ClientUtils.getSprite(res);
				})
				.build();

		partSawblade = new AMTCrossVariantReference<>("sawblade", model);
		partItemInput = new AMTCrossVariantReference<>("item_input", model);
		partItemOutput = new AMTCrossVariantReference<>("item_output", model);
		partItemInserter = new AMTCrossVariantReference<>("item_inserter", model);

		animationInteract = IIAnimationCachedMap.create(model, ResLoc.of(IIReference.RES_II, "sawmill/interact"));
		animationRotate = IIAnimationCachedMap.create(model, ResLoc.of(IIReference.RES_II, "sawmill/rotate"));
		animationDustPile = IIAnimationCachedMap.create(model, ResLoc.of(IIReference.RES_II, "sawmill/sawdust"));

		animationProductionStart = IIAnimationCachedMap.create(model, ResLoc.of(IIReference.RES_II, "sawmill/production_start"));
		animationProductionLoop = IIAnimationCachedMap.create(model, ResLoc.of(IIReference.RES_II, "sawmill/production_loop"));
		animationProductionReach = IIAnimationCachedMap.create(model, ResLoc.of(IIReference.RES_II, "sawmill/production_reach"));

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
		IIAnimationUtils.disposeOf(model);
	}
}
