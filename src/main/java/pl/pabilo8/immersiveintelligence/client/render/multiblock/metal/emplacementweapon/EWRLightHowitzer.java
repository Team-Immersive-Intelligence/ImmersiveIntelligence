package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
	private IIAnimationCachedMap rotateYaw, rotatePitch, fire, load, chill;
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
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));

		this.shell = new AMTCrossVariantReference<>("6bCal", model);
	}

	@Override
	public void apply(EmplacementWeaponLightHowitzer weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		NonNullList<ItemStack> ammoList = weapon.getLoadedAmmo();
		ItemStack ammoStack = ammoList.isEmpty()?ItemStack.EMPTY: ammoList.get(0);
		this.shell.get().withStack(ammoStack, BulletState.BULLET_UNUSED);

		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		this.load.apply(weapon.gunHandler.getLoadingProgress(partialTicks));
		this.fire.apply(weapon.gunHandler.getShotDelay(partialTicks));
	}
}
