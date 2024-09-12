package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityHeavyAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
@RegisteredTileRenderer(name = "multiblock/heavy_ammunition_assembler", clazz = TileEntityHeavyAmmunitionAssembler.class)
public class HeavyAmmunitionAssemblerRenderer extends IIMultiblockRenderer<TileEntityHeavyAmmunitionAssembler>
{
	AMT[] model;
	AMT glass;
	AMTBullet casing, core;
	IIAnimationCompiledMap drawer1, drawer2, drawer3, drawer4;
	final HashMap<IAmmoTypeItem<?, ?>, IIAnimationCompiledMap> productionAnimations = new HashMap<>();

	@Override
	public void drawAnimated(TileEntityHeavyAmmunitionAssembler te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		for(AMT amt : model)
			amt.defaultize();

		drawer1.apply(te.drawer1.getProgress(partialTicks));
		drawer2.apply(te.drawer2.getProgress(partialTicks));
		drawer3.apply(te.drawer3.getProgress(partialTicks));
		drawer4.apply(te.drawer4.getProgress(partialTicks));

		applyStandardRotation(te.facing);
		if(!te.getIsMirrored())
			mirrorRender();
		GlStateManager.translate(0, 0, 0);
		IIAnimationUtils.setModelVisibility(glass, false);
		for(AMT amt : model)
			amt.render(tes, buf);

		IIAnimationUtils.setModelVisibility(glass, true);
		glass.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), header -> new AMT[]{
				new AMTLocator("total", header),
				casing = new AMTBullet("casing", header, null).withState(BulletState.CASING),
				core = new AMTBullet("core", header, null).withState(BulletState.CORE)
		});
		glass = IIAnimationUtils.getPart(model, "glass");

		drawer1 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer1"));
		drawer2 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer2"));
		drawer3 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer3"));
		drawer4 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer4"));


		productionAnimations.clear();
		/*productionAnimations.put(IIContent.itemAmmoRocketLight, IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/rocket_6bcal")));
		for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.RECIPES)
			productionAnimations.put(recipe.ammoItem, IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/"+recipe.ammoItem.getName())));*/
	}
}
