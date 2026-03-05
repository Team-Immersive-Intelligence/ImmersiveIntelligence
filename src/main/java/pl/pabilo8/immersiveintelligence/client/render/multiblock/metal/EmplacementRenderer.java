package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon.EmplacementWeaponRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.09.2025
 */
@RegisteredTileRenderer(name = "multiblock/emplacement", clazz = TileEntityEmplacement.class)
public class EmplacementRenderer extends IIMultiblockRenderer<TileEntityEmplacement>
{
	private static final Map<Class<? extends EmplacementWeapon>, EmplacementWeaponRenderer<?>> weaponRenderers = new HashMap<>();
	private AMTCachedModel<TileEntityEmplacement> model;
	private IIAnimationCachedMap animationOpen;


	@Override
	public void drawAnimated(TileEntityEmplacement te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply mirroring and style customization (variant)
		applyStandardMirroring(te, true);
		model.getVariant(te, te.style, te.currentWeapon==null?"": te.currentWeapon.getName(), te.upgradeManager);

		//Apply platform and door animation
		animationOpen.apply(te.door.getProgress(partialTicks));

		//Apply weapon animations
		if(te.currentWeapon!=null)
		{
			//noinspection rawtypes
			EmplacementWeaponRenderer r = weaponRenderers.get(te.currentWeapon.getClass());
			if(r!=null)
				//noinspection unchecked
				r.apply(te.currentWeapon, model, buf, tes, partialTicks);
			else
				IILogger.error("No renderer found for emplacement weapon "+te.currentWeapon.getClass().getName());
		}

		//Render the model
		model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		ResLoc modelDir = IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/");
		this.model = AMTCachedModelBuilder.startTileEntityModel(TileEntityEmplacement.class)
				.withModel(model)
				.withHeader(modelDir.with("emplacement.obj.amt"))
				//Style Variants
				.withModel(te -> te==null||te.style.getStyle().equals("sandbags"),
						modelDir.with("variant_sandbags.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("bricks"),
						modelDir.with("variant_bricks.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("concrete"),
						modelDir.with("variant_concrete.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("wooden"),
						modelDir.with("variant_wooden.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("steel"),
						modelDir.with("variant_steel.obj"))
				//Weapon
				.withHeaderProvider(te -> {
					if(te!=null&&te.currentWeapon!=null)
						return weaponRenderers.get(te.currentWeapon.getClass()).provideHeader();
					return null;
				})
				.withModelProvider((te, header) -> {
					if(te!=null&&te.currentWeapon!=null)
						return weaponRenderers.get(te.currentWeapon.getClass()).provideModel(header, te.style.getStyle(), te.upgradeManager.getAllInstalled()).getParts();
					return new AMT[0];
				})
				.build();

		this.animationOpen = IIAnimationCachedMap.create(this.model, IIReference.RES_II.with("emplacement/open"));
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withBaseModelLocation(modelDir.with("upgrade_preview_base.obj"));
		for(EmplacementWeaponRenderer<?> weaponRenderer : weaponRenderers.values())
			weaponRenderer.loadAnimations(this.model);
	}

	public static void registerWeaponRenderer(Class<? extends EmplacementWeapon> weaponClass, EmplacementWeaponRenderer<?> renderer)
	{
		weaponRenderers.put(weaponClass, renderer);
	}
}
