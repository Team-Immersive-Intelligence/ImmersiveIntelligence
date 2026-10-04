package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBlendModeGroup;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponHeavyRailgun;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.Arrays;
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
	private IIAnimationCachedMap rotateYaw, rotatePitch, load, load2, fire, chill;
	private List<AMTCrossVariantReference<AMTBullet>> shellsBasket, shellsLoaded, shellsHead;

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
				new AMTLocator("load_shell1", header),
				new AMTLocator("load_shell2", header),
				new AMTLocator("shell_head", header),
				new AMTLocator("shell_hatch", header),
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
				new AMTBullet("shell_hatch4", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_head1", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_head2", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_head3", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade)),
				new AMTBullet("shell_head4", header, AmmoRegistry.getGenericModel(IIContent.itemRailgunGrenade))
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
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));

		this.shellsBasket = Arrays.asList(
				new AMTCrossVariantReference<>("shell1", model),
				new AMTCrossVariantReference<>("shell2", model),
				new AMTCrossVariantReference<>("shell3", model),
				new AMTCrossVariantReference<>("shell4", model),
				new AMTCrossVariantReference<>("shell5", model),
				new AMTCrossVariantReference<>("shell6", model),
				new AMTCrossVariantReference<>("shell7", model),
				new AMTCrossVariantReference<>("shell8", model)
		);
		this.shellsLoaded = Arrays.asList(
				new AMTCrossVariantReference<>("shell_hatch1", model),
				new AMTCrossVariantReference<>("shell_hatch2", model),
				new AMTCrossVariantReference<>("shell_hatch3", model),
				new AMTCrossVariantReference<>("shell_hatch4", model)
		);
		this.shellsHead = Arrays.asList(
				new AMTCrossVariantReference<>("shell_head1", model),
				new AMTCrossVariantReference<>("shell_head2", model),
				new AMTCrossVariantReference<>("shell_head3", model),
				new AMTCrossVariantReference<>("shell_head4", model)
		);
	}

	@Override
	public void apply(EmplacementWeaponHeavyRailgun weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(-90, 90, partialTicks));

		//Firing animations
		float fireProgress = weapon.gunHandler.getShotDelay(partialTicks)/weapon.getShotDelay();
		this.fire.apply(1f-MathHelper.clamp((fireProgress-0.5f)/0.45f, 0, 1));

		//The head and hatch follow the current four-round batch, including shells being loaded.
		float loading = weapon.getReloadProgress(partialTicks);
		NonNullList<ItemStack> loadedAmmo = weapon.getLoadedAmmo();
		applyAmmoItems(weapon, weapon.getUnloadedAmmo(), BulletState.BULLET_USED, this.shellsBasket);
		applyAmmoItems(weapon, loadedAmmo, BulletState.BULLET_USED, this.shellsHead);
		applyAmmoItems(weapon, loadedAmmo, BulletState.BULLET_USED, this.shellsLoaded);

		//Loading animations
		if(weapon.isFinalReloadBatch())
			this.load2.apply(loading);
		else
			this.load.apply(loading);

		//Idle animation
		float chillProgress = weapon.getChillProgress(partialTicks);
		if(chillProgress > 0)
			this.chill.apply(chillProgress);
	}
}
