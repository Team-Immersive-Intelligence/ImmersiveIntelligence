package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponRocketLauncher;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Applies Rocket Launcher aiming, two-row loading, and firing animations.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRRocketLauncher extends EmplacementWeaponRenderer<EmplacementWeaponRocketLauncher>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, chill, loadUpper, loadLower;
	private List<AMTCrossVariantReference<AMTBullet>> rockets;

	public EWRRocketLauncher()
	{
		super("rocket_launcher");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				new AMTModel(
						new AMTLocator("turen_origin", header),
						new AMTLocator("projector_origin", header),
						new AMTBullet("rocket1", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket2", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket3", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket4", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket5", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket6", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket7", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight)),
						new AMTBullet("rocket8", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoRocketLight))
				)
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.loadUpper = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load_upper"));
		this.loadLower = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load_lower"));
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));

		this.rockets = Arrays.asList(
				new AMTCrossVariantReference<>("rocket1", model),
				new AMTCrossVariantReference<>("rocket2", model),
				new AMTCrossVariantReference<>("rocket3", model),
				new AMTCrossVariantReference<>("rocket4", model),
				new AMTCrossVariantReference<>("rocket5", model),
				new AMTCrossVariantReference<>("rocket6", model),
				new AMTCrossVariantReference<>("rocket7", model),
				new AMTCrossVariantReference<>("rocket8", model)
		);
	}

	@Override
	public void apply(EmplacementWeaponRocketLauncher weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		this.applyAmmoItems(weapon, BulletState.BULLET_UNUSED, rockets);

		float loading = weapon.getReloadProgress(partialTicks);
		if(weapon.getReloadStage() > 0)
		{
			this.loadUpper.apply(1);
			this.loadLower.apply(loading);
		}
		else
		{
			this.loadLower.apply(0);
			this.loadUpper.apply(loading);
		}

		float chillProgress = weapon.getChillProgress(partialTicks);
		if(chillProgress > 0)
			this.chill.apply(chillProgress);
	}
}
