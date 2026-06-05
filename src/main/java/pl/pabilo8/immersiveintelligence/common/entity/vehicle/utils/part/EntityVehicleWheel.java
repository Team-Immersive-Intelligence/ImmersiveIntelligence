package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a wheel entity for vehicles with advanced physics including suspension,
 * climbing capabilities, and force calculation. Each wheel operates independently
 * but contributes to overall vehicle movement.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 29.09.2025
 * @since 13.07.2020
 */
public class EntityVehicleWheel<T extends EntityVehicleBase<T>> extends EntityVehiclePart<T> implements IRotaryEnergy
{
	//--- Controls ---//
	/**
	 * Whether this wheel receives power from the engine, can be steered, or just follows the drivetrain.
	 */
	private WheelType type;
	@Nullable
	private VehicleWheelGroup<T> wheelGroup;
	/**
	 * Rotation speed in Degrees/tick
	 */
	private float speedValue = 0;
	/**
	 * Torque value in IT
	 */
	private float torqueValue = 0;
	/**
	 * Braking factor 0-1
	 */
	private float brakeFactor = 0;
	/**
	 * Efficiency factor 0-1, depends on damage of the tire
	 */
	private float efficiencyModifier = 1f;
	/**
	 * Designated steering angle, when the wheel is steerable
	 */
	private float steeringAngle = 0.0f;

	//--- Animation ---//
	/**
	 * Distance this wheel has already traversed in blocks, for rendering purposes
	 */
	private float wheelTraverse = 0;
	/**
	 * Current upward suspension compression from the wheel's extended position, in vehicle-local +Y.
	 */
	private double suspensionCompression = 0.0;
	/**
	 * Previous tick's upward compression, used for interpolation during render/debug.
	 */
	private double lastSuspensionCompression = 0.0;
	/**
	 * Max upward suspension compression in blocks. Unlike the old render-only value, this now affects the wheel OBB.
	 */
	private double maxSuspensionCompression = 1.0;
	/**
	 * Maximum downward wheel travel from its hard mount point. This is the important physical droop value.
	 */
	private double maxSuspensionDroop = 0.0;
	/**
	 * Current downward travel from the hard mount point, in vehicle-local -Y.
	 */
	private double suspensionDroop = 0.0;
	/**
	 * Previous tick's downward travel, used by renderers/debug views for interpolation.
	 */
	private double lastSuspensionDroop = 0.0;
	/**
	 * Last support state resolved by the suspension probe.
	 */
	private boolean suspensionSupported = false;
	/**
	 * Weight share of the vehicle's total mass that this wheel carries
	 */
	private double weightShare = 1.0;

	//--- Enhanced Force System ---//
	/**
	 * Max vertical force this wheel can exert (both positive and negative)
	 */
	private double maxVerticalForce = 1.0;
	/**
	 * Current vertical force balance (positive = upward, negative = downward)
	 */
	private double verticalForceBalance = 0.0;
	/**
	 * Current climb height based on force balance
	 */
	private double currentClimbHeight = 0.0;
	/**
	 * Grip factor based on vertical force balance and contact quality
	 */
	private double gripFactor = 1.0;

	//--- Force Calculation ---//
	private VerticalForces lastVerticalForces = new VerticalForces(false, 0, Vec3d.ZERO, null, 0, 0, 0, false);

	//--- Last Computed Outputs ---//
	private WheelForces lastForces = new WheelForces(Vec3d.ZERO, 0, false);

	/**
	 * Constructs a new vehicle wheel entity.
	 *
	 * @param parentExt the parent vehicle entity
	 * @param name      the name of this wheel part
	 * @param offset    the offset from vehicle center
	 * @param aabb      the bounding box for collision
	 */
	public EntityVehicleWheel(T parentExt, String name, Vec3d offset, AxisAlignedBB aabb)
	{
		super(parentExt, name, offset, aabb);
		this.type = WheelType.IDLER;
	}

	/**
	 * Configures the wheel's control capabilities.
	 *
	 * @return this wheel for method chaining
	 */
	public EntityVehicleWheel<T> withType(WheelType type)
	{
		this.type = type;
		return this;
	}

