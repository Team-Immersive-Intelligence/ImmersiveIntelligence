package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponCPDS;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRCPDS extends EmplacementWeaponRenderer<EmplacementWeaponCPDS>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, cameraTrack, cameraIdle, load, unload, fire;

	public EWRCPDS()
	{
		super("cpds");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(super.provideModel(header, style, upgrades),
				new AMTParticle("fire", header)
						.setParticle(IIParticles.PARTICLE_GUNFIRE)
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.cameraTrack = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("camera_track"));
		this.cameraIdle = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("camera_idle"));
		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load"));
		this.unload = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("unload"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
	}

	@Override
	public void apply(EmplacementWeaponCPDS weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));

		this.fire.apply(weapon.shootDelay);
	}
}
