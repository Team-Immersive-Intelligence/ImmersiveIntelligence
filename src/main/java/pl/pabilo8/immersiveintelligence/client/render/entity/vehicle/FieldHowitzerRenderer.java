package pl.pabilo8.immersiveintelligence.client.render.entity.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBipedAdapter;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;

@RegisteredEntityRenderer(clazz = EntityFieldHowitzer.class, name = "vehicle/field_howitzer")
public class FieldHowitzerRenderer extends IIVehicleRenderer<EntityFieldHowitzer>
{
	private IIAnimationCachedMap load, fire, aimPitch;
	private IIAnimationCachedMap forward, backward, left, right;
	private IIAnimationCachedMap passengerDefault;
	private AMTCrossVariantReference<AMTBipedAdapter> bipedGunner, bipedCommander;

	public FieldHowitzerRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void draw(EntityFieldHowitzer entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply vehicle transforms
		applyVehicleTransforms(entity, partialTicks);
		model.getVariant(entity, entity.getStyle());
		model.defaultize();

		//Animate wheels
		applyWheelAnimations(entity, partialTicks);

		//General animations
		passengerDefault.apply(1f);
		aimPitch.apply(1f-entity.aim.getPitchNormalized(partialTicks));

		if(entity.ammoProvider.isReloading())
			load.apply(entity.ammoProvider.getLoadingProgress(partialTicks));
		if(entity.commanderControls.getKey("forward"))
			forward.apply(1f);
		else if(entity.commanderControls.getKey("backwards"))
			backward.apply(1f);
		else if(entity.commanderControls.getKey("turnLeft"))
			left.apply(1f);
		else if(entity.commanderControls.getKey("turnRight"))
			right.apply(1f);

		//Finally, render
		model.render(tes, buf);
	}

	@Override
	public void compileModels()
	{
		//Assemble main model
		model = AMTCachedModelBuilder.startEntityModel(EntityFieldHowitzer.class)
				.withModel(modelFile)
				.withHeader(headerFile)
				.withTextureProvider(this::replaceVehicleTextures)
				.withModelProvider((entity, header) -> new AMT[]{
						new AMTBipedAdapter("hans_gunner", header),
						new AMTBipedAdapter("hans_commander", header),
						new AMTParticle("fire", header)
				})
				.build();

		//Passenger display references
		this.bipedGunner = new AMTCrossVariantReference<>("hans_gunner", model);
		this.bipedCommander = new AMTCrossVariantReference<>("hans_commander", model);

		//General animations
		load = IIAnimationCachedMap.create(model, animationsDirectory.with("load"));
		fire = IIAnimationCachedMap.create(model, animationsDirectory.with("fire"));
		aimPitch = IIAnimationCachedMap.create(model, animationsDirectory.with("aim_pitch"));

		passengerDefault = IIAnimationCachedMap.create(model, animationsDirectory.with("hans_default"));

		forward = IIAnimationCachedMap.create(model, animationsDirectory.with("forward"));
		backward = IIAnimationCachedMap.create(model, animationsDirectory.with("backward"));
		left = IIAnimationCachedMap.create(model, animationsDirectory.with("left"));
		right = IIAnimationCachedMap.create(model, animationsDirectory.with("right"));
		wheelAnimations = new WheelAnimationBuilder()
				.withWheel(e -> e.partWheelRight, "wheel1")
				.withWheel(e -> e.partWheelLeft, "wheel2")
				.build();

		//Upgrade system
		UpgradeTechTree.getTreeFor(EntityFieldHowitzer.class)
				.withBaseModelLocation(modelFile);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityFieldHowitzer entity, EntityLivingBase passenger, float partialTicks)
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
