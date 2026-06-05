package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.07.2020
 */
public class EntityVehiclePart<T extends Entity & IVehicleMultiPart<T>> extends MultiPartEntityPart implements IAdvancedTextOverlay, IVehicleComponent, IEntitySpecialRepairable
{
	/**
	 * Offset from center of the vehicle
	 */
	public Vec3d offset;
	/**
	 * Bounding box of this part, moved dynamically
	 */
	public AxisAlignedBB aabb;
	/**
	 * The parent (vehicle) entity
	 */
	public T parentExt;
	/**
	 * The health storage and damage for this part
	 */
	@Nullable
	public SyncedDurability durability;
	@Nullable
	public SeatInfo<?> assignedSeat;
	protected boolean collidable = true;
	@Nullable
	protected VehicleSegment<?> segment;

	public EntityVehiclePart(T parent, String partName, Vec3d offset, AxisAlignedBB aabb)
	{
		super(parent, partName, (float)aabb.getAverageEdgeLength(), (float)Math.abs(aabb.maxY-aabb.minY));
		this.parentExt = parent;
		this.offset = offset;
		this.aabb = normalize(aabb);
	}

	public EntityVehiclePart(T parent, String partName, Vec3d offset, double radius, double height)
	{
		this(parent, partName, offset, new AxisAlignedBB(-radius, -height, -radius, radius, height, radius));
	}

	public EntityVehiclePart(T parent, String partName, Vec3d offset, double radius)
	{
		this(parent, partName, offset, radius, radius);
	}

	public EntityVehiclePart<T> withHitbox(@Nonnull SyncedDurability hitbox)
	{
		return withHitbox(hitbox, true);
	}

	public EntityVehiclePart<T> withHitbox(@Nonnull SyncedDurability hitbox, boolean collidable)
	{
		this.durability = hitbox;
		this.collidable = collidable;
		return this;
	}

	public EntityVehiclePart<T> withSeat(@Nonnull SeatInfo<?> seatInfo)
	{
		this.assignedSeat = seatInfo;
		return this;
	}

	public EntityVehiclePart<T> withSegment(@Nonnull VehicleSegment<?> segment)
	{
		this.segment = segment;
		return this;
	}

	public boolean isCollisionEnabled()
	{
		return collidable;
	}

	@Nullable
	public VehicleSegment<?> getSegment()
	{
		return segment;
	}


	@Override
	public void applyEntityCollision(Entity entityIn)
	{
		//disable collisions for entities of the same multipart
		if(!(entityIn instanceof EntityVehicleSeat)&&entityIn!=parentExt&&Arrays.stream(parentExt.getParts()).noneMatch(entity -> entity==entityIn))
			super.applyEntityCollision(entityIn);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(durability!=null)
		{
			durability.attackFrom(source, amount);
			if(parentExt instanceof ISyncNBTEntity)
				((ISyncNBTEntity<?>)parentExt).updateEntityForEvent(SyncEvents.ENTITY_DAMAGED);
		}
		return false;
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		if(aabb==null)
			return super.getEntityBoundingBox();
		return getCollisionOBB().getEnclosingAABB();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return collidable?getEntityBoundingBox(): null;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return collidable?getEntityBoundingBox(): null;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		return parentExt.interactRayTracedPart(player, hand);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		return parentExt.getOverlayTextOnPart(this, player, mop);
	}

	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
	{
		super.setLocationAndAngles(x, y, z, yaw, pitch);
		//Minecraft still stores an AABB, so expose the OBB's enclosing box for broad-phase queries.
		setEntityBoundingBox(getEntityBoundingBox());
	}

	/**
	 * Returns this part's precise oriented collision box at its current transform.
	 */
	public VehicleOBB getCollisionOBB()
	{
		return getCollisionOBB(parentExt.getPositionVector(), parentExt.rotationYaw, parentExt.rotationPitch, parentExt.getRotationRoll());
	}

	/**
	 * Returns this part's precise oriented collision box at a tested vehicle transform.
	 */
	public VehicleOBB getCollisionOBB(Vec3d vehiclePosition, float yaw, float pitch, float roll)
	{
		VehicleSegment<?> activeSegment = getActiveSegment();
		if(activeSegment!=null)
			return activeSegment.createPartOBB(aabb, vehiclePosition, offset, yaw, pitch, roll, getAdditionalCollisionYaw());

		Vec3d partPosition = vehiclePosition.add(VehicleOBB.transformLocal(offset, yaw, pitch, roll));
		return VehicleOBB.fromLocalAABB(aabb, partPosition, getCollisionYaw(yaw), pitch, roll);
	}

	/**
	 * Returns this part's world position at a tested vehicle transform.
	 */
	public Vec3d getPartWorldPosition(Vec3d vehiclePosition, float yaw, float pitch, float roll)
	{
		VehicleSegment<?> activeSegment = getActiveSegment();
		if(activeSegment!=null)
			return activeSegment.getPartWorldPosition(vehiclePosition, offset, yaw, pitch, roll);
		return vehiclePosition.add(VehicleOBB.transformLocal(offset, yaw, pitch, roll));
	}

	@Nullable
	protected VehicleSegment<?> getActiveSegment()
	{
		if(segment!=null)
			return segment;
		if(parentExt instanceof EntityVehicleBase)
			return ((EntityVehicleBase<?>)parentExt).getRootSegment();
		return null;
	}

	/**
	 * Ray-traces this part's precise OBB rather than Minecraft's enclosing AABB.
	 */
	@Nullable
	public Vec3d rayTraceOBB(Vec3d start, Vec3d end)
	{
		return getCollisionOBB().rayTrace(start, end);
	}

	/**
	 * Allows wheels and other specialised parts to rotate their own OBB without moving their mount point.
	 */
	protected float getCollisionYaw(float vehicleYaw)
	{
		return vehicleYaw+getAdditionalCollisionYaw();
	}

	/**
	 * Additional yaw relative to the part segment. Wheels use this for steering.
	 */
	protected float getAdditionalCollisionYaw()
	{
		return 0;
	}

	@Override
	public boolean canRenderOnFire()
	{
		//handled by parent
		return false;
	}

	protected Vec3d getWorldPos()
	{
		return getPartWorldPosition(parentExt.getPositionVector(),
				parentExt.rotationYaw, parentExt.rotationPitch, parentExt.getRotationRoll());
	}

	@Nullable
	@Override
	public SyncedDurability getDurability()
	{
		return durability;
	}

	@Override
	public boolean canRepair()
	{
		return durability!=null&&durability.canRepair();
	}

	@Override
	public boolean repair(int repairPoints)
	{
		assert durability!=null;
		return durability.repair(repairPoints);
	}

	@Override
	public int getRepairCost()
	{
		assert durability!=null;
		return durability.armor;
	}

	private static AxisAlignedBB normalize(AxisAlignedBB box)
	{
		return new AxisAlignedBB(
				Math.min(box.minX, box.maxX), Math.min(box.minY, box.maxY), Math.min(box.minZ, box.maxZ),
				Math.max(box.minX, box.maxX), Math.max(box.minY, box.maxY), Math.max(box.minZ, box.maxZ)
		);
	}
}
