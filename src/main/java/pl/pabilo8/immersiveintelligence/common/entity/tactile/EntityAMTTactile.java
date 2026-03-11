package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

/**
 * A generic dynamic collision box (AABB) entity used by various multiblocks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.10.2023
 */
public class EntityAMTTactile extends Entity implements IEntityAdditionalSpawnData
{
	private static final AxisAlignedBB EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
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
	public Vec3d translation = Vec3d.ZERO, rotation = Vec3d.ZERO, scale = Vec3d.ZERO;
	/**
	 * Whether this tactile cannot be seen and collided with
	 */
	public boolean visibility = true;
	/**
	 * Bounding box of this part, moved dynamically
	 */
	public AxisAlignedBB aabb;
	/**
	 * Manager object this one adheres to
	 */
	private TactileManager manager;
	/**
	 * Parent object of this
	 */
	@Nullable
	private EntityAMTTactile parent;
	/**
	 * Rotation in Z axis
	 */
	private float rotationRoll = 0;

	public EntityAMTTactile(World worldIn)
	{
		super(worldIn);
	}

	public EntityAMTTactile(TactileManager manager, String name, Vec3d offset, AxisAlignedBB aabb)
	{
		super(manager.getWorld());
		this.name = name;
		this.manager = manager;
		this.offset = offset;
		this.aabb = aabb;
		this.height = (float)(aabb.maxY-aabb.minY);
		this.width = (float)Math.max(aabb.maxX-aabb.minX, aabb.maxZ-aabb.minZ);
	}

	public EntityAMTTactile(TactileManager manager, String name, Vec3d offset, double radius, double height)
	{
		this(manager, name, offset, new AxisAlignedBB(-radius, -height, -radius, radius, height, radius));
	}

	public EntityAMTTactile(TactileManager manager, String name, Vec3d offset, double radius)
	{
		this(manager, name, offset, radius, radius);
	}

	public void setParent(@Nullable EntityAMTTactile parent)
	{
		this.parent = parent;
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void onEntityUpdate()
	{
		if(!world.isRemote)
		{
			if(manager==null||!manager.getEntities().contains(this))
			{
				setDead();
				return;
			}

			//To calculate motion from previous position
			this.prevPosX = posX;
			this.prevPosY = posY;
			this.prevPosZ = posZ;

			//Is one of root elements
			if(parent==null)
			{
				BlockPos handlerPos = manager.getPos();
				this.posX = handlerPos.getX()+offset.x+translation.x;
				this.posY = handlerPos.getY()+offset.y+translation.y;
				this.posZ = handlerPos.getZ()-0.5+offset.z+translation.z;
//			setRotation((float)rotation.y, (float)rotation.x);
				rotationPitch = (float)rotation.x;
				rotationYaw = (float)rotation.y;
				rotationRoll = (float)rotation.z;
			}
			//Belongs to another element
			else
			{
				//TODO: 20.12.2023 previous position caching
				Vec3d relativeOffset = offset.subtract(parent.offset);
				this.rotationYaw = (float)(parent.rotationYaw+rotation.y);
				this.rotationPitch = (float)(parent.rotationPitch+rotation.x);
				this.rotationRoll = (float)(parent.rotationRoll+rotation.z);

				Vec3d angle = new Matrix4().setIdentity()
						.rotate(Math.toRadians(-rotationYaw), 0, 1, 0)
						.rotate(Math.toRadians(rotationRoll), 0, 0, 1)
						.rotate(Math.toRadians(rotationPitch), 1, 0, 0)
						.apply(relativeOffset.add(translation));

				this.posX = parent.posX+angle.x;
				this.posY = parent.posY+angle.y;
				this.posZ = parent.posZ+angle.z;
			}

			this.motionX = posX-prevPosX;
			this.motionY = posY-prevPosY;
			this.motionZ = posZ-prevPosZ;

			if(!visibility)
			{
				setEntityBoundingBox(EMPTY);
				return;
			}
			world.updateEntityWithOptionalForce(this, false);
		}

		AxisAlignedBB newAABB = aabb.offset(posX, posY, posZ);
		if(scale!=Vec3d.ZERO)
		{
			double xLength = newAABB.minX+newAABB.maxX;
			double yLength = newAABB.minY+newAABB.maxY;
			double xzScale = (this.scale.x+this.scale.z-2)/2;
			newAABB.grow(xLength*xzScale, yLength*xzScale, xLength*xzScale);
		}
		setEntityBoundingBox(newAABB);

		if(!world.isRemote)
			world.getEntitiesWithinAABB(EntityLivingBase.class, newAABB).forEach(this::applyEntityCollision);

	}

	@Override
	public boolean canBeCollidedWith()
	{
		return visibility;
	}

	@Override
	public void applyEntityCollision(Entity entity)
	{
		//Tactiles shouldn't collide with each other for simplicity's sake
		if(!(entity instanceof EntityAMTTactile))
		{
			entity.move(MoverType.PISTON, motionX, motionY, motionZ);
			if(!world.isRemote&&manager!=null)
				manager.onCollide(this, entity);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(!world.isRemote&&manager!=null)
			return manager.onAttacked(this, source, amount);
		return false;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound)
	{
		return false;
	}

	@Override
	public boolean writeToNBTAtomically(NBTTagCompound compound)
	{
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
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
		if(!world.isRemote&&manager!=null)
			return manager.onInteract(this, player, hand);
		return false;
	}

	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
	{
		super.setLocationAndAngles(x, y, z, yaw, pitch);
		//move the bounding box with the entity
		if(aabb!=null)
			setEntityBoundingBox(aabb.offset(x, y, z));
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	public float getRotationYawHead()
	{
		return rotationYaw;
	}

	public void defaultizeAnimation()
	{
		this.translation = this.rotation = this.scale = Vec3d.ZERO;
		this.visibility = true;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, this.name);
		buffer.writeDouble(this.aabb.minX);
		buffer.writeDouble(this.aabb.minY);
		buffer.writeDouble(this.aabb.minZ);
		buffer.writeDouble(this.aabb.maxX);
		buffer.writeDouble(this.aabb.maxY);
		buffer.writeDouble(this.aabb.maxZ);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.name = ByteBufUtils.readUTF8String(additionalData);
		this.aabb = new AxisAlignedBB(
				additionalData.readDouble(),
				additionalData.readDouble(),
				additionalData.readDouble(),
				additionalData.readDouble(),
				additionalData.readDouble(),
				additionalData.readDouble()
		);
	}
}