	public EntityVehicleWheel<T> withWheelGroup(@Nullable VehicleWheelGroup<T> group)
	{
		this.wheelGroup = group;
		return this;
	}

	/**
	 * Configures the wheel's force capabilities.
	 *
	 * @param maxVerticalForce maximum vertical force the wheel can exert
	 * @return this wheel for method chaining
	 */
	public EntityVehicleWheel<T> withForceCapabilities(double maxVerticalForce)
	{
		this.maxVerticalForce = maxVerticalForce;
		return this;
	}

	/**
	 * Configures the wheel's suspension properties
	 *
	 * @param maxCompression maximum compression in blocks
	 * @return this wheel for method chaining
	 */
	public EntityVehicleWheel<T> withSuspension(double maxCompression)
	{
		this.maxSuspensionCompression = Math.max(0, maxCompression);
		return this;
	}

	/**
	 * Configures full suspension travel. Droop lets the wheel extend downward to keep contact; compression lets
	 * it move upward when terrain pushes it into the spring.
	 *
	 * @param maxCompression maximum visual compression in blocks
	 * @param maxDroop       maximum physical downward travel in blocks
	 * @return this wheel for method chaining
	 */
	public EntityVehicleWheel<T> withSuspension(double maxCompression, double maxDroop)
	{
		this.maxSuspensionCompression = Math.max(0, maxCompression);
		this.maxSuspensionDroop = Math.max(0, maxDroop);
		return this;
	}

	/**
	 * Configures only the physical downward suspension travel.
	 *
	 * @param maxDroop maximum physical downward travel in blocks
	 * @return this wheel for method chaining
	 */
	public EntityVehicleWheel<T> withSuspensionDroop(double maxDroop)
	{
		this.maxSuspensionDroop = Math.max(0, maxDroop);
		return this;
	}

	public EntityVehicleWheel<T> withWeightShare(double weightShare)
	{
		this.weightShare = weightShare;
		return this;
	}

	@Override
	public EntityVehicleWheel<T> withHitbox(@Nonnull SyncedDurability hitbox)
	{
		super.withHitbox(hitbox);
		return this;
	}

	//--- Enhanced Force Calculation ---

