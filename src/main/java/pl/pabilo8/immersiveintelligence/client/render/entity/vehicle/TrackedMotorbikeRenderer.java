package pl.pabilo8.immersiveintelligence.client.render.entity.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityTrackedMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

@RegisteredEntityRenderer(clazz = EntityFieldHowitzer.class, name = "vehicle/tracked_motorbike")
public class TrackedMotorbikeRenderer extends IIVehicleRenderer<EntityTrackedMotorbike>
{
	private IIAnimationCachedMap engine, engineStarting, transmission, turn, passengerDefault;
	private IIAnimationCachedMap gearPrimary, gearSecondary;
	private IIAnimationCachedMap gearPrimaryReverse, gearPrimary1, gearPrimary2, gearSecondaryOverdrive, gearSecondaryReduction;
	private AMTCrossVariantReference<AMTChain> trackLeft, trackRight;
	private AMTCrossVariantReference<AMTBipedAdapter> bipedDriver, bipedPassenger;

	public TrackedMotorbikeRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void draw(EntityTrackedMotorbike entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply vehicle transforms
		applyVehicleTransforms(entity, partialTicks);

		//Get model variant
		model.getVariant(entity, entity.getStyle());

		//Animate wheels and tracks
		applyWheelAnimations(entity, partialTicks);
		applyTrackAnimation(trackRight, entity.partWheelRightFront, 1440, partialTicks);
		applyTrackAnimation(trackLeft, entity.partWheelLeftFront, 1440, partialTicks);

		//General animations
		passengerDefault.apply(0);
		turn.apply((entity.partWheelFront.getSteeringAngle()/45f)*-0.5f+0.5f);
		transmission.apply(AMTUtils.getDebugProgress(8, partialTicks));

		//Gearbox animations
		applyGearboxAnimation(entity.transmission1, gearSecondary, partialTicks, gearSecondaryOverdrive);
		applyGearboxAnimation(entity.transmission2, gearPrimary, partialTicks, gearPrimaryReverse, gearPrimary1, gearPrimary2);
		applyEngineAnimation(entity.engine, engineStarting, engine, partialTicks);

		//Finally, render
		model.render(tes, buf);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
		AMTLoader.preloadTexturesFromMTL(SHARED_DIRECTORY.with("track_8").withExtension(ResLoc.EXT_MTL), map);
	}

	@Override
	public void compileModels()
	{
		//Get track segment model
		AMT trackSegment = new AMTModel(DefaultVertexFormats.ITEM, SHARED_DIRECTORY.with("track_8")
				.withExtension(ResLoc.EXT_OBJ))
				.getParts()[0];

		//Assemble main model
		model = AMTCachedModelBuilder.startEntityModel(EntityTrackedMotorbike.class)
				.withModel(modelFile)
				.withHeader(headerFile)
				.withTextureProvider(this::replaceVehicleTextures)
				.withModelProvider((entity, header) -> new AMT[]{
						new AMTLocator("wheels", header),
						new AMTChain("track_left", header)
								.withSegmentModel((AMTQuads)trackSegment)
								.withNodesFromAnimation(animationsDirectory.with("track")),
						new AMTChain("track_right", header)
								.withSegmentModel((AMTQuads)trackSegment)
								.withNodesFromAnimation(animationsDirectory.with("track")),
						new AMTBipedAdapter("hans_driver", header),
						new AMTBipedAdapter("hans_passenger", header),
						new AMTParticle("smoke_exhaust", header),
						new AMTParticle("light_steering_wheel", header)
				})
				.build();

		trackLeft = new AMTCrossVariantReference<>("track_left", model);
		trackRight = new AMTCrossVariantReference<>("track_right", model);
		bipedDriver = new AMTCrossVariantReference<>("hans_driver", model);
		bipedPassenger = new AMTCrossVariantReference<>("hans_passenger", model);

		//General animations
		engine = IIAnimationCachedMap.create(model, animationsDirectory.with("engine"));
		engineStarting = IIAnimationCachedMap.create(model, animationsDirectory.with("start_engine"));
		transmission = IIAnimationCachedMap.create(model, animationsDirectory.with("transmission"));
		turn = IIAnimationCachedMap.create(model, animationsDirectory.with("turn"));
		passengerDefault = IIAnimationCachedMap.create(model, animationsDirectory.with("hans_default"));
		wheelAnimations = new WheelAnimationBuilder()
				.withWheel(e -> e.partWheelFront, "wheel")
				.withWheel(e -> e.partWheelLeftFront, "LeftWheelLead1")
				.withWheel(e -> e.partWheelRightFront, "RightWheelRear1")
				.withWheel(e -> e.partWheelLeft1, "wheel_left1")
				.withWheel(e -> e.partWheelLeft2, "wheel_left2")
				.withWheel(e -> e.partWheelRight1, "wheel_right1")
				.withWheel(e -> e.partWheelRight2, "wheel_right2")
				.withWheel(e -> e.partWheelLeftBack, "LeftWheelLead2")
				.withWheel(e -> e.partWheelRightBack, "RightWheelRear2")
				.build();

		//Gear switching animations
		gearPrimary = IIAnimationCachedMap.create(model, animationsDirectory.with("gearbox_primary"));
		gearPrimaryReverse = IIAnimationCachedMap.create(model, animationsDirectory.with("gear_primary_reverse"));
		gearPrimary1 = IIAnimationCachedMap.create(model, animationsDirectory.with("gear_primary1"));
		gearPrimary2 = IIAnimationCachedMap.create(model, animationsDirectory.with("gear_primary2"));

		gearSecondary = IIAnimationCachedMap.create(model, animationsDirectory.with("gearbox_secondary"));
		gearSecondaryOverdrive = IIAnimationCachedMap.create(model, animationsDirectory.with("gear_secondary_overdrive"));
		gearSecondaryReduction = IIAnimationCachedMap.create(model, animationsDirectory.with("gear_secondary_reduction"));
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityTrackedMotorbike entity, EntityLivingBase passenger, float partialTicks)
	{
		EntityVehicleSeat seat = (EntityVehicleSeat)passenger.getRidingEntity();
		if(seat==null)
			return false;
		//Select animation adapter based on seat
		switch(seat.info.getSeatID())
		{
			case "rider":
				bipedDriver.get().applyAnimationStateTo(model);
				return true;
			case "passenger":
				bipedPassenger.get().applyAnimationStateTo(model);
				return true;
			default:
				return false;
		}
	}
}
