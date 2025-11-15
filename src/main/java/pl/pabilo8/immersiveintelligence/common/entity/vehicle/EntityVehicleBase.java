package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.WheelForces;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.MissingAnnotationException;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for vehicle entities with enhanced physics and multipart structure.
 * Handles overall vehicle movement, collision detection, and coordination between vehicle parts.
 * Uses simplified 2D-style physics in X-Z plane similar to the VehicleSimulator.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.09.2025
 */
public abstract class EntityVehicleBase<T extends EntityVehicleBase<T>> extends Entity implements ISyncNBTEntity<T>, IVehicleMultiPart<T>,
		IEntitySpecialRepairable, IManagedUpgradableDevice<T>, IStyleCustomizable
{
	//--- Constants ---//
	private static final StyleConstraints DEFAULT_STYLE_CONSTRAINTS = new StyleConstraints("steel",
			true, Sets.newHashSet("steel"), Collections.emptySet());

	//--- Parts ---//
	private AxisAlignedBB AABB;
	protected VehicleBlueprint blueprint;
	protected EntityVehiclePart<T>[] partArray;
	protected EntityVehicleWheel<T>[] wheels;
	protected SeatInfo<?>[] seats;

	//--- Systems ---//
	@SyncNBT(events = SyncEvents.TILE_UPGRADES_MODIFIED)
	protected StyleCustomization style;
	@SyncNBT(events = SyncEvents.TILE_UPGRADES_MODIFIED)
	protected UpgradeManager<T> upgradeManager;

	//--- Motion --- //
	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public VehicleDurability durabilityMain;
	@SyncNBT
	public Vec3d velocity = Vec3d.ZERO;
	@SyncNBT(events = {SyncEvents.ENTITY_VEHICLE_CONTROLS, SyncEvents.ENTITY_PASSENGER, SyncEvents.ENTITY_COLLISION})
	public float prevRotationRoll = 0, rotationRoll = 0;
	@SyncNBT
	public double angularVelocity = 0.0;
	public boolean hasCollidedBefore = false;

	/**
	 * Constructs a new towable vehicle entity.
	 *
	 * @param world the world in which the entity will be spawned
	 */
	public EntityVehicleBase(World world)
	{
		super(world);
	}

	@Override
	protected final void entityInit()
	{
		//Initialize part collections
		ArrayList<EntityVehicleWheel<T>> wheelsList = new ArrayList<>();
		ArrayList<SeatInfo<T>> seatsList = new ArrayList<>();

		VehicleBlueprint meta = IIUtils.getAnnotation(VehicleBlueprint.class, this);
		if(meta==null)
			throw new MissingAnnotationException(this, VehicleBlueprint.class);
		this.blueprint = meta;

		//Set main durability
		this.durabilityMain = new VehicleDurability(blueprint.baseDurability(), blueprint.baseArmor());
		//noinspection unchecked
		this.upgradeManager = ((UpgradeManager<T>)new UpgradeManager<>(this));
		this.style = new StyleCustomization(getVehicleStyleConstraints());

		//Calculate vehicle size and collect wheels
		double minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
		double maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

		//Initialize vehicle-specific parts
		this.partArray = this.vehicleInit();

		for(EntityVehiclePart<T> part : partArray)
		{
			//Calculate X bounds
			if(part.offset.x-part.aabb.minX < minX)
				minX = part.offset.x-part.aabb.minX;
			else if(part.offset.x+part.aabb.maxX > maxX)
				maxX = part.offset.x+part.aabb.maxX;

			//Calculate Y bounds
			if(part.offset.y-part.aabb.minY < minY)
				minY = part.offset.y-part.aabb.minY;
			else if(part.offset.y+part.aabb.maxY > maxY)
				maxY = part.offset.y+part.aabb.maxY;

			//Calculate Z bounds
			if(part.offset.z-part.aabb.minZ < minZ)
				minZ = part.offset.z-part.aabb.minZ;
			else if(part.offset.z+part.aabb.maxZ > maxZ)
				maxZ = part.offset.z+part.aabb.maxZ;

			//Collect wheels
			if(part instanceof EntityVehicleWheel)
				wheelsList.add(((EntityVehicleWheel<T>)part));

			//Collect seats
			if(part.assignedSeat!=null)
				//noinspection unchecked
				seatsList.add((SeatInfo<T>)part.assignedSeat);
		}

		//Simulate drive wheels first
		wheelsList.sort((o1, o2) -> Boolean.compare(!o1.getType().isDriven(), !o2.getType().isDriven()));

		//Convert wheel and seat lists to arrays
		//noinspection unchecked
		this.wheels = wheelsList.toArray(new EntityVehicleWheel[0]);
		this.seats = seatsList.toArray(new SeatInfo[0]);

		//Set vehicle AABB size
		setSize((float)(Math.max(maxX-minX, maxZ-minZ))*MathHelper.SQRT_2, (float)(maxY-minY)+1f);

		//Initialize part positions
		updateParts();
	}

	/**
	 * Initializes the vehicle with hitboxes and parts.
	 * Implemented by subclasses to define vehicle-specific parts.
	 */
	@SuppressWarnings({"unchecked", "RedundantSuppression"})
	protected abstract EntityVehiclePart<T>[] vehicleInit();

	@Override
	protected void setSize(float width, float height)
	{
		if(this.width==width&&this.height==height)
			return;

		this.width = width;
		this.height = height;
		this.AABB = new AxisAlignedBB(-width/2, 0, -width/2, width/2, height, width/2);
	}

	//--- Movement ---//

	@Override
	public final void onUpdate()
	{
		super.onUpdate();

		//Check for main damage
		hasCollidedBefore = collidedHorizontally;
		if(!world.isRemote&&durabilityMain.isDead())
		{
			setDead();
			return;
		}
		//Perform child class' abstract update method
		onVehicleUpdate();

		//Handle movement and update parts based on result
		handleMovement();
		//Fix vehicle being stuck in blocks and prevent it from levitating
		fixPositionErrors();
		//Update parts
		updateParts();

		//Send an update to the clients after a colision, client has some errors in collision handling
		if(!this.world.isRemote&&!hasCollidedBefore&&collidedHorizontally)
		{
			hasCollidedBefore = true;
			sendServerPositionMotionUpdate();
		}
	}

	/**
	 * Called each update tick for vehicle-specific logic.
	 * Implemented by subclasses to handle vehicle-specific behavior.
	 */
	protected abstract void onVehicleUpdate();

	/**
	 * Handles the vehicle's movement physics including forces, collisions, and orientation.
	 * Integrates wheel forces, applies gravity and drag, and updates position/orientation.
	 */
	public void handleMovement()
	{
		final double mass = this.blueprint.mass();
		//Update rotation values
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationRoll = this.rotationRoll;

		Vec3d collectedForce = Vec3d.ZERO;
		double torque = 0;
		int groundedWheels = 0;

		//Simulate individual wheels and gather results
		for(EntityVehicleWheel<T> wheel : this.wheels)
		{
			//Simulate suspension compression for rendering
			wheel.simulateSuspensionCompression();
			//Simulate wheel physics
			WheelForces forces = wheel.calculateForces();
			if(forces.isGrounded)
			{

				collectedForce = collectedForce.add(forces.force);
				torque += forces.torque;
				groundedWheels++;
			}
		}
		this.velocity = this.velocity.add(collectedForce);

		//Apply gravity if not grounded
		this.velocity = groundedWheels > 0?new Vec3d(this.velocity.x, 0, this.velocity.z): this.velocity.subtract(0, 0.02, 0);

		//Apply motion damping
		applyMotionDamping(groundedWheels);

		//Update rotation
		this.angularVelocity += torque;
		float newYaw = (float)MathHelper.wrapDegrees(this.rotationYaw+Math.toDegrees(this.angularVelocity));
		if(isRotationSafe(newYaw))
			this.rotationYaw = newYaw;
		else
			this.angularVelocity *= 0.6;

		//Use velocity-based thresholds for zeroing
		double velocityThreshold = 0.001*mass; //Scale with mass
		double angularThreshold = 0.0001*mass;

		if(Math.abs(this.velocity.x) < velocityThreshold)
			this.velocity = new Vec3d(0, this.velocity.y, this.velocity.z);
		if(Math.abs(this.velocity.z) < velocityThreshold)
			this.velocity = new Vec3d(this.velocity.x, this.velocity.y, 0);
		if(Math.abs(this.angularVelocity) < angularThreshold)
			this.angularVelocity = 0;

		//Handle collisions and get adjusted position
		Vec3d currentPos = new Vec3d(posX, posY, posZ);
		this.prevPosX = currentPos.x;
		this.prevPosY = currentPos.y;
		this.prevPosZ = currentPos.z;

		Vec3d nextPos = handleCollisions(currentPos, velocity);
		this.motionX = nextPos.x-currentPos.x;
		this.motionY = nextPos.y-currentPos.y;
		this.motionZ = nextPos.z-currentPos.z;

		//this.velocityChanged = true;
		this.posX += motionX;
		this.posY += motionY;
		this.posZ += motionZ;
	}

	/**
	 * Consolidated motion damping for air drag, rolling resistance, linear and angular damping.
	 */
	private void applyMotionDamping(int groundedWheels)
	{
		double tick = 0.05;
		double mass = this.blueprint.mass();

		double vx = this.velocity.x;
		double vz = this.velocity.z;
		double speedSq = vx*vx+vz*vz;
		double speed = Math.sqrt(speedSq);

		//Air drag
		if(speedSq > 0.001)
		{
			//Calculate frontal area from bounding box
			double frontalArea = this.width*this.height*blueprint.frontalAreaFactor();

			//Drag force = 0.5 * density * velocity² * drag coefficient * area
			double dragForce = 0.5*1.225*speedSq*blueprint.airDragCoefficient()*frontalArea;

			//Convert to acceleration (F = ma -> a = F/m) and scale by tick
			double dragDeceleration = (dragForce/mass)*tick;

			//Clamp deceleration so we don't overshoot and reverse direction
			double decel = Math.min(dragDeceleration, speed);
			if(speed > 0&&decel > 0)
			{
				vx -= (vx/speed)*decel;
				vz -= (vz/speed)*decel;
			}
		}

		//Rolling resistance (when grounded)
		if(groundedWheels > 0&&speedSq > 0.01)
		{
			double rollingResistance = blueprint.rollingResistance();
			double wheelFactor = groundedWheels/(double)this.wheels.length;

			double resistance = rollingResistance*wheelFactor*mass*0.98; //gravity
			resistance *= tick; //scale for tick time

			//Clamp so we don't overshoot
			double resDelta = Math.min(resistance, speed);
			if(speed > 0&&resDelta > 0)
			{
				vx -= (vx/speed)*resDelta;
				vz -= (vz/speed)*resDelta;
			}
		}

		//Linear damping
		if(groundedWheels > 0)
		{
			double speedFactor = Math.min(speedSq/0.5, 1.0); //0.5 m/s threshold (squared applied)
			double effectiveDamping = blueprint.linearDamping()*(0.95+0.05*speedFactor);
			vx *= effectiveDamping;
			vz *= effectiveDamping;
		}
		else
		{
			//In air, use minimal damping
			vx *= 0.995;
			vz *= 0.995;
		}

		//Commit velocity change preserving Y component
		this.velocity = new Vec3d(vx, this.velocity.y, vz);

		//Angular damping
		double angularDamping = groundedWheels > 0?
				blueprint.angularDamping()*0.7: //More damping when grounded
				blueprint.angularDamping()*0.9;  //Less damping in air
		this.angularVelocity *= angularDamping;
	}

	/**
	 * Handles collision detection and response for all vehicle parts.
	 */
	private Vec3d handleCollisions(Vec3d currentPos, Vec3d attemptedMove)
	{
		//Do not calculate collision no motion is performed
		if(attemptedMove.equals(Vec3d.ZERO))
			return currentPos;

		Vec3d adjustedMove = attemptedMove;
		//Check each part for collisions
		for(EntityVehiclePart<T> part : partArray)
		{
			Vec3d partPos = this.getPositionVector().add(IIMath.offsetPosDirectionXYZ(part.offset, this.rotationYaw, this.rotationPitch, this.rotationRoll));
			AxisAlignedBB partCurrentBB = part.aabb.offset(partPos);
			AxisAlignedBB partNextBB = partCurrentBB.expand(attemptedMove.x, attemptedMove.y, attemptedMove.z);

			//Get block collision boxes
			List<AxisAlignedBB> collisions = new ArrayList<>(world.getCollisionBoxes(null, partNextBB));
			//TODO: 15.11.2025 driving over/ramming entities + mutually damaging vehicles by collision (also with blocks)
			for(Entity entity : world.getEntitiesInAABBexcluding(part, partNextBB, e -> e!=this&&e instanceof EntityVehicleBase))
			{
				if(entity instanceof IEntityMultiPart&&entity.getParts()!=null)
					for(Entity entityPart : entity.getParts())
						collisions.add(entityPart.getCollisionBoundingBox());
				else
					collisions.add(entity.getEntityBoundingBox());
			}

			if(!collisions.isEmpty())
			{
				Vec3d partAdjustedMove = attemptedMove;
				for(AxisAlignedBB collisionBox : collisions)
				{
					//IILogger.info(partPos.toString()+" collided with "+collisionBox.toString());
					//Handle Y-axis collisions
					if(partAdjustedMove.y!=0)
					{
						double yOffset = collisionBox.calculateYOffset(partCurrentBB, partAdjustedMove.y);
						if(yOffset!=partAdjustedMove.y)
						{
							partAdjustedMove = new Vec3d(partAdjustedMove.x, yOffset, partAdjustedMove.z);
							//update current bounding box to reflect the applied Y offset
							partCurrentBB = partCurrentBB.offset(0.0D, yOffset, 0.0D);
							if(partAdjustedMove.y < 0) this.onGround = true;
							collided = collidedVertically = true;
						}
					}
					//handle X using updated bounding box
					if(partAdjustedMove.x!=0)
					{
						double xOffset = collisionBox.calculateXOffset(partCurrentBB, partAdjustedMove.x);
						if(xOffset!=partAdjustedMove.x)
						{
							partAdjustedMove = new Vec3d(xOffset, partAdjustedMove.y, partAdjustedMove.z);
							partCurrentBB = partCurrentBB.offset(xOffset, 0.0D, 0.0D);
							collided = collidedHorizontally = true;
						}
					}
					//handle Z using updated bounding box
					if(partAdjustedMove.z!=0)
					{
						double zOffset = collisionBox.calculateZOffset(partCurrentBB, partAdjustedMove.z);
						if(zOffset!=partAdjustedMove.z)
						{
							partAdjustedMove = new Vec3d(partAdjustedMove.x, partAdjustedMove.y, zOffset);
							partCurrentBB = partCurrentBB.offset(0.0D, 0.0D, zOffset);
							collided = collidedHorizontally = true;
						}
					}

				}

				//Take the most restrictive movement across all parts
				adjustedMove = new Vec3d(
						Math.abs(partAdjustedMove.x) <= Math.abs(adjustedMove.x)?partAdjustedMove.x: adjustedMove.x,
						Math.abs(partAdjustedMove.y) <= Math.abs(adjustedMove.y)?partAdjustedMove.y: adjustedMove.y,
						Math.abs(partAdjustedMove.z) <= Math.abs(adjustedMove.z)?partAdjustedMove.z: adjustedMove.z
				);
			}

			//Turn the <s>tables</s> wheels
			if(part instanceof EntityVehicleWheel)
				((EntityVehicleWheel<T>)part).addWheelTraverse((float)adjustedMove.lengthSquared());
		}

		return currentPos.add(adjustedMove);
	}

	/**
	 * Tests whether the vehicle rotated to a given yaw would collide with the world.
	 */
	private boolean isRotationSafe(float testYaw)
	{
		//For each part (wheels + hull)
		for(EntityVehiclePart<T> part : partArray)
		{
			//Compute rotated part position for the *test* yaw
			Vec3d rotatedPos = this.getPositionVector().add(
					IIMath.offsetPosDirectionXYZ(
							part.offset,
							testYaw,
							this.rotationPitch,
							this.rotationRoll
					)
			);

			AxisAlignedBB futureBB = part.aabb.offset(rotatedPos);

			//Query block collisions
			List<AxisAlignedBB> boxes = world.getCollisionBoxes(null, futureBB);

			if(!boxes.isEmpty())
				return false; //rotation causes intersection -> not safe

			//Also test entity collisions (other vehicles)
			for(Entity e : world.getEntitiesInAABBexcluding(this, futureBB,
					ent -> ent instanceof EntityVehicleBase))
			{
				EntityVehicleBase<?> other = (EntityVehicleBase<?>)e;
				for(EntityVehiclePart<?> vehiclePart : other.getVehicleParts())
					if(vehiclePart.getEntityBoundingBox().intersects(futureBB))
						return false;
			}
		}

		return true;
	}

	/**
	 * Attempts to resolve very small penetrations (< 0.125 blocks) by nudging the entire vehicle out of block intersections.
	 * Performs a ground-snap to resolve cases where downward motion was clipped early, leaving the vehicle hovering fractions of a block above the ground.
	 */
	public void fixPositionErrors()
	{
		//threshold for inside-block correction
		final double INSIDE_THRESHOLD = 0.125;
		//max downward correction allowed
		final double SNAP_MAX = 0.25;
		//depth to scan for ground
		final double PROBE = 0.3;

		Vec3d accumulatedCorrection = Vec3d.ZERO;
		boolean anyWheelOnGround = false;

		//Phase 1: Fix micro-penetrations
		for(EntityVehiclePart<T> part : partArray)
		{
			//Collect wheels for phase two
			if(part instanceof EntityVehicleWheel)
				if(part.onGround)
					anyWheelOnGround = true;

			//Simulate final offset
			Vec3d partPos = this.getPositionVector().add(
					IIMath.offsetPosDirectionXYZ(
							part.offset,
							this.rotationYaw,
							this.rotationPitch,
							this.rotationRoll
					)
			);

			AxisAlignedBB partBB = part.aabb.offset(partPos);
			List<AxisAlignedBB> blockBoxes = world.getCollisionBoxes(null, partBB);
			//Check for intersections inside threshold
			for(AxisAlignedBB block : blockBoxes)
			{
				AxisAlignedBB inter = partBB.intersect(block);
				double dx = (inter.maxX-inter.minX)/2.0;
				double dy = (inter.maxY-inter.minY)/2.0;
				double dz = (inter.maxZ-inter.minZ)/2.0;

				if(dx < INSIDE_THRESHOLD||dy < INSIDE_THRESHOLD||dz < INSIDE_THRESHOLD)
				{
					//Push out any intersections
					Vec3d direction = partBB.getCenter().subtract(block.getCenter()).normalize();
					double depth = Math.min(dx, Math.min(dy, dz));
					Vec3d push = direction.scale(depth);

					accumulatedCorrection = accumulatedCorrection.add(push);
				}
			}
		}

		//Apply correction for phase 1
		if(!accumulatedCorrection.equals(Vec3d.ZERO))
		{
			Vec3d finalCorrection = accumulatedCorrection.normalize()
					.scale(Math.min(INSIDE_THRESHOLD*0.5, accumulatedCorrection.lengthVector()));
			if(isMicroCorrectionSafe(finalCorrection))
			{
				this.posX += finalCorrection.x;
				this.posY += finalCorrection.y;
				this.posZ += finalCorrection.z;
			}
		}

		//Phase 2: Ground snapping
		if(collidedVertically&&!anyWheelOnGround)
		{
			double minGap = Double.MAX_VALUE;
			boolean hasGroundBelow = false;

			//Try to lower each wheel until it hits the ground
			for(EntityVehicleWheel<T> part : wheels)
			{
				Vec3d partPos = this.getPositionVector().add(
						IIMath.offsetPosDirectionXYZ(
								part.offset,
								this.rotationYaw,
								this.rotationPitch,
								this.rotationRoll
						)
				);
				AxisAlignedBB wheelBB = part.aabb.offset(partPos);
				AxisAlignedBB probeBB = wheelBB.offset(0, -PROBE, 0);

				//Get blocks below the wheel
				List<AxisAlignedBB> blocksBelow = world.getCollisionBoxes(null, probeBB);
				for(AxisAlignedBB block : blocksBelow)
				{
					double gap = wheelBB.minY-block.maxY;
					if(gap >= 0&&gap < minGap)
					{
						minGap = gap;
						hasGroundBelow = true;
					}
				}
			}

			//If ground found below within snap range, snap down
			if(hasGroundBelow&&minGap < SNAP_MAX)
			{
				Vec3d snap = new Vec3d(0, -minGap, 0);

				if(isMicroCorrectionSafe(snap))
				{
					this.posY -= minGap;
					this.prevPosY -= minGap;
				}
			}
		}


	}

	/**
	 * Tests if shifting the entire vehicle by the given offset causes
	 * any new collisions. Used by fixMicroPositioning().
	 */
	private boolean isMicroCorrectionSafe(Vec3d shift)
	{
		if(shift.equals(Vec3d.ZERO))
			return false;

		for(EntityVehiclePart<T> part : partArray)
		{
			Vec3d newPos = this.getPositionVector().add(shift).add(
					IIMath.offsetPosDirectionXYZ(
							part.offset,
							this.rotationYaw,
							this.rotationPitch,
							this.rotationRoll
					)
			);

			AxisAlignedBB newBB = part.aabb.offset(newPos);

			//If part is now inside a block, correction is unsafe
			if(!world.getCollisionBoxes(null, newBB).isEmpty())
				return false;

			//If part collides with other entities
			List<Entity> hits = world.getEntitiesInAABBexcluding(this, newBB, e -> e!=this);
			if(!hits.isEmpty())
				return false;
		}

		return true;
	}


	@Override
	public double getAngularVelocity()
	{
		return angularVelocity;
	}

	@Override
	public Vec3d getVelocity()
	{
		return velocity;
	}

	@Override
	public VehicleBlueprint getVehicleBlueprint()
	{
		return blueprint;
	}

	@Override
	public SeatInfo<?> getSeatInfo(String seatID)
	{
		for(SeatInfo<?> seat : this.seats)
			if(seatID.equals(seat.getSeatID()))
				return seat;
		return null;
	}

	//--- Part Handling ---//

	@Override
	public void updateParts()
	{
		//Create vectors with proper rotation accounting
		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			//Transform offset using the rotated vectors
			Vec3d newPos = this.getPositionVector().add(IIMath.offsetPosDirectionXYZ(part.offset, this.rotationYaw, this.rotationPitch, this.rotationRoll));

			float yawAngle = this.rotationYaw;
			if(part instanceof EntityVehicleWheel)
			{
				EntityVehicleWheel<T> wheel = (EntityVehicleWheel<T>)part;
				yawAngle = wheel.rotationYaw;
				wheel.addWheelTraverse((float)part.getPositionVector().distanceTo(newPos));
			}
			part.setLocationAndAngles(newPos.x, newPos.y, newPos.z, yawAngle, 0);
			part.onUpdate();
		}
	}

	@Nullable
	@Override
	public Entity[] getParts()
	{
		return partArray;
	}

	@Override
	public EntityVehiclePart<T>[] getVehicleParts()
	{
		return partArray;
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
	public void setDead()
	{
		getPassengers().forEach(Entity::setDead);
		super.setDead();
	}

	//--- Damage ---//

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		//immersive vehicles(tm)
		if(source.damageType.equals("bullet"))
		{
			DamageSource temp_source = new DamageSource("bullet").setProjectile().setDamageBypassesArmor();
			durabilityMain.attackFrom(temp_source, amount);
			return true;
		}
		return super.attackEntityFrom(source, amount);
	}

	//--- Collision ---//

	@Override
	public World getWorld()
	{
		return getEntityWorld();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return AABB.offset(posX, posY, posZ);
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return getEntityBoundingBox();
	}

	@Override
	protected boolean canFitPassenger(Entity passenger)
	{
		return passenger instanceof EntityVehicleSeat;
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(isPassenger(passenger))
			passenger.setPosition(posX, posY, posZ);
	}

	@Override
	public void applyOrientationToEntity(Entity passenger)
	{
		if(passenger!=null&&isPassenger(passenger))
		{
			passenger.rotationYaw = this.rotationYaw;
			passenger.rotationPitch = this.rotationPitch;
		}
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	//--- ISyncNBTEntity ---//

	@Override
	public void readEntityFromNBT(@Nonnull NBTTagCompound compound)
	{
		ISyncNBTEntity.super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(@Nonnull NBTTagCompound compound)
	{
		ISyncNBTEntity.super.writeEntityToNBT(compound);
	}

	//--- IEntitySpecialRepairable ---//

	@Override
	public boolean canRepair()
	{
		return durabilityMain.canRepair();
	}

	@Override
	public boolean repair(int repairPoints)
	{
		return durabilityMain.repair(repairPoints);
	}

	@Override
	public int getRepairCost()
	{
		return 1;
	}

	//--- IManagedUpgradableDevice ---//

	@Nonnull
	@Override
	public UpgradeManager<T> getUpgradeManager()
	{
		return this.upgradeManager;
	}

	@Override
	public T master()
	{
		//noinspection unchecked
		return ((T)this);
	}

	//--- StyleCustomization ---//

	@Override
	public StyleCustomization getStyle()
	{
		return style;
	}

	protected StyleConstraints getVehicleStyleConstraints()
	{
		return DEFAULT_STYLE_CONSTRAINTS;
	}
}
