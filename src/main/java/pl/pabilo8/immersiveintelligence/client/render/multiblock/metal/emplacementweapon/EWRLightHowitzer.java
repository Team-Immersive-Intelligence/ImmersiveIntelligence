package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponLightHowitzer;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRLightHowitzer extends EmplacementWeaponRenderer<EmplacementWeaponLightHowitzer>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, fire, load, work, chill;
	private AMTCrossVariantReference<AMTBullet> shell;

	public EWRLightHowitzer()
	{
		super("light_howitzer");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				new AMTModel(
						new AMTLocator("turret_origin", header),
						new AMTLocator("barrel_origin", header),
						new AMTBullet("6bCal", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoLightArtillery)),
						new AMTBullet("casing", header, AmmoRegistry.getGenericModel(IIContent.itemAmmoLightArtillery))
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
		this.work = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("work"));
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));

		this.shell = new AMTCrossVariantReference<>("6bCal", model);
	}

	@Override
	public void apply(EmplacementWeaponLightHowitzer weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		float loadingProgress = weapon.gunHandler.getLoadingProgress(partialTicks);
		float shotDelay = weapon.gunHandler.getShotDelay(partialTicks);
		float chillProgress = weapon.getChillProgress(partialTicks);

		this.applyAmmoItem(weapon, BulletState.BULLET_UNUSED, shell);
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		this.work.apply(AMTUtils.getDebugProgress(60, partialTicks));

		if(chillProgress > 0)
			this.chill.apply(chillProgress);
		if(loadingProgress > 0)
			this.load.apply(loadingProgress);
		if(shotDelay > 0)
			this.fire.apply(shotDelay);
	}
}
