package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
@RegisteredTileRenderer(name = "multiblock/ammunition_assembler", clazz = TileEntityAmmunitionAssembler.class)
public class AmmunitionAssemblerRenderer extends IIMultiblockRenderer<TileEntityAmmunitionAssembler>
{
	AMT[] model;
	AMTLocator total;
	AMTBullet casing, core;
	IIAnimationCompiledMap hatch;
	final HashMap<IAmmoTypeItem<?, ?>, IIAnimationCompiledMap> productionAnimations = new HashMap<>();

	@Override
	public void drawAnimated(TileEntityAmmunitionAssembler te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		for(AMT amt : model)
			amt.defaultize();
		applyStandardMirroring(te, true);

		hatch.apply(te.hatch.getProgress(partialTicks));

		ItemStack stack = te.getProductionResult(0);
		if(!stack.isEmpty())
		{
			IAmmoTypeItem<?, ?> item = (IAmmoTypeItem<?, ?>)stack.getItem();
			IIAnimationCompiledMap anim = productionAnimations.get(item);
			IAmmoModel<?, ?> model = AmmoRegistry.getGenericModel(item);

			for(int i = te.processQueue.size()-1; i >= 0; i--)
			{
				IIMultiblockProcess<AmmunitionAssemblerRecipe> process = te.processQueue.get(i);
				anim.apply(te.getProductionProgress(process, partialTicks));
				casing.setModel(model);
				casing.withGunpowderPercentage(1f);
				casing.withStack(stack, BulletState.CASING);
				core.setModel(model);
				core.withStack(stack, BulletState.CORE);
				IIAnimationUtils.setModelVisibility(casing, true);
				IIAnimationUtils.setModelVisibility(core, true);

				total.render(tes, buf);
			}

		}

		IIAnimationUtils.setModelVisibility(casing, false);
		IIAnimationUtils.setModelVisibility(core, false);

		for(AMT amt : model)
			amt.render(tes, buf);
	}

	@Override
	protected void applyStandardMirroring(TileEntityAmmunitionAssembler te, boolean start)
	{
		if(te.getIsMirrored())
		{
			applyStandardRotation(te.facing.rotateYCCW());
		}
		else
		{
			applyStandardRotation(te.facing.rotateY());
			mirrorRender();
		}
		GlStateManager.translate(2, -0.5, -1);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), header -> new AMT[]{
				total = new AMTLocator("total", header),
				casing = new AMTBullet("case", header, null).withState(BulletState.CASING),
				core = new AMTBullet("core", header, null).withState(BulletState.CORE)
		});
		hatch = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "ammunition_assembler/door"));

		//Load ammo production animations, excluding ones for the Heavy Ammunition Assembler
		productionAnimations.clear();
		for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.RECIPES)
			if(!recipe.advanced)
				productionAnimations.put(recipe.ammoItem, IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "ammunition_assembler/"+recipe.ammoItem.getName())));
	}
}
