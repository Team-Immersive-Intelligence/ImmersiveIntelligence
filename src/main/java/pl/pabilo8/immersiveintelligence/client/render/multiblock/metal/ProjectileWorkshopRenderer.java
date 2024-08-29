package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @updated 09.07.2024
 * @ii-approved 0.3.1
 * @since 21-06-2019
 */
@RegisteredTileRenderer(name = "multiblock/projectile_workshop", clazz = TileEntityProjectileWorkshop.class)
public class ProjectileWorkshopRenderer extends IIMultiblockRenderer<TileEntityProjectileWorkshop>
{
	AMT[] model;
	IIBooleanAnimation active, mode;
	AMTItem item;
	AMTBullet bullet;
	IIAnimationCompiledMap lid1, lid2, coreWorkshop, coreFiller;

	@Override
	public void drawAnimated(TileEntityProjectileWorkshop te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Animate the breadbox
		lid1.apply(te.lid1.getProgress(partialTicks));
		lid2.apply(te.lid2.getProgress(partialTicks));

		//Get parameters from TE
		active.apply(!te.getRedstoneAtPos(0));
		boolean upgradeFiller = te.hasUpgrade(IIContent.UPGRADE_CORE_FILLER);

		//Apply mode visibility and animation
		mode.apply(upgradeFiller);
		float progress = 0;

		//Apply bullet and item info
		if(te.currentProcess!=null)
		{
			bullet.setModel(AmmoRegistry.getGenericModel(te.currentProcess.recipe.ammo));
			bullet.withStack(te.currentProcess.recipe.getEffect(), BulletState.CORE);
			if(!upgradeFiller)
				item.setStack(te.currentProcess.recipe.ingredient);
			progress = te.getProductionProgress(te.currentProcess, partialTicks);
		}
		(upgradeFiller?coreFiller: coreWorkshop).apply(progress);

		//Render
		applyStandardMirroring(te, true);
		for(AMT amt : model)
			amt.render(tes, buf);

	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), header -> new AMT[]{
				new AMTLocator("core_workshop", header),
				new AMTLocator("core_filler", header),
				item = new AMTItem("item", header),
				bullet = new AMTBullet("item_out", header, null)
						.withState(BulletState.CORE)
		});
		active = new IIBooleanAnimation(
				IIAnimationUtils.getPart(model, "conveyor"),
				IIAnimationUtils.getPart(model, "conveyor_off")
		);
		mode = new IIBooleanAnimation(
				IIAnimationUtils.getPart(model, "core_filler"),
				IIAnimationUtils.getPart(model, "core_workshop")
		);
		lid1 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "projectile_workshop/left_door"));
		lid2 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "projectile_workshop/right_door"));

		coreWorkshop = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "projectile_workshop/production_core"));
		coreFiller = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "projectile_workshop/production_filling"));
	}
}
