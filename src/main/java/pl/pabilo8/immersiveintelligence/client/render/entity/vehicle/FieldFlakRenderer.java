package pl.pabilo8.immersiveintelligence.client.render.entity.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBipedAdapter;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldFlak;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;

@RegisteredEntityRenderer(clazz = EntityFieldFlak.class, name = "vehicle/field_flak")
public class FieldFlakRenderer extends IIVehicleRenderer<EntityFieldFlak>
{
	private IIAnimationCachedMap aimYaw, aimPitch, load1, load2, unload1, unload2, fire1, fire2;
	private IIAnimationCachedMap forward, backward, left, right;
	private AMTCrossVariantReference<AMTBipedAdapter> bipedGunner, bipedCommander;

	public FieldFlakRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void draw(EntityFieldFlak entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply vehicle transforms
		double rotYaw = MathHelper.clampedLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
		GlStateManager.rotate((float)(rotYaw-90), 0, 1, 0);
		//TODO: 29.05.2026 proper rotations after Trej fixes the model

		model.getVariant(entity, entity.getStyle());

		//Animate wheels
		applyWheelAnimations(entity, partialTicks);

		//General animations
		aimPitch.apply(1f-entity.aim.getPitchNormalized(partialTicks));
		aimYaw.apply(1f-entity.aim.getYawNormalized(partialTicks));

		if(entity.ammoProviderMagazine1.isReloading())
			load1.apply(entity.ammoProviderMagazine1.getLoadingProgress(partialTicks));
		if(entity.ammoProviderMagazine2.isReloading())
			load2.apply(entity.ammoProviderMagazine2.getLoadingProgress(partialTicks));

		//Finally, render
		model.render(tes, buf);
	}

	@Override
	public void compileModels()
	{
		//Assemble main model
		model = AMTCachedModelBuilder.startEntityModel(EntityFieldFlak.class)
				.withModel(modelFile)
				.withHeader(headerFile)
				.withTextureProvider(this::replaceVehicleTextures)
				.withModelProvider((entity, header) -> new AMT[]{
						new AMTBipedAdapter("hans_gunner", header),
						new AMTBipedAdapter("hans_commander", header),
						new AMTLocator("turret_base", header),
						new AMTParticle("cannon1_fire", header),
						new AMTParticle("cannon2_fire", header)
				})
				.build();

		//Passenger display references
		this.bipedGunner = new AMTCrossVariantReference<>("hans_gunner", model);
		this.bipedCommander = new AMTCrossVariantReference<>("hans_commander", model);

		//General animations
		load1 = IIAnimationCachedMap.create(model, animationsDirectory.with("load1"));
		load2 = IIAnimationCachedMap.create(model, animationsDirectory.with("load2"));
		unload1 = IIAnimationCachedMap.create(model, animationsDirectory.with("unload1"));
		unload2 = IIAnimationCachedMap.create(model, animationsDirectory.with("unload2"));
		fire1 = IIAnimationCachedMap.create(model, animationsDirectory.with("fire1"));
		fire2 = IIAnimationCachedMap.create(model, animationsDirectory.with("fire2"));

		aimYaw = IIAnimationCachedMap.create(model, animationsDirectory.with("aim_yaw"));
		aimPitch = IIAnimationCachedMap.create(model, animationsDirectory.with("aim_pitch"));

		forward = IIAnimationCachedMap.create(model, animationsDirectory.with("forward"));
		backward = IIAnimationCachedMap.create(model, animationsDirectory.with("backward"));
		left = IIAnimationCachedMap.create(model, animationsDirectory.with("left"));
		right = IIAnimationCachedMap.create(model, animationsDirectory.with("right"));

		wheelAnimations = new WheelAnimationBuilder()
				.withWheel(e -> e.partWheelRight, "wheel1")
				.withWheel(e -> e.partWheelLeft, "wheel2")
				.build();

		//Upgrade system
		UpgradeTechTree.getTreeFor(EntityFieldFlak.class)
				.withBaseModelLocation(modelFile);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityFieldFlak entity, EntityLivingBase passenger, float partialTicks)
	{
		EntityVehicleSeat seat = (EntityVehicleSeat)passenger.getRidingEntity();
		if(seat==null)
			return false;
		//Select animation adapter based on seat
		switch(seat.info.getSeatID())
		{
			case "gunner":
				bipedGunner.get().applyAnimationStateTo(model);
				return true;
			case "commander":
				bipedCommander.get().applyAnimationStateTo(model);
				return true;
			default:
				return false;
		}
	}
}
