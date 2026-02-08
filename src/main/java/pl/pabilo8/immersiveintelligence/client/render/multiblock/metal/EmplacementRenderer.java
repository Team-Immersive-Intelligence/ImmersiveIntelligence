package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.09.2025
 */
@RegisteredTileRenderer(name = "multiblock/emplacement", clazz = TileEntityEmplacement.class)
public class EmplacementRenderer extends IIMultiblockRenderer<TileEntityEmplacement>
{
	private AMTCachedModel<TileEntityEmplacement> model;
	private IIAnimationCachedMap animationOpen;

	@Override
	public void drawAnimated(TileEntityEmplacement te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply mirroring and style customization (variant)
		applyStandardMirroring(te, true);
		model.getVariant(te, te.style);
		//Apply platform and door animation
		animationOpen.apply(te.door.getProgress(partialTicks));
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
				.withModel(modelDir.with("weapon/light_howitzer.obj"))
				.withHeader(modelDir.with("weapon/light_howitzer.obj.amt"))
				.build();

		this.animationOpen = IIAnimationCachedMap.create(this.model, IIReference.RES_II.with("emplacement/open"));

		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withBaseModelLocation(modelDir.with("upgrade_preview_base.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN, modelDir.with("weapon/machinegun.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER, modelDir.with("weapon/infrared_observer.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON, modelDir.with("weapon/autocannon.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER, modelDir.with("weapon/heavy_chemthrower.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN, modelDir.with("weapon/heavy_railgun.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_SEARCHLIGHT, modelDir.with("weapon/searchlight.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER, modelDir.with("weapon/spotlight_tower.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_TESLA, modelDir.with("weapon/tesla.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_CPDS, modelDir.with("weapon/cpds.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MORTAR, modelDir.with("weapon/mortar.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_LIGHT_HOWITZER, modelDir.with("weapon/light_howitzer.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MLRS, modelDir.with("weapon/mlrs.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_GUIDED_MISSILE_LAUNCHER, modelDir.with("weapon/guided_missile_launcher.obj"))
		;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);

		AMTLoader.preloadTexturesFromOBJ(IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/weapon/light_howitzer.obj"), map);
	}
}
