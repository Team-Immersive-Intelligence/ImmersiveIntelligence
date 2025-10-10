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
	//--- Physics Constants ---//
	private static final StyleConstraints DEFAULT_STYLE_CONSTRAINTS = new StyleConstraints("steel",
			true, Sets.newHashSet("steel"), Collections.emptySet());
	//--- Parts ---//
	private AxisAlignedBB AABB;
	protected EntityVehiclePart<T>[] partArray;
	protected EntityVehicleWheel<T>[] wheels;

	//--- Systems ---//
	@SyncNBT(events = SyncEvents.TILE_UPGRADES_MODIFIED)
	protected StyleCustomization style;
	@SyncNBT(events = SyncEvents.TILE_UPGRADES_MODIFIED)
	protected UpgradeManager<T> upgradeManager;
	protected VehicleBlueprint blueprint;
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
		}

		//Convert wheels list to array
		//noinspection unchecked
		this.wheels = wheelsList.toArray(new EntityVehicleWheel[0]);

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
		//Clamp and normalize lateral friction
		double maxLateralForce = 1.0/(this.wheels.length*mass/2);
		double torque = 0;
		int groundedWheels = 0;

		//this.motionX = 0;
		//this.motionY = 0;
		//this.motionZ = 0;

		//Simulate individual wheels and gather results
		for(EntityVehicleWheel<T> wheel : this.wheels)
		{
			WheelForces forces = wheel.calculateForces(maxLateralForce);
			if(forces.isGrounded)
			{
				this.motionX += forces.force.x;
				this.motionZ += forces.force.z;
				torque += forces.torque;
				groundedWheels++;
			}
		}

		// Only apply forces if we have ground contact
		if(groundedWheels > 0)
		{
			this.motionY = 0;
		}
		//Apply gravity if not grounded
		else
			this.motionY -= 0.08;

		//Apply damping
		this.motionX *= blueprint.linearDamping();
		this.motionZ *= blueprint.linearDamping();
		this.angularVelocity += torque;
		this.angularVelocity *= blueprint.angularDamping();

		//Update rotation
		this.rotationYaw = (float)MathHelper.wrapDegrees(this.rotationYaw+Math.toDegrees(this.angularVelocity));

		//Zero tiny velocities
		if(Math.abs(this.motionX) < 1e-4)
			this.motionX = 0;
		if(Math.abs(this.motionZ) < 1e-4)
			this.motionZ = 0;
		if(Math.abs(this.angularVelocity) < 1e-4)
			this.angularVelocity = 0;

		//Handle collisions and get adjusted position
		Vec3d currentPos = new Vec3d(posX, posY, posZ);
		this.prevPosX = currentPos.x;
		this.prevPosY = currentPos.y;
		this.prevPosZ = currentPos.z;

		//Vec3d nextPos = handleCollisions(currentPos, new Vec3d(motionX, motionY, motionZ));
		//this.motionX = nextPos.x-currentPos.x;
		//this.motionY = nextPos.y-currentPos.y;
		//this.motionZ = nextPos.z-currentPos.z;
		this.velocityChanged = true;
		this.posX += motionX;
		this.posY += motionY;
		this.posZ += motionZ;
	}

	/**
	 * Handles collision detection and response for all vehicle parts.
	 * Keeps the existing Y-axis collision handling while adapting X-Z collisions.
	 */
	private Vec3d handleCollisions(Vec3d currentPos, Vec3d attemptedMove)
	{
		Vec3d adjustedMove = attemptedMove;
		//Check each part for collisions
		for(EntityVehiclePart<T> part : partArray)
		{

			AxisAlignedBB partCurrentBB = part.aabb.offset(part.posX, part.posY, part.posZ);
			AxisAlignedBB partNextBB = partCurrentBB.offset(attemptedMove.x, attemptedMove.y, attemptedMove.z);

			//Get block collision boxes
			List<AxisAlignedBB> collisions = new ArrayList<>(world.getCollisionBoxes(part, partNextBB));
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
		}

		return currentPos.add(adjustedMove);
	}

	@Override
	public double getAngularVelocity()
	{
		return angularVelocity;
	}

	@Override
	public VehicleBlueprint getVehicleBlueprint()
	{
		return blueprint;
	}

	//--- Part Handling ---//

	@Override
	public void updateParts()
	{
		//Create vectors with proper rotation accounting
		Vec3d vecX = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-rotationYaw)), rotationPitch);
		Vec3d vecZ = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-rotationYaw-90+rotationRoll)), rotationPitch);

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
