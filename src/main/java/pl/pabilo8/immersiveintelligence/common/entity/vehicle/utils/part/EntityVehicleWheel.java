package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wheel entity for vehicles with advanced physics including suspension,
 * climbing capabilities, and force calculation. Each wheel operates independently
 * but contributes to overall vehicle movement.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 29.09.2025
 * @since 13.07.2020
 */
public class EntityVehicleWheel<T extends Entity & IVehicleMultiPart<T>> extends EntityVehiclePart<T> implements IRotaryEnergy
{
	//--- Controls ---//
	/**
	 * Whether this wheel receives power from the engine, can be steered, or just follows the drivetrain.
	 */
	private WheelType type;
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
	 * Current compression for this wheel's suspension, in blocks, used for rendering purposes
	 */
	private double suspensionCompression = 0.0;
	/**
	 * Previous tick's compression for this wheel's suspension, used for interpolation during render
	 */
	private double lastSuspensionCompression = 0.0;
	/**
	 * Max compression for this wheel's suspension, in blocks
	 */
	private double maxSuspensionCompression = 1.0;
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
		this.maxSuspensionCompression = maxCompression;
		return this;
	}

	public EntityVehicleWheel<T> withWeightShare(double weightShare)
	{
		this.weightShare = weightShare;
		return this;
	}

	@Override
	public EntityVehicleWheel<T> withHitbox(@Nonnull VehicleDurability hitbox)
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
		float powerFactor = Math.abs(speedValue/360f);

		if(powerFactor > 0)
			IILogger.info(partName+" | torque: "+torqueValue+" | speed: "+speedValue+"| powerFactor: "+powerFactor);

		//Calculate vertical forces first (gravity and climbing)
		VerticalForces verticalForces = calculateVerticalForces();
		this.lastVerticalForces = verticalForces;

		//Update grip based on vertical force balance
		updateGripFactor();

		//Calculate efficiency
		this.efficiencyModifier = calculateEffectiveEfficiency();
		double driveForce = powerFactor*(1f-brakeFactor)*efficiencyModifier*gripFactor;
		double angularVel = this.parentExt.getAngularVelocity();

		//Wheel position in world coordinates
		Vec3d wheelPos = parentExt.getPositionVector().add(IIMath.offsetPosDirectionXYZ(offset, parentExt.rotationYaw, parentExt.rotationPitch, 0));

		//Wheel orientation in the world
		double wheelAngle = Math.toRadians(MathHelper.wrapDegrees(parentExt.rotationYaw+this.steeringAngle));

		//Forward direction is (sinA, cosA) which is (-sin(wheelAngle), cos(wheelAngle))
		//Lateral (right) direction is (cosA, -sinA) which is (cos(wheelAngle), sin(wheelAngle))
		double sinA = -MathHelper.sin((float)wheelAngle);
		double cosA = MathHelper.cos((float)wheelAngle);

		//Velocity at wheel position
		double relX = wheelPos.x-parentExt.posX;
		double relZ = wheelPos.z-parentExt.posZ;
		Vec3d velocity = parentExt.getVelocity();
		//Rotational velocity components
		double wheelVx = velocity.x-angularVel*relZ;
		double wheelVz = velocity.z+angularVel*relX;

		//Project velocity onto wheel axes
		double vLat = wheelVx*cosA-wheelVz*sinA;

		//Lateral friction (all wheels)
		double latFrictionCoef = this.type.isDriven()?blueprint.lateralFrictionDrive(): blueprint.lateralFrictionIdler();
		//Additional steering friction
		if(Math.abs(this.steeringAngle) > 0.1f)
			latFrictionCoef *= blueprint.steeringFrictionMultiplier();
		double latFriction = -vLat*latFrictionCoef;

		//Total force in wheel's local axes
		double fx = driveForce*sinA+latFriction*cosA;
		double fz = driveForce*cosA-latFriction*sinA;

		//Compile and return forces
		Vec3d force = new Vec3d(fx*blueprint.forceFactor(), verticalForces.verticalSum, fz*blueprint.forceFactor());
		double torque = (relX*fz-relZ*fx)*blueprint.torqueFactor();
		addWheelTraverse((float)force.lengthVector());

		boolean effectivelyGrounded = verticalForces.isGrounded||(verticalForces.verticalSum > 0&&verticalForces.canClimb);
		return this.lastForces = new WheelForces(force, torque, effectivelyGrounded);
	}

	/**
	 * Calculates vertical forces (gravity, wall climbing) acting on this wheel using integration for gradual movement
	 */
	private VerticalForces calculateVerticalForces()
	{
		boolean isGrounded = checkForGround();
		double targetForce = 0.0;
		double climbForce = 0.0;

		//Calculate target forces based on conditions
		ClimbDetectionResult climbResult = checkClimbObstacle();
		boolean shouldClimb = climbResult.canClimb&&isMovingTowardObstacle(climbResult);

		if(shouldClimb)
		{
			//More aggressive climb force calculation
			double requiredForce = climbResult.climbHeight*0.0625; //Increased from 0.1 to 0.3
			targetForce = Math.min(requiredForce, maxVerticalForce)*gripFactor;

			//Override gravity when actively climbing
			climbForce = targetForce;
		}
		//Gravity
		else if(!isGrounded)
			targetForce = -0.04*weightShare; //*parentExt.getVehicleBlueprint().mass()

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
		newForceBalance = MathHelper.clamp(newForceBalance, -maxVerticalForce, maxVerticalForce);

		//Update climb height based on integrated force balance
		double heightChange = newForceBalance*0.1;
		currentClimbHeight = MathHelper.clamp(currentClimbHeight+heightChange, 0, maxVerticalForce*2);

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
		//Use vehicle velocity direction for more natural climbing
		Vec3d climbDirection = parentExt.getVelocity().normalize();
		//Cut out the Y component, climbing check should be horizontal
		climbDirection = new Vec3d(climbDirection.x, 0, climbDirection.z);
		//Use wheel's forward vector, if vehicle does not move
		if(climbDirection.lengthSquared() < 0.1)
			climbDirection = getForwardVector();

		double checkDistance = 0.5+this.maxVerticalForce*0.5;
		Vec3d start = new Vec3d(wheelWorldPos.x, wheelWorldPos.y, wheelWorldPos.z);
		AxisAlignedBB partCurrentBB = aabb.offset(start);
		AxisAlignedBB partNextBB = partCurrentBB.expand(climbDirection.x*checkDistance, 0, climbDirection.z*checkDistance);

		//Get block collision boxes
		List<AxisAlignedBB> collisions = new ArrayList<>(world.getCollisionBoxes(null, partNextBB));
		//Get other vehicle collision boxes, useful in case of f.e. tanks driving over cars, bridge-deploying vehicles
		for(Entity entity : world.getEntitiesInAABBexcluding(this.parentExt, partNextBB, e -> e!=this.parentExt&&e instanceof EntityVehicleBase))
			if(entity instanceof IEntityMultiPart&&entity.getParts()!=null)
				for(Entity entityPart : entity.getParts())
					collisions.add(entityPart.getCollisionBoundingBox());
			else
				collisions.add(entity.getEntityBoundingBox());

		//Sort from highest to lowest Y
		collisions.sort((bb1, bb2) -> Double.compare(bb2.maxY, bb1.maxY));

		//Process collisions
		for(AxisAlignedBB collisionBoundingBox : collisions)
		{
			double obstacleTop = collisionBoundingBox.maxY;
			double heightDifference = obstacleTop-wheelWorldPos.y;

			if(heightDifference > 0&&heightDifference <= this.maxVerticalForce*2) //Scale with force capability
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
		double stability = 1.0-(forceImbalance/maxVerticalForce)*0.7;

		//Additional grip reduction when climbing high
		if(verticalForceBalance > 0&&currentClimbHeight > maxVerticalForce)
			stability *= 0.8;

		//Factor in torque value for dynamic grip adjustment
		gripFactor = MathHelper.clamp(Math.max(0.3, stability)*(torqueValue/20), 0.3, 1);
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
		//Get wheel world-space bounding box
		AxisAlignedBB bb = aabb.offset(this.posX, this.posY, this.posZ);

		//Expand downward by a small epsilon to catch "almost touching" situations
		final double epsilon = 0.05;
		AxisAlignedBB probeBB = bb.offset(0, -epsilon, 0);

		//Query collisions with blocks only (exactly like move logic)
		List<AxisAlignedBB> collisions = world.getCollisionBoxes(null, probeBB);

		return !collisions.isEmpty();
	}

	/**
	 * Gets the wheel's forward direction vector
	 */
	private Vec3d getForwardVector()
	{
		double wheelAngle = Math.toRadians(MathHelper.wrapDegrees(parentExt.rotationYaw+this.steeringAngle));
		return new Vec3d(-Math.sin(wheelAngle), 0, Math.cos(wheelAngle));
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
		//Store previous value for interpolation
		this.lastSuspensionCompression = this.suspensionCompression;

		//Base compression based on vertical forces
		double forceCompression = Math.abs(verticalForceBalance)*0.1;

		//Additional compression when grounded
		if(lastVerticalForces.isGrounded)
			forceCompression += 0.2;

		//Smoothly transition to new compression value
		double smoothing = 0.4;
		this.suspensionCompression += (forceCompression-this.suspensionCompression)*smoothing;

		//Clamp to valid range
		this.suspensionCompression = MathHelper.clamp(this.suspensionCompression, 0.0, maxSuspensionCompression);
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