	/**
	 * Calculates the forces and torque generated by this wheel using enhanced friction model.
	 */
	public WheelForces calculateForces()
	{
		if(parentExt==null)
			return this.lastForces = new WheelForces(Vec3d.ZERO, 0.0, false);
		VehicleBlueprint blueprint = parentExt.getVehicleBlueprint();
		float effectiveSpeed = getEffectiveRotationSpeed();
		float effectiveTorque = getEffectiveTorque();
		double powerFactor = MathHelper.clamp(Math.abs(effectiveSpeed)/Math.max(1.0, blueprint.nominalWheelSpeed()), 0.0, 1.0);

		/*if(powerFactor > 0)
			IILogger.info(partName+" | torque: "+torqueValue+" | speed: "+speedValue+"| powerFactor: "+powerFactor);*/

		//Calculate vertical forces first (gravity and climbing)
		VerticalForces verticalForces = calculateVerticalForces();
		this.lastVerticalForces = verticalForces;

		//Update grip based on vertical force balance
		updateGripFactor();

		//Calculate efficiency
		this.efficiencyModifier = calculateEffectiveEfficiency();
		VehicleWheelGroup<T> activeGroup = wheelGroup!=null&&wheelGroup.hasDriveInput()?wheelGroup: null;
		double driveForce = activeGroup!=null?
				activeGroup.getDriveForce(this, blueprint, efficiencyModifier, gripFactor):
				Math.signum(effectiveSpeed)*blueprint.driveForce()*powerFactor*MathHelper.clamp(Math.abs(effectiveTorque)/Math.max(1.0, blueprint.nominalWheelTorque()), 0.0, 1.0)*(1f-brakeFactor)*efficiencyModifier*gripFactor;
		double angularVel = this.parentExt.getAngularVelocity();

		//Wheel position and orientation in world coordinates. The horizontal force axes are derived from
		//the wheel OBB itself, so segment rotation and hull pitch/roll do not desynchronise physics.
		VehicleOBB wheelOBB = getCollisionOBB();
		Vec3d wheelPos = wheelOBB.center;
		Vec3d forward = new Vec3d(-wheelOBB.axisZ.x, 0, -wheelOBB.axisZ.z);
		if(forward.lengthSquared() < 1.0E-6)
			forward = getForwardVector();
		else
			forward = forward.normalize();
		Vec3d right = new Vec3d(wheelOBB.axisX.x, 0, wheelOBB.axisX.z);
		if(right.lengthSquared() < 1.0E-6)
			right = new Vec3d(forward.z, 0, -forward.x);
		else
			right = right.normalize();

		//Velocity at wheel position
		double relX = wheelPos.x-parentExt.posX;
		double relZ = wheelPos.z-parentExt.posZ;
		Vec3d velocity = parentExt.getVelocity();
		//Rotational velocity components
		double wheelVx = velocity.x-angularVel*relZ;
		double wheelVz = velocity.z+angularVel*relX;

		//Project velocity onto wheel axes
		double vLat = wheelVx*right.x+wheelVz*right.z;

		//Lateral friction (all wheels)
		double latFrictionCoef = wheelGroup!=null?
				wheelGroup.getLateralFriction(blueprint, this):
				(this.type.isDriven()?blueprint.lateralFrictionDrive(): blueprint.lateralFrictionIdler());
		//Additional steering friction
		if(wheelGroup==null&&Math.abs(this.steeringAngle) > 0.1f)
			latFrictionCoef *= blueprint.steeringFrictionMultiplier();
		double latFriction = -vLat*latFrictionCoef;

		//Total force in wheel's local axes
		double fx = driveForce*forward.x+latFriction*right.x;
		double fz = driveForce*forward.z+latFriction*right.z;

		//Compile and return forces. driveForce already includes the primary blueprint scalar; forceFactor remains as a
		//legacy multiplier for old vehicle annotations that have already been balanced around it.
		Vec3d force = new Vec3d(fx*blueprint.forceFactor(), verticalForces.verticalSum, fz*blueprint.forceFactor());
		double torque = (relX*fz-relZ*fx)*blueprint.steeringTorque();
		// Vehicle local convention: +X right, -Z front, +Z rear.
		double pitchTorque = this.offset.z*verticalForces.verticalSum*blueprint.orientationTorqueFactor();
		double rollTorque = this.offset.x*verticalForces.verticalSum*blueprint.orientationTorqueFactor();
		addWheelTraverse((float)force.lengthVector());

		boolean effectivelyGrounded = verticalForces.isGrounded||(verticalForces.verticalSum > 0&&verticalForces.canClimb);
		return this.lastForces = new WheelForces(force, torque, pitchTorque, rollTorque, effectivelyGrounded);
	}

