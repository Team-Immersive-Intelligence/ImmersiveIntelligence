package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nullable;

/**
 * A generic dynamic collision box (AABB) entity used by various multiblocks.
 *
 * @author Pabilo8
 * @since 07.10.2023
 */
public class EntityAMTTactile extends Entity
{
	/**
	 * Handler object this one adheres to
	 */
	private TactileHandler handler;
	/**
	 * Parent object of this
	 */
	@Nullable
	private EntityAMTTactile parent;
	/**
	 * Name of this part
	 */
	public String name;
	/**
	 * Offset from center of parent or block
	 */
	public Vec3d offset;
	/**
	 * Animation properties
	 */
	public Vec3d translation, rotation, scale;
	/**
	 * Bounding box of this part, moved dynamically
	 */
	public AxisAlignedBB aabb;

	public EntityAMTTactile(World worldIn)
	{
		super(worldIn);
	}

	public EntityAMTTactile(TactileHandler handler, String name, Vec3d offset, AxisAlignedBB aabb)
	{
		super(handler.getWorld());
		this.name = name;
		this.handler = handler;
		this.offset = offset;
		this.aabb = aabb;
	}

	public EntityAMTTactile(TactileHandler handler, String name, Vec3d offset, double radius, double height)
	{
		this(handler, name, offset, new AxisAlignedBB(-radius, -height, -radius, radius, height, radius));
	}

	public EntityAMTTactile(TactileHandler handler, String name, Vec3d offset, double radius)
	{
		this(handler, name, offset, radius, radius);
	}

	public void setParent(@Nullable EntityAMTTactile parent)
	{
		this.parent = parent;
	}

	@Override
	protected void entityInit()
	{

	}


	//TODO: 12.10.2023 caching?
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		//Is one of root elements
		if(parent==null)
		{
			this.posX = offset.x+translation.x;
			this.posY = offset.y+translation.y;
			this.posZ = offset.z+translation.z;
			setRotation((float)rotation.y, (float)rotation.x);
		}
		//Belongs to another element
		else
		{
			//Roll is unused in Tactile rotation
			Vec3d vecX = IIUtils.offsetPosDirection((float)offset.x,
					Math.toRadians(MathHelper.wrapDegrees(-parent.rotationYaw)),
					Math.toRadians(MathHelper.wrapDegrees(parent.rotationPitch))
			);
			Vec3d vecZ = IIUtils.offsetPosDirection((float)offset.z,
					Math.toRadians(MathHelper.wrapDegrees(-parent.rotationYaw-90)),
					Math.toRadians(MathHelper.wrapDegrees(parent.rotationPitch))
			);

			this.posX = vecX.x+vecZ.x;
			this.posY = vecX.y+vecZ.y;
			this.posZ = vecX.z+vecZ.z;
			setRotation((float)(parent.rotationYaw+rotation.y), (float)(parent.rotationPitch+rotation.x));
		}

		AxisAlignedBB newAABB = aabb.offset(posX, posY, posZ);
		if(scale!=Vec3d.ZERO)
		{
			double xLength = newAABB.minX+newAABB.maxX;
			double yLength = newAABB.minY+newAABB.maxY;
			//Z is unused in Tactile scaling
			newAABB.grow(xLength*(scale.x-1), yLength*(scale.y-1), xLength*(scale.x-1));
		}
		setEntityBoundingBox(newAABB);
	}

	@Override
	public void applyEntityCollision(Entity entity)
	{
		//Tactiles shouldn't collide with each other for simplicity's sake
		if(!(entity instanceof EntityAMTTactile))
			super.applyEntityCollision(entity);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		handler.onAttacked(source, amount);
		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{

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
		return handler.onInteract(this, player, hand);
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
		return false;
	}

	public void defaultizeAnimation()
	{
		this.translation = this.rotation = this.scale = Vec3d.ZERO;
	}
}
