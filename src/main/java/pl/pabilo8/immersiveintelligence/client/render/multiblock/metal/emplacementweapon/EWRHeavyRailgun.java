package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBlendModeGroup;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponHeavyRailgun;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Applies Heavy Railgun aiming, staged loading, and firing animations.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 17.08.2026
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRHeavyRailgun extends EmplacementWeaponRenderer<EmplacementWeaponHeavyRailgun>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, load, load2, fire;

	public EWRHeavyRailgun()
	{
		super("heavy_railgun");
	}

	@Override
	public void reloadModels()
	{
		super.reloadModels();
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN, MODEL_DIR.with("heavy_railgun_preview.obj"));
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(super.provideModel(header, style, upgrades),
				new AMTLocator("base", header),
				new AMTLocator("gun_origin", header),
				new AMTParticle("fire", header)
						.setParticle(IIParticles.PARTICLE_GUNFIRE),
				new AMTBlendModeGroup("turret_chargeup_blend", header),
				new AMTBlendModeGroup("gun_chargeup_blend", header),
				new AMTBullet("shell1", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell2", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell3", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell4", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell5", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell6", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell7", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell8", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_hatch1", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_hatch2", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_hatch3", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_hatch4", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade))
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load1"));
		this.load2 = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load2"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
	}

	@Override
	public void apply(EmplacementWeaponHeavyRailgun weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(-90, 90, partialTicks));

		this.fire.apply(1f-Math.min(1f, (weapon.getShotDelay()-weapon.gunHandler.getShotDelay(partialTicks))/(weapon.getShotDelay()*0.5f)));
		float loading = weapon.getReloadProgress(partialTicks);
		if(weapon.isFinalReloadBatch())
		{
			this.load.apply(0);
			this.load2.apply(loading);
		}
		else
		{
			this.load.apply(loading);
			this.load2.apply(0);
		}
	}
}
