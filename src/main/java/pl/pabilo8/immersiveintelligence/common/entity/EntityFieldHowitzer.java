package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 18.07.2020
 */
public class EntityFieldHowitzer extends Entity implements IVehicleMultiPart, IEntitySpecialRepairable, ITowable, IEntityZoomProvider
{
	private static final DataParameter<Boolean> dataMarkerForward = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerBackward = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerTurnLeft = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerTurnRight = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> dataMarkerGunPitchUp = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerGunPitchDown = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Float> dataMarkerAcceleration = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerSpeed = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerShootingProgress = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerReloadProgress = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerGunPitch = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.FLOAT);

	private static final DataParameter<Integer> dataMarkerWheelRightDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerWheelLeftDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerMainDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerGunDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerShieldDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);

	private static final DataParameter<NBTTagCompound> dataMarkerShell = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.COMPOUND_TAG);

	public int rightWheelDurability, leftWheelDurability, mainDurability, gunDurability, shieldDurability;
	public int setupTime = 0;
	public boolean alreadyShoot = false;
	public boolean towingOperation = false;
	//Yes
	public float acceleration = 0f, speed = 0f;
	public ItemStack shell = ItemStack.EMPTY;
	public float shootingProgress = 0f, reloadProgress = 0f, gunPitch = 0f;

	public boolean forward = false, backward = false, turnLeft = false, turnRight = false, reloadKeyPress = false, fireKeyPress = false, gunPitchUp = false, gunPitchDown = false;

	public EntityVehicleMultiPart[] partArray;

	public EntityVehicleWheel partWheelRight = new EntityVehicleWheel(this, "wheel_right", 1F, 1.0F);
	public EntityVehicleWheel partWheelLeft = new EntityVehicleWheel(this, "wheel_left", 1F, 1.0F);
	public EntityVehicleMultiPart partMain = new EntityVehicleMultiPart(this, "main", 1F, 1.0F);
	public EntityVehicleMultiPart partMain2 = new EntityVehicleMultiPart(this, "main2", 1F, 1.0F);
	public EntityVehicleMultiPart partGun = new EntityVehicleMultiPart(this, "gun", 1F, 1.0F);
	public EntityVehicleMultiPart partShieldRight = new EntityVehicleMultiPart(this, "shield_right", 1F, 1.0F);
	public EntityVehicleMultiPart partShieldLeft = new EntityVehicleMultiPart(this, "shield_left", 1F, 1.0F);

	public int destroyTimer = -1;
	static final AxisAlignedBB AABB = new AxisAlignedBB(-1.5, 0, -1.5, 1.5, 1.75, 1.5);
	static final AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.25, 0d, 0.25, 0.25, 1d, -0.25);
	static final AxisAlignedBB AABB_MAIN = new AxisAlignedBB(-0.5, 0.125d, -0.5, 0.5, 0.625, 0.5);
	static final AxisAlignedBB AABB_GUN = new AxisAlignedBB(-0.35, 0.25d, 0.35, 0.35, 1d, -0.35);
	static final AxisAlignedBB AABB_SHIELD = new AxisAlignedBB(-0.35, 0d, -0.35, 0.35, 1.25d, 0.35);

	public EntityFieldHowitzer(World worldIn)
	{
		super(worldIn);
		partArray = new EntityVehicleMultiPart[]{partWheelRight, partWheelLeft, partMain, partMain2, partGun, partShieldRight, partShieldLeft};
		rightWheelDurability = FieldHowitzer.wheelDurability;
		leftWheelDurability = FieldHowitzer.wheelDurability;
		mainDurability = FieldHowitzer.mainDurability;
		gunDurability = FieldHowitzer.gunDurability;
		shieldDurability = FieldHowitzer.shieldDurability;
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(dataMarkerForward, forward);
		this.dataManager.register(dataMarkerBackward, backward);
		this.dataManager.register(dataMarkerTurnLeft, turnLeft);
		this.dataManager.register(dataMarkerTurnRight, turnRight);
		this.dataManager.register(dataMarkerGunPitchUp, gunPitchUp);
		this.dataManager.register(dataMarkerGunPitchDown, gunPitchDown);

		this.dataManager.register(dataMarkerAcceleration, acceleration);
		this.dataManager.register(dataMarkerSpeed, speed);
		this.dataManager.register(dataMarkerShootingProgress, shootingProgress);
		this.dataManager.register(dataMarkerReloadProgress, reloadProgress);
		this.dataManager.register(dataMarkerGunPitch, gunPitch);

		this.dataManager.register(dataMarkerWheelRightDurability, rightWheelDurability);
		this.dataManager.register(dataMarkerWheelLeftDurability, leftWheelDurability);
		this.dataManager.register(dataMarkerMainDurability, mainDurability);
		this.dataManager.register(dataMarkerGunDurability, gunDurability);
		this.dataManager.register(dataMarkerShieldDurability, shieldDurability);

		this.dataManager.register(dataMarkerShell, ItemStack.EMPTY.serializeNBT());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		forward = compound.getBoolean("forward");
		backward = compound.getBoolean("backward");
		turnLeft = compound.getBoolean("turnLeft");
		turnRight = compound.getBoolean("turnRight");
		gunPitchUp = compound.getBoolean("gunPitchUp");
		gunPitchDown = compound.getBoolean("gunPitchDown");

		acceleration = compound.getFloat("acceleration");
		speed = compound.getFloat("speed");
		shootingProgress = compound.getFloat("shootingProgress");
		reloadProgress = compound.getFloat("reloadProgress");
		gunPitch = compound.getFloat("gunPitch");
		shell = new ItemStack(compound.getCompoundTag("shell"));


		if(compound.hasKey("rightWheelDurability"))
			rightWheelDurability = compound.getInteger("rightWheelDurability");
		if(compound.hasKey("leftWheelDurability"))
			leftWheelDurability = compound.getInteger("leftWheelDurability");
		if(compound.hasKey("mainDurability"))
			mainDurability = compound.getInteger("mainDurability");
		if(compound.hasKey("gunDurability"))
			gunDurability = compound.getInteger("gunDurability");
		if(compound.hasKey("shieldDurability"))
			shieldDurability = compound.getInteger("shieldDurability");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("forward", forward);
		compound.setBoolean("backward", backward);
		compound.setBoolean("turnLeft", turnLeft);
		compound.setBoolean("turnRight", turnRight);
		compound.setBoolean("gunPitchUp", gunPitchUp);
		compound.setBoolean("gunPitchDown", gunPitchDown);

		compound.setFloat("acceleration", acceleration);
		compound.setFloat("speed", speed);
		compound.setFloat("shootingProgress", shootingProgress);
		compound.setFloat("reloadProgress", reloadProgress);
		compound.setFloat("gunPitch", gunPitch);
		compound.setTag("shell", shell.serializeNBT());

		compound.setInteger("rightWheelDurability", rightWheelDurability);
		compound.setInteger("leftWheelDurability", leftWheelDurability);
		compound.setInteger("mainDurability", mainDurability);
		compound.setInteger("gunDurability", gunDurability);
		compound.setInteger("shieldDurability", shieldDurability);
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return AABB.offset(posX, posY, posZ);
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return null;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return getEntityBoundingBox();
	}

	@Override
	public void onUpdate()
	{
		//spawn seats
		if(firstUpdate&&!world.isRemote)
		{
			//commander
			EntityVehicleSeat.getOrCreateSeat(this, 0);
			//gunner
			EntityVehicleSeat.getOrCreateSeat(this, 1);
		}
		//super.onUpdate();
		// TODO: 06.08.2021 death animation
		if(!world.isRemote&&mainDurability <= 0)
			setDead();

		if(towingOperation)
		{
			acceleration = Math.round((acceleration-Math.signum(acceleration)*2)/2)*2;
			setupTime += 1;
			if(setupTime > FieldHowitzer.towingTime)
				towingOperation = false;
		}
		else if(!isRiding())
		{
			if(world.isRemote)
			{
				Entity pre = ClientUtils.mc().player.getRidingEntity();
				if(pre instanceof EntityVehicleSeat&&pre.getRidingEntity()==this)
				{
					//Handle, send to server, get other from server
					int seat = ((EntityVehicleSeat)pre).seatID;
					handleClientKeyInput(seat);
					handleClientKeyOutput(seat);
				}
				else
				{
					//Get from server
					handleClientKeyOutput(-1);
				}
			}
			else
			{
				handleServerKeyInput();
				if(EntityVehicleSeat.getOrCreateSeat(this, 0).getPassengers().size()==0)
				{
					gunPitchUp = false;
					gunPitchDown = false;
					reloadProgress = 0;
					shootingProgress = 0;
				}
				if(EntityVehicleSeat.getOrCreateSeat(this, 1).getPassengers().size()==0)
				{
					forward = false;
					backward = false;
					turnLeft = false;
					turnRight = false;
				}
			}
		}

		if(!towingOperation)
		{
			if(forward||backward||turnLeft||turnRight)
			{
				if(setupTime < FieldHowitzer.setupTime)
					setupTime++;
				else
				{
					if(shootingProgress==0&&reloadProgress==0)
					{
						if(turnLeft)
						{
							partWheelLeft.wheelTraverse += 1.5f;
							partWheelRight.wheelTraverse -= 1.5f;
							turnTowards(-5, 0);
						}
						else if(turnRight)
						{
							partWheelRight.wheelTraverse += 1.5f;
							partWheelLeft.wheelTraverse -= 1.5f;
							turnTowards(5, 0);
						}
						else if(forward)
							acceleration = Math.min(acceleration+0.1f, 1f);
						else
							acceleration = Math.max(acceleration-0.15f, -1f);
					}
				}
			}
			else
			{
				setupTime = Math.max(setupTime-1, 0);
				acceleration *= 0.25f;
				if(Math.abs(acceleration) < 0.15)
					acceleration = 0;
				if(setupTime==0)
				{
					if(gunPitchUp)
						gunPitch = Math.min(gunPitch+0.5f, 85);
					else if(gunPitchDown)
						gunPitch = Math.max(gunPitch-0.5f, -5);
				}
			}
		}


		if(reloadProgress==0&&!shell.isEmpty())
		{
			if(fireKeyPress&&shootingProgress==0)
			{
				shootingProgress = 1;
				if(!world.isRemote)
				{
					dataManager.set(dataMarkerShootingProgress, shootingProgress);
					dataManager.set(dataMarkerShell, shell.serializeNBT());
					dataManager.setDirty(dataMarkerShell);
				}
			}
			else if(shootingProgress > 0&&shootingProgress < FieldHowitzer.fireTime)
			{
				shootingProgress++;
				if(!world.isRemote&&!alreadyShoot&&shootingProgress > FieldHowitzer.fireTime*0.6)
				{
					double true_angle = Math.toRadians((-rotationYaw));
					double true_angle2 = Math.toRadians(180+gunPitch);
					Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(2f, true_angle, true_angle2);
					if(world.isRemote)
					{

					}
					else
					{
						world.playSound(null, posX, posY, posZ, IISounds.howitzer_shot, SoundCategory.PLAYERS, 1.25f, 0.5f);
						EntityBullet a = BulletHelper.createBullet(world, shell.copy(), getPositionVector().add(gun_end.scale(-1)).addVector(0, 1, 0), gun_end.scale(-1).normalize());
						a.setShooters(this, getParts());
						world.spawnEntity(a);
					}
					alreadyShoot = true;
				}
			}
			else if(shootingProgress >= FieldHowitzer.fireTime)
			{
				alreadyShoot = false;
				if(!world.isRemote)
				{
					blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, getPosition(), IIContent.itemAmmoLightArtillery.getCasingStack(1));
					dataManager.set(dataMarkerShell, shell.serializeNBT());
					dataManager.setDirty(dataMarkerShell);
				}
				shell = ItemStack.EMPTY;
				shootingProgress = 0;
				fireKeyPress = false;
			}
		}
		else if(reloadKeyPress||reloadProgress > 0)
		{
			EntityVehicleSeat seat = EntityVehicleSeat.getOrCreateSeat(this, 0);
			if(seat.getPassengers().size() > 0)
			{
				Entity entity = seat.getPassengers().get(0);
				if(entity instanceof EntityLivingBase)
				{
					ItemStack stack = ((EntityLivingBase)entity).getHeldItem(EnumHand.MAIN_HAND);
					if(stack.getItem()==IIContent.itemAmmoLightArtillery)
					{
						if(reloadProgress==0)
						{
							reloadKeyPress = false;
							reloadProgress = 1;
						}
						else if(reloadProgress < FieldHowitzer.reloadTime)
						{
							reloadProgress++;
						}
						else if(reloadProgress >= FieldHowitzer.reloadTime)
						{
							reloadProgress = 0;
							this.shell = stack.copy();
							shell.setCount(1);
							if(!world.isRemote)
							{
								((EntityLivingBase)entity).getHeldItem(EnumHand.MAIN_HAND).shrink(1);
								dataManager.set(dataMarkerShell, shell.serializeNBT());
								dataManager.setDirty(dataMarkerShell);
							}
						}

					}
					else
						reloadProgress = 0;
				}
			}

		}


		speed = 0;

		updateParts();
		handleMovement();

	}

	private void handleMovement()
	{
		float r = MathHelper.wrapDegrees(rotationYaw);

		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
		Vec3d pos1_z = Utils.offsetPosDirection(-0.75f, true_angle2, 0);
		//Vec3d pos2_z = Utils.offsetPosDirection(0.75f, true_angle2, 0);

		partWheelLeft.rotationYaw = this.rotationYaw;
		partWheelLeft.travel(0, 0, 1f, -0.0125f, acceleration*0.015);

		partWheelRight.rotationYaw = this.rotationYaw;
		partWheelRight.travel(0, 0, 1f, -0.0125f, acceleration*0.015);

		if(!partWheelLeft.isEntityInsideOpaqueBlock()&&!partWheelLeft.isEntityInsideOpaqueBlock()&&!partWheelRight.isEntityInsideOpaqueBlock()&&!partWheelRight.isEntityInsideOpaqueBlock())
		{
			partWheelRight.wheelTraverse += acceleration*2.5f;
			partWheelLeft.wheelTraverse += acceleration*2.5f;
			Vec3d currentPos = new Vec3d(partWheelLeft.posX+pos1_z.x, partWheelLeft.posY, partWheelLeft.posZ+pos1_z.z);
			setPosition(currentPos.x, currentPos.y, currentPos.z);
			Utils.setEntityVelocity(this, partWheelLeft.motionX, partWheelLeft.motionY, partWheelLeft.motionZ);
		}
	}

	public void turnTowards(float yaw, float pitch)
	{
		float f = this.rotationPitch;
		float f1 = this.rotationYaw;
		this.rotationYaw = (float)((double)this.rotationYaw + (double)yaw * 0.15D);
		this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15D);
		this.rotationPitch = MathHelper.clamp(this.rotationPitch, -90.0F, 90.0F);
		this.prevRotationPitch += this.rotationPitch - f;
		this.prevRotationYaw += this.rotationYaw - f1;
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
	public boolean canRepair()
	{
		return gunDurability < FieldHowitzer.gunDurability||
				mainDurability < FieldHowitzer.mainDurability||
				leftWheelDurability < FieldHowitzer.wheelDurability||
				rightWheelDurability < FieldHowitzer.wheelDurability||
				shieldDurability < FieldHowitzer.wheelDurability;
	}

	@Override
	public boolean repair(int repairPoints)
	{
		if(gunDurability < FieldHowitzer.gunDurability)
			gunDurability = Math.min(gunDurability+repairPoints, FieldHowitzer.gunDurability);
		else if(mainDurability < FieldHowitzer.mainDurability)
			mainDurability = Math.min(mainDurability+repairPoints, FieldHowitzer.mainDurability);
		else if(leftWheelDurability < FieldHowitzer.wheelDurability)
			leftWheelDurability = Math.min(leftWheelDurability+repairPoints, FieldHowitzer.wheelDurability);
		else if(rightWheelDurability < FieldHowitzer.wheelDurability)
			rightWheelDurability = Math.min(rightWheelDurability+repairPoints, FieldHowitzer.wheelDurability);
		else if(shieldDurability < FieldHowitzer.shieldDurability)
			shieldDurability = Math.min(shieldDurability+repairPoints, FieldHowitzer.shieldDurability);
		else return false;
		return true;
	}

	@Override
	public int getRepairCost()
	{
		return 1;
	}

	@Override
	public Entity getTowingEntity()
	{
		return getRidingEntity();
	}

	@Override
	public boolean startTowing(Entity tower)
	{
		if(getTowingEntity()==null&&getRecursivePassengers().stream().allMatch(entity -> entity instanceof EntityVehicleSeat))
		{
			towingOperation = true;
			setupTime = 0;
			startRiding(tower);
			return true;
		}
		return false;
	}

	@Override
	public boolean stopTowing()
	{
		if(getTowingEntity()!=null)
		{
			towingOperation = true;
			setupTime = 0;
			dismountRidingEntity();
			return true;
		}
		return false;
	}

	@Override
	public boolean canMoveTowed()
	{
		return !towingOperation;
	}

	@Override
	public void moveTowableWheels(float speed)
	{
		this.partWheelRight.wheelTraverse += speed*2;
		this.partWheelLeft.wheelTraverse += speed*2;
	}

	@Override
	protected boolean canFitPassenger(Entity passenger)
	{
		return passenger instanceof EntityVehicleSeat;
	}

	@Override
	public void getSeatRidingPosition(int seatID, Entity passenger)
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));

		Vec3d pos2 = Utils.offsetPosDirection(-0.65f, true_angle, 0);
		Vec3d pos3 = Utils.offsetPosDirection(seatID==0?-0.75f: 0.75f, true_angle2, 0);
		if(setupTime > 0)
		{
			double ticks = MathHelper.clamp((setupTime/(FieldHowitzer.setupTime*0.2)), 0, 1);
			pos3 = pos3.subtract(pos3.scale(ticks*0.55))
					.addVector(0, -0.2, 0);
		}
		else if(shootingProgress > 0&&(seatID==1)||(shootingProgress > FieldHowitzer.fireTime*0.3f))
			pos3 = pos3.addVector(0, -0.2, 0);
		else if(seatID==0&&(gunPitchDown||gunPitchUp))
			pos3 = pos3.addVector(0, -0.2, 0);
		else if(seatID==0&&reloadProgress > 0.2f&&reloadProgress < 0.4f)
			pos3 = pos3.add(pos3.scale(-0.1875*((reloadProgress-0.4)/0.1)));
		else if(seatID==0&&reloadProgress > 0.4f&&reloadProgress < 0.5f)
			pos3 = pos3.add(pos3.scale(-0.1875*(1f-(reloadProgress-0.4)/0.1)));

		passenger.setPosition(posX+pos2.x+pos3.x, posY+pos3.y, posZ+pos2.z+pos3.z);
	}

	@Override
	public void getSeatRidingAngle(int seatID, Entity passenger)
	{
		float yy = this.rotationYaw;
		if(setupTime > 0)
		{
			yy += MathHelper.clamp((setupTime/(FieldHowitzer.setupTime*0.2)), 0, 1)*(seatID==0?65: -65);
		}
		else if(seatID==0&&reloadProgress > 0)
		{
			if(reloadProgress < 0.8)
				yy += MathHelper.clamp(reloadProgress/(FieldHowitzer.reloadTime*0.2), 0, 1)*65;
			else
				yy += MathHelper.clamp(1f-(((reloadProgress/(FieldHowitzer.reloadTime))-0.8f)/0.2f), 0, 1)*65;
		}
		passenger.setRenderYawOffset(yy);

		float f = MathHelper.wrapDegrees(passenger.rotationYaw-this.rotationYaw);
		float f1 = MathHelper.clamp(f, -75.0F, 75.0F);
		passenger.prevRotationYaw += f1-f;
		passenger.rotationYaw += f1-f;

		passenger.setRotationYawHead(passenger.rotationYaw);

	}

	@Override
	public boolean shouldSeatPassengerSit(int seatID, Entity passenger)
	{
		return false;
	}

	@Override
	public void onSeatDismount(int seatID, Entity passenger)
	{

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
	public boolean onInteractWithPart(EntityVehicleMultiPart part, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote&&!towingOperation)
		{
			if(part==partShieldRight||part==partWheelRight)
				player.startRiding(EntityVehicleSeat.getOrCreateSeat(this, 0));
			else if(part==partShieldLeft||part==partWheelLeft)
				player.startRiding(EntityVehicleSeat.getOrCreateSeat(this, 1));
			return true;
		}
		return false;
	}

	@Override
	public String[] getOverlayTextOnPart(EntityVehicleMultiPart part, EntityPlayer player, RayTraceResult mop, boolean hammer)
	{
		return new String[0];
	}

	@Override
	public World getWorld()
	{
		return this.getEntityWorld();
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float amount)
	{
		boolean isValidSource = source.isProjectile()||source.isExplosion()||source.isFireDamage();
		if(part==partGun)
		{
			if(isValidSource)
			{
				gunDurability -= amount*0.85;
				dataManager.set(dataMarkerGunDurability, gunDurability);
				world.playSound(null, posX, posY, posZ, IISounds.impact_metal, SoundCategory.BLOCKS, 1.5f, 1f);
			}
			else
				world.playSound(null, posX, posY, posZ, IISounds.ricochet_metal, SoundCategory.NEUTRAL, 1.5f, 8/amount);
			return true;
		}
		else if((part==partMain||part==partMain2))
		{
			if(isValidSource)
			{
				mainDurability -= amount;
				dataManager.set(dataMarkerMainDurability, mainDurability);
				world.playSound(null, posX, posY, posZ, IISounds.impact_metal, SoundCategory.BLOCKS, 1.5f, 1f);
			}
			else
				world.playSound(null, posX, posY, posZ, IISounds.ricochet_metal, SoundCategory.NEUTRAL, 1.5f, 8/amount);

		}
		else if(part==partWheelRight)
		{
			rightWheelDurability -= amount;
			dataManager.set(dataMarkerWheelRightDurability, rightWheelDurability);
		}
		else if(part==partWheelLeft)
		{
			leftWheelDurability -= amount;
			dataManager.set(dataMarkerWheelLeftDurability, leftWheelDurability);
		}
		else if((part==partShieldLeft||part==partShieldRight))
		{
			if(isValidSource&&amount > 8)
			{
				if(shieldDurability < 0)
				{
					mainDurability -= amount*0.85;
					dataManager.set(dataMarkerMainDurability, mainDurability);
				}
				else
				{
					shieldDurability -= amount*0.85;
					dataManager.set(dataMarkerShieldDurability, shieldDurability);
				}
				world.playSound(null, posX, posY, posZ, IISounds.impact_metal, SoundCategory.BLOCKS, 1.5f, 1f);
			}
			else
				world.playSound(null, posX, posY, posZ, IISounds.ricochet_metal, SoundCategory.NEUTRAL, 1.5f, 8/amount);
		}
		else
			return false;
		return true;
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	public Entity[] getParts()
	{
		return partArray;
	}

	private void updateParts()
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));

		Vec3d pos1_x = Utils.offsetPosDirection(0.5f, true_angle, 0);
		Vec3d pos2_x = pos1_x.scale(1);
		Vec3d pos3 = pos1_x.scale(-1.5);
		Vec3d pos1_z = Utils.offsetPosDirection(0.75f, true_angle2, 0);
		Vec3d pos2_z = Utils.offsetPosDirection(-0.75f, true_angle2, 0);

		this.partWheelLeft.setLocationAndAngles(posX+pos1_z.x, posY, posZ+pos1_z.z, 0.0F, 0);
		this.partWheelLeft.setEntityBoundingBox(AABB_WHEEL.offset(this.partWheelLeft.posX, this.partWheelLeft.posY, this.partWheelLeft.posZ));
		Utils.setEntityVelocity(this.partWheelLeft, this.motionX, this.motionY, this.motionZ);
		this.partWheelLeft.onUpdate();

		this.partWheelRight.setLocationAndAngles(posX+pos2_z.x, posY, posZ+pos2_z.z, 0.0F, 0);
		this.partWheelRight.setEntityBoundingBox(AABB_WHEEL.offset(this.partWheelRight.posX, this.partWheelRight.posY, this.partWheelRight.posZ));
		Utils.setEntityVelocity(this.partWheelRight, this.motionX, this.motionY, this.motionZ);
		this.partWheelRight.onUpdate();

		this.partMain.setLocationAndAngles(posX, posY, posZ, 0.0F, 0);
		this.partMain.setEntityBoundingBox(AABB_MAIN.offset(this.partMain.posX, this.partMain.posY, this.partMain.posZ));
		Utils.setEntityVelocity(this.partMain, this.motionX, this.motionY, this.motionZ);
		this.partMain.onUpdate();

		this.partMain2.setLocationAndAngles(posX+pos3.x, posY+pos3.y, posZ+pos3.z, 0.0F, 0);
		this.partMain2.setEntityBoundingBox(AABB_MAIN.offset(this.partMain2.posX, this.partMain2.posY, this.partMain2.posZ));
		Utils.setEntityVelocity(this.partMain2, this.motionX, this.motionY, this.motionZ);
		this.partMain2.onUpdate();

		this.partGun.setLocationAndAngles(posX+pos1_x.x, posY+0.65, posZ+pos1_x.z, 0.0F, 0);
		this.partGun.setEntityBoundingBox(AABB_GUN.offset(this.partGun.posX, this.partGun.posY, this.partGun.posZ));
		Utils.setEntityVelocity(this.partGun, this.motionX, this.motionY, this.motionZ);
		this.partGun.onUpdate();

		this.partShieldLeft.setLocationAndAngles(posX+pos1_z.x+pos2_x.x, posY+0.375, posZ+pos1_z.z+pos2_x.z, 0.0F, 0);
		this.partShieldLeft.setEntityBoundingBox(AABB_SHIELD.offset(this.partShieldLeft.posX, this.partShieldLeft.posY, this.partShieldLeft.posZ));
		Utils.setEntityVelocity(this.partShieldLeft, this.motionX, this.motionY, this.motionZ);
		this.partShieldLeft.onUpdate();

		this.partShieldRight.setLocationAndAngles(posX+pos2_z.x+pos2_x.x, posY+0.375, posZ+pos2_z.z+pos2_x.z, 0.0F, 0);
		this.partShieldRight.setEntityBoundingBox(AABB_SHIELD.offset(this.partShieldRight.posX, this.partShieldRight.posY, this.partShieldRight.posZ));
		Utils.setEntityVelocity(this.partShieldRight, this.motionX, this.motionY, this.motionZ);
		this.partShieldRight.onUpdate();
	}

	@Override
	public void setDead()
	{
		getPassengers().forEach(Entity::setDead);
		super.setDead();
	}

	private void handleClientKeyInput(int seat)
	{
		boolean hasChanged;

		if(seat==1)
		{
			boolean f = forward, b = backward, tl = turnLeft, tr = turnRight;
			forward = ClientUtils.mc().gameSettings.keyBindForward.isKeyDown();
			backward = ClientUtils.mc().gameSettings.keyBindBack.isKeyDown();
			turnLeft = ClientUtils.mc().gameSettings.keyBindLeft.isKeyDown();
			turnRight = ClientUtils.mc().gameSettings.keyBindRight.isKeyDown();
			hasChanged = f^forward||b^backward||tl^turnLeft||tr^turnRight;
		}
		else
		{
			boolean u = gunPitchUp, d = gunPitchDown, fk = fireKeyPress, rr = reloadKeyPress;
			gunPitchUp = ClientUtils.mc().gameSettings.keyBindForward.isKeyDown();
			gunPitchDown = ClientUtils.mc().gameSettings.keyBindBack.isKeyDown();
			fireKeyPress = Mouse.isButtonDown(1);
			reloadKeyPress = ClientProxy.keybind_manualReload.isKeyDown();
			hasChanged = u^gunPitchUp||d^gunPitchDown||fk^fireKeyPress||rr^reloadKeyPress;
		}


		if(hasChanged)
			IIPacketHandler.INSTANCE.sendToServer(new MessageEntityNBTSync(this, updateKeys(seat)));
	}

	@SideOnly(Side.CLIENT)
	private NBTTagCompound updateKeys(int seat)
	{
		NBTTagCompound compound = new NBTTagCompound();
		if(seat==1)
		{
			compound.setBoolean("forward", forward);
			compound.setBoolean("backward", backward);
			compound.setBoolean("turnLeft", turnLeft);
			compound.setBoolean("turnRight", turnRight);
		}
		else
		{
			compound.setBoolean("gunPitchUp", gunPitchUp);
			compound.setBoolean("gunPitchDown", gunPitchDown);
			compound.setBoolean("fireKeyPress", fireKeyPress);
			compound.setBoolean("reloadKeyPress", reloadKeyPress);
		}

		return compound;
	}

	public void syncKeyPress(NBTTagCompound tag)
	{
		if(tag.hasKey("forward"))
			forward = tag.getBoolean("forward");
		if(tag.hasKey("backward"))
			backward = tag.getBoolean("backward");
		if(tag.hasKey("turnLeft"))
			turnLeft = tag.getBoolean("turnLeft");
		if(tag.hasKey("turnRight"))
			turnRight = tag.getBoolean("turnRight");
		if(tag.hasKey("gunPitchUp"))
			gunPitchUp = tag.getBoolean("gunPitchUp");
		if(tag.hasKey("gunPitchDown"))
			gunPitchDown = tag.getBoolean("gunPitchDown");
		if(tag.hasKey("fireKeyPress"))
			fireKeyPress = tag.getBoolean("fireKeyPress");
		if(tag.hasKey("reloadKeyPress"))
			reloadKeyPress = tag.getBoolean("reloadKeyPress");
	}

	private void handleClientKeyOutput(int seat)
	{
		if(seat!=1)
		{
			forward = dataManager.get(dataMarkerForward);
			backward = dataManager.get(dataMarkerBackward);
			turnLeft = dataManager.get(dataMarkerTurnLeft);
			turnRight = dataManager.get(dataMarkerTurnRight);
		}
		else
		{
			dataManager.get(dataMarkerForward);
			dataManager.get(dataMarkerBackward);
			dataManager.get(dataMarkerTurnLeft);
			dataManager.get(dataMarkerTurnRight);
		}
		if(seat!=0)
		{
			gunPitchUp = dataManager.get(dataMarkerGunPitchUp);
			gunPitchDown = dataManager.get(dataMarkerGunPitchDown);
		}
		else
		{
			dataManager.get(dataMarkerGunPitchUp);
			dataManager.get(dataMarkerGunPitchDown);
		}

		acceleration = dataManager.get(dataMarkerAcceleration);
		speed = dataManager.get(dataMarkerSpeed);
		shootingProgress = dataManager.get(dataMarkerShootingProgress);
		reloadProgress = dataManager.get(dataMarkerReloadProgress);
		gunPitch = dataManager.get(dataMarkerGunPitch);
		shell = new ItemStack(dataManager.get(dataMarkerShell));

		rightWheelDurability = dataManager.get(dataMarkerWheelRightDurability);
		leftWheelDurability = dataManager.get(dataMarkerWheelLeftDurability);
		mainDurability = dataManager.get(dataMarkerMainDurability);
		gunDurability = dataManager.get(dataMarkerGunDurability);
		shieldDurability = dataManager.get(dataMarkerShieldDurability);

	}

	private void handleServerKeyInput()
	{
		dataManager.set(dataMarkerForward, forward);
		dataManager.set(dataMarkerBackward, backward);
		dataManager.set(dataMarkerTurnLeft, turnLeft);
		dataManager.set(dataMarkerTurnRight, turnRight);
		dataManager.set(dataMarkerGunPitchUp, gunPitchUp);
		dataManager.set(dataMarkerGunPitchDown, gunPitchDown);

		dataManager.set(dataMarkerAcceleration, acceleration);
		dataManager.set(dataMarkerSpeed, speed);
		dataManager.set(dataMarkerShootingProgress, shootingProgress);
		dataManager.set(dataMarkerReloadProgress, reloadProgress);
		dataManager.set(dataMarkerGunPitch, gunPitch);

		dataManager.set(dataMarkerWheelRightDurability, rightWheelDurability);
		dataManager.set(dataMarkerWheelLeftDurability, leftWheelDurability);
		dataManager.set(dataMarkerMainDurability, mainDurability);
		dataManager.set(dataMarkerGunDurability, gunDurability);
		dataManager.set(dataMarkerShieldDurability, shieldDurability);

		dataManager.set(dataMarkerShell, shell.serializeNBT());
	}

	@Override
	public IAdvancedZoomTool getZoom()
	{
		return Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem()==IIContent.itemBinoculars?IIContent.itemBinoculars: null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getZoomStack()
	{
		return Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(source.damageType.equals("bullet")) //immersive vehicles(tm)
		{
			DamageSource temp_source = new DamageSource("bullet").setProjectile().setDamageBypassesArmor();
			//attack every part
			for(EntityVehicleMultiPart part : new EntityVehicleMultiPart[]{partWheelRight, partWheelLeft, partShieldLeft, partShieldRight, partGun})
				attackEntityFromPart(part, temp_source, amount);
			return true;
		}
		return super.attackEntityFrom(source, amount);
	}
}
