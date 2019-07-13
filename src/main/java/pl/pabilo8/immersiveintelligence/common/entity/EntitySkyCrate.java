package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.SkylineHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static blusunrize.immersiveengineering.api.CapabilitySkyhookData.SKYHOOK_USER_DATA;

/**
 * Created by Pabilo8 on 07-06-2019.
 * I would really want to name it Skyblock, but it seems that the name is already taken...
 * Also, IE planned to add them some time ago (https://github.com/BluSunrize/ImmersiveEngineering/issues/2027)
 * The name skycrate derives from a name of a file inside IE's jar (skycrate.obj), but the model isn't used.
 */
public class EntitySkyCrate extends Entity
{
	public static final double GRAVITY = 10;
	private static final double MAX_SPEED = 2.0;
	public static final double MOVE_SPEED_HOR = .25;
	public static final double MOVE_SPEED_VERT = .1;
	private Connection connection;
	public double linePos;//Start is 0, end is 1
	public double horizontalSpeed;//Blocks per tick, vertical if the connection is vertical
	private double angle;
	public double friction = .99;
	public EnumHand hand;
	private boolean limitSpeed;
	private final Set<BlockPos> ignoreCollisions = new HashSet<>();

	public EntitySkyCrate(World world)
	{
		super(world);
		this.setSize(1f, 1f);

	}

