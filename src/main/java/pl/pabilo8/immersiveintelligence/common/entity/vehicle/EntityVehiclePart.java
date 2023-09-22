package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 09.07.2020
 */
public class EntityVehiclePart extends MultiPartEntityPart implements IAdvancedTextOverlay
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
	public IVehicleMultiPart parentExt;
	/**
	 * The health storage and damage for this part
	 */
	@Nullable
	public VehicleDurability hitbox;

	public EntityVehiclePart(IVehicleMultiPart parent, String partName, Vec3d offset, AxisAlignedBB aabb)
	{
		super(parent, partName, (float)aabb.getAverageEdgeLength(), (float)Math.abs(aabb.maxY-aabb.minY));
		this.parentExt = parent;
		this.offset = offset;
		this.aabb = aabb;
	}

	public EntityVehiclePart(IVehicleMultiPart parent, String partName, Vec3d offset, double radius, double height)
	{
		this(parent, partName, offset, new AxisAlignedBB(-radius, -height, -radius, radius, height, radius));
	}

	public EntityVehiclePart(IVehicleMultiPart parent, String partName, Vec3d offset, double radius)
	{
		this(parent, partName, offset, radius, radius);
	}

	public EntityVehiclePart setHitbox(@Nonnull VehicleDurability hitbox)
	{
		this.hitbox = hitbox;
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
		if(hitbox!=null)
			hitbox.attackFrom(source, amount);
		return false;
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
}
