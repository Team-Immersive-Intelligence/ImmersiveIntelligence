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
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponMachinegun;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRMachinegun extends EmplacementWeaponRenderer<EmplacementWeaponMachinegun>
{
	private IIAnimationCachedMap install, rotateYaw, rotatePitch, fire;
	private List<AMTCrossVariantReference<AMTBullet>> bulletsLeft, bulletsRight;

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
						//Part locators
						new AMTLocator("turret_origin", header),
						new AMTLocator("cannon_origin", header),
						new AMTLocator("belt", header),
						new AMTLocator("belt2", header),
						new AMTLocator("lid1_origin", header),
						new AMTLocator("lid2_origin", header),

						//Gunfire particles
						new AMTParticle("fire1", header),
						new AMTParticle("fire2", header),

						//Right Belt bullets
						new AMTBullet("shell_l1", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l2", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l3", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l4", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l5", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l6", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),

						//Left belt bullets
						new AMTBullet("shell_l7", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l8", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l9", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l10", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l11", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
								.withState(BulletState.BULLET_UNUSED),
						new AMTBullet("shell_l12", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMachinegun))
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
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));

		//Right Belt bullets
		this.bulletsRight = Arrays.asList(
				new AMTCrossVariantReference<>("shell_l6", model),
				new AMTCrossVariantReference<>("shell_l5", model),
				new AMTCrossVariantReference<>("shell_l4", model),
				new AMTCrossVariantReference<>("shell_l3", model),
				new AMTCrossVariantReference<>("shell_l2", model),
				new AMTCrossVariantReference<>("shell_l1", model)
		);

		//Left Belt bullets
		this.bulletsLeft = Arrays.asList(
				new AMTCrossVariantReference<>("shell_l12", model),
				new AMTCrossVariantReference<>("shell_l11", model),
				new AMTCrossVariantReference<>("shell_l10", model),
				new AMTCrossVariantReference<>("shell_l9", model),
				new AMTCrossVariantReference<>("shell_l8", model),
				new AMTCrossVariantReference<>("shell_l7", model)
		);
	}

	@Override
	public void apply(EmplacementWeaponMachinegun weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		assert weapon.setup!=null;
		NonNullList<ItemStack> allAmmo = weapon.getAllAmmo();
		applyAmmoItems(weapon, allAmmo, BulletState.BULLET_UNUSED, bulletsRight);
		applyAmmoItems(weapon, allAmmo, BulletState.BULLET_UNUSED, bulletsLeft);

		float shotDelay = weapon.gunHandler.getShotDelay(partialTicks);

		this.install.apply(weapon.setup.getProgress(partialTicks));

		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));

		this.fire.apply(shotDelay);
	}
}
