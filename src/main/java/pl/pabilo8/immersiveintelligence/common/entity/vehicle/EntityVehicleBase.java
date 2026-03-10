package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.MissingAnnotationException;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enhanced base class for vehicle entities with comprehensive pitch/roll simulation
 * and wheel-based climbing system following "needs and wants" philosophy.
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

	//--- Motion & Orientation ---//
	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public VehicleDurability durabilityMain;
	@SyncNBT
	public Vec3d velocity = Vec3d.ZERO;
	@SyncNBT(events = {SyncEvents.ENTITY_VEHICLE_CONTROLS, SyncEvents.ENTITY_PASSENGER, SyncEvents.ENTITY_COLLISION})
	public float prevRotationRoll = 0, rotationRoll = 0;
	@SyncNBT
	public double angularVelocity = 0.0;
	public boolean hasCollidedBefore = false;
	private float targetPitch = 0;
	private float targetRoll = 0;

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
		this.style.withColor(IIColor.fromHSV(14/64f, 0.35f, 0.85f));

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

	//--- Enhanced Movement with Pitch/Roll ---//

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
		//Calculate orientation based on wheel force differences
		calculateForceBasedOrientation();
		//Fix vehicle being stuck in blocks and prevent it from levitating
		fixPositionErrors();
		//Update parts
		updateParts();

		//Send an update to the clients after a collision, client has some errors in collision handling
		if(!this.world.isRemote&&!hasCollidedBefore&&collidedHorizontally)
		{
			//hasCollidedBefore = true;
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
		double collectedVerticalForce = 0.0;
		double torque = 0;
		int groundedWheels = 0;

		//Simulate individual wheels and gather results
		for(EntityVehicleWheel<T> wheel : this.wheels)
		{
			//Simulate suspension compression for rendering
			wheel.simulateSuspensionCompression();

			//Simulate wheel physics
			WheelForces forces = wheel.calculateForces();
			collectedVerticalForce += forces.force.y;
			if(forces.isGrounded)
			{
				collectedForce = collectedForce.add(new Vec3d(forces.force.x, 0, forces.force.z));
				torque += forces.torque;
				groundedWheels++;
			}
		}

		//Add horizontal forces to velocity
		this.velocity = this.velocity.add(collectedForce);

		//Apply vertical forces (averaged across all wheels)
		if(wheels.length > 0)
		{
			double averageVerticalForce = collectedVerticalForce/wheels.length;
			this.velocity = new Vec3d(this.velocity.x, this.velocity.y+averageVerticalForce, this.velocity.z);
		}

		//Apply motion damping
		applyMotionDamping(groundedWheels);

		//Update rotation with stability check
		this.angularVelocity += torque;
		float newYaw = (float)MathHelper.wrapDegrees(this.rotationYaw+Math.toDegrees(this.angularVelocity));
		if(isTransformSafe(null, newYaw, null, null))
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

		this.posX += motionX;
		this.posY += motionY;
		this.posZ += motionZ;
	}

	/**
	 * Calculates pitch and roll based on vertical force differences and wheel geometry
	 */
	private void calculateForceBasedOrientation()
	{
		//A vehicle must have at least 2 wheels to calculate orientation
		if(wheels.length < 2)
		{
			targetPitch = 0;
			targetRoll = 0;
			return;
		}

		//Find extreme wheel positions for distance calculation
		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
		double minZ = Double.MAX_VALUE, maxZ = Double.MIN_VALUE;

		for(EntityVehicleWheel<T> wheel : wheels)
		{
			minX = Math.min(minX, wheel.offset.x);
			maxX = Math.max(maxX, wheel.offset.x);
			minZ = Math.min(minZ, wheel.offset.z);
			maxZ = Math.max(maxZ, wheel.offset.z);
		}

		//Vehicle length (front-back)
		double xLength = maxX-minX;
		//Vehicle width (left-right)
		double zLength = maxZ-minZ;

		double frontForce = 0, rearForce = 0;
		double leftForce = 0, rightForce = 0;
		int frontWheels = 0, rearWheels = 0, leftWheels = 0, rightWheels = 0;

		for(EntityVehicleWheel<T> wheel : wheels)
		{
			double verticalForce = wheel.getVerticalForceBalance();

			//Front/Rear classification
			if(wheel.offset.x > 0)
			{
				frontForce += verticalForce;
				frontWheels++;
			}
			else
			{
				rearForce += verticalForce;
				rearWheels++;
			}

			//Left/Right classification
			if(wheel.offset.z > 0)
			{
				rightForce += verticalForce;
				rightWheels++;
			}
			else
			{
				leftForce += verticalForce;
				leftWheels++;
			}
		}

		///Calculate pitch angle using vertical force difference
		if(frontWheels > 0&&rearWheels > 0&&xLength > 0)
		{
			frontForce /= frontWheels;
			rearForce /= rearWheels;
			double avgForce = (frontForce+rearForce)/2;

			//Normalize force difference and convert to angle
			double forceDifference = (frontForce-rearForce)/Math.max(1.0, Math.abs(avgForce));
			double pitchRad = Math.atan2(forceDifference*xLength*0.5, xLength);
			targetPitch = (float)Math.toDegrees(pitchRad)*70; //Increased sensitivity for climbing
		}

		//Calculate roll angle using vertical force difference
		if(leftWheels > 0&&rightWheels > 0&&zLength > 0)
		{
			leftForce /= leftWheels;
			rightForce /= rightWheels;
			double avgForce = (leftForce+rightForce)/2;

			//Normalize force difference and convert to angle
			double forceDifference = (leftForce-rightForce)/Math.max(1.0, Math.abs(avgForce));
			double rollRad = Math.atan2(forceDifference*zLength*0.5, zLength);
			targetRoll = (float)Math.toDegrees(rollRad)*70; //Increased sensitivity for climbing
		}

		//Apply safe transformation
		float pitchDiff = targetPitch-this.rotationPitch;
		float rollDiff = targetRoll-this.rotationRoll;

		//Try small increments to avoid collisions
		float maxIncrement = 2.0f; //degrees per tick

		float testPitch = this.rotationPitch+MathHelper.clamp(pitchDiff, -maxIncrement, maxIncrement);
		float testRoll = this.rotationRoll+MathHelper.clamp(rollDiff, -maxIncrement, maxIncrement);

		//Check if the new orientation is safe
		if(isTransformSafe(null, null, testPitch, testRoll))
		{
			this.rotationPitch = testPitch;
			this.rotationRoll = testRoll;
		}
		else
		{
			//If not safe, try to find a safe intermediate orientation
			testPitch = this.rotationPitch+MathHelper.clamp(pitchDiff*0.5f, -maxIncrement, maxIncrement);
			testRoll = this.rotationRoll+MathHelper.clamp(rollDiff*0.5f, -maxIncrement, maxIncrement);

			//Transform, if safe, else maintain current orientation
			if(isTransformSafe(null, null, testPitch, testRoll))
			{
				this.rotationPitch = testPitch;
				this.rotationRoll = testRoll;
			}
		}
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
	 *
	 * @apiNote same collision detection code as in {@link #isTransformSafe(Vec3d, Float, Float, Float)}, but performs physical actions.
	 */
	private Vec3d handleCollisions(Vec3d currentPos, Vec3d attemptedMove)
	{
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
				if(entity instanceof IEntityMultiPart&&entity.getParts()!=null)
					for(Entity entityPart : entity.getParts())
						collisions.add(entityPart.getCollisionBoundingBox());
				else
					collisions.add(entity.getEntityBoundingBox());

			if(!collisions.isEmpty())
			{
				Vec3d partAdjustedMove = attemptedMove;

				//Find the most restrictive movement for each axis independently
				double mostRestrictiveY = partAdjustedMove.y;
				double mostRestrictiveX = partAdjustedMove.x;
				double mostRestrictiveZ = partAdjustedMove.z;

				AxisAlignedBB tempBB = partCurrentBB;

				//First pass: Find the most restrictive Y movement from ALL collision boxes
				for(AxisAlignedBB collisionBox : collisions)
				{
					if(mostRestrictiveY!=0)
					{
						double yOffset = collisionBox.calculateYOffset(tempBB, mostRestrictiveY);
						//Take the most restrictive (smallest absolute value) Y movement
						if(Math.abs(yOffset) < Math.abs(mostRestrictiveY))
						{
							mostRestrictiveY = yOffset;
						}
					}
				}

				//Apply the Y movement first
				if(mostRestrictiveY!=partAdjustedMove.y)
				{
					tempBB = tempBB.offset(0.0D, mostRestrictiveY, 0.0D);
					if(mostRestrictiveY < 0)
					{
						this.onGround = true;
						collided = collidedVertically = true;
					}
				}

				//Second pass: Find the most restrictive X movement
				for(AxisAlignedBB collisionBox : collisions)
				{
					if(mostRestrictiveX!=0)
					{
						double xOffset = collisionBox.calculateXOffset(tempBB, mostRestrictiveX);
						if(Math.abs(xOffset) < Math.abs(mostRestrictiveX))
						{
							mostRestrictiveX = xOffset;
							collided = collidedHorizontally = true;
						}
					}
				}

				//Apply X movement
				if(mostRestrictiveX!=partAdjustedMove.x)
				{
					tempBB = tempBB.offset(mostRestrictiveX, 0.0D, 0.0D);
				}

				//Third pass: Find the most restrictive Z movement
				for(AxisAlignedBB collisionBox : collisions)
				{
					if(mostRestrictiveZ!=0)
					{
						double zOffset = collisionBox.calculateZOffset(tempBB, mostRestrictiveZ);
						if(Math.abs(zOffset) < Math.abs(mostRestrictiveZ))
						{
							mostRestrictiveZ = zOffset;
							collided = collidedHorizontally = true;
						}
					}
				}

				partAdjustedMove = new Vec3d(mostRestrictiveX, mostRestrictiveY, mostRestrictiveZ);

				//Take the most restrictive movement across all parts
				adjustedMove = new Vec3d(
						Math.abs(partAdjustedMove.x) <= Math.abs(adjustedMove.x)?partAdjustedMove.x: adjustedMove.x,
						Math.abs(partAdjustedMove.y) <= Math.abs(adjustedMove.y)?partAdjustedMove.y: adjustedMove.y,
						Math.abs(partAdjustedMove.z) <= Math.abs(adjustedMove.z)?partAdjustedMove.z: adjustedMove.z
				);
			}

			//Update wheel traverse
			if(part instanceof EntityVehicleWheel)
				((EntityVehicleWheel<T>)part).addWheelTraverse((float)adjustedMove.lengthSquared());
		}

		return currentPos.add(adjustedMove);
	}

	/**
	 * Tests whether the vehicle at a given transform would collide with the world.
	 *
	 * @param position The position to test at. If null, current position is used.
	 * @param yaw      The yaw to test. If null, current yaw is used.
	 * @param pitch    The pitch to test. If null, current pitch is used.
	 * @param roll     The roll to test.If null, current roll is used.
	 */
	@ParametersAreNullableByDefault
	private boolean isTransformSafe(Vec3d position, Float yaw, Float pitch, Float roll)
	{
		//Null-safe parameters
		position = position==null?this.getPositionVector(): position;
		yaw = yaw==null?this.rotationYaw: yaw;
		pitch = pitch==null?this.rotationPitch: pitch;
		roll = roll==null?this.rotationRoll: roll;

		//For each part (wheels + hull)
		for(EntityVehiclePart<T> part : partArray)
		{
			//Compute rotated part position for the *test* yaw
			Vec3d rotatedPos = position.add(IIMath.offsetPosDirectionXYZ(part.offset, yaw, pitch, roll));
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
		boolean anyWheelClimbing = false;

		//Phase 1: Fix micro-penetrations
		for(EntityVehiclePart<T> part : partArray)
		{
			//Collect wheels for phase two
			if(part instanceof EntityVehicleWheel)
			{
				if(part.onGround)
					anyWheelOnGround = true;
				if(((EntityVehicleWheel<T>)part).getVerticalForceBalance() > 0)
					anyWheelClimbing = true;
			}

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
					Vec3d direction = IIMath.getAABBCenter(partBB).subtract(IIMath.getAABBCenter(block)).normalize();
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
			if(isTransformSafe(finalCorrection, null, null, null))
			{
				this.posX += finalCorrection.x;
				this.posY += finalCorrection.y;
				this.posZ += finalCorrection.z;
			}
		}

		//Phase 2: Ground snapping
		if(collidedVertically&&!anyWheelOnGround&&!anyWheelClimbing)
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

				if(isTransformSafe(snap, null, null, null))
				{
					this.posY -= minGap;
					this.prevPosY -= minGap;
				}
			}
		}
	}

	/**
	 * Applies slipping forces when there's vertical force imbalance
	 */
	private void applySlippingForces()
	{
		double totalLeftWeight = 0, totalRightWeight = 0;
		double totalFrontWeight = 0, totalRearWeight = 0;
		double frontImbalance = 0, rearImbalance = 0;
		double leftImbalance = 0, rightImbalance = 0;
		int frontWheels = 0, rearWheels = 0, leftWheels = 0, rightWheels = 0;

		//Calculate force imbalances
		for(EntityVehicleWheel<T> wheel : wheels)
		{
			double verticalForce = wheel.getVerticalForceBalance();
			double expectedForce = -0.02*wheel.getWeightShare(); //Expected gravity force

			if(wheel.offset.x > 0) //Front
			{
				totalFrontWeight += wheel.getWeightShare();
				frontImbalance += (verticalForce-expectedForce);
				frontWheels++;
			}
			else //Rear
			{
				totalRearWeight += wheel.getWeightShare();
				rearImbalance += (verticalForce-expectedForce);
				rearWheels++;
			}

			if(wheel.offset.z > 0) //Right
			{
				totalRightWeight += wheel.getWeightShare();
				rightImbalance += (verticalForce-expectedForce);
				rightWheels++;
			}
			else //Left
			{
				totalLeftWeight += wheel.getWeightShare();
				leftImbalance += (verticalForce-expectedForce);
				leftWheels++;
			}
		}

		//Average the imbalances
		if(frontWheels > 0) frontImbalance /= frontWheels;
		if(rearWheels > 0) rearImbalance /= rearWheels;
		if(leftWheels > 0) leftImbalance /= leftWheels;
		if(rightWheels > 0) rightImbalance /= rightWheels;

		//Calculate slipping forces
		//Front-rear imbalance causes slipping sideways (depending on which side has more weight)
		double longitudinalImbalance = frontImbalance-rearImbalance;
		double lateralImbalance = leftImbalance-rightImbalance;

		//Only apply slipping when there's significant imbalance
		if(Math.abs(longitudinalImbalance) > 0.01||Math.abs(lateralImbalance) > 0.01)
		{
			//Slipping force is proportional to imbalance and opposite to the heavier side
			double slipFactor = 0.02; //Adjust for desired slipping intensity

			//If front is heavier and lifting, slip backward
			if(longitudinalImbalance < -0.01&&totalFrontWeight > totalRearWeight)
			{
				Vec3d slipDirection = getForwardVector().scale(-1); //Backward
				this.velocity = this.velocity.add(slipDirection.scale(Math.abs(longitudinalImbalance)*slipFactor));
			}
			//If rear is heavier and lifting, slip forward
			else if(longitudinalImbalance > 0.01&&totalRearWeight > totalFrontWeight)
			{
				Vec3d slipDirection = getForwardVector(); //Forward
				this.velocity = this.velocity.add(slipDirection.scale(Math.abs(longitudinalImbalance)*slipFactor));
			}

			//If left is heavier and lifting, slip right
			if(lateralImbalance < -0.01&&totalLeftWeight > totalRightWeight)
			{
				Vec3d slipDirection = getRightVector(); //Right
				this.velocity = this.velocity.add(slipDirection.scale(Math.abs(lateralImbalance)*slipFactor));
			}
			//If right is heavier and lifting, slip left
			else if(lateralImbalance > 0.01&&totalRightWeight > totalLeftWeight)
			{
				Vec3d slipDirection = getRightVector().scale(-1); //Left
				this.velocity = this.velocity.add(slipDirection.scale(Math.abs(lateralImbalance)*slipFactor));
			}
		}
	}

	/**
	 * Gets the right direction vector
	 */
	private Vec3d getForwardVector()
	{
		double wheelAngle = Math.toRadians(MathHelper.wrapDegrees(rotationYaw));
		return new Vec3d(Math.sin(wheelAngle), 0, Math.cos(wheelAngle));
	}

	/**
	 * Gets the right direction vector
	 */
	private Vec3d getRightVector()
	{
		double wheelAngle = Math.toRadians(MathHelper.wrapDegrees(rotationYaw));
		return new Vec3d(Math.cos(wheelAngle), 0, Math.sin(wheelAngle));
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
	public World getIIWorld()
	{
		return getEntityWorld();
	}

	@Override
	public BlockPos getIIPos()
	{
		return getPosition();
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
		return null;
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

	@SideOnly(Side.CLIENT)
	@Override
	public void setVelocity(double x, double y, double z)
	{

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

	@Override
	public boolean reloadEntity()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeEntityToNBT(nbt);
		this.entityInit();
		this.readEntityFromNBT(nbt);
		return ISyncNBTEntity.super.reloadEntity();
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
