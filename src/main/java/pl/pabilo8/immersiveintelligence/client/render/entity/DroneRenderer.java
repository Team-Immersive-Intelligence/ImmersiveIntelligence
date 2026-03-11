package pl.pabilo8.immersiveintelligence.client.render.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.12.2022
 */
@RegisteredEntityRenderer(clazz = EntityDrone.class, name = "drone")
public class DroneRenderer extends IIEntityRenderer<EntityDrone>
{
	private static IIAnimationCompiledMap animationFloat, animationEngine, animationSetup;
	private static AMTModel model;
	private static AMT IRMount, IRBall;

	public DroneRenderer(RenderManager render)
	{
		super(render);
	}

	@Override
	public void draw(EntityDrone entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		float rotYaw = (float)MathHelper.clampedLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);

		GlStateManager.rotate(-rotYaw+180, 0, 1, 0);
		GlStateManager.translate(-0.25, 0, -0.25-0.125);

		GlStateManager.rotate(((float)MathHelper.clamp(entity.motionY, -2f, 2f))*5f, 1, 0, 0);


		animationFloat.apply(AMTUtils.getDebugProgress(8, partialTicks));
		animationEngine.apply(AMTUtils.getDebugProgress(4, partialTicks));

		animationSetup.apply(AMTUtils.getAnimationProgress(entity.ticksExisted, 40, false, partialTicks));
		if(entity.ticksExisted > 40)
			faceCamera(entity, rotYaw, partialTicks);

		model.render(tes, buf);
		GlStateManager.enableCull();
	}

	public void faceCamera(EntityDrone entity, float rotYaw, float partialTicks)
	{
		IRMount.setRotation(new Vec3d(0, -MathHelper.clampedLerp(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks)+rotYaw, 0));
		IRBall.setRotation(new Vec3d(MathHelper.clampedLerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks), 0, 0));
	}

	@Override
	public void compileModels()
	{
		IIColor missilePaint = IIColor.fromPackedRGB(0x2fad64);
		model = new AMTModel(
				DefaultVertexFormats.BLOCK,
				ResLoc.of(IIReference.RES_ENTITY_MODEL, "combat_drone").withExtension(ResLoc.EXT_OBJ),
				header -> new AMT[]{
						new AMTLocator("Rotors", header),
						new AMTBullet("WeaponMount1", header.getOffset("WeaponMount1"), AmmoRegistry.getModel(IIContent.itemAmmoRocketLight))
								.withProperties(IIContent.ammoCoreSteel, CoreType.CANISTER, missilePaint).withState(BulletState.BULLET_UNUSED),
						new AMTBullet("WeaponMount2", header.getOffset("WeaponMount2"), AmmoRegistry.getModel(IIContent.itemAmmoRocketLight))
								.withProperties(IIContent.ammoCoreSteel, CoreType.CANISTER, missilePaint).withState(BulletState.BULLET_UNUSED),
						new AMTBullet("WeaponMount3", header.getOffset("WeaponMount3"), AmmoRegistry.getModel(IIContent.itemAmmoRocketLight))
								.withProperties(IIContent.ammoCoreSteel, CoreType.CANISTER, missilePaint).withState(BulletState.BULLET_UNUSED),
						new AMTBullet("WeaponMount4", header.getOffset("WeaponMount4"), AmmoRegistry.getModel(IIContent.itemAmmoRocketLight))
								.withProperties(IIContent.ammoCoreSteel, CoreType.CANISTER, missilePaint).withState(BulletState.BULLET_UNUSED)
				}
		);

		IRMount = model.getPart("IRMount");
		IRBall = model.getPart("IRBall");

		animationFloat = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "drone/propellers"));
		animationEngine = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "drone/engine"));
		animationSetup = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "drone/setup"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromMTL(ResLoc.of(IIReference.RES_ENTITY_MODEL, "combat_drone").withExtension(ResLoc.EXT_MTL), map);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}
}
