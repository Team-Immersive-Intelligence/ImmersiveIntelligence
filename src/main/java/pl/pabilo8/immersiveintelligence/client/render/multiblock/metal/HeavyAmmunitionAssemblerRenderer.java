package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityHeavyAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/heavy_ammunition_assembler", clazz = TileEntityHeavyAmmunitionAssembler.class)
public class HeavyAmmunitionAssemblerRenderer extends IIMultiblockRenderer<TileEntityHeavyAmmunitionAssembler>
{
	final HashMap<IAmmoTypeItem<?, ?>, IIAnimationCompiledMap> productionAnimations = new HashMap<>();
	AMTModel model;
	AMT glass;
	AMTBullet casing, core;
	IIAnimationCompiledMap drawer1, drawer2, drawer3, drawer4;

	@Override
	public void drawAnimated(TileEntityHeavyAmmunitionAssembler te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state
		model.defaultize();

		//Draw drawer animations
		drawer1.apply(te.drawer1.getProgress(partialTicks));
		drawer2.apply(te.drawer2.getProgress(partialTicks));
		drawer3.apply(te.drawer3.getProgress(partialTicks));
		drawer4.apply(te.drawer4.getProgress(partialTicks));

		//Apply standard rotations
		applyStandardRotation(te.facing);
		if(!te.getIsMirrored())
			mirrorRender();
		GlStateManager.translate(0, 0, 0);

		//Render solid part of the model
		glass.setVisible(false);
		model.render(tes, buf);

		//Render translucent part of the model
		glass.setVisible(true);
		glass.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model, header -> new AMT[]{
				new AMTLocator("total", header),
				casing = new AMTBullet("casing", header, null).withState(BulletState.CASING),
				core = new AMTBullet("core", header, null).withState(BulletState.CORE)
		});
		glass = this.model.getPart("glass");

		drawer1 = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer1"));
		drawer2 = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer2"));
		drawer3 = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer3"));
		drawer4 = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/drawer4"));


		productionAnimations.clear();
		/*productionAnimations.put(IIContent.itemAmmoRocketLight, IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/rocket_6bcal")));
		for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.RECIPES)
			productionAnimations.put(recipe.ammoItem, IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/"+recipe.ammoItem.getName())));*/
	}
}
