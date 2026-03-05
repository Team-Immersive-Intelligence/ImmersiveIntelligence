package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponAutocannon;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRAutocannon extends EmplacementWeaponRenderer<EmplacementWeaponAutocannon>
{
	private IIAnimationCachedMap install, uninstall, rotateYaw, rotatePitch, load, unload, fire;

	public EWRAutocannon()
	{
		super("autocannon");
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.install = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("install"));
		this.uninstall = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("uninstall"));
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.load = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("load"));
		this.unload = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("unload"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
	}

	@Override
	public void apply(EmplacementWeaponAutocannon weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		// TODO: hook up to weapon state (aim/reload/fire). Kept minimal until server-side state fields are finalized.
	}
}
