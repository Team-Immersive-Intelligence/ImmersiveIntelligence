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
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

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
	public VehicleDurability durability;
	@Nullable
	public SeatInfo<?> assignedSeat;

	public EntityVehiclePart(T parent, String partName, Vec3d offset, AxisAlignedBB aabb)
	{
		super(parent, partName, (float)aabb.getAverageEdgeLength(), (float)Math.abs(aabb.maxY-aabb.minY));
		this.parentExt = parent;
		this.offset = offset;
		this.aabb = aabb;
	}

	public EntityVehiclePart(T parent, String partName, Vec3d offset, double radius, double height)
	{
		this(parent, partName, offset, new AxisAlignedBB(-radius, -height, -radius, radius, height, radius));
	}

	public EntityVehiclePart(T parent, String partName, Vec3d offset, double radius)
	{
		this(parent, partName, offset, radius, radius);
	}

	public EntityVehiclePart<T> withHitbox(@Nonnull VehicleDurability hitbox)
	{
		this.durability = hitbox;
		return this;
	}

	public EntityVehiclePart<T> withSeat(@Nonnull SeatInfo<?> seatInfo)
	{
		this.assignedSeat = seatInfo;
		return this;
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
		return aabb.offset(posX, posY, posZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return getEntityBoundingBox();
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		return parentExt.onInteractWithPart(this, player, hand);
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
		//move the bounding box with the entity
		setEntityBoundingBox(aabb.offset(x, y, z));
	}

	@Override
	public boolean canRenderOnFire()
	{
		//handled by parent
		return false;
	}

	protected Vec3d getWorldPos()
	{
		return parentExt.getPositionVector().add(IIMath.offsetPosDirectionXYZ(offset,
				parentExt.rotationYaw, parentExt.rotationPitch, parentExt.getRotationRoll()));
	}

	@Nullable
	@Override
	public VehicleDurability getDurability()
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
}
