package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EntityReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoGuidedMissile extends EntityAmmoMissile
{
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public EntityReference<Entity> homingTarget;

	public EntityAmmoGuidedMissile(World world)
	{
		super(world);
		this.homingTarget = new EntityReference<>(this::getEntityWorld);
	}

	/**
	 * Sets the entity this missile will steer towards while its booster is active.
	 *
	 * @param target homing target, or null to disable homing
	 * @return this missile
	 */
	public EntityAmmoGuidedMissile setHomingTarget(@Nullable Entity target)
	{
		if(target==null||target==this||target.world!=world||!target.isEntityAlive())
			homingTarget.set(target);
		else
			homingTarget.set(null);
		return this;
	}

	@Override
	protected void updatePhysics()
	{
		//Use the same pre-decrement state as the booster physics, including its final active tick.
		boolean canHome = isBoosterActive();
		super.updatePhysics();
		if(canHome)
			updateHoming();
	}

	private void updateHoming()
	{
		Entity target = homingTarget.get();
		if(target==null)
			return;

		//Calculate the desired motion towards the target
		Vec3d targetCenter = new Vec3d(target.posX, target.posY+target.height*0.5, target.posZ);
		Vec3d desiredMotion = targetCenter.subtract(getPositionVector());
		if(desiredMotion.lengthSquared() < 1.0e-8||baseMotion.lengthSquared() < 1.0e-8)
			return;

		//Turn yaw and pitch toward the target
		float[] currentRotation = IIMath.getRotationFromVector(baseMotion);
		float[] desiredRotation = IIMath.getRotationFromVector(desiredMotion);
		float yaw = currentRotation[0]+MathHelper.clamp(
				MathHelper.wrapDegrees(desiredRotation[0]-currentRotation[0]),
				-15, 15
		);
		float pitch = currentRotation[1]+MathHelper.clamp(
				desiredRotation[1]-currentRotation[1],
				-15, 15
		);
		//Correct base motion
		this.baseMotion = IIMath.offsetPosDirection(1d, Math.toRadians(yaw), Math.toRadians(pitch));
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void spawnTrailParticles()
	{
		super.spawnTrailParticles();
		ParticleRegistry.spawnParticle("ammo/missile_wire", getPositionVector(), Vec3d.ZERO, new Vector2f())
				.withProperty(ParticleProperties.STRETCH, new Vector3f((float)prevPosX, (float)prevPosY, (float)prevPosZ));
	}
}
