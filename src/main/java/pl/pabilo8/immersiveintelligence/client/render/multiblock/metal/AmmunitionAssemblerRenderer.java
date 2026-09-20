package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/ammunition_assembler", clazz = TileEntityAmmunitionAssembler.class)
public class AmmunitionAssemblerRenderer extends IIMultiblockRenderer<TileEntityAmmunitionAssembler>
{
	private final HashMap<IAmmoTypeItem<?, ?>, IIAnimationCompiledMap> productionAnimations = new HashMap<>();
	private AMTModel model;
	private AMTLocator total;
	private AMTBullet casing, core, lid;
	private IIAnimationCompiledMap hatch;

	@Override
	public void drawAnimated(TileEntityAmmunitionAssembler te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.defaultize();
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

				lid.setModel(model);
				lid.withStack(stack, BulletState.LID);

				core.setModel(model);
				core.withStack(stack, BulletState.CORE);

				casing.setVisible(true);
				core.setVisible(true);
				lid.setVisible(true);

				total.render(tes, buf);
			}

		}

		casing.setVisible(false);
		core.setVisible(false);
		lid.setVisible(false);

		model.render(tes, buf);
	}

	@Override
	protected void applyStandardMirroring(TileEntityAmmunitionAssembler te, boolean start)
	{
		if(te.getIsMirrored())
			applyStandardRotation(te.facing.rotateYCCW());
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
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model, header -> new AMT[]{
				total = new AMTLocator("total", header),
				casing = new AMTBullet("case", header, null),
				lid = new AMTBullet("lid", header, null),
				core = new AMTBullet("core", header, null)
		});
		this.hatch = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "ammunition_assembler/door"));

		//Load ammo production animations, excluding ones for the Heavy Ammunition Assembler
		this.productionAnimations.clear();
		for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.getRecipes(AmmunitionAssemblerRecipe.class))
			if(!recipe.advanced)
				this.productionAnimations.put(recipe.ammoItem,
						IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "ammunition_assembler/"+recipe.ammoItem.getName())));
	}
}
