package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponMachinegun;
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
public class EWRMachinegun extends EmplacementWeaponRenderer<EmplacementWeaponMachinegun>
{
	private IIAnimationCachedMap install, uninstall, rotateYaw, rotatePitch, fire;
	private AMTCrossVariantReference<AMTBullet> bulletR1, bulletR2, bulletR3, bulletR4, bulletR5;
	private AMTCrossVariantReference<AMTBullet> bulletL1, bulletL2, bulletL3, bulletL4, bulletL5;

	public EWRMachinegun()
	{
		super("machinegun");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		//Heavy Barrel upgrade model
		AMTModel upgradeHeavyBarrel = upgrades.contains(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL)?
				new AMTModel(DefaultVertexFormats.BLOCK, MODEL_DIR.with("machinegun_heavy_barrel").withExtension(ResLoc.EXT_OBJ)):
				new AMTModel();
		AMTModel upgradeWaterCooled = upgrades.contains(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED)?
				new AMTModel(DefaultVertexFormats.BLOCK, MODEL_DIR.with("machinegun_watercooled").withExtension(ResLoc.EXT_OBJ)):
				new AMTModel();

		return new AMTModel(
				//Base model
				super.provideModel(header, style, upgrades),
				//Style-dependent addon
				new AMTModel(DefaultVertexFormats.BLOCK, MODEL_DIR.with("machinegun_variant_"+style).withExtension(ResLoc.EXT_OBJ)),
				//Upgrades
				upgradeHeavyBarrel, upgradeWaterCooled,
				//FX
				new AMTModel(
						//Gunfire particles
						new AMTParticle("fire1", header)
								.setParticle(IIParticles.PARTICLE_GUNFIRE),
						new AMTParticle("fire2", header)
								.setParticle(IIParticles.PARTICLE_GUNFIRE),

						//Right Belt bullets
						new AMTBullet("shell_r1", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_r2", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_r3", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_r4", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_r5", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),

						//Left belt bullets
						new AMTBullet("shell_l1", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l2", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l3", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l4", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l5", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED)
				)
		);
	}

	@Override
	public void reloadModels()
	{
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN, MODEL_DIR.with("machinegun_preview.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL, MODEL_DIR.with("machinegun_heavy_barrel.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED, MODEL_DIR.with("machinegun_watercooled.obj"));
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		//Animations
		this.install = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("install"));
		this.uninstall = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("uninstall"));
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));

		//Right Belt bullets
		this.bulletR1 = new AMTCrossVariantReference<>("shell_r1", model);
		this.bulletR2 = new AMTCrossVariantReference<>("shell_r2", model);
		this.bulletR3 = new AMTCrossVariantReference<>("shell_r3", model);
		this.bulletR4 = new AMTCrossVariantReference<>("shell_r4", model);
		this.bulletR5 = new AMTCrossVariantReference<>("shell_r5", model);
		//Left Belt bullets
		this.bulletL1 = new AMTCrossVariantReference<>("shell_l1", model);
		this.bulletL2 = new AMTCrossVariantReference<>("shell_l2", model);
		this.bulletL3 = new AMTCrossVariantReference<>("shell_l3", model);
		this.bulletL4 = new AMTCrossVariantReference<>("shell_l4", model);
		this.bulletL5 = new AMTCrossVariantReference<>("shell_l5", model);
	}

	@Override
	public void apply(EmplacementWeaponMachinegun weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		NonNullList<ItemStack> loadedAmmo = weapon.getLoadedAmmo();
		int ammoCount = loadedAmmo.size();
		this.bulletL1.get().withStack(ammoCount > 0?loadedAmmo.get(0): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletL2.get().withStack(ammoCount > 1?loadedAmmo.get(1): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletL3.get().withStack(ammoCount > 2?loadedAmmo.get(2): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletL4.get().withStack(ammoCount > 3?loadedAmmo.get(3): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletL5.get().withStack(ammoCount > 4?loadedAmmo.get(4): ItemStack.EMPTY, BulletState.BULLET_UNUSED);

		this.bulletR1.get().withStack(ammoCount > 0?loadedAmmo.get(0): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletR2.get().withStack(ammoCount > 1?loadedAmmo.get(1): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletR3.get().withStack(ammoCount > 2?loadedAmmo.get(2): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletR4.get().withStack(ammoCount > 3?loadedAmmo.get(3): ItemStack.EMPTY, BulletState.BULLET_UNUSED);
		this.bulletR5.get().withStack(ammoCount > 4?loadedAmmo.get(4): ItemStack.EMPTY, BulletState.BULLET_UNUSED);

		assert weapon.setup!=null;
		if(weapon.setup.getState())
			this.install.apply(weapon.setup.getProgress(partialTicks));
		else
			this.uninstall.apply(weapon.setup.getProgress(partialTicks));

		weapon.aim.withCenterYaw(0);
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		this.fire.apply(weapon.gunHandler.getShotDelay(partialTicks));
	}
}