	/**
	 * Calculates vertical forces (gravity, wall climbing) acting on this wheel using integration for gradual movement
	 */
	private VerticalForces calculateVerticalForces()
	{
		boolean isGrounded = checkForGround();
		VehicleBlueprint blueprint = parentExt.getVehicleBlueprint();
		double targetForce = 0.0;
		double climbForce = 0.0;

		//Calculate target forces based on conditions
		ClimbDetectionResult climbResult = checkClimbObstacle();
		boolean shouldClimb = climbResult.canClimb&&isMovingTowardObstacle(climbResult);

		if(shouldClimb)
		{
			//More aggressive climb force calculation
			double requiredForce = climbResult.climbHeight*blueprint.climbForceScale();
			targetForce = Math.min(requiredForce, getEffectiveMaxVerticalForce())*gripFactor;

			//Override gravity when actively climbing
			climbForce = targetForce;
		}
		//Gravity
		else if(!isGrounded)
			targetForce = -blueprint.wheelGravityForce()*weightShare;

		//Integration: gradually approach target force
		double forceChangeRate;
		if(shouldClimb)
			forceChangeRate = 0.8;
		else if(isGrounded)
			forceChangeRate = 0.3;
		else
			forceChangeRate = 0.2;

		double newForceBalance = verticalForceBalance+(targetForce-verticalForceBalance)*forceChangeRate;

		//Remove the grounded damping that was killing climb forces
		//Only apply strong damping when we're grounded AND not trying to climb
		if(isGrounded&&!shouldClimb&&Math.abs(targetForce) < 0.001)
			newForceBalance *= 0.3;

		//Apply limits
		newForceBalance = MathHelper.clamp(newForceBalance, -getEffectiveMaxVerticalForce(), getEffectiveMaxVerticalForce());

		//Update climb height based on integrated force balance
		double heightChange = newForceBalance*0.1;
		currentClimbHeight = MathHelper.clamp(currentClimbHeight+heightChange, 0, getEffectiveMaxVerticalForce()*2);

		verticalForceBalance = newForceBalance;

		return new VerticalForces(climbResult.canClimb, climbResult.climbHeight,
				climbResult.obstacleNormal, climbResult.climbedBox,
				targetForce, climbForce, verticalForceBalance, isGrounded);
	}

	/**
	 * Checks for climbable obstacles in the wheel's path
	 */
	private ClimbDetectionResult checkClimbObstacle()
	{
		Vec3d wheelWorldPos = getPositionVector();
		VehicleOBB currentOBB = getCollisionOBB();
		//Use vehicle velocity direction for more natural climbing
		Vec3d climbDirection = parentExt.getVelocity().normalize();
		//Cut out the Y component, climbing check should be horizontal
		climbDirection = new Vec3d(climbDirection.x, 0, climbDirection.z);
		//Use wheel's forward vector, if vehicle does not move
		if(climbDirection.lengthSquared() < 0.1)
			climbDirection = getForwardVector();

		double effectiveClimb = getEffectiveMaxVerticalForce();
		double checkDistance = 0.5+effectiveClimb*0.5;
		Vec3d start = new Vec3d(wheelWorldPos.x, wheelWorldPos.y, wheelWorldPos.z);
		VehicleOBB projectedOBB = currentOBB.offset(climbDirection.x*checkDistance, 0, climbDirection.z*checkDistance);
		AxisAlignedBB partNextBB = currentOBB.getEnclosingAABB().union(projectedOBB.getEnclosingAABB()).grow(0.03125);

		//Get block collision boxes
		List<AxisAlignedBB> collisions = new ArrayList<>(world.getCollisionBoxes(null, partNextBB));
		//Get other vehicle collision boxes, useful in case of f.e. tanks driving over cars, bridge-deploying vehicles
		for(Entity entity : world.getEntitiesInAABBexcluding(this.parentExt, partNextBB, e -> e!=this.parentExt&&e instanceof EntityVehicleBase))
			if(entity instanceof IEntityMultiPart&&entity.getParts()!=null)
				for(Entity entityPart : entity.getParts())
					collisions.add(entityPart.getCollisionBoundingBox());
			else
				collisions.add(entity.getEntityBoundingBox());
		collisions.removeIf(Objects::isNull);

		//Sort from highest to lowest Y
		collisions.sort((bb1, bb2) -> Double.compare(bb2.maxY, bb1.maxY));

		//Process collisions
		for(AxisAlignedBB collisionBoundingBox : collisions)
		{
			if(!projectedOBB.intersects(collisionBoundingBox))
				continue;

			double obstacleTop = collisionBoundingBox.maxY;
			double wheelBottom = currentOBB.getEnclosingAABB().minY;
			double heightDifference = obstacleTop-wheelBottom;

			if(heightDifference > 0&&heightDifference <= Math.max(parentExt.getVehicleBlueprint().obstacleClimbHeight(), effectiveClimb*2)+0.25) //Scale with force capability
				return new ClimbDetectionResult(true, heightDifference+0.125,
						IIMath.getAABBCenter(collisionBoundingBox).subtract(start).normalize().scale(-1), collisionBoundingBox);
		}

		return new ClimbDetectionResult(false, 0, Vec3d.ZERO, null);
	}

