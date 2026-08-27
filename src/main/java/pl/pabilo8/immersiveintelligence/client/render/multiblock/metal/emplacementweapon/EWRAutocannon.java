package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponAutocannon;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Renders the four-barrel Autocannon Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 21.08.2026
 * @ii-approved 0.3.1
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRAutocannon extends EmplacementWeaponRenderer<EmplacementWeaponAutocannon>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, load, unload, chill;
	private final IIAnimationCachedMap[] fire = new IIAnimationCachedMap[4];

	public EWRAutocannon()
	{
		super("autocannon");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				new AMTModel(
						new AMTLocator("base", header),
						new AMTLocator("gun_origin", header)
				)
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		for(int i = 0; i < fire.length; i++)
			this.fire[i] = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"+(i+1)));
		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load"));
		this.unload = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("unload"));
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));
	}

	@Override
	public void apply(EmplacementWeaponAutocannon weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		if(weapon.isUnloading())
			this.unload.apply(weapon.getReloadAnimationProgress(partialTicks));
		else
			this.load.apply(weapon.getReloadProgress(partialTicks));
		this.fire[getFireAnimationVariant(weapon)].apply(getFireAnimationTime(weapon, partialTicks));
	}

	private int getFireAnimationVariant(EmplacementWeaponAutocannon weapon)
	{
		return Math.floorMod(weapon.fireTimeCounter-1, weapon.getFireAnimationVariants());
	}

	private float getFireAnimationTime(EmplacementWeaponAutocannon weapon, float partialTicks)
	{
		int shotDelay = weapon.getShotDelay();
		if(shotDelay <= 0)
			return 1f;
		return MathHelper.clamp(1f-weapon.gunHandler.getShotDelay(partialTicks)/shotDelay, 0f, 1f);
	}
}
