package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponGuidedMissileLauncher;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRGuidedMissileLauncher extends EmplacementWeaponRenderer<EmplacementWeaponGuidedMissileLauncher>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, trackerPitch, load, fire;
	private AMTCrossVariantReference<AMTBullet> rocket;

	public EWRGuidedMissileLauncher()
	{
		super("guided_missile_launcher");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				//Style-dependent
				new AMTModel(DefaultVertexFormats.BLOCK, MODEL_DIR.with("guided_missile_launcher_"+style).withExtension(ResLoc.EXT_OBJ)),
				//FX
				new AMTModel(
						new AMTBullet("rocket", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoGuidedMissile))
				)
		);
	}

	@Override
	public void reloadModels()
	{
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_GUIDED_MISSILE_LAUNCHER,
						MODEL_DIR.with("guided_missile_launcher_preview").withExtension(ResLoc.EXT_OBJ));
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.trackerPitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("tracker_pitch"));

		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));

		this.rocket = new AMTCrossVariantReference<>("rocket", model);
	}

	@Override
	public void apply(EmplacementWeaponGuidedMissileLauncher weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		rocket.get().withState(BulletState.BULLET_UNUSED)
				.withProperties(IIContent.ammoCoreIron, CoreType.SHAPED, DecoColors.POWER2);
		load.apply(AMTUtils.getDebugProgress(120, partialTicks));

		rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		float pitch = weapon.aim.getPitchNormalized(partialTicks);
		rotatePitch.apply(pitch);
		trackerPitch.apply(pitch);
		fire.apply(weapon.shootDelay);
	}
}
