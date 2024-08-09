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
 * @author Pabilo8
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
		spinTicks = Math.max(0, spinTicks-1);
		if(onGround)
			spin = (spin > 180?Math.min(360, spin+SPIN_DEGREES): Math.max(0, spin-SPIN_DEGREES))%360;
		else if(spinTicks > 0)
			spin = MathHelper.wrapDegrees(spin+(spinDirection?-SPIN_DEGREES: SPIN_DEGREES));
	}

	@Override
	protected void doProjectileMotion()
	{
		//Volumetric raytrace through the projectile's flight path
		if(velocity <= 0)
		{
			double initX = motionX, initY = motionY, initZ = motionZ;
			applyInMotionColision(this.world.getCollisionBoxes(this, aabb
					.offset(posX, posY, posZ)
					.expand(motionX, motionY, motionZ))
			);
			if(initX!=motionX||initY!=motionY||initZ!=motionZ)
				spinTicks /= 2;
		}
		else
			flightTracer.stepTrace(world, getPositionVector(), getNextPositionVector(), hit -> {
				if(hit!=null)
				{
					//Proximity fuses don't use penetration logic
					if(shouldDetonateAfterContact())
					{
						detonate();
						return true;
					}

					//But other fuses do
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


		//Finalize the motion
		posX += motionX;
		posY += motionY;
		posZ += motionZ;

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
		//Get colision boxes
		List<AxisAlignedBB> boxes = new ArrayList<>();
		ForgeEventFactory.gatherCollisionBoxes(world, this, new AxisAlignedBB(hit.getBlockPos()).grow(width), boxes);
		world.getBlockState(hit.getBlockPos()).addCollisionBoxToList(world, hit.getBlockPos(),
				aabb.offset(hit.hitVec), boxes, this, false);

		//Apply collisions
		applyInMotionColision(boxes);

		//Decay velocity
		this.velocity *= 0.3f;
		spinDirection = !spinDirection;
		spinTicks += 180;

		//Sync with client
		updateEntityForEvent(SyncEvents.ENTITY_COLLISION);
	}

	/**
	 * Applies collision with selected bounding boxes for a moving grenade
	 *
	 * @param boxes List of bounding boxes to collide with
	 */
	private void applyInMotionColision(List<AxisAlignedBB> boxes)
	{
		double initY = motionY;
		AxisAlignedBB offsetAABB = aabb.offset(posX, posY, posZ);
		for(AxisAlignedBB axisalignedbb : boxes)
		{
			motionY = axisalignedbb.calculateYOffset(offsetAABB, motionY);
			offsetAABB = offsetAABB.offset(0, motionY, 0);
			motionX = axisalignedbb.calculateXOffset(offsetAABB, motionX);
			offsetAABB = offsetAABB.offset(motionX, 0, 0);
			motionZ = axisalignedbb.calculateZOffset(offsetAABB, motionZ);
			offsetAABB = offsetAABB.offset(0, 0, motionZ);
		}

		//check Y axis motion
		if(this.onGround = initY!=motionY)
			gravityMotionY = 0;
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