	/**
	 * Determines if the wheel is moving toward the detected obstacle
	 */
	private boolean isMovingTowardObstacle(ClimbDetectionResult climbResult)
	{
		if(parentExt==null) return false;

		Vec3d velocity = parentExt.getVelocity();
		if(velocity.lengthSquared() < 0.01) return false; //Not moving

		//double dotProduct = new Vec3d(velocity.x, 0, velocity.z).normalize().dotProduct(climbResult.obstacleNormal);
		return true; //dotProduct < -0.3; //Moving toward obstacle
	}

	/**
	 * Updates grip factor based on vertical force balance
	 */
	private void updateGripFactor()
	{
		//Grip reduces when vertical forces are imbalanced (climbing or falling)
		double forceImbalance = Math.abs(verticalForceBalance);
		double maxForce = Math.max(0.001, getEffectiveMaxVerticalForce());
		double stability = 1.0-(forceImbalance/maxForce)*0.7;

		//Additional grip reduction when climbing high
		if(verticalForceBalance > 0&&currentClimbHeight > maxForce)
			stability *= 0.8;

		//Factor in torque value for dynamic grip adjustment
		double torqueFactor = MathHelper.clamp(Math.abs(getEffectiveTorque())/Math.max(1.0, parentExt.getVehicleBlueprint().nominalWheelTorque()), 0.3, 1.0);
		gripFactor = MathHelper.clamp(Math.max(0.3, stability)*torqueFactor, 0.3, 1);
	}

	/**
	 * Calculates effective efficiency considering damage
	 */
	private float calculateEffectiveEfficiency()
	{
		return this.durability==null?1f:
				this.durability.getDamageFactor() > 0.3?1f: 0.15f;
	}

	/**
	 * Precision ground detection based on the wheel's AABB.
	 * Returns true if the wheel is touching or nearly touching any solid block underneath.
	 */
	private boolean checkForGround()
	{
		//Suspension probing already resolved the best physical wheel position this tick.
		if(suspensionSupported)
			return true;

		//Fallback for wheels without travel or surfaces that appear after the suspension update.
		VehicleOBB wheelOBB = getCollisionOBB();
		return isSupportBelow(wheelOBB, 0.05);
	}

	/**
	 * Gets the wheel's forward direction vector
	 */
	private Vec3d getForwardVector()
	{
		return VehicleOBB.getForwardVector(MathHelper.wrapDegrees(parentExt.rotationYaw+this.steeringAngle));
	}


	@Override
	protected float getAdditionalCollisionYaw()
	{
		return this.steeringAngle;
	}

	//--- Suspension ---//

	/**
	 * Calculates the suspension compression for rendering with smooth interpolation
	 * This only calculates the value, does not set it
	 *
	 * @param partialTicks partial tick time for interpolation
	 * @return interpolated suspension compression value
	 */
	public double calculateSuspensionCompression(float partialTicks)
	{
		return this.lastSuspensionCompression+(this.suspensionCompression-this.lastSuspensionCompression)*partialTicks;
	}

	/**
	 * Simulates suspension compression based on vehicle state and stores the result
	 * Call this each tick during vehicle update
	 */
	public void simulateSuspensionCompression()
	{
		//Store previous values for interpolation
		this.lastSuspensionCompression = this.suspensionCompression;
		this.lastSuspensionDroop = this.suspensionDroop;

		//Resolve physical suspension travel first. Wheels should fall down quickly when support disappears,
		//and retract a little more gently when the hull moves closer to terrain.
		SuspensionProbe probe = resolveSuspensionDroop();
		this.suspensionSupported = probe.supported;
		double droopSmoothing = probe.targetDroop > this.suspensionDroop?0.85: 0.55;
		this.suspensionDroop += (probe.targetDroop-this.suspensionDroop)*droopSmoothing;
		this.suspensionDroop = MathHelper.clamp(this.suspensionDroop, 0.0, this.maxSuspensionDroop);

		// Compression is now physical and upwards. Droop alone keeps wheels in contact; compression prevents
		// wheels from acting like rigid stilts when they are pushed into a block face or loaded by the hull.
		double targetCompression = resolveSuspensionCompression(this.suspensionDroop);
		double compressionSmoothing = targetCompression > this.suspensionCompression?0.75: 0.45;
		this.suspensionCompression += (targetCompression-this.suspensionCompression)*compressionSmoothing;
		this.suspensionCompression = MathHelper.clamp(this.suspensionCompression, 0.0, maxSuspensionCompression);
	}

