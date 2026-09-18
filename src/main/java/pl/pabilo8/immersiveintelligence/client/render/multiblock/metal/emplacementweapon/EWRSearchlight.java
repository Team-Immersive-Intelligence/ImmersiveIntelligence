package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponSearchlight;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class EWRSearchlight extends EmplacementWeaponRenderer<EmplacementWeaponSearchlight>
{
	private IIAnimationCachedMap rotateYaw, rotatePitch, active;
	private AMTCrossVariantReference<AMTParticle> ray;

	public EWRSearchlight()
	{
		super("searchlight");
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				new AMTLocator("turret_origin", header),
				new AMTLocator("projector_origin", header),
				new AMTParticle("ray", header)
						.setParticle("light/searchlight")
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.active = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("active"));
		this.ray = new AMTCrossVariantReference<>("ray", model);
	}

	@Override
	public void apply(EmplacementWeaponSearchlight weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		assert weapon.setup!=null;

		//Rotation animation
		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(-90, 90, partialTicks));

		//Heat-up animation
		this.active.apply(weapon.setup.getProgress(partialTicks));

		//Apply light beam length vector
		this.ray.get().setParticleProperty(ParticleProperties.STRETCH, weapon.targetPosition);
	}
}
