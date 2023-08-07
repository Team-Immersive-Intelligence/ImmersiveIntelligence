package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;

/**
 * @author Pabilo8
 * @since 14.12.2022
 */
public class DroneRenderer extends IIEntityRenderer<EntityDrone>
{
	private static IIAnimationCompiledMap animationFloat, animationEngine, animationSetup;
	private static AMT[] models;
	private static AMT modelIRMount, modelIRBall;

	public DroneRenderer(RenderManager render)
	{
		super(render, "drone");
	}

	@Override
	public void draw(EntityDrone entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		float rotYaw = (float)MathHelper.clampedLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);

		GlStateManager.rotate(-rotYaw+180, 0, 1, 0);
		GlStateManager.translate(-0.25, 0, -0.25-0.125);

		GlStateManager.rotate(((float)MathHelper.clamp(entity.motionY, -2f, 2f))*5f, 1, 0, 0);


		animationFloat.apply(IIAnimationUtils.getDebugProgress(entity.getWorld(), 8, partialTicks));
		animationEngine.apply(IIAnimationUtils.getDebugProgress(entity.getWorld(), 4, partialTicks));

		animationSetup.apply(IIAnimationUtils.getAnimationProgress(entity.ticksExisted, 40, false, partialTicks));
		if(entity.ticksExisted > 40)
			faceCamera(entity, rotYaw, partialTicks);

		for(AMT amt : models)
			amt.render(tes, buf);
		GlStateManager.enableCull();
	}

	public void faceCamera(EntityDrone entity, float rotYaw, float partialTicks)
	{
		IIAnimationUtils.setModelRotation(modelIRMount, 0,
				-MathHelper.clampedLerp(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks)+rotYaw,
				0);
		IIAnimationUtils.setModelRotation(modelIRBall,
				(MathHelper.clampedLerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks)),
				0, 0);
	}

	@Override
	public void compileModels()
	{
		models = IIAnimationUtils.getAMTFromRes(
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/entity/combat_drone.obj"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/entity/combat_drone.obj.amt"),
				header -> new AMT[]{
						new AMTLocator("Rotors", header),
						new AMTBullet("WeaponMount1", header.getOffset("WeaponMount1"), AmmoRegistry.INSTANCE.getModel(IIContent.itemAmmoMortar))
								.withProperties(0xcfcfcf, EnumCoreTypes.CANISTER, 0x2fad64).withState(BulletState.BULLET_UNUSED),
						new AMTBullet("WeaponMount2", header.getOffset("WeaponMount2"), AmmoRegistry.INSTANCE.getModel(IIContent.itemAmmoMortar))
								.withProperties(0xcfcfcf, EnumCoreTypes.CANISTER, 0x2fad64).withState(BulletState.BULLET_UNUSED),
						new AMTBullet("WeaponMount3", header.getOffset("WeaponMount3"), AmmoRegistry.INSTANCE.getModel(IIContent.itemAmmoMortar))
								.withProperties(0xcfcfcf, EnumCoreTypes.CANISTER, 0x2fad64).withState(BulletState.BULLET_UNUSED),
						new AMTBullet("WeaponMount4", header.getOffset("WeaponMount4"), AmmoRegistry.INSTANCE.getModel(IIContent.itemAmmoMortar))
								.withProperties(0xcfcfcf, EnumCoreTypes.CANISTER, 0x2fad64).withState(BulletState.BULLET_UNUSED)
				}
		);

		modelIRMount = IIAnimationUtils.getPart(models, "IRMount");
		modelIRBall = IIAnimationUtils.getPart(models, "IRBall");

		animationFloat = IIAnimationCompiledMap.create(models, new ResourceLocation(ImmersiveIntelligence.MODID, "drone/propellers"));
		animationEngine = IIAnimationCompiledMap.create(models, new ResourceLocation(ImmersiveIntelligence.MODID, "drone/engine"));
		animationSetup = IIAnimationCompiledMap.create(models, new ResourceLocation(ImmersiveIntelligence.MODID, "drone/setup"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		/*ResLoc.MODEL_MTL.of(ENTITY_MODEL,"combat_drone");
		ResLoc.MODEL_OBJ.of(ENTITY_MODEL,"combat_drone");
		ResLoc.MODEL_OBJIE.of(ENTITY_MODEL,"combat_drone");
		ResLoc.ANIMATION.of("drone/propellers");*/

		IIAnimationLoader.preloadTexturesFromMTL(new ResourceLocation(ImmersiveIntelligence.MODID, "models/entity/combat_drone.mtl"), map);
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(models);
	}
}