	private double resolveSuspensionCompression(double droop)
	{
		if(maxSuspensionCompression <= 1.0E-6||parentExt==null||world==null)
			return 0.0;

		// A supported wheel compresses slightly under load; a wheel clipping into blocky terrain compresses
		// as far as needed to become free. This is the upward half of the spring.
		double loadCompression = (suspensionSupported||lastVerticalForces.isGrounded)?
				Math.min(maxSuspensionCompression, Math.abs(verticalForceBalance)*0.1+
						(maxSuspensionDroop <= 0?0: (1.0-MathHelper.clamp(droop/maxSuspensionDroop, 0.0, 1.0))*maxSuspensionCompression*0.35)):
				0.0;

		VehicleOBB relaxed = getCollisionOBBWithSuspensionTravel(parentExt.getPositionVector(),
				parentExt.rotationYaw, parentExt.rotationPitch, parentExt.getRotationRoll(), droop, loadCompression);
		if(!intersectsSuspensionBlockingGeometry(relaxed))
			return loadCompression;

		double step = 1.0/16.0;
		for(double compression = loadCompression; compression <= maxSuspensionCompression+1.0E-6; compression += step)
		{
			double candidateCompression = Math.min(maxSuspensionCompression, compression);
			VehicleOBB candidate = getCollisionOBBWithSuspensionTravel(parentExt.getPositionVector(),
					parentExt.rotationYaw, parentExt.rotationPitch, parentExt.getRotationRoll(), droop, candidateCompression);
			if(!intersectsSuspensionBlockingGeometry(candidate))
				return candidateCompression;
		}
		return maxSuspensionCompression;
	}

	private SuspensionProbe resolveSuspensionDroop()
	{
		if(maxSuspensionDroop <= 1.0E-6||parentExt==null||world==null)
			return new SuspensionProbe(0.0, false);

		// Scan down in sixteenth-block increments. This is intentional: Minecraft terrain is blocky, and
		// snapping to the game's native 1/16 grid avoids tiny jitter while still keeping tracks planted.
		double step = 1.0/16.0;
		double bestFreeDrop = 0.0;
		for(double drop = 0.0; drop <= maxSuspensionDroop+1.0E-6; drop += step)
		{
			double candidateDrop = Math.min(maxSuspensionDroop, drop);
			VehicleOBB candidate = getCollisionOBBWithSuspensionTravel(parentExt.getPositionVector(),
					parentExt.rotationYaw, parentExt.rotationPitch, parentExt.getRotationRoll(), candidateDrop, this.suspensionCompression);
			if(intersectsSuspensionBlockingGeometry(candidate))
				break;
			bestFreeDrop = candidateDrop;

			if(isSupportBelow(candidate, Math.max(0.04, step*0.9)))
				return new SuspensionProbe(candidateDrop, true);
		}

		// No terrain was found inside the travel range, so let the wheel hang at the deepest free position.
		return new SuspensionProbe(bestFreeDrop, false);
	}

