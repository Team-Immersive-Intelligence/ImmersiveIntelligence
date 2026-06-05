package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints.PaintStyleConstraint;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.*;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.MissingAnnotationException;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Enhanced base class for vehicle entities with comprehensive pitch/roll simulation
 * and wheel-based climbing system following "needs and wants" philosophy.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.09.2025
 */
public abstract class EntityVehicleBase<T extends EntityVehicleBase<T>> extends Entity implements ISyncNBTEntity<T>, IVehicleMultiPart<T>,
		IEntitySpecialRepairable, IManagedUpgradableDevice<T>, IStyleCustomizable, IIEInventory
{
	//--- Constants ---//
	private static final StyleConstraints DEFAULT_STYLE_CONSTRAINTS = new StyleConstraints("steel",
			PaintStyleConstraint.PAINTS_COLOR_ONLY, Sets.newHashSet("steel"), Collections.emptySet());

	//--- Parts ---//
	private AxisAlignedBB AABB;
	protected VehicleBlueprint blueprint;
	protected IVehicleComponent[] components;
	protected EntityVehiclePart<T>[] partArray;
	protected EntityVehicleWheel<T>[] wheels;
	protected VehicleWheelGroup<T>[] wheelGroups;
	protected VehicleSegment<T> rootSegment;
	protected VehicleSegment<T>[] segments;
	protected SeatInfo<?>[] seats;

	//--- Systems ---//
	@SyncNBT(events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public StyleCustomization style;
	@SyncNBT(events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<T> upgradeManager;

	//--- Motion & Orientation ---//
	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public SyncedDurability durabilityMain;
	@SyncNBT
	public Vec3d velocity = Vec3d.ZERO;
	@SyncNBT(events = {SyncEvents.ENTITY_VEHICLE_CONTROLS, SyncEvents.ENTITY_PASSENGER, SyncEvents.ENTITY_COLLISION})
	public float prevRotationRoll = 0, rotationRoll = 0;
	@SyncNBT
	public double angularVelocity = 0.0;
	@SyncNBT
	public double angularVelocityPitch = 0.0, angularVelocityRoll = 0.0;
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
		internalVehicleInit();
	}

	protected final void internalVehicleInit()
	{
		//Initialize part collections
		ArrayList<EntityVehicleWheel<T>> wheelsList = new ArrayList<>();
		ArrayList<SeatInfo<T>> seatsList = new ArrayList<>();

		VehicleBlueprint meta = IIUtils.getAnnotation(VehicleBlueprint.class, this);
		if(meta==null)
			throw new MissingAnnotationException(this, VehicleBlueprint.class);
		this.blueprint = meta;

		//Set main durability
		this.durabilityMain = new SyncedDurability(blueprint.baseDurability(), blueprint.baseArmor());
		//noinspection unchecked
		this.upgradeManager = ((UpgradeManager<T>)new UpgradeManager<>(this));
		this.style = new StyleCustomization(getVehicleStyleConstraints());
		this.style.withColor(IIColor.getPaintSystemColor(Utils.RAND.nextInt(64)));

		//Root segment is the hull. Subclasses may add independently rotated segments such as turrets.
		//noinspection unchecked
		this.rootSegment = new VehicleSegment<>((T)this, "hull", Vec3d.ZERO);
		this.segments = vehicleSegments();
		if(this.segments==null||this.segments.length==0)
			this.segments = defaultVehicleSegments();

		//Calculate vehicle size and collect wheels. This remains an enclosing AABB used by Minecraft's broad phase;
		//individual parts perform precise OBB checks.
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

		//Initialize vehicle-specific parts
		this.components = new IVehicleComponent[0];
		this.partArray = this.vehicleInit();

		for(EntityVehiclePart<T> part : partArray)
		{
			minX = Math.min(minX, part.offset.x+part.aabb.minX);
			minY = Math.min(minY, part.offset.y+part.aabb.minY);
			minZ = Math.min(minZ, part.offset.z+part.aabb.minZ);
			maxX = Math.max(maxX, part.offset.x+part.aabb.maxX);
			maxY = Math.max(maxY, part.offset.y+part.aabb.maxY);
			maxZ = Math.max(maxZ, part.offset.z+part.aabb.maxZ);

			//Collect wheels
			if(part instanceof EntityVehicleWheel)
				wheelsList.add(((EntityVehicleWheel<T>)part));

			//Collect seats
			if(part.assignedSeat!=null)
				//noinspection unchecked
				seatsList.add((SeatInfo<T>)part.assignedSeat);
		}

		for(EntityVehiclePart<T> part : partArray)
			if(part.getSegment()==null)
				part.withSegment(rootSegment);

		//Simulate drive wheels first
		wheelsList.sort((o1, o2) -> Boolean.compare(!o1.getType().isDriven(), !o2.getType().isDriven()));

		//Convert wheel and seat lists to arrays
		//noinspection unchecked
		this.wheels = wheelsList.toArray(new EntityVehicleWheel[0]);
		this.wheelGroups = vehicleWheelGroups();
		if(this.wheelGroups==null)
			this.wheelGroups = new VehicleWheelGroup[0];
		assignDefaultWheelGroups();
		this.seats = seatsList.toArray(new SeatInfo[0]);

		//Set vehicle AABB size
		setSize((float)(Math.max(maxX-minX, maxZ-minZ))*MathHelper.SQRT_2, (float)(maxY-minY)+1f);

		//Initialize part positions
		updateParts();
	}

	@Override
	protected final void entityInit()
	{
		//Do not initialize here, as it is called before the constructor
	}

	/**
	 * Initializes the vehicle with hitboxes and parts.
	 * Implemented by subclasses to define vehicle-specific parts.
	 */
	@SuppressWarnings({"unchecked", "RedundantSuppression"})
	protected abstract EntityVehiclePart<T>[] vehicleInit();

	/**
	 * Declares independently rotated vehicle segments. The default single segment is the hull.
	 * Subclasses should return extra segments here before assigning parts through
	 * {@link EntityVehiclePart#withSegment(VehicleSegment)}.
	 */
	@SuppressWarnings("unchecked")
	protected VehicleSegment<T>[] vehicleSegments()
	{
		return defaultVehicleSegments();
	}

	@SuppressWarnings("unchecked")
	private VehicleSegment<T>[] defaultVehicleSegments()
	{
		return new VehicleSegment[]{rootSegment};
	}

	/**
	 * Declares logical wheel receivers: track, axle, or single-wheel groups.
	 * Transmissions should target these groups for easier balancing. Ungrouped wheels receive
	 * automatic single-wheel groups so old vehicles keep working.
	 */
	@SuppressWarnings("unchecked")
	protected VehicleWheelGroup<T>[] vehicleWheelGroups()
	{
		return new VehicleWheelGroup[0];
	}

	@SuppressWarnings("unchecked")
	private void assignDefaultWheelGroups()
	{
		ArrayList<VehicleWheelGroup<T>> groups = new ArrayList<>();
		Collections.addAll(groups, this.wheelGroups);
		for(EntityVehicleWheel<T> wheel : this.wheels)
			if(wheel.getWheelGroup()==null)
				groups.add(new VehicleWheelGroup<>((T)this, wheel.partName).withMode(VehicleWheelGroup.Mode.SINGLE).withWheels(wheel));
		this.wheelGroups = groups.toArray(new VehicleWheelGroup[0]);
	}

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
		for(VehicleSegment<T> segment : segments)
			segment.onUpdate();
		for(VehicleWheelGroup<T> group : wheelGroups)
			group.onUpdate();

		//Handle movement and update parts based on result
		handleMovement();
		//Calculate orientation based on wheel force differences
		calculateForceBasedOrientation();
		//Fix vehicle being stuck in blocks and prevent it from levitating
		fixPositionErrors();
		//Update parts
		updateParts();

		//Send an update to the clients after a collision, client has some errors in collision handling
		//hasCollidedBefore = true;
		if(!this.world.isRemote&&!hasCollidedBefore&&collidedHorizontally)
			sendServerPositionMotionUpdate();
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
		double yawTorque = 0;
		double pitchTorque = 0;
		double rollTorque = 0;
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
				yawTorque += forces.torque;
				pitchTorque += forces.pitchTorque;
				rollTorque += forces.rollTorque;
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

		//Update all rotation axes through the same OBB transform safety path. There are no AABB rotation checks here.
		this.angularVelocity += yawTorque;
		this.angularVelocityPitch += pitchTorque;
		this.angularVelocityRoll += rollTorque;
		applyAngularMotion();

		//Use velocity-based thresholds for zeroing
		double velocityThreshold = 0.001*mass; //Scale with mass
		double angularThreshold = 0.0001*mass;

		if(Math.abs(this.velocity.x) < velocityThreshold)
			this.velocity = new Vec3d(0, this.velocity.y, this.velocity.z);
		if(Math.abs(this.velocity.z) < velocityThreshold)
			this.velocity = new Vec3d(this.velocity.x, this.velocity.y, 0);
		if(Math.abs(this.angularVelocity) < angularThreshold)
			this.angularVelocity = 0;
		if(Math.abs(this.angularVelocityPitch) < angularThreshold)
			this.angularVelocityPitch = 0;
		if(Math.abs(this.angularVelocityRoll) < angularThreshold)
			this.angularVelocityRoll = 0;

		//Handle collisions and get adjusted position
		Vec3d currentPos = new Vec3d(posX, posY, posZ);
		this.prevPosX = currentPos.x;
		this.prevPosY = currentPos.y;
		this.prevPosZ = currentPos.z;

		Vec3d nextPos = handleCollisions(currentPos, velocity);
		this.motionX = nextPos.x-currentPos.x;
		this.motionY = nextPos.y-currentPos.y;
		this.motionZ = nextPos.z-currentPos.z;

		//Do not keep accelerating into a face after the OBB clipper has removed that component.
		if(Math.abs(this.motionX-this.velocity.x) > 1.0E-5)
			this.velocity = new Vec3d(0, this.velocity.y, this.velocity.z);
		if(Math.abs(this.motionY-this.velocity.y) > 1.0E-5)
			this.velocity = new Vec3d(this.velocity.x, 0, this.velocity.z);
		if(Math.abs(this.motionZ-this.velocity.z) > 1.0E-5)
			this.velocity = new Vec3d(this.velocity.x, this.velocity.y, 0);

		this.posX += motionX;
		this.posY += motionY;
		this.posZ += motionZ;
		setEntityBoundingBox(getEntityBoundingBox());
	}

	/**
	 * Calculates pitch and roll targets from wheel vertical force differences. The resulting rotation is
	 * applied through OBB-only transform checks, so vehicles may rotate freely on all axes while still
	 * respecting Minecraft terrain and other vehicle OBBs.
	 */
	private void calculateForceBasedOrientation()
	{
		if(wheels.length < 2)
		{
			targetPitch = 0;
			targetRoll = 0;
			return;
		}

		double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
		for(EntityVehicleWheel<T> wheel : wheels)
		{
			minX = Math.min(minX, wheel.offset.x);
			maxX = Math.max(maxX, wheel.offset.x);
			minZ = Math.min(minZ, wheel.offset.z);
			maxZ = Math.max(maxZ, wheel.offset.z);
		}

		// Vehicle local convention: +X right, -X left, -Z front, +Z rear.
		double width = Math.max(0.001, maxX-minX);
		double length = Math.max(0.001, maxZ-minZ);
		double frontForce = 0, rearForce = 0, leftForce = 0, rightForce = 0;
		int frontWheels = 0, rearWheels = 0, leftWheels = 0, rightWheels = 0;

		for(EntityVehicleWheel<T> wheel : wheels)
		{
			double verticalForce = wheel.getVerticalForceBalance();
			if(wheel.offset.z <= 0)
			{
				frontForce += verticalForce;
				frontWheels++;
			}
			if(wheel.offset.z >= 0)
			{
				rearForce += verticalForce;
				rearWheels++;
			}
			if(wheel.offset.x <= 0)
			{
				leftForce += verticalForce;
				leftWheels++;
			}
			if(wheel.offset.x >= 0)
			{
				rightForce += verticalForce;
				rightWheels++;
			}
		}

		if(frontWheels > 0&&rearWheels > 0)
		{
			frontForce /= frontWheels;
			rearForce /= rearWheels;
			double avgForce = Math.max(1.0, Math.abs((frontForce+rearForce)*0.5));
			targetPitch = (float)Math.toDegrees(Math.atan2((frontForce-rearForce)/avgForce*length*0.5, length))*70;
		}
		if(leftWheels > 0&&rightWheels > 0)
		{
			leftForce /= leftWheels;
			rightForce /= rightWheels;
			double avgForce = Math.max(1.0, Math.abs((leftForce+rightForce)*0.5));
			targetRoll = (float)Math.toDegrees(Math.atan2((leftForce-rightForce)/avgForce*width*0.5, width))*70;
		}

		float maxIncrement = (float)blueprint.rotationStepLimit();
		float pitchStep = MathHelper.clamp(targetPitch-this.rotationPitch, -maxIncrement, maxIncrement);
		float rollStep = MathHelper.clamp(targetRoll-this.rotationRoll, -maxIncrement, maxIncrement);
		applySafeRotationDelta(0, pitchStep, rollStep);
	}

	private void applyAngularMotion()
	{
		double scale = blueprint.angularVelocityToDegrees();
		float yawStep = (float)MathHelper.clamp(Math.toDegrees(this.angularVelocity)*scale, -blueprint.rotationStepLimit(), blueprint.rotationStepLimit());
		float pitchStep = (float)MathHelper.clamp(Math.toDegrees(this.angularVelocityPitch)*scale, -blueprint.rotationStepLimit(), blueprint.rotationStepLimit());
		float rollStep = (float)MathHelper.clamp(Math.toDegrees(this.angularVelocityRoll)*scale, -blueprint.rotationStepLimit(), blueprint.rotationStepLimit());
		if(!applySafeRotationDelta(yawStep, pitchStep, rollStep))
		{
			this.angularVelocity *= 0.6;
			this.angularVelocityPitch *= 0.6;
			this.angularVelocityRoll *= 0.6;
		}
	}

	private boolean applySafeRotationDelta(float yawDelta, float pitchDelta, float rollDelta)
	{
		boolean changed = false;
		float newYaw = MathHelper.wrapDegrees(this.rotationYaw+yawDelta);
		if(Math.abs(yawDelta) < 1.0E-5||isTransformSafe(null, newYaw, null, null))
		{
			this.rotationYaw = newYaw;
			changed |= Math.abs(yawDelta) >= 1.0E-5;
		}
		else
			this.angularVelocity *= 0.6;

		float newPitch = MathHelper.wrapDegrees(this.rotationPitch+pitchDelta);
		if(Math.abs(pitchDelta) < 1.0E-5||isTransformSafe(null, null, newPitch, null))
		{
			this.rotationPitch = newPitch;
			changed |= Math.abs(pitchDelta) >= 1.0E-5;
		}
		else
			this.angularVelocityPitch *= 0.6;

		float newRoll = MathHelper.wrapDegrees(this.rotationRoll+rollDelta);
		if(Math.abs(rollDelta) < 1.0E-5||isTransformSafe(null, null, null, newRoll))
		{
			this.rotationRoll = newRoll;
			changed |= Math.abs(rollDelta) >= 1.0E-5;
		}
		else
			this.angularVelocityRoll *= 0.6;
		return changed;
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
	 * <p>
	 * Minecraft's AABB collision offsets do not understand rotated boxes, so movement is clipped by testing
	 * the vehicle's OBB parts at candidate positions. The AABB is still used as a broad-phase query against
	 * blocks and entities; actual acceptance is decided by OBB-vs-AABB or OBB-vs-OBB checks.
	 */
	private Vec3d handleCollisions(Vec3d currentPos, Vec3d attemptedMove)
	{
		this.collided = this.collidedHorizontally = this.collidedVertically = false;
		this.onGround = false;

		if(attemptedMove.equals(Vec3d.ZERO))
			return currentPos;

		Vec3d adjustedMove = clipMovementToWorld(currentPos, attemptedMove);
		Vec3d steppedMove = tryStepMovement(currentPos, attemptedMove, adjustedMove);
		if(horizontalLengthSquared(steppedMove) > horizontalLengthSquared(adjustedMove)+1.0E-5)
			adjustedMove = steppedMove;

		if(Math.abs(adjustedMove.x-attemptedMove.x) > 1.0E-5||Math.abs(adjustedMove.z-attemptedMove.z) > 1.0E-5)
		{
			this.collided = true;
			this.collidedHorizontally = true;
		}
		if(Math.abs(adjustedMove.y-attemptedMove.y) > 1.0E-5)
		{
			this.collided = true;
			this.collidedVertically = true;
			if(attemptedMove.y < 0)
				this.onGround = true;
		}

		//Update wheel traverse after final movement choice.
		for(EntityVehicleWheel<T> wheel : this.wheels)
			wheel.addWheelTraverse((float)adjustedMove.lengthVector());

		return currentPos.add(adjustedMove);
	}

	private Vec3d clipMovementToWorld(Vec3d currentPos, Vec3d attemptedMove)
	{
		Vec3d move = Vec3d.ZERO;
		move = move.add(getMoveComponent(1, clipMoveAxis(currentPos, move, attemptedMove.y, 1)));
		move = move.add(getMoveComponent(0, clipMoveAxis(currentPos, move, attemptedMove.x, 0)));
		move = move.add(getMoveComponent(2, clipMoveAxis(currentPos, move, attemptedMove.z, 2)));
		return move;
	}

	/**
	 * Minecraft terrain is blocky enough that pure smooth-world collision feels wrong. If the horizontal move is
	 * blocked, try the classic vehicle step: lift, move horizontally, then settle down onto the new surface.
	 */
	private Vec3d tryStepMovement(Vec3d currentPos, Vec3d attemptedMove, Vec3d clippedMove)
	{
		if(Math.abs(attemptedMove.x) < 1.0E-5&&Math.abs(attemptedMove.z) < 1.0E-5)
			return clippedMove;
		if(horizontalLengthSquared(clippedMove) >= horizontalLengthSquared(attemptedMove)*0.85)
			return clippedMove;
		if(!this.onGround&&!hasGroundedWheel())
			return clippedMove;

		double stepHeight = getStepHeightForMove();
		if(stepHeight <= 0)
			return clippedMove;

		double up = clipMoveAxis(currentPos, Vec3d.ZERO, stepHeight, 1);
		if(up < 0.0625)
			return clippedMove;

		Vec3d raised = new Vec3d(0, up, 0);
		double x = clipMoveAxis(currentPos, raised, attemptedMove.x, 0);
		Vec3d raisedX = raised.addVector(x, 0, 0);
		double z = clipMoveAxis(currentPos, raisedX, attemptedMove.z, 2);
		Vec3d horizontal = raisedX.addVector(0, 0, z);

		//Settle onto the new block face instead of hovering at the full step height.
		double down = clipMoveAxis(currentPos, horizontal, -up-0.0625, 1);
		return horizontal.addVector(0, down, 0);
	}

	private double clipMoveAxis(Vec3d currentPos, Vec3d alreadyAcceptedMove, double delta, int axis)
	{
		if(Math.abs(delta) < 1.0E-7)
			return 0;

		Vec3d start = currentPos.add(alreadyAcceptedMove);
		Vec3d fullMove = getMoveComponent(axis, delta);
		if(!wouldCollideAt(start.add(fullMove), this.rotationYaw, this.rotationPitch, this.rotationRoll))
			return delta;

		if(wouldCollideAt(start, this.rotationYaw, this.rotationPitch, this.rotationRoll))
			return 0;

		double safe = 0.0;
		double blocked = 1.0;
		for(int i = 0; i < 10; i++)
		{
			double mid = (safe+blocked)*0.5;
			Vec3d candidateMove = getMoveComponent(axis, delta*mid);
			if(wouldCollideAt(start.add(candidateMove), this.rotationYaw, this.rotationPitch, this.rotationRoll))
				blocked = mid;
			else
				safe = mid;
		}

		double clipped = delta*safe;
		double clearance = Math.signum(delta)*0.001;
		if(Math.abs(clipped) > Math.abs(clearance))
			clipped -= clearance;
		return clipped;
	}

	private Vec3d getMoveComponent(int axis, double amount)
	{
		switch(axis)
		{
			case 0:
				return new Vec3d(amount, 0, 0);
			case 1:
				return new Vec3d(0, amount, 0);
			case 2:
				return new Vec3d(0, 0, amount);
			default:
				return Vec3d.ZERO;
		}
	}

	private double horizontalLengthSquared(Vec3d vec)
	{
		return vec.x*vec.x+vec.z*vec.z;
	}

	private double getStepHeightForMove()
	{
		double max = blueprint.obstacleClimbHeight();
		for(EntityVehicleWheel<T> wheel : wheels)
			max = Math.max(max, Math.min(1.25, wheel.getMaxVerticalForce()*2.0));
		return max;
	}

	private boolean hasGroundedWheel()
	{
		for(EntityVehicleWheel<T> wheel : wheels)
			if(wheel.getLastVerticalForces().isGrounded)
				return true;
		return false;
	}

	private boolean wouldCollideAt(Vec3d position, float yaw, float pitch, float roll)
	{
		for(EntityVehiclePart<T> part : partArray)
			if(part.isCollisionEnabled()&&partCollidesAt(part, position, yaw, pitch, roll))
				return true;
		return false;
	}

	private boolean partCollidesAt(EntityVehiclePart<T> part, Vec3d position, float yaw, float pitch, float roll)
	{
		VehicleOBB obb = part.getCollisionOBB(position, yaw, pitch, roll);
		AxisAlignedBB broad = obb.getEnclosingAABB().grow(0.001);

		for(AxisAlignedBB blockBox : world.getCollisionBoxes(null, broad))
			if(obb.intersects(blockBox))
				return true;

		for(Entity entity : world.getEntitiesInAABBexcluding(this, broad, candidate -> !isIgnoredCollisionEntity(candidate)))
		{
			if(entity instanceof EntityVehiclePart)
			{
				if(obb.intersects(((EntityVehiclePart<?>)entity).getCollisionOBB()))
					return true;
			}
			else if(entity instanceof EntityVehicleBase)
			{
				EntityVehicleBase<?> other = (EntityVehicleBase<?>)entity;
				for(EntityVehiclePart<?> otherPart : other.getVehicleParts())
					if(otherPart.isCollisionEnabled()&&obb.intersects(otherPart.getCollisionOBB()))
						return true;
			}
			else
			{
				// Ordinary entities are not terrain. They are shoved out of the vehicle's way after movement
				// instead of being allowed to hold a tracked vehicle in mid-air.
			}
		}
		return false;
	}

	private boolean isIgnoredCollisionEntity(Entity entity)
	{
		if(entity==null||entity==this)
			return true;
		if(entity instanceof EntityVehicleSeat)
			return true;
		if(this.getRecursivePassengers().contains(entity))
			return true;
		if(partArray!=null)
			for(EntityVehiclePart<T> part : partArray)
				if(entity==part)
					return true;
		return !entity.canBeCollidedWith()&&!(entity instanceof EntityVehicleBase)&&!(entity instanceof EntityVehiclePart);
	}

	/**
	 * Tests whether the vehicle at a given transform would collide with the world.
	 *
	 * @param position The absolute vehicle position to test at. If null, current position is used.
	 * @param yaw      The yaw to test. If null, current yaw is used.
	 * @param pitch    The pitch to test. If null, current pitch is used.
	 * @param roll     The roll to test. If null, current roll is used.
	 */
	@ParametersAreNullableByDefault
	private boolean isTransformSafe(Vec3d position, Float yaw, Float pitch, Float roll)
	{
		position = position==null?this.getPositionVector(): position;
		yaw = yaw==null?this.rotationYaw: yaw;
		pitch = pitch==null?this.rotationPitch: pitch;
		roll = roll==null?this.rotationRoll: roll;
		return !wouldCollideAt(position, yaw, pitch, roll);
	}

	/**
	 * Attempts to resolve very small penetrations (< 0.125 blocks) by nudging the entire vehicle out of block intersections.
	 * Performs a ground-snap to resolve cases where downward motion was clipped early, leaving the vehicle hovering fractions of a block above the ground.
	 */
	public void fixPositionErrors()
	{
		final double INSIDE_THRESHOLD = 0.125;
		final double SNAP_MAX = 0.25;
		final double PROBE = 0.3;

		Vec3d accumulatedCorrection = Vec3d.ZERO;
		boolean anyWheelOnGround = false;
		boolean anyWheelClimbing = false;

		//Phase 1: Fix tiny OBB penetrations. Larger intersections are left to the main movement clipper,
		//otherwise the vehicle can jump violently when wedged between block faces.
		for(EntityVehiclePart<T> part : partArray)
		{
			if(part instanceof EntityVehicleWheel)
			{
				EntityVehicleWheel<T> wheel = (EntityVehicleWheel<T>)part;
				if(wheel.getLastVerticalForces().isGrounded)
					anyWheelOnGround = true;
				if(wheel.getVerticalForceBalance() > 0)
					anyWheelClimbing = true;
			}

			if(!part.isCollisionEnabled())
				continue;

			VehicleOBB partOBB = part.getCollisionOBB(this.getPositionVector(), this.rotationYaw, this.rotationPitch, this.rotationRoll);
			AxisAlignedBB broad = partOBB.getEnclosingAABB().grow(0.001);
			for(AxisAlignedBB block : world.getCollisionBoxes(null, broad))
			{
				VehicleOBB.CollisionResult result = partOBB.calculateCollision(block);
				if(result!=null&&result.depth < INSIDE_THRESHOLD)
					accumulatedCorrection = accumulatedCorrection.add(result.getPushOut());
			}
		}

		if(!accumulatedCorrection.equals(Vec3d.ZERO))
		{
			Vec3d finalCorrection = accumulatedCorrection.normalize()
					.scale(Math.min(INSIDE_THRESHOLD*0.5, accumulatedCorrection.lengthVector()));
			Vec3d correctedPosition = this.getPositionVector().add(finalCorrection);
			if(isTransformSafe(correctedPosition, null, null, null))
			{
				this.posX += finalCorrection.x;
				this.posY += finalCorrection.y;
				this.posZ += finalCorrection.z;
			}
		}

		//Phase 2: Ground snapping. Use wheel OBBs so a tilted vehicle settles against block tops naturally.
		if(collidedVertically&&!anyWheelOnGround&&!anyWheelClimbing)
		{
			double minGap = Double.MAX_VALUE;
			boolean hasGroundBelow = false;

			for(EntityVehicleWheel<T> wheel : wheels)
			{
				VehicleOBB wheelOBB = wheel.getCollisionOBB(this.getPositionVector(), this.rotationYaw, this.rotationPitch, this.rotationRoll);
				AxisAlignedBB wheelBox = wheelOBB.getEnclosingAABB();
				AxisAlignedBB probeBB = wheelBox.offset(0, -PROBE, 0);

				for(AxisAlignedBB block : world.getCollisionBoxes(null, probeBB))
				{
					double gap = wheelBox.minY-block.maxY;
					if(gap >= 0&&gap < minGap)
					{
						VehicleOBB snappedWheel = wheelOBB.offset(0, -gap, 0);
						if(snappedWheel.intersects(block))
						{
							minGap = gap;
							hasGroundBelow = true;
						}
					}
				}
			}

			if(hasGroundBelow&&minGap < SNAP_MAX)
			{
				Vec3d correctedPosition = this.getPositionVector().addVector(0, -minGap, 0);
				if(isTransformSafe(correctedPosition, null, null, null))
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

			if(wheel.offset.z < 0) //Front (-Z)
			{
				totalFrontWeight += wheel.getWeightShare();
				frontImbalance += (verticalForce-expectedForce);
				frontWheels++;
			}
			else //Rear (+Z)
			{
				totalRearWeight += wheel.getWeightShare();
				rearImbalance += (verticalForce-expectedForce);
				rearWheels++;
			}

			if(wheel.offset.x > 0) //Right (+X)
			{
				totalRightWeight += wheel.getWeightShare();
				rightImbalance += (verticalForce-expectedForce);
				rightWheels++;
			}
			else //Left (-X)
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
		return VehicleOBB.getForwardVector(MathHelper.wrapDegrees(rotationYaw));
	}

	/**
	 * Gets the right direction vector (+X in vehicle-local space).
	 */
	private Vec3d getRightVector()
	{
		return VehicleOBB.getRightVector(MathHelper.wrapDegrees(rotationYaw));
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

	public VehicleSegment<T> getRootSegment()
	{
		return rootSegment;
	}

	public VehicleSegment<T>[] getVehicleSegments()
	{
		return segments;
	}

	public VehicleWheelGroup<T>[] getWheelGroups()
	{
		return wheelGroups;
	}

	public double getAngularVelocityPitch()
	{
		return angularVelocityPitch;
	}

	public double getAngularVelocityRoll()
	{
		return angularVelocityRoll;
	}

	//--- Part Handling ---//

	@Override
	public void updateParts()
	{
		//Create vectors with proper rotation accounting
		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			//Transform offset using the part segment, then the hull rotation.
			Vec3d newPos = part.getPartWorldPosition(this.getPositionVector(), this.rotationYaw, this.rotationPitch, this.rotationRoll);

			float yawAngle = this.rotationYaw;
			if(part instanceof EntityVehicleWheel)
			{
				EntityVehicleWheel<T> wheel = (EntityVehicleWheel<T>)part;
				yawAngle = this.rotationYaw+wheel.getSteeringAngle();
				wheel.addWheelTraverse((float)part.getPositionVector().distanceTo(newPos));
			}
			part.setLocationAndAngles(newPos.x, newPos.y, newPos.z, yawAngle, 0);
			part.onUpdate();
		}
		setEntityBoundingBox(getEntityBoundingBox());
		pushIntersectingEntities();
	}

	/**
	 * Gives regular Minecraft entities a chance to react to precise vehicle part collisions.
	 * The actual broad-phase still uses AABBs, but the contact test is the part OBB.
	 */
	private void pushIntersectingEntities()
	{
		if(world.isRemote)
			return;

		double speedSq = velocity.x*velocity.x+velocity.z*velocity.z;
		double speed = Math.sqrt(speedSq);
		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			if(!part.isCollisionEnabled())
				continue;
			VehicleOBB obb = part.getCollisionOBB();
			AxisAlignedBB broad = obb.getEnclosingAABB().grow(0.125);
			for(Entity entity : world.getEntitiesInAABBexcluding(this, broad, this::isPushableCollisionEntity))
			{
				AxisAlignedBB entityBox = entity.getEntityBoundingBox();
				if(entityBox==null||!obb.intersects(entityBox))
					continue;

				pushEntityAwayFromPart(entity, obb, speed);
				if(speedSq > blueprint.entityDamageSpeedSq())
				{
					float damage = (float)((speedSq-blueprint.entityDamageSpeedSq())*blueprint.entityDamageScale());
					if(damage > 0.5f)
						entity.attackEntityFrom(IIDamageSources.causeVehicleDamage(this), damage);
				}
			}
		}
	}

	private boolean isPushableCollisionEntity(Entity entity)
	{
		if(isIgnoredCollisionEntity(entity))
			return false;
		return !(entity instanceof EntityVehicleBase)&&!(entity instanceof EntityVehiclePart)&&!(entity instanceof EntityVehicleSeat);
	}

	private void pushEntityAwayFromPart(Entity entity, VehicleOBB obb, double vehicleSpeed)
	{
		AxisAlignedBB box = entity.getEntityBoundingBox();
		if(box==null)
			return;

		Vec3d entityCenter = box.getCenter();
		Vec3d push = new Vec3d(entityCenter.x-obb.center.x, 0, entityCenter.z-obb.center.z);
		if(push.lengthSquared() < 1.0E-5)
			push = new Vec3d(velocity.x, 0, velocity.z);
		if(push.lengthSquared() < 1.0E-5)
			push = VehicleOBB.getForwardVector(this.rotationYaw);
		push = push.normalize();

		double strength = blueprint.entityPushStrength()*(0.08+Math.min(0.8, vehicleSpeed*0.35));
		entity.addVelocity(push.x*strength, Math.min(0.08, strength*0.2), push.z*strength);
		entity.velocityChanged = true;
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
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		return IVehicleMultiPart.super.interactRayTracedPart(player, hand);
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
		return getVehicleBroadphaseBox(this.getPositionVector(), this.rotationYaw, this.rotationPitch, this.rotationRoll);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return getEntityBoundingBox().grow(0.5);
	}

	private AxisAlignedBB getVehicleBroadphaseBox(Vec3d position, float yaw, float pitch, float roll)
	{
		if(AABB==null)
			return super.getEntityBoundingBox();
		if(partArray==null||partArray.length==0)
			return AABB.offset(position.x, position.y, position.z);

		AxisAlignedBB box = null;
		for(EntityVehiclePart<T> part : partArray)
		{
			AxisAlignedBB partBox = part.getCollisionOBB(position, yaw, pitch, roll).getEnclosingAABB();
			box = box==null?partBox: box.union(partBox);
		}
		return box==null?AABB.offset(position.x, position.y, position.z): box;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return isIgnoredCollisionEntity(entityIn)?null: getEntityBoundingBox();
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
	public MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.STEEL;
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

	//--- Capabilities ---//

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			for(IVehicleComponent component : components)
				if(component instanceof IFluidHandler)
					return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			for(IVehicleComponent component : components)
				if(component instanceof IFluidHandler)
					return (T)component;
		return super.getCapability(capability, facing);
	}

	//--- IIEInventory ---//

	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
	}

	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	public int getSlotLimit(int slot)
	{
		return 64;
	}

	public void doGraphicalUpdates(int slot)
	{
		updateEntityForEvent(SyncEvents.ENTITY_INTERACT);
	}
}
