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
	private IIAnimationCachedMap install, uninstall, rotateYaw, rotatePitch, fire;

	public EWRLightHowitzer()
	{
		super("light_howitzer");
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.install = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("install"));
		this.uninstall = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("uninstall"));

		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
	}

	@Override
	public void apply(EmplacementWeaponLightHowitzer weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		assert weapon.setup!=null;

		//this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
	}
}