	public EntitySkyCrate(World world, Connection connection, double linePos, IBlockState block)
	{
		this(world);
		setConnectionAndPos(connection, linePos, MOVE_SPEED_HOR);

		float f1 = MathHelper.sqrt(this.motionX*this.motionX+this.motionZ*this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX)*180.0D/Math.PI)+90.0F;
		this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY)*180.0D/Math.PI)-90.0F;
		while(this.rotationPitch-this.prevRotationPitch < -180.0F)
			this.prevRotationPitch -= 360.0F;
		while(this.rotationPitch-this.prevRotationPitch >= 180.0F)
			this.prevRotationPitch += 360.0F;
		while(this.rotationYaw-this.prevRotationYaw < -180.0F)
			this.prevRotationYaw -= 360.0F;
		while(this.rotationYaw-this.prevRotationYaw >= 180.0F)
			this.prevRotationYaw += 360.0F;

		EntityFallingBlock fab = new EntityFallingBlock(world, this.posX, this.posY, this.posZ, block);
		fab.fallTime = 1;
		world.spawnEntity(fab);
		fab.startRiding(this, true);
	}

	public void setConnectionAndPos(Connection c, double linePos, double speed)
	{
		this.linePos = linePos;
		this.horizontalSpeed = speed;
		this.connection = c;
		Vec3d pos = connection.getVecAt(this.linePos).add(new Vec3d(connection.start));
		this.setLocationAndAngles(pos.x, pos.y, pos.z, this.rotationYaw, this.rotationPitch);
		this.setPosition(pos.x, pos.y, pos.z);
		if(!connection.vertical)
			this.angle = Math.atan2(connection.across.z, connection.across.x);
		ignoreCollisions.clear();
		IImmersiveConnectable iicStart = ApiUtils.toIIC(c.start, world, false);
		IImmersiveConnectable iicEnd = ApiUtils.toIIC(c.end, world, false);
		if(iicStart!=null&&iicEnd!=null)
		{
			ignoreCollisions.addAll(iicStart.getIgnored(iicEnd));
			ignoreCollisions.addAll(iicEnd.getIgnored(iicStart));
		}
	}

	@Override
	protected void entityInit()
	{
	}


	/**
	 * Checks if the entity is in range to render.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength()*4.0D;
		d1 *= 64.0D;
		return distance < d1*d1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if(ticksExisted==1)
			ImmersiveIntelligence.proxy.startSkyhookSound(this);
		Entity ent = null;
		List<Entity> list = this.getPassengers();
		if(!list.isEmpty())
			ent = list.get(0);
		if(connection==null)
		{
			if(!world.isRemote)
				setDead();
			return;
		}

		boolean moved = false;
		double inLineDirection;
		double horSpeedToUse = horizontalSpeed;
		if(connection.vertical)
			inLineDirection = -Math.sin(Math.toRadians(ent.rotationPitch))
					*Math.signum(connection.across.y);
		else
		{
			float forward = 1f;
			double strafing = 0f;
			double playerAngle = Math.toRadians(ent.rotationYaw)+Math.PI/2;
			double angleToLine = playerAngle-angle;
			inLineDirection = Math.cos(angleToLine)*forward+Math.sin(angleToLine)*strafing;
		}
		if(inLineDirection!=0)
		{
			double slope = connection.getSlopeAt(linePos);
			double slopeInDirection = Math.signum(inLineDirection)*slope;
			double speed = MOVE_SPEED_VERT;
			double slopeFactor = 1;
			if(!connection.vertical)
			{
				//Linear interpolation w.r.t. the angle of the line
				double lambda = Math.atan(slopeInDirection)/(Math.PI/2);
				speed = lambda*MOVE_SPEED_VERT+(1-lambda)*MOVE_SPEED_HOR;
				slopeFactor = 1/Math.sqrt(1+slope*slope);
			}
			if(slopeInDirection > -.1)
			{

				horizontalSpeed = (3*horizontalSpeed+inLineDirection*speed*slopeFactor)/4;
				moved = true;
			}
		}
		if(!moved)//Gravity based motion
		{
			double deltaVHor;
			if(connection.vertical)
				deltaVHor = -GRAVITY*Math.signum(connection.across.y);
			else
			{
				double param = (linePos*connection.horizontalLength-connection.catOffsetX)/connection.catA;
				double pos = Math.exp(param);
				double neg = 1/pos;
				double cosh = (pos+neg)/2;
				double sinh = (pos-neg)/2;
				//Formula taken from https://physics.stackexchange.com/a/83592 (x coordinate of the final vector),
				//after plugging in the correct function
				double vSquared = horizontalSpeed*horizontalSpeed*cosh*cosh*20*20;//cosh^2=1+sinh^2 and horSpeed*sinh=vertSpeed. 20 to convert from blocks/tick to block/s
				deltaVHor = -sinh/(cosh*cosh)*(GRAVITY+vSquared/(connection.catA*cosh));
			}
			horizontalSpeed += deltaVHor/(20*20);// First 20 is because this happens in one tick rather than one second, second 20 is to convert units
		}

		if(limitSpeed)
		{
			double totSpeed = getSpeed();
			double max = MAX_SPEED;
			if(totSpeed > max)
				horizontalSpeed *= max/totSpeed;
		}
		if(horizontalSpeed > 0)
		{
			double distToEnd = connection.horizontalLength*(1-linePos);
			if(horizontalSpeed > distToEnd)
			{
				horSpeedToUse = distToEnd;
			}
		}
		else
		{
			double distToStart = -connection.horizontalLength*linePos;
			if(horizontalSpeed < distToStart)
			{
				horSpeedToUse = distToStart;
			}
		}
		horizontalSpeed *= friction;
		linePos += horSpeedToUse/connection.horizontalLength;
		Vec3d pos = connection.getVecAt(linePos);
		double posXTemp = pos.x+connection.start.getX();
		double posYTemp = pos.y+connection.start.getY();
		double posZTemp = pos.z+connection.start.getZ();
		motionX = posXTemp-posX;
		motionZ = posZTemp-posZ;
		motionY = posYTemp-posY;
		if(!isValidPosition(posXTemp, posYTemp, posZTemp, ent))
		{
			setDead();
			return;
		}
		posX = posXTemp;
		posY = posYTemp;
		posZ = posZTemp;

		super.onUpdate();
		float f1 = MathHelper.sqrt(this.motionX*this.motionX+this.motionZ*this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX)*180.0D/Math.PI)+90.0F;
		this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY)*180.0D/Math.PI)-90.0F;

		while(this.rotationPitch-this.prevRotationPitch < -180.0F)
			this.prevRotationPitch -= 360.0F;
		while(this.rotationPitch-this.prevRotationPitch >= 180.0F)
			this.prevRotationPitch += 360.0F;
		while(this.rotationYaw-this.prevRotationYaw < -180.0F)
			this.prevRotationYaw -= 360.0F;
		while(this.rotationYaw-this.prevRotationYaw >= 180.0F)
			this.prevRotationYaw += 360.0F;

		this.rotationPitch = this.prevRotationPitch+(this.rotationPitch-this.prevRotationPitch)*0.2F;
		this.rotationYaw = this.prevRotationYaw+(this.rotationYaw-this.prevRotationYaw)*0.2F;

		if(this.isInWater())
		{
			for(int j = 0; j < 4; ++j)
			{
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX-this.motionX*(double)f3, this.posY-this.motionY*(double)f3, this.posZ-this.motionZ*(double)f3, this.motionX, this.motionY, this.motionZ);
			}
		}

		double dx = this.posX-this.prevPosX;
		double dy = this.posY-this.prevPosY;
		double dz = this.posZ-this.prevPosZ;

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	private static double getSpeedPerHor(Connection connection, double pos)
	{
		if(connection.vertical)
			return 1;
		else
		{
			double slope = connection.getSlopeAt(pos);
			return Math.sqrt(slope*slope+1);
		}
	}

	public boolean isValidPosition(double x, double y, double z, @Nonnull Entity ent)
	{
		final double tolerance = connection.vertical?5: 10;//TODO are these values good?
		double radius = ent.width/2;
		double height = ent.height;
		double yOffset = getMountedYOffset()+ent.getYOffset();
		AxisAlignedBB passengerBB = new AxisAlignedBB(x-radius, y+yOffset, z-radius, x+radius, y+yOffset+height, z+radius);
		double passengerHeight = passengerBB.maxY-passengerBB.minY;
		AxisAlignedBB feet = new AxisAlignedBB(passengerBB.minX, passengerBB.minY, passengerBB.minZ,
				passengerBB.maxX, passengerBB.minY+.05*passengerHeight, passengerBB.maxZ);
		List<AxisAlignedBB> boxes = SkylineHelper.getCollisionBoxes(ent, passengerBB, world, ignoreCollisions);
		// Heuristic to prevent dragging passengers through blocks too much, but also keep most setups working
		// Allow positions where the intersection is less than 10% of the passenger BB volume
		double totalCollisionVolume = 0;
		double totalCollisionArea = 0;
		double passengerVolume = getVolume(passengerBB);
		double passengerArea = passengerVolume/passengerHeight;
		for(AxisAlignedBB box : boxes)
		{
			AxisAlignedBB intersection = box.intersect(passengerBB);
			totalCollisionVolume += getVolume(intersection);
			if(totalCollisionVolume*tolerance > passengerVolume)
				return false;
			if(!connection.vertical&&box.intersects(feet))
			{
				AxisAlignedBB feetIntersect = box.intersect(feet);
				totalCollisionArea += (feetIntersect.maxX-feetIntersect.minX)*(feetIntersect.maxZ-feetIntersect.minZ);
				if(totalCollisionArea > .5*passengerArea)
					return false;
			}
		}
		return true;
	}

	private double getVolume(AxisAlignedBB box)
	{
		return (box.maxX-box.minX)*(box.maxY-box.minY)*(box.maxZ-box.minZ);
	}

	/**
	 * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
	 * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
	 */
	@Override
	@Nullable
	public Entity getControllingPassenger()
	{
		List<Entity> list = this.getPassengers();
		return list.isEmpty()?null: list.get(0);
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
	public boolean isInvisible()
	{
		return true;
	}

	/**
	 * Return whether this entity should be rendered as on fire.
	 */
	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	public boolean isPushedByWater()
	{
		return false;
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding this one.
	 */
	@Override
	public double getMountedYOffset()
	{
		return -2;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
	}

	@Override
	public float getCollisionBorderSize()
	{
		return 0.0F;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness()
	{
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender()
	{
		return 15728880;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		this.setDead();
		return true;
	}

	@Override
	public boolean canPassengerSteer()
	{
		return false;
	}

	private void handleDismount(Entity passenger)
	{
		passenger.setPositionAndUpdate(posX, posY+getMountedYOffset()+passenger.getYOffset(), posZ);
		passenger.motionX = motionX;
		passenger.motionY = motionY;
		passenger.motionZ = motionZ;
		if(motionY < 0)
		{
			passenger.fallDistance = SkylineHelper.fallDistanceFromSpeed(motionY);
			passenger.onGround = false;
		}
		if(passenger.hasCapability(SKYHOOK_USER_DATA, EnumFacing.UP))
			Objects.requireNonNull(passenger.getCapability(SKYHOOK_USER_DATA, EnumFacing.UP))
					.release();
	}

	@Override
	protected void removePassenger(Entity passenger)
	{
		super.removePassenger(passenger);
		this.setDead();
		if(!world.isRemote)
			ApiUtils.addFutureServerTask(world, () -> handleDismount(passenger));
		else
			ApiUtils.callFromOtherThread(Minecraft.getMinecraft()::addScheduledTask, () -> handleDismount(passenger));
	}

	/**
	 * Sets position and rotation, clamping and wrapping params to valid values. Used by network code.
	 */
	@Override
	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
	{
		//NOP
		//Ń
	}

	public Connection getConnection()
	{
		return connection;
	}

	public double getSpeed()
	{
		if(connection==null)
			return 0;
		if(connection.vertical)
		{
			return Math.abs(horizontalSpeed);//In this case vertical speed
		}
		else
		{
			double slope = connection.getSlopeAt(linePos);
			return Math.abs(horizontalSpeed)*Math.sqrt(1+slope*slope);
		}
	}
}