package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponLightHowitzer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRLightHowitzer extends EmplacementWeaponRenderer<EmplacementWeaponLightHowitzer>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, fire, load, chill;

	public EWRLightHowitzer()
	{
		super("light_howitzer");
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load"));
		this.chill = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("chill"));
	}

	@Override
	public void apply(EmplacementWeaponLightHowitzer weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		this.load.apply(weapon.gunHandler.getLoadingProgress(partialTicks));
		this.fire.apply(weapon.gunHandler.getShotDelay(partialTicks));
	}
}
