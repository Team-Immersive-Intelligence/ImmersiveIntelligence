package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
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
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel.WheelForces;
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
	@SyncNBT
	public VehicleDurability durabilityMain;
	@SyncNBT
	public Vec3d velocity = Vec3d.ZERO;
	@SyncNBT(events = {SyncEvents.ENTITY_VEHICLE_CONTROLS, SyncEvents.ENTITY_PASSENGER, SyncEvents.ENTITY_COLLISION})
	public float rotationRoll = 0;
	@SyncNBT
	public double angularVelocity = 0.0;

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
		if(!world.isRemote&&durabilityMain.isDead())
		{
			setDead();
			return;
		}

		onVehicleUpdate();

		//Handle movement and update parts based on result
		handleMovement();
		updateParts();
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
		this.prevRotationYaw = this.rotationYaw;

		// Calculate realistic lateral friction based on mass and wheel count
		double maxLateralForce = calculateRealisticLateralFriction(mass);
		double torque = 0;
		int groundedWheels = 0;

		//Simulate individual wheels and gather results
		for(EntityVehicleWheel<T> wheel : this.wheels)
		{
			//Simulate suspension compression for rendering
			wheel.simulateSuspensionCompression();
			//Simulate wheel physics
			WheelForces forces = wheel.calculateForces(maxLateralForce);
			if(forces.isGrounded)
			{
				this.velocity = this.velocity.add(forces.force);
				torque += forces.torque;
				groundedWheels++;
			}
		}

		// Only apply forces if we have ground contact
		if(groundedWheels > 0)
		{
			this.velocity = new Vec3d(this.velocity.x, 0, this.velocity.z);
			// Apply rolling resistance when grounded
			applyRollingResistance(groundedWheels);
		}
		//Apply gravity if not grounded
		else
			this.velocity = this.velocity.subtract(0, 0.02, 0);

		// Apply realistic air drag
		applyAirDrag();

		// Apply enhanced damping based on vehicle state
		applyRealisticDamping(groundedWheels > 0);

		this.angularVelocity += torque;

		// Enhanced angular damping based on ground contact
		double angularDamping = groundedWheels > 0?
				blueprint.angularDamping()*0.7: // More damping when grounded
				blueprint.angularDamping()*0.9;  // Less damping in air
		this.angularVelocity *= angularDamping;

		//Update rotation
		this.rotationYaw = (float)MathHelper.wrapDegrees(this.rotationYaw+Math.toDegrees(this.angularVelocity));

		// Use velocity-based thresholds for zeroing
		double velocityThreshold = 0.001*mass; // Scale with mass
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

		this.velocityChanged = true;
		this.posX += motionX;
		this.posY += motionY;
		this.posZ += motionZ;
	}

	/**
	 * Calculates realistic lateral friction based on mass and wheel configuration
	 */
	private double calculateRealisticLateralFriction(double mass)
	{
		// Base friction coefficient from blueprint
		double baseFriction = Math.max(blueprint.lateralFrictionDrive(), blueprint.lateralFrictionIdler());

		// Adjust for wheel count - more wheels = more total friction
		double wheelFactor = Math.sqrt(this.wheels.length)/2.0;

		// Mass affects how much force is needed to overcome friction
		double massFactor = mass/1000.0; // Normalize to ~1000kg base

		return baseFriction*wheelFactor/(massFactor*this.wheels.length);
	}

	/**
	 * Applies realistic air drag based on vehicle speed and size
	 */
	private void applyAirDrag()
	{
		double speedSquared = this.velocity.x*this.velocity.x+this.velocity.z*this.velocity.z;

		if(speedSquared > 0.001)
		{
			double speed = Math.sqrt(speedSquared);

			// Calculate frontal area from bounding box
			double frontalArea = this.width*this.height*blueprint.frontalAreaFactor();

			// Drag force = 0.5 * density * velocity² * drag coefficient * area
			double dragForce = 0.5*1.225*speedSquared*blueprint.airDragCoefficient()*frontalArea;

			// Convert to acceleration (F = ma -> a = F/m)
			double dragDeceleration = dragForce/this.blueprint.mass();

			// Scale by tick time (1/20th of second)
			dragDeceleration *= 0.05;

			// Apply drag opposite to velocity direction
			if(speed > 0)
			{
				this.velocity.subtract(
						(this.velocity.x/speed)*dragDeceleration,
						0,
						(this.velocity.z/speed)*dragDeceleration
				);
			}
		}
	}

	/**
	 * Applies rolling resistance when vehicle is on ground
	 */
	private void applyRollingResistance(int groundedWheels)
	{
		double speed = this.velocity.x*this.velocity.x+this.velocity.z*this.velocity.z;
		if(speed > 0.01)
		{
			// Rolling resistance from blueprint
			double rollingResistance = blueprint.rollingResistance();

			// More wheels = more total rolling resistance
			double wheelFactor = groundedWheels/(double)this.wheels.length;

			double resistance = rollingResistance*wheelFactor*this.blueprint.mass()*0.98; // gravity
			resistance *= 0.05; // Scale for tick time

			// Apply resistance opposite to velocity direction
			this.velocity.subtract(
					(this.velocity.x/Math.sqrt(speed))*resistance,
					0,
					(this.velocity.z/Math.sqrt(speed))*resistance
			);
		}
	}

	/**
	 * Applies realistic damping based on ground contact and speed
	 */
	private void applyRealisticDamping(boolean isGrounded)
	{
		double speed = this.velocity.x*this.velocity.x+this.velocity.z*this.velocity.z;

		if(isGrounded)
		{
			// When grounded, use smaller damping for low speeds, normal for high speeds
			double speedFactor = Math.min(speed/0.5, 1.0); // 0.5 m/s threshold
			double effectiveDamping = blueprint.linearDamping()*(0.95+0.05*speedFactor);

			this.velocity = new Vec3d(this.velocity.x*effectiveDamping, this.velocity.y, this.velocity.z*effectiveDamping);
		}
		else
		{
			// In air, use minimal damping
			this.velocity = new Vec3d(this.velocity.x*0.995, this.velocity.y, this.velocity.z*0.995);
		}
	}

	/**
	 * Handles collision detection and response for all vehicle parts.
	 * Keeps the existing Y-axis collision handling while adapting X-Z collisions.
	 */
	private Vec3d handleCollisions(Vec3d currentPos, Vec3d attemptedMove)
	{
		Vec3d vecX = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-prevRotationYaw)), prevRotationPitch);
		Vec3d vecZ = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-prevRotationYaw-90)), prevRotationPitch);
		Vec3d adjustedMove = attemptedMove;

		//Check each part for collisions
		for(EntityVehiclePart<T> part : partArray)
		{
			AxisAlignedBB partCurrentBB = part.aabb.offset(currentPos
					.add(vecX.scale(part.offset.x))
					.add(vecZ.scale(part.offset.z))
					.addVector(0, part.offset.y, 0)
			);
			AxisAlignedBB partNextBB = partCurrentBB.offset(attemptedMove.x, attemptedMove.y, attemptedMove.z);

			//Get block collision boxes
			List<AxisAlignedBB> collisions = new ArrayList<>(world.getCollisionBoxes(null, partNextBB));
			for(Entity entity : world.getEntitiesInAABBexcluding(part, partNextBB, e -> e!=this&&e instanceof EntityVehicleBase))
				collisions.add(entity.getEntityBoundingBox());

			if(!collisions.isEmpty())
				for(AxisAlignedBB collisionBox : collisions)
				{
					//Handle Y-axis collisions (vertical) - keep existing behavior
					if(attemptedMove.y!=0)
					{
						double yOffset = collisionBox.calculateYOffset(partCurrentBB, attemptedMove.y);
						if(yOffset!=attemptedMove.y)
						{
							adjustedMove = new Vec3d(adjustedMove.x, yOffset, adjustedMove.z);
							if(attemptedMove.y < 0) this.onGround = true;
						}
					}

					//Handle X-axis collisions with reduced bounce
					if(attemptedMove.x!=0)
					{
						double xOffset = collisionBox.calculateXOffset(partCurrentBB, attemptedMove.x);
						if(xOffset!=attemptedMove.x)
							adjustedMove = new Vec3d(xOffset, adjustedMove.y, adjustedMove.z);
					}

					//Handle Z-axis collisions with reduced bounce
					if(attemptedMove.z!=0)
					{
						double zOffset = collisionBox.calculateZOffset(partCurrentBB, attemptedMove.z);
						if(zOffset!=attemptedMove.z)
							adjustedMove = new Vec3d(adjustedMove.x, adjustedMove.y, zOffset);
					}
				}

			if(part instanceof EntityVehicleWheel)
				((EntityVehicleWheel<T>)part).addWheelTraverse((float)adjustedMove.lengthSquared());
		}

		return currentPos.add(adjustedMove);
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
		Vec3d vecX = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-rotationYaw)), rotationPitch);
		Vec3d vecZ = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-rotationYaw-90)), rotationPitch);

		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			//Transform offset using the rotated vectors
			Vec3d offsetX = vecX.scale(part.offset.x);
			Vec3d offsetZ = vecZ.scale(part.offset.z);
			Vec3d newPos = offsetX.add(offsetZ).addVector(posX, posY, posZ);
			float yawAngle = this.rotationYaw;
			if(part instanceof EntityVehicleWheel)
			{
				EntityVehicleWheel<T> wheel = (EntityVehicleWheel<T>)part;
				yawAngle = wheel.rotationYaw;
				wheel.addWheelTraverse((float)part.getPositionVector().distanceTo(newPos));
			}
			part.setLocationAndAngles(newPos.x, posY+part.offset.y, newPos.z, yawAngle, 0);
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
		return false;
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