	private boolean intersectsSuspensionBlockingGeometry(VehicleOBB obb)
	{
		AxisAlignedBB broad = obb.getEnclosingAABB().grow(0.001);
		for(AxisAlignedBB blockBox : world.getCollisionBoxes(null, broad))
			if(obb.intersects(blockBox))
				return true;

		// Do not let hanging wheels sink into other vehicles either. Regular entities are ignored here;
		// they are pushed by the parent vehicle after movement instead of being treated as terrain.
		for(Entity entity : world.getEntitiesInAABBexcluding(this.parentExt, broad, e -> e!=this.parentExt&&e instanceof EntityVehicleBase))
		{
			if(entity instanceof IEntityMultiPart&&entity.getParts()!=null)
			{
				for(Entity entityPart : entity.getParts())
					if(entityPart instanceof EntityVehiclePart&&obb.intersects(((EntityVehiclePart<?>)entityPart).getCollisionOBB()))
						return true;
			}
			else if(entity.getEntityBoundingBox()!=null&&obb.intersects(entity.getEntityBoundingBox()))
				return true;
		}
		return false;
	}

	private boolean isSupportBelow(VehicleOBB wheelOBB, double epsilon)
	{
		VehicleOBB probeOBB = wheelOBB.offset(0, -Math.max(0.001, epsilon), 0);
		AxisAlignedBB probeBB = probeOBB.getEnclosingAABB();

		for(AxisAlignedBB collision : world.getCollisionBoxes(null, probeBB))
			if(probeOBB.intersects(collision))
				return true;

		for(Entity entity : world.getEntitiesInAABBexcluding(this.parentExt, probeBB, e -> e!=this.parentExt&&e instanceof EntityVehicleBase))
		{
			if(entity instanceof IEntityMultiPart&&entity.getParts()!=null)
			{
				for(Entity entityPart : entity.getParts())
					if(entityPart instanceof EntityVehiclePart&&probeOBB.intersects(((EntityVehiclePart<?>)entityPart).getCollisionOBB()))
						return true;
			}
			else if(entity.getEntityBoundingBox()!=null&&probeOBB.intersects(entity.getEntityBoundingBox()))
				return true;
		}
		return false;
	}

	@Override
	public VehicleOBB getCollisionOBB(Vec3d vehiclePosition, float yaw, float pitch, float roll)
	{
		return getCollisionOBBWithSuspensionTravel(vehiclePosition, yaw, pitch, roll, this.suspensionDroop, this.suspensionCompression);
	}

	private VehicleOBB getCollisionOBBWithSuspensionTravel(Vec3d vehiclePosition, float yaw, float pitch, float roll, double droop, double compression)
	{
		VehicleSegment<?> activeSegment = getActiveSegment();
		double travelY = MathHelper.clamp(compression, 0.0, maxSuspensionCompression)-MathHelper.clamp(droop, 0.0, maxSuspensionDroop);
		Vec3d suspendedOffset = offset.addVector(0, travelY, 0);
		if(activeSegment!=null)
			return activeSegment.createPartOBB(aabb, vehiclePosition, suspendedOffset, yaw, pitch, roll, getAdditionalCollisionYaw());

		Vec3d partPosition = vehiclePosition.add(VehicleOBB.transformLocal(suspendedOffset, yaw, pitch, roll));
		return VehicleOBB.fromLocalAABB(aabb, partPosition, getCollisionYaw(yaw), pitch, roll);
	}

	@Override
	public Vec3d getPartWorldPosition(Vec3d vehiclePosition, float yaw, float pitch, float roll)
	{
		VehicleSegment<?> activeSegment = getActiveSegment();
		Vec3d suspendedOffset = offset.addVector(0, this.suspensionCompression-this.suspensionDroop, 0);
		if(activeSegment!=null)
			return activeSegment.getPartWorldPosition(vehiclePosition, suspendedOffset, yaw, pitch, roll);
		return vehiclePosition.add(VehicleOBB.transformLocal(suspendedOffset, yaw, pitch, roll));
	}

	private static class SuspensionProbe
	{
		private final double targetDroop;
		private final boolean supported;

		private SuspensionProbe(double targetDroop, boolean supported)
		{
			this.targetDroop = targetDroop;
			this.supported = supported;
		}
	}

	//--- Control Methods ---

	/**
	 * Sets the steering angle for steerable wheels.
	 *
	 * @param angle the desired steering angle in degrees
	 */
	public void setSteeringAngle(float angle)
	{
		if(this.type.isSteerable())
			this.steeringAngle = MathHelper.clamp(angle, -45.0f, 45.0f);
	}

