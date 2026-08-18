package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponRocketLauncher;

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
	private IIAnimationCachedMap rotateYaw, rotatePitch, fire, loadUpper, loadLower;

	public EWRRocketLauncher()
	{
		super("rocket_launcher");
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
		this.loadUpper = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load_upper"));
		this.loadLower = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load_lower"));
	}

	@Override
	public void apply(EmplacementWeaponRocketLauncher weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));

		float loading = weapon.getReloadProgress(partialTicks);
		if(weapon.getReloadStage() > 0)
		{
			this.loadUpper.apply(1);
			this.loadLower.apply(loading);
		}
		else
		{
			this.loadUpper.apply(loading);
			this.loadLower.apply(0);
		}
		this.fire.apply(weapon.gunHandler.getShotDelay(partialTicks));
	}
}
