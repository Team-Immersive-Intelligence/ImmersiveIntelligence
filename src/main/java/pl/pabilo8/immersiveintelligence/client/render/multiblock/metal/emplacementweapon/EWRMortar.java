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
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponMortar;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Renders the Mortar Emplacement weapon with its current shell.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.09.2026
 * @since 19.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRMortar extends EmplacementWeaponRenderer<EmplacementWeaponMortar>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, fire, load, chill;
	private AMTCrossVariantReference<AMTBullet> shell, shellLoaded;

	public EWRMortar()
	{
		super("mortar");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				new AMTModel(
						new AMTLocator("turret_origin", header),
						new AMTLocator("mortar_origin", header),
						new AMTLocator("mortar_move_mechanism", header),
						new AMTBullet("shell", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMortar)),
						new AMTBullet("shell_loaded", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoMortar))
				)
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load"));
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));
		this.shell = new AMTCrossVariantReference<>("shell", model);
		this.shellLoaded = new AMTCrossVariantReference<>("shell_loaded", model);
	}

	@Override
	public void apply(EmplacementWeaponMortar weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		float loadingProgress = weapon.gunHandler.getLoadingProgress(partialTicks);
		float shotDelay = 1f-(weapon.gunHandler.getShotDelay(partialTicks)/weapon.getShotDelay());
		float chillProgress = weapon.getChillProgress(partialTicks);

		this.applyAmmoItem(weapon, BulletState.BULLET_UNUSED, shell);
		this.applyAmmoItem(weapon, BulletState.BULLET_UNUSED, shellLoaded);
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(-90, 90, partialTicks));

		if(loadingProgress!=0&&loadingProgress!=1)
			this.load.apply(loadingProgress);
		if(shotDelay > 0)
			this.fire.apply(shotDelay);
		if(chillProgress > 0)
			this.chill.apply(chillProgress);
	}
}