	//--- Public Moving Methods ---

	/**
	 * Updates wheel rotation and traverse for visual effects.
	 *
	 * @param distance how much has the wheel moved
	 */
	public void addWheelTraverse(float distance)
	{
		this.wheelTraverse += distance*(float)getWheelRadius()*90f;
	}

	/**
	 * Sets the brake value for this wheel.
	 *
	 * @param brakeFactor brake factor 0-1
	 */
	public void setBrakeFactor(float brakeFactor)
	{
		this.brakeFactor = MathHelper.clamp(brakeFactor, 0f, 1f);
	}

	private float getEffectiveTorque()
	{
		return wheelGroup!=null&&wheelGroup.hasDriveInput()?wheelGroup.getTorque(): torqueValue;
	}

	private float getEffectiveRotationSpeed()
	{
		return wheelGroup!=null&&wheelGroup.hasDriveInput()?wheelGroup.getRotationSpeed(): speedValue;
	}

	private double getEffectiveMaxVerticalForce()
	{
		if(parentExt==null)
			return maxVerticalForce;
		return Math.max(maxVerticalForce, parentExt.getVehicleBlueprint().obstacleClimbHeight()*0.5);
	}

	@Nullable
	public VehicleWheelGroup<T> getWheelGroup()
	{
		return wheelGroup;
	}

	//--- Properties ---

	public WheelType getType()
	{
		return type;
	}

	public float getSteeringAngle()
	{
		return this.steeringAngle;
	}

	public WheelForces getLastForces()
	{
		return lastForces;
	}

	public VerticalForces getLastVerticalForces()
	{
		return lastVerticalForces;
	}

	public float getWheelTraverse()
	{
		return wheelTraverse;
	}

	public float getBrakeFactor()
	{
		return brakeFactor;
	}

	public float getEfficiencyModifier()
	{
		return efficiencyModifier;
	}

	public double getVerticalForceBalance()
	{
		return verticalForceBalance;
	}

	public double getCurrentClimbHeight()
	{
		return currentClimbHeight;
	}

	public double getSuspensionDroop()
	{
		return suspensionDroop;
	}

	public double getLastSuspensionDroop()
	{
		return lastSuspensionDroop;
	}

	public double getMaxSuspensionDroop()
	{
		return maxSuspensionDroop;
	}

	public double getSuspensionCompression()
	{
		return suspensionCompression;
	}

	public double getMaxSuspensionCompression()
	{
		return maxSuspensionCompression;
	}

	public boolean isSuspensionSupported()
	{
		return suspensionSupported;
	}

	public double getMaxVerticalForce()
	{
		return maxVerticalForce;
	}

	public double getGripFactor()
	{
		return gripFactor;
	}

	public double getWheelRadius()
	{
		return height/2;
	}

	public double getWeightShare()
	{
		return weightShare;
	}

	//--- IRotaryEnergy ---//

	@Override
	public float getTorque()
	{
		return torqueValue;
	}

	@Override
	public void setTorque(float torque)
	{
		this.torqueValue = torque;
	}

	@Override
	public float getRotationSpeed()
	{
		return speedValue;
	}

	@Override
	public void setRotationSpeed(float speed)
	{
		this.speedValue = speed;
	}

	@Override
	public RotationSide getSide(@Nullable EnumFacing facing)
	{
		return RotationSide.INPUT;
	}

	//--- Helper class for climb detection ---//
	private static class ClimbDetectionResult
	{
		public final boolean canClimb;
		public final double climbHeight;
		public final Vec3d obstacleNormal;
		public final AxisAlignedBB climbedBox;

		public ClimbDetectionResult(boolean canClimb, double climbHeight, Vec3d obstacleNormal, AxisAlignedBB climbedBox)
		{
			this.canClimb = canClimb;
			this.climbHeight = climbHeight;
			this.obstacleNormal = obstacleNormal;
			this.climbedBox = climbedBox;
		}
	}
}
