package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoGrenade extends EntityAmmoProjectile
{
	public static final int SPIN_DEGREES = 30;
	public int spinTicks = 360*3, spin = 0;
	public boolean spinDirection;

	public EntityAmmoGrenade(World world)
	{
		super(world);
	}

	@Override
	protected boolean shouldDetonateAfterContact()
	{
		return false;
	}

	@Override
	protected void updatePhysics()
	{
		if(onGround)
		{
			velocity -= 0.99f*velocity;
			if(velocity < 0.01)
				velocity = 0;
		}
		else
			velocity -= DRAG*velocity;

		gravityMotionY -= GRAVITY*this.mass*SLOWMO;
		gravityMotionY *= 1d-DRAG;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote&&!clientLoaded)
			return;

		spinTicks = Math.max(0, spinTicks-1);
		if(onGround)
			spin = (spin > 180?Math.min(360, spin+SPIN_DEGREES): Math.max(0, spin-SPIN_DEGREES))%360;
		else if(spinTicks > 0)
			spin = MathHelper.wrapDegrees(spin+(spinDirection?-SPIN_DEGREES: SPIN_DEGREES));
	}

	private boolean hasRicochetedThisTick = false;

	@Override
	protected void doProjectileMotion()
	{
		this.hasRicochetedThisTick = false;

		//Volumetric raytrace through the projectile's flight path
		flightTracer.stepTrace(world, getPositionVector(), getNextPositionVector(), hit -> {
			if(hit!=null)
			{
				if(shouldDetonateAfterContact())
				{
					detonate();
					return true;
				}

				switch(hit.typeOfHit)
				{
					case BLOCK:
						return handleBlockDamage(hit);
					case ENTITY:
						return handleEntityDamage(hit);
				}
			}
			return false;
		});
		
		if (this.isDead) return;

		// Apply AABB collision (handles slow collisions, rolling, prevents clipping, and catches edge ricochets)
		applyInMotionColision();

		//Finalize the motion
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		this.setPosition(posX, posY, posZ);
	}

	private void applyInMotionColision()
	{
		double initX = motionX;
		double initY = motionY;
		double initZ = motionZ;
		
		AxisAlignedBB offsetAABB = this.getEntityBoundingBox();
		
		List<AxisAlignedBB> boxes = new java.util.ArrayList<>();
		for (AxisAlignedBB box : this.world.getCollisionBoxes(this, offsetAABB.expand(motionX, motionY, motionZ))) {
			if (owner != null && owner.getEntityBoundingBox() != null && box.intersects(owner.getEntityBoundingBox())) continue;
			
			// Ignore fragile blocks (like leaves and glass) in AABB sweep so we pass through them
			boolean isFragile = false;
			net.minecraft.util.math.BlockPos bp = new net.minecraft.util.math.BlockPos(
				box.minX + (box.maxX - box.minX)/2.0, 
				box.minY + (box.maxY - box.minY)/2.0, 
				box.minZ + (box.maxZ - box.minZ)/2.0
			);
			net.minecraft.block.state.IBlockState state = world.getBlockState(bp);
			if (state.getBlock() != net.minecraft.init.Blocks.AIR) {
				pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler pen = pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry.getPenetrationHandler(state);
				if (pen != null && pen.getPenetrationHardness().compareTo(pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness.FRAGILE) <= 0) {
					isFragile = true;
				}
			}

			if (!isFragile) {
				boxes.add(box);
			}
		}
		
		for(AxisAlignedBB axisalignedbb : boxes)
			motionY = axisalignedbb.calculateYOffset(offsetAABB, motionY);
		offsetAABB = offsetAABB.offset(0, motionY, 0);
		
		for(AxisAlignedBB axisalignedbb : boxes)
			motionX = axisalignedbb.calculateXOffset(offsetAABB, motionX);
		offsetAABB = offsetAABB.offset(motionX, 0, 0);
		
		for(AxisAlignedBB axisalignedbb : boxes)
			motionZ = axisalignedbb.calculateZOffset(offsetAABB, motionZ);

		//check Y axis motion
		if(this.onGround = (initY != motionY && initY < 0.0D))
			gravityMotionY = 0;

		// Handle grazing/edge bounces that FactoryTracer missed
		// Skip if onHitRicochet already triggered this tick to prevent double-inversion!
		if (!this.hasRicochetedThisTick && (initX != motionX || initY != motionY || initZ != motionZ)) {
			double impactY = Math.abs(initY - motionY);
			
			// If we ONLY hit the floor, and the impact is very weak (just gravity pulling us down while resting), do not bounce!
			if (initX == motionX && initZ == motionZ && impactY > 0 && impactY <= 0.15) {
				// Just resting on the ground, kill vertical base motion so friction can apply!
				if (this.baseMotion.y > 0) {
					Vec3d flattened = new Vec3d(this.baseMotion.x, 0, this.baseMotion.z);
					if (flattened.lengthVector() > 0) {
						this.baseMotion = flattened.normalize();
					} else {
						this.baseMotion = Vec3d.ZERO;
					}
				}
			} else {
				double bounceX = initX != motionX ? -initX : initX;
				double bounceY = initY != motionY ? -initY : initY;
				double bounceZ = initZ != motionZ ? -initZ : initZ;
				
				Vec3d bouncedVelocity = new Vec3d(bounceX, bounceY, bounceZ).scale(0.45);
				
				this.velocity = (float) bouncedVelocity.lengthVector();
				if (this.velocity > 0.001) {
					this.baseMotion = bouncedVelocity.normalize();
				}
				this.gravityMotionY = 0;

				spinDirection = !spinDirection;
				spinTicks += 180;
				
				playSound(net.minecraft.init.SoundEvents.BLOCK_METAL_HIT, 0.5f, 1f);
			}
		}

		// If the grenade has upward momentum after bouncing, it is NOT resting on the ground!
		// This prevents updatePhysics from destroying 99% of its velocity on the next tick.
		if (baseMotion.y > 0 && velocity > 0.05) {
			this.onGround = false;
		}
	}

	@Override
	protected boolean handleBlockDamage(RayTraceResult hit)
	{
		IBlockState state = world.getBlockState(hit.getBlockPos());
		IPenetrationHandler penHandler = PenetrationRegistry.getPenetrationHandler(state);
		PenetrationHardness blockHardness = penHandler.getPenetrationHardness();

		//ricochet regardless
		if(blockHardness.compareTo(PenetrationHardness.FRAGILE) > 0)
		{
			if(fuseType!=FuseType.CONTACT)
				onHitRicochet(hit, penHandler);
			else
				detonate();
			return true;
		}

		if(penHandler.canBeDamaged())
			PenetrationCache.dealBlockDamage(world, getDirection(), 20, hit.getBlockPos(), penHandler);
		super.onHitPenetrate(hit, penHandler);

		return false;
	}

	@Override
	protected void onHitRicochet(RayTraceResult hit, IPenetrationHandler handler)
	{
		this.hasRicochetedThisTick = true;

		//If ricochets are disabled, end the bullet's lifecycle
		if(!pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils.ammoRicochets)
		{
			detonate();
			return;
		}

		// Don't let it bounce infinitely without sideHit
		if (hit.sideHit == null)
		{
			// Guess side hit based on actual motion
			hit.sideHit = net.minecraft.util.EnumFacing.getFacingFromVector((float)motionX, (float)motionY, (float)motionZ).getOpposite();
		}

		// We no longer manually teleport the grenade or zero out its motion!
		// The AABB sweep (applyInMotionColision) will cleanly push the grenade exactly to the wall this tick,
		// and the newly calculated baseMotion/velocity will take over on the next tick!

		// Compute the true velocity vector before bounce
		Vec3d trueVelocity = new Vec3d(baseMotion.x * velocity, baseMotion.y * velocity + gravityMotionY, baseMotion.z * velocity);

		penetrationDepth -= handler.getThickness()*2;
		gravityMotionY = 0;
		spinDirection = !spinDirection;
		spinTicks += 180;

		// Reflect the true velocity off the surface normal
		Vec3d surfaceNormal = new Vec3d(hit.sideHit.getDirectionVec());
		Vec3d bouncedVelocity = trueVelocity.subtract(
				surfaceNormal.scale(2 * trueVelocity.dotProduct(surfaceNormal))
		);
		
		// Apply decay
		bouncedVelocity = bouncedVelocity.scale(0.45);
		
		// Set the new velocity and baseMotion
		this.velocity = (float) bouncedVelocity.lengthVector();
		
		if (this.velocity > 0.001)
			this.baseMotion = bouncedVelocity.normalize();

		updateEntityForEvent(SyncEvents.ENTITY_COLLISION);

		//Clear the lists
		ignoredEntities.clear();
		ignoredPositions.clear();

		//Play impact sound
		net.minecraft.util.SoundEvent sound = handler.getSpecialSound(pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect.RICOCHET);
		if(sound!=null)
			playSound(sound, 0.5f, 1f);
	}

	@Override
	protected void doRotations()
	{
		Vec3d normalized;
		if(this.onGround)
			normalized = new Vec3d(baseMotion.x, baseMotion.y, baseMotion.z).normalize();
		else
			normalized = new Vec3d(motionX, motionY, motionZ).normalize();

		float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
		this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/Math.PI);
		this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/Math.PI);
	}
}
