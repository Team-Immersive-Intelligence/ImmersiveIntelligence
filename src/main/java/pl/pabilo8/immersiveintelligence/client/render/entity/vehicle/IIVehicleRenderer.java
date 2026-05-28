package pl.pabilo8.immersiveintelligence.client.render.entity.vehicle;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.client.model.TextureRecoloringRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IPassengerAnimationsRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTChain;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityTrackedMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion.VehicleEngineBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion.VehicleTransmission;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIVectorLine;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 23.11.2025
 */
public abstract class IIVehicleRenderer<E extends EntityVehicleBase<E>> extends IIEntityRenderer<E> implements IPassengerAnimationsRenderer<E>
{
	protected static final ResLoc SHARED_DIRECTORY = IIReference.RES_ENTITY_MODEL.with("vehicle/shared/");
	protected AMTCachedModel<E> model;
	protected Map<Function<E, EntityVehicleWheel<E>>, IIAnimationCachedMap> wheelAnimations;

	@Nonnull
	protected ResLoc modelDirectory, animationsDirectory;
	protected ResLoc modelFile, headerFile, mtlFile;

	protected IIVehicleRenderer(RenderManager render)
	{
		super(render);

		//Register model directory location
		RegisteredEntityRenderer meta = IIUtils.getAnnotation(RegisteredEntityRenderer.class, this);
		if(meta!=null)
		{
			String onlyName = meta.name().replace("vehicle/", "");
			this.modelDirectory = IIReference.RES_ENTITY_MODEL.with(meta.name()+"/");
			this.animationsDirectory = IIReference.RES_II.with(meta.name()+"/");
			this.modelFile = this.modelDirectory.with(onlyName).withExtension(ResLoc.EXT_OBJ);
			this.headerFile = this.modelDirectory.with(onlyName).withExtension(ResLoc.EXT_OBJAMT);
			this.mtlFile = this.modelFile.withExtension(ResLoc.EXT_MTL);
		}
		else
		{
			this.modelDirectory = IIReference.RES_ENTITY_MODEL.with("vehicle/");
			this.animationsDirectory = IIReference.RES_II;
		}
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromMTL(mtlFile, map);

	}

	//--- Utils ---//
	protected void applyVehicleTransforms(E entity, float partialTicks)
	{
		double rotYaw = MathHelper.clampedLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
		double rotPitch = MathHelper.clampedLerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks);
		double rotRoll = MathHelper.clampedLerp(entity.prevRotationRoll, entity.rotationRoll, partialTicks);
		GlStateManager.rotate((float)(-rotYaw+180), 0, 1, 0);
		GlStateManager.rotate((float)rotPitch, 1, 0, 0);
		GlStateManager.rotate((float)rotRoll, 0, 0, 1);
	}

	protected TextureAtlasSprite replaceVehicleTextures(ResourceLocation res, E entity)
	{
		if(entity==null||entity.getStyle()==null)
			return ClientUtils.getSprite(res);
		StyleCustomization style = entity.getStyle();

		if(res.getResourcePath().contains("_painted"))
		{
			//Unpainted
			if(style.getColor()==IIColor.WHITE)
				return ClientUtils.getSprite(new ResourceLocation(
						res.getResourceDomain(),
						res.getResourcePath().replace("_painted", "").replace("paint/", "")));
			//Painted
			return TextureRecoloringRegistry.getRecoloredTextureSprite(res, style.getColor());
		}

		//Default option
		return ClientUtils.getSprite(res);
	}

	protected void applyWheelAnimations(E entity, float partialTicks)
	{
		wheelAnimations.forEach((wheelGetter, animation) ->
		{
			EntityVehicleWheel<E> apply = wheelGetter.apply(entity);
			float rotationProgress = apply.getWheelTraverse()+apply.getWheelTraverse();
			animation.apply((rotationProgress%360)/360f);
		});
	}

	protected void applyTrackAnimation(AMTCrossVariantReference<AMTChain> track, EntityVehicleWheel<EntityTrackedMotorbike> wheel,
									   int trackWidth, float partialTicks)
	{
		float trackProgressLeft = (wheel.getWheelTraverse()%trackWidth)/(float)trackWidth;
		track.get().setProgress(trackProgressLeft);
	}

	protected void applyGearboxAnimation(VehicleTransmission<E> transmission, IIAnimationCachedMap mainAnimation,
										 float partialTicks, IIAnimationCachedMap... shiftingAnimations)
	{
		//Apply main animation
		mainAnimation.apply(transmission.getTotalShiftingProgress(partialTicks));

		//Select and apply shifting animation if currently shifting
		float progress = transmission.getShiftingProgress(partialTicks);
		if(progress==0)
			return;

		//Gears
		int currentGear = transmission.getCurrentGear();
		int nextGear = transmission.getNextGear();

		//Switching up
		if(currentGear < nextGear)
			shiftingAnimations[currentGear].apply(1f-progress);
			//Switching down
		else
			shiftingAnimations[nextGear].apply(progress);

	}

	protected <T extends VehicleEngineBase<T, ?>> void applyEngineAnimation(T engineBase,
																			IIAnimationCachedMap startingAnimation, IIAnimationCachedMap runningAnimation,
																			float partialTicks)
	{
		if(engineBase.isStarting())
			startingAnimation.apply(engineBase.getStartingProgress(partialTicks));
		else if(engineBase.isActive())
			runningAnimation.apply(AMTUtils.getDebugProgress(engineBase.getAnimationTicks(), partialTicks));
	}

	protected class WheelAnimationBuilder
	{
		private final Map<Function<E, EntityVehicleWheel<E>>, IIAnimationCachedMap> wheelAnimations = new HashMap<>();

		public WheelAnimationBuilder()
		{

		}

		public WheelAnimationBuilder withWheel(Function<E, EntityVehicleWheel<E>> wheelGetter, String wheelName)
		{
			//Create new animation of wheel rotating 360 degrees on x-axis
			IIAnimationGroup group = new IIAnimationGroup(wheelName, null, null,
					new IIVectorLine(new float[]{0, 1}, new Vec3d[]{new Vec3d(0, 0, 0), new Vec3d(360, 0, 0)}),
					null, null, null);
			IIAnimation animation = new IIAnimation(animationsDirectory.with("generated/wheel/"+wheelName), new IIAnimationGroup[]{group});

			//Cache the animation
			IIAnimationCachedMap cached = IIAnimationCachedMap.create(model, animation);

			//Finally, add to the map
			this.wheelAnimations.put(wheelGetter, cached);
			return this;
		}

		public Map<Function<E, EntityVehicleWheel<E>>, IIAnimationCachedMap> build()
		{
			return this.wheelAnimations;
		}
	}
}
