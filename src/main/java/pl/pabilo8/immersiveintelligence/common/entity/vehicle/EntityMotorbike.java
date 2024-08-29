package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.render.vehicle.MotorbikeRenderer;
import pl.pabilo8.immersiveintelligence.client.util.carversound.MovingSoundMotorbikeEngine;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 07.07.2020
 */
public class EntityMotorbike extends Entity implements IVehicleMultiPart, IEntitySpecialRepairable, IEntityAdditionalSpawnData
{
	//--- AABBs ---//
	private static final AxisAlignedBB AABB = new AxisAlignedBB(-2.5, 0, -2.5, 2.5, 1.5, 2.5);
	private static final AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
	private static final AxisAlignedBB AABB_TANK = new AxisAlignedBB(-0.35, 0d, 0.35, 0.35, 0.55d, -0.35);
	private static final AxisAlignedBB AABB_STORAGE = new AxisAlignedBB(-0.35, 0d, 0.35, 0.35, 0.55d, -0.35);
	private static final AxisAlignedBB AABB_ENGINE = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
	private static final AxisAlignedBB AABB_WOODGAS = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
	private static final AxisAlignedBB AABB_SEAT = new AxisAlignedBB(-0.3, -0.25d, 0.3, 0.3, 0.25d, -0.3);

	//--- DataMarkers ---//
	private static final DataParameter<NBTTagCompound> dataMarkerFluid = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.COMPOUND_TAG);
	private static final DataParameter<Integer> dataMarkerFluidCap = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.VARINT);

	private static final DataParameter<Integer> dataMarkerWheelFrontDurability = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerWheelBackDurability = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerEngineDurability = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerFuelTankDurability = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.VARINT);

	private static final DataParameter<Boolean> dataMarkerAccelerated = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerBrake = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerEngineWorking = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerTurnLeft = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerTurnRight = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Float> dataMarkerAcceleration = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerSpeed = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerTilt = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerYaw = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.FLOAT);

	private static final DataParameter<String> dataMarkerUpgrade = EntityDataManager.createKey(EntityMotorbike.class, DataSerializers.STRING);

	//--- Entity Variables ---//
	private final EntityVehiclePart[] partArray;
	public EntityVehicleWheel partWheelFront, partWheelBack;
	public EntityVehiclePart partFuelTank, partEngine;
	public EntityVehiclePart partSeat, partUpgradeSeat, partUpgradeCargo;

	private int destroyTimer = -1;

	public FluidTank tank = new FluidTank(12000)
	{
	};
	NonSidedFluidHandler fluidHandler = new NonSidedFluidHandler(this);

	public float acceleration = 0f, speed = 0f, tilt = 0f, brakeProgress = 0f, engineProgress = 0;
	public boolean accelerated = false, brake = false, engineWorking = false, turnLeft = false, turnRight = false, engineKeyPress = false, towingKeyPress = false;
	public int frontWheelDurability, backWheelDurability, engineDurability, fuelTankDurability;
	public int untowingTries = 0;

	public String upgrade = "";

	public EntityMotorbike(World worldIn)
	{
		super(worldIn);
		setSize(2.5f, 1.5f);
		partArray = new EntityVehiclePart[]{
				partWheelFront = new EntityVehicleWheel(this, "wheel_front", new Vec3d(1.25, 0, 0), AABB_WHEEL),
				partWheelBack = new EntityVehicleWheel(this, "wheel_back", new Vec3d(-1.5, 0, 0), AABB_WHEEL),
				partFuelTank = new EntityVehiclePart(this, "fuel_tank", new Vec3d(0.1, 1.175, 0), AABB_TANK),
				partEngine = new EntityVehiclePart(this, "engine", Vec3d.ZERO, AABB_ENGINE),
				partSeat = new EntityVehiclePart(this, "seat", new Vec3d(-0.65, 1, 0), AABB_SEAT),
				partUpgradeSeat = new EntityVehiclePart(this, "upgrade_seat", new Vec3d(-1.35, 1, 0), AABB_SEAT),
				partUpgradeCargo = new EntityVehiclePart(this, "upgrade_cargo", new Vec3d(-1.35, 1, 0), AABB_STORAGE)
		};

		frontWheelDurability = Motorbike.wheelDurability;
		backWheelDurability = Motorbike.wheelDurability;
		engineDurability = Motorbike.engineDurability;
		fuelTankDurability = Motorbike.fuelTankDurability;
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(dataMarkerFluid, new NBTTagCompound());
		this.dataManager.register(dataMarkerFluidCap, 0);

		this.dataManager.register(dataMarkerWheelFrontDurability, frontWheelDurability);
		this.dataManager.register(dataMarkerWheelBackDurability, backWheelDurability);
		this.dataManager.register(dataMarkerEngineDurability, engineDurability);
		this.dataManager.register(dataMarkerFuelTankDurability, fuelTankDurability);

		this.dataManager.register(dataMarkerAccelerated, false);
		this.dataManager.register(dataMarkerBrake, false);
		this.dataManager.register(dataMarkerTurnRight, false);
		this.dataManager.register(dataMarkerTurnLeft, false);
		this.dataManager.register(dataMarkerEngineWorking, false);

		this.dataManager.register(dataMarkerAcceleration, 1f);
		this.dataManager.register(dataMarkerSpeed, 1f);
		this.dataManager.register(dataMarkerTilt, 1f);
		this.dataManager.register(dataMarkerYaw, 0f);

		this.dataManager.register(dataMarkerUpgrade, "");
		if(world.isRemote)
			playEngineSound();
	}

	@SideOnly(Side.CLIENT)
	private void playEngineSound()
	{
		ClientUtils.mc().getSoundHandler().playSound(new MovingSoundMotorbikeEngine(this));
	}

	//--- NBT Handling ---//
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		readTank(compound);
		acceleration = compound.getFloat("acceleration");
		speed = compound.getFloat("speed");
		tilt = compound.getFloat("tilt");

		accelerated = compound.getBoolean("accelerated");
		brake = compound.getBoolean("brake");
		turnLeft = compound.getBoolean("turnLeft");
		turnRight = compound.getBoolean("turnRight");
		engineWorking = compound.getBoolean("engineWorking");

		if(compound.hasKey("frontWheelDurability"))
			frontWheelDurability = compound.getInteger("frontWheelDurability");
		if(compound.hasKey("backWheelDurability"))
			backWheelDurability = compound.getInteger("backWheelDurability");
		if(compound.hasKey("engineDurability"))
			engineDurability = compound.getInteger("engineDurability");
		if(compound.hasKey("fuelTankDurability"))
			fuelTankDurability = compound.getInteger("fuelTankDurability");

		upgrade = compound.getString("upgrade");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		writeTank(compound);
		compound.setFloat("acceleration", acceleration);
		compound.setFloat("speed", speed);
		compound.setFloat("tilt", tilt);

		compound.setBoolean("accelerated", accelerated);
		compound.setBoolean("brake", brake);
		compound.setBoolean("turnLeft", turnLeft);
		compound.setBoolean("turnRight", turnRight);
		compound.setBoolean("engineWorking", engineWorking);

		compound.setInteger("frontWheelDurability", frontWheelDurability);
		compound.setInteger("backWheelDurability", backWheelDurability);
		compound.setInteger("engineDurability", engineDurability);
		compound.setInteger("fuelTankDurability", fuelTankDurability);

		compound.setString("upgrade", upgrade);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	//--- Parts Handling ---//
	@Override
	protected boolean canFitPassenger(Entity passenger)
	{
		return passenger instanceof EntityVehicleSeat;
	}

	//delete seats with the vehicle
	@Override
	public void setDead()
	{
		getPassengers().forEach(Entity::setDead);
		super.setDead();
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
	public void getSeatRidingPosition(int seatID, Entity passenger)
	{
		if(seatID==0||seatID==1)
		{
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
			float tylt = -((tilt*0.5f)+(tilt*(speed/10f)*0.5f))*20;
			double true_angle4 = Math.toRadians((tylt-90) > 180?360f-(tylt-90): (tylt-90));

			Vec3d pos2 = IIMath.offsetPosDirection(seatID==0?-0.4f: -1.35f, true_angle, 0);
			Vec3d pos3 = IIMath.offsetPosDirection(seatID==0?0.7f: 0.75f, true_angle2, -true_angle4);

			passenger.setPosition(posX+pos2.x+pos3.x+motionX, posY+pos3.y, posZ+pos2.z+pos3.z+motionZ);
		}
		else if(seatID==2)
		{
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
			Vec3d pos_mtb = IIMath.offsetPosDirection(-2.25f, true_angle, 0);

			passenger.setPositionAndUpdate(posX+pos_mtb.x+motionX, posY+motionY, posZ+pos_mtb.z+motionZ);
			passenger.rotationYaw = this.rotationYaw+180;

		}

	}

	@Override
	public void getSeatRidingAngle(int seatID, Entity passenger)
	{
		if(seatID==0||seatID==1)
		{
			passenger.setRenderYawOffset(this.rotationYaw);

			float f = MathHelper.wrapDegrees(passenger.rotationYaw-this.rotationYaw);
			float f1 = MathHelper.clamp(f, -55.0F, 55.0F);
			passenger.prevRotationYaw += f1-f;
			passenger.rotationYaw += f1-f;
			passenger.setRotationYawHead(passenger.rotationYaw);
		}
		else if(seatID==2)
		{
			passenger.prevRotationYaw = 180+this.rotationYaw;
			passenger.rotationYaw = 180+this.rotationYaw;
		}
	}

	@Override
	public boolean shouldSeatPassengerSit(int seatID, Entity passenger)
	{
		return true;
	}

	@Override
	public void onSeatDismount(int seatID, Entity passenger)
	{
		if(speed > 1f&&seatID < 2)
			passenger.attackEntityFrom(IIDamageSources.causeMotorbikeDamageGetOut(this), 4.5f*speed);
	}

	//--- Main ---//
	@Override
	public void onUpdate()
	{
		if(firstUpdate)
		{
			if(!world.isRemote)
			{
				EntityVehicleSeat.getOrCreateSeat(this, 0);
				EntityVehicleSeat.getOrCreateSeat(this, 1);
				EntityVehicleSeat.getOrCreateSeat(this, 2);
			}
			updateParts(this);

		}

		//super.onUpdate();

		if(world.isRemote)
		{
			Entity pre = ClientUtils.mc().player.getRidingEntity();
			handleClientKeyOutput();
			if(pre instanceof EntityVehicleSeat&&pre.getRidingEntity()==this&&((EntityVehicleSeat)pre).seatID==0)
			{
				updateParts(this);
				handleClientKeyInput();
			}

			//for animation only, not synced
			if(brake)
			{
				brakeProgress = Math.min(brakeProgress+0.15f, 1f);
			}
			else
			{
				brakeProgress = Math.max(brakeProgress-0.25f, 0f);
			}
		}

		if(!world.isRemote)
		{
			updateTank(false);
			handleServerKeyInput();
			dataManager.set(dataMarkerUpgrade, upgrade);
		}

		//waste fuel and kill the passenger, try at least ^^
		if(destroyTimer==-1)
		{
			accelerated = accelerated&&getPassengers().stream().anyMatch(entity -> entity instanceof EntityVehicleSeat&&((EntityVehicleSeat)entity).seatID==0&&entity.getPassengers().size() > 0);
			if(engineDurability <= 0)
				destroyTimer = 100;
		}
		else
		{
			engineWorking = true;
			accelerated = true;
			destroyTimer -= 1;
		}
		if(!world.isRemote&&destroyTimer==0)
			selfDestruct();

		engineProgress = MathHelper.clamp(engineProgress+(engineWorking&&hasFuel()?1: -25), 0, 25);

		engineWorking = engineWorking&&hasFuel();
		if(engineWorking&&tank.getFluid()!=null&&world.getTotalWorldTime()%(DieselHandler.getBurnTime(tank.getFluid().getFluid())/20f)==0)
			tank.drain((int)Math.ceil(Motorbike.fuelBurnAmount*(speed/4f)), true);
		if(turnLeft)
			tilt -= 0.1f;
		else if(turnRight)
			tilt += 0.1f;
		else if(tilt!=0)
		{
			tilt = tilt < 0?tilt+0.1f: tilt-0.1f;
			if(Math.abs(tilt) < 0.01f)
				tilt = 0;
		}
		tilt = MathHelper.clamp(tilt, -1f, 1f);

		boolean canTowedMove = getRecursivePassengers().stream().noneMatch(entity -> entity instanceof ITowable&&!((ITowable)entity).canMoveTowed());

		if(engineWorking&&engineProgress >= 25&&accelerated&&canTowedMove)
		{
			acceleration = Math.min(acceleration+0.1f, 1f);
		}
		else
		{
			acceleration = Math.max(acceleration-0.15f, 0f);
		}
		speed = MathHelper.clamp(speed+(acceleration > 0?(0.25f*acceleration): -0.3f), 0, 20);

		if(brake)
			speed *= 0.85;

		partWheelFront.wheelTraverse += speed*2*(1f-tilt);
		partWheelBack.wheelTraverse += speed*2;

		List<Entity> towables = EntityVehicleSeat.getOrCreateSeat(this, 2).getPassengers();
		if(towables.size() > 0)
		{
			if(towables.get(0) instanceof ITowable)
				((ITowable)towables.get(0)).moveTowableWheels(speed);
		}

		if(world.isRemote)
		{
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw)); //z
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90)); //x

			if(engineWorking&&engineProgress >= 25)
				spawnExhaustParticle(true_angle, true_angle2);
			if(hasFuel()&&engineDurability < Motorbike.engineDurability*0.85f)
				spawnEngineDamageParticle(true_angle, true_angle2);
		}


		/*
		if(world.isRemote&&world.getTotalWorldTime()%60==0)

		 */
		//if(!world.isRemote)
		updateParts(this);
		handleMovement();

		if(tilt!=0&&speed > 0)
		{
			rotationYaw += tilt*(speed/10f)*3.5f;
		}
		if(!engineWorking)
			if(turnLeft||turnRight)
			{
				rotationYaw += tilt*0.5;
			}

		updateParts(this);
		super.onUpdate();

	}

	private void handleMovement()
	{
		//float r = world.isRemote?rotationYaw: -rotationYaw;
		//double true_angle = Math.toRadians((r) > 180?360f-(r): (r));
		//IILogger.info(true_angle);
		this.prevRotationYaw = MathHelper.wrapDegrees(prevRotationYaw);
		this.rotationYaw = MathHelper.wrapDegrees(rotationYaw);

		Vec3d pos1_x = getLookVec().scale(-1.25f);

		partWheelFront.rotationYaw = this.rotationYaw+(tilt*5);
		/*
		partWheelFront.move(MoverType.SELF,dd.x,dd.y+partWheelFront.stepHeight,dd.z);
		partWheelFront.move(MoverType.SELF,0,(-0.5f+(Math.min(speed,1f)*0.125f))-partWheelFront.stepHeight,0);
		 */
		partWheelFront.travel(0, 0, world.isRemote?0f: 1f, -0.0125f, speed*0.0125*2f);

		partWheelBack.rotationYaw = this.rotationYaw;
		partWheelBack.travel(0, 0, world.isRemote?0f: 1f, -0.0125f, speed*0.0125*2f);
		/*
		partWheelBack.move(MoverType.SELF,dd.x,dd.y+partWheelBack.stepHeight,dd.z);
		partWheelBack.move(MoverType.SELF,0,-0.25f-partWheelBack.stepHeight,0);
		 */

		if(partWheelFront.collidedHorizontally&&destroyTimer!=-1)
			destroyTimer = 1;

		if(!partWheelFront.isEntityInsideOpaqueBlock())
		{
			motionX = partWheelFront.motionX;
			motionY = partWheelFront.motionY;
			motionZ = partWheelFront.motionZ;
			markVelocityChanged();
			Vec3d currentPos = new Vec3d(partWheelFront.posX+pos1_x.x, partWheelFront.posY, partWheelFront.posZ+pos1_x.z);
			setPosition(currentPos.x, currentPos.y, currentPos.z);
		}


		if(world.isRemote)
			IIEntityUtils.setEntityMotion(this, partWheelFront.motionX, partWheelFront.motionY, partWheelFront.motionZ);


		if(!world.isRemote&&speed > 1f)
		{
			List<EntityLivingBase> entitiesWithinAABB = world.getEntitiesWithinAABB(EntityLivingBase.class, partWheelFront.getEntityBoundingBox());
			for(EntityLivingBase e : entitiesWithinAABB)
			{
				if(!isPassenger(e))
					e.attackEntityFrom(IIDamageSources.causeMotorbikeDamage(this), 6f*speed);
			}
		}

		if(partWheelBack.isEntityInsideOpaqueBlock())
			partWheelBack.posY = partWheelFront.posY;
	}

	private void handleServerKeyInput()
	{
		dataManager.set(dataMarkerAccelerated, accelerated);
		dataManager.set(dataMarkerBrake, brake);
		dataManager.set(dataMarkerTurnLeft, turnLeft);
		dataManager.set(dataMarkerTurnRight, turnRight);
		dataManager.set(dataMarkerEngineWorking, engineWorking);

		dataManager.set(dataMarkerAcceleration, acceleration);
		dataManager.set(dataMarkerSpeed, speed);
		dataManager.set(dataMarkerTilt, tilt);
		dataManager.set(dataMarkerYaw, rotationYaw);

		dataManager.set(dataMarkerEngineDurability, engineDurability);
		dataManager.set(dataMarkerFuelTankDurability, fuelTankDurability);
		dataManager.set(dataMarkerWheelBackDurability, frontWheelDurability);
		dataManager.set(dataMarkerWheelFrontDurability, backWheelDurability);
	}

	private void handleClientKeyOutput()
	{
		accelerated = dataManager.get(dataMarkerAccelerated);
		brake = dataManager.get(dataMarkerBrake);
		turnLeft = dataManager.get(dataMarkerTurnLeft);
		turnRight = dataManager.get(dataMarkerTurnRight);
		engineWorking = dataManager.get(dataMarkerEngineWorking);
		acceleration = dataManager.get(dataMarkerAcceleration);
		speed = dataManager.get(dataMarkerSpeed);

		engineDurability = dataManager.get(dataMarkerEngineDurability);
		backWheelDurability = dataManager.get(dataMarkerWheelBackDurability);
		frontWheelDurability = dataManager.get(dataMarkerWheelFrontDurability);
		fuelTankDurability = dataManager.get(dataMarkerFuelTankDurability);

		upgrade = dataManager.get(dataMarkerUpgrade);

	}

	//--- Input/Controls Handling and Sync ---//
	private void handleClientKeyInput()
	{
		boolean a = accelerated, b = brake, tl = turnLeft, tr = turnRight, en = engineWorking;
		if(world.getTotalWorldTime()%20==0)
			updateTank(true);
		accelerated = ClientUtils.mc().gameSettings.keyBindForward.isKeyDown();
		brake = ClientUtils.mc().gameSettings.keyBindBack.isKeyDown();
		turnLeft = ClientUtils.mc().gameSettings.keyBindLeft.isKeyDown();
		turnRight = ClientUtils.mc().gameSettings.keyBindRight.isKeyDown();

		if(destroyTimer==-1)
		{
			if(ClientProxy.keybind_motorbikeEngine.isKeyDown())
			{
				if(!engineKeyPress)
				{
					engineKeyPress = true;
					engineWorking = !engineWorking;
				}
			}
			else
				engineKeyPress = false;

			if(ClientProxy.keybind_motorbikeTowing.isKeyDown())
			{
				if(!towingKeyPress)
				{
					towingKeyPress = true;
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("startTowing", true);
					IIPacketHandler.sendToServer(new MessageEntityNBTSync(this, tag));
				}
			}
			else
				towingKeyPress = false;
		}

		if(a^accelerated||b^brake||tl^turnLeft||tr^turnRight||en^engineWorking)
			IIPacketHandler.sendToServer(new MessageEntityNBTSync(this, updateKeys()));
	}

	public void selfDestruct()
	{
		if(!world.isRemote)
		{
			if(hasFuel())
				world.newExplosion(null, posX, posY, posZ, tank.getFluidAmount()/12000f*4, false, false);
			IIPacketHandler.sendToClient(new MessageParticleEffect("motorbike_explosion", world, new Vec3d(this.getEntityId(), 0, 0)));
			setDead();
		}
		else
		{
			spawnDebrisExplosion();
			if(hasFuel())
				spawnExplosionParticles();
		}
	}

	@SideOnly(Side.CLIENT)
	private NBTTagCompound updateKeys()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("accelerated", accelerated);
		compound.setBoolean("brake", brake);
		compound.setBoolean("turnLeft", turnLeft);
		compound.setBoolean("turnRight", turnRight);
		compound.setBoolean("engineWorking", engineWorking);
		return compound;
	}

	private boolean hasFuel()
	{
		return tank.getFluidAmount() > 0;
	}

	//--- Collision ---//
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

	private void spawnExhaustParticle(double angle1, double angle2)
	{
		float exhaustRandom = (world.getTotalWorldTime()%20/20f)*0.2f;
		Vec3d exhaustVec = new Vec3d(0, 0.3+exhaustRandom, 0).add(IIMath.offsetPosDirection(0.345f, angle2, 0)).add(IIMath.offsetPosDirection(-1.5f, angle1, 0));
		Vec3d exhaustSpeedVec = IIMath.offsetPosDirection(-0.25f, angle1, 0);

		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX+exhaustVec.x, posY+exhaustVec.y, posZ+exhaustVec.z, 0+exhaustSpeedVec.x, 0.015625, exhaustSpeedVec.z);
	}

	private void spawnEngineDamageParticle(double angle1, double angle2)
	{
		float worldRandom = Math.abs((world.getTotalWorldTime()%40/40f)-0.5f)/0.5f;

		Vec3d smokeVec = new Vec3d(0, 0.5, 0).add(IIMath.offsetPosDirection(-0.25f+(worldRandom*0.015625f*8), angle2, 0));
		Vec3d modVec = IIMath.offsetPosDirection(0.2f, angle2, 0);
		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX+smokeVec.x, posY+smokeVec.y, posZ+smokeVec.z, worldRandom*0.015625*modVec.x, 0.015625*modVec.y, worldRandom*0.015625*modVec.z);

		if(engineDurability < Motorbike.engineDurability*0.65)
		{

			Vec3d fireVec = new Vec3d(0, 0.55, 0).add(IIMath.offsetPosDirection(0.135f, angle2, 0));
			ParticleRegistry.spawnFlameFX(getPositionVector().add(fireVec), new Vec3d(worldRandom*modVec.x, 0.1, worldRandom*modVec.z), 2.5f, 16);

			if(engineDurability < Motorbike.engineDurability*0.35)
			{
				modVec = IIMath.offsetPosDirection(-0.2f, angle2, 0);
				Vec3d fireVec2 = new Vec3d(0, 0.55, 0).add(IIMath.offsetPosDirection(-0.2f, angle2, 0));
				ParticleRegistry.spawnFlameFX(getPositionVector().add(fireVec2), new Vec3d(worldRandom*modVec.x, 0.1, worldRandom*modVec.z), 2.5f, 16);

				if(engineDurability < Motorbike.engineDurability*0.2)
				{
					smokeVec = new Vec3d(0, 0.5, 0).add(IIMath.offsetPosDirection(-0.25f+(worldRandom*0.015625f*8), angle2, 0));
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX+smokeVec.x, posY+smokeVec.y, posZ+smokeVec.z, worldRandom*0.015625*modVec.x, 0.015625*modVec.y, worldRandom*0.015625*modVec.z);
				}
			}
		}

	}

	private void spawnExplosionParticles()
	{
		ParticleRegistry.spawnFlameExplosion(getPositionVector(), 1f+(tank.getFluidAmount()/12000f), rand);
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float amount)
	{
		if(part==partEngine&&source.isProjectile()||source.isExplosion()||source.isFireDamage())
		{
			engineDurability -= amount*0.85;
			dataManager.set(dataMarkerEngineDurability, engineDurability);
			if(world.isRemote)
				playSound(IISounds.hitMetal.getImpactSound(), Math.min(amount/16f, 1f), 1f);
		}
		else if((part==partFuelTank||part==partSeat)&&source.isProjectile()||source.isExplosion()||source.isFireDamage())
		{
			fuelTankDurability -= amount*0.85;
			dataManager.set(dataMarkerFuelTankDurability, fuelTankDurability);
			if(world.isRemote)
				playSound(IISounds.hitMetal.getImpactSound(), Math.min(amount/16f, 1f), 1f);
		}
		else if(part==partWheelFront)
		{
			frontWheelDurability -= amount;
			dataManager.set(dataMarkerWheelFrontDurability, frontWheelDurability);
		}
		else if(part==partWheelBack)
		{
			backWheelDurability -= amount;
			dataManager.set(dataMarkerWheelBackDurability, backWheelDurability);
		}
		else
			return false;
		return true;
	}

	@Override
	public boolean canRenderOnFire()
	{
		//gets replaced by particle effects
		return false;
	}


	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T)fluidHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean canRepair()
	{
		return engineDurability < Motorbike.engineDurability||
				fuelTankDurability < Motorbike.fuelTankDurability||
				frontWheelDurability < Motorbike.wheelDurability||
				backWheelDurability < Motorbike.wheelDurability;
	}

	@Override
	public boolean repair(int repairPoints)
	{
		if(engineDurability < Motorbike.engineDurability)
			engineDurability = Math.min(engineDurability+repairPoints, Motorbike.engineDurability);
		else if(fuelTankDurability < Motorbike.fuelTankDurability)
			fuelTankDurability = Math.min(fuelTankDurability+repairPoints, Motorbike.fuelTankDurability);
		else if(frontWheelDurability < Motorbike.wheelDurability)
			frontWheelDurability = Math.min(frontWheelDurability+repairPoints, Motorbike.wheelDurability);
		else if(backWheelDurability < Motorbike.wheelDurability)
			backWheelDurability = Math.min(backWheelDurability+repairPoints, Motorbike.wheelDurability);
		else return false;
		return true;
	}

	@Override
	public int getRepairCost()
	{
		return 1;
	}

	private void updateTank(boolean read)
	{
		if(read)
			readTank(dataManager.get(dataMarkerFluid));
		else
		{
			NBTTagCompound tag = new NBTTagCompound();
			writeTank(tag);
			dataManager.set(dataMarkerFluid, tag);
		}
	}

	private void writeTank(NBTTagCompound nbt)
	{
		boolean write = tank.getFluidAmount() > 0;
		NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
		if(write)
			nbt.setTag("tank", tankTag);
	}

	private void readTank(NBTTagCompound nbt)
	{
		if(nbt.hasKey("tank"))
		{
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}

	@Override
	public boolean onInteractWithPart(EntityVehiclePart part, EntityPlayer player, EnumHand hand)
	{
		if(!getRecursivePassengers().contains(player))
		{
			if((part==partFuelTank||part==partEngine))
			{
				FluidStack f = FluidUtil.getFluidContained(player.getHeldItem(EnumHand.MAIN_HAND));
				if(f==null)
					f = FluidUtil.getFluidContained(player.getHeldItem(EnumHand.OFF_HAND));

				if(f!=null)
				{
					if(DieselHandler.isValidFuel(f.getFluid()))
					{
						FluidUtil.interactWithFluidHandler(player, hand, tank);
						if(!world.isRemote)
							updateTank(false);
					}
					return true;
				}
			}
			else if(!world.isRemote&&part==partSeat)
				return player.startRiding(EntityVehicleSeat.getOrCreateSeat(this, 0));
			else if(!world.isRemote&&upgrade.equals("seat")&&part==partUpgradeSeat)
				return player.startRiding(EntityVehicleSeat.getOrCreateSeat(this, 1));
		}
		return false;
	}

	@Override
	public String[] getOverlayTextOnPart(EntityVehiclePart part, EntityPlayer player, RayTraceResult mop)
	{
		if(!isPassenger(player)&&(part==partEngine||part==partFuelTank))
		{
			if(blusunrize.immersiveengineering.common.util.Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
			{
				String s;
				if(tank.getFluid()!=null)
					s = tank.getFluid().getLocalizedName()+": "+tank.getFluidAmount()+"mB";
				else
					s = I18n.format(Lib.GUI+"empty");
				return new String[]{s};
			}
		}
		return null;
	}

	public void setUpgrade(String upgrade)
	{
		this.upgrade = upgrade;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound tag = ByteBufUtils.readTag(additionalData);
		if(tag!=null)
			readEntityFromNBT(tag);
	}

	static class NonSidedFluidHandler implements IFluidHandler
	{
		EntityMotorbike motorbike;

		NonSidedFluidHandler(EntityMotorbike motorbike)
		{
			this.motorbike = motorbike;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null)
				return 0;
			if(!DieselHandler.isValidFuel(resource.getFluid()))
				return 0;

			int i = motorbike.tank.fill(resource, doFill);
			if(i > 0)
			{
				motorbike.updateTank(false);
			}
			return i;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			if(resource==null)
				return null;
			return this.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			FluidStack f = motorbike.tank.drain(maxDrain, doDrain);
			if(f!=null&&f.amount > 0)
			{
				motorbike.updateTank(false);
			}
			return f;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return motorbike.tank.getTankProperties();
		}
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		super.notifyDataManagerChange(key);
	}

	public void syncKeyPress(NBTTagCompound tag)
	{
		if(tag.hasKey("startTowing"))
		{
			if(acceleration > 0)
				return;
			EntityVehicleSeat seat = EntityVehicleSeat.getOrCreateSeat(this, 2);

			//dismount
			if(seat.getPassengers().size() > 0)
			{
				Entity towed = seat.getPassengers().get(0);
				if(towed instanceof ITowable)
				{
					((ITowable)towed).stopTowing();
				}
				else
					towed.dismountRidingEntity();

				getRecursivePassengers().forEach(entity -> {
					if(entity instanceof EntityPlayer)
					{
						((EntityPlayer)entity).sendStatusMessage(new TextComponentTranslation(IIReference.INFO_KEY+"towing.untowed"), true);
					}
				});
			}
			//mount
			else
			{
				List<Entity> entitiesWithinAABB = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox(), input -> input instanceof ITowable);
				if(entitiesWithinAABB.size() > 0)
				{
					//sort based on distance
					entitiesWithinAABB.sort((o1, o2) -> (int)(((o1.getPositionVector().distanceTo(getPositionVector()))-(o2.getPositionVector().distanceTo(getPositionVector())))*10));
					assert entitiesWithinAABB.get(0) instanceof ITowable;
					((ITowable)entitiesWithinAABB.get(0)).startTowing(seat);
					getRecursivePassengers().forEach(entity -> {
						if(entity instanceof EntityPlayer)
						{
							((EntityPlayer)entity).sendStatusMessage(new TextComponentTranslation(IIReference.INFO_KEY+"towing.towed"), true);
						}
					});
				}
				else
					getRecursivePassengers().forEach(entity -> {
						if(entity instanceof EntityPlayer)
						{
							if(untowingTries < 50)
								((EntityPlayer)entity).sendStatusMessage(new TextComponentTranslation(IIReference.INFO_KEY+"towing.no"), true);
							else
							{
								((EntityPlayer)entity).sendStatusMessage(new TextComponentTranslation(IIReference.INFO_KEY+"towing.no_easter_egg"), true);
								untowingTries = 0;
							}
							untowingTries += 1;
						}
					});
			}
		}
		else
		{
			if(tag.hasKey("accelerated"))
				accelerated = tag.getBoolean("accelerated");
			if(tag.hasKey("brake"))
				brake = tag.getBoolean("brake");
			if(tag.hasKey("turnLeft"))
				turnLeft = tag.getBoolean("turnLeft");
			if(tag.hasKey("turnRight"))
				turnRight = tag.getBoolean("turnRight");
			if(tag.hasKey("engineWorking"))
			{
				boolean ew = engineWorking;
				engineWorking = tag.getBoolean("engineWorking");
				if(ew^engineWorking&&engineWorking)
					world.playSound(null, posX, posY, posZ, hasFuel()?IISounds.motorbikeStart: IISounds.motorbikeStartNoFuel, SoundCategory.NEUTRAL, 1f, 0f);
			}
		}

	}

	@Override
	public EntityVehiclePart[] getParts()
	{
		return partArray;
	}

	@SideOnly(Side.CLIENT)
	private void spawnDebrisExplosion()
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
		float tylt = -(tilt*(speed/15f))*45;
		double true_angle4 = Math.toRadians((tylt-90) > 180?360f-(tylt-90): (tylt-90));

		ArrayList<ModelRendererTurbo> models = new ArrayList<>();
		models.addAll(Arrays.asList(MotorbikeRenderer.model.baseModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.engineModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.exhaustPipesModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.steeringGearModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.frontThingyModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.frontThingyUpperModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.backWheelModel));
		models.addAll(Arrays.asList(MotorbikeRenderer.model.frontWheelModel));

		for(ModelRendererTurbo mod : models)
		{
			Vec3d vx = IIMath.offsetPosDirection((float)(mod.rotationPointX*0.0625), -true_angle, 0);
			Vec3d vz = IIMath.offsetPosDirection((float)(-mod.rotationPointZ*0.0625), true_angle, 0);
			Vec3d vo = new Vec3d(posX, posY, posZ)
					.add(vx)
					.add(IIMath.offsetPosDirection((float)(mod.rotationPointY*0.0625), -true_angle2, -true_angle4))
					.add(vz);
			Vec3d vecDir = new Vec3d(rand.nextGaussian()*0.25, rand.nextGaussian()*0.25, rand.nextGaussian()*0.025);

			ParticleRegistry.spawnTMTModelFX(vo, vx.add(vz).add(vecDir).scale(0.66), 0.0625f, mod, MotorbikeRenderer.TEXTURE);
		}
	}

	@Override
	public void playSound(SoundEvent soundIn, float volume, float pitch)
	{
		super.playSound(soundIn, volume, pitch);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(source.damageType.equals("bullet")) //immersive vehicles(tm)
		{
			DamageSource temp_source = new DamageSource("bullet").setProjectile().setDamageBypassesArmor();
			//attack every part
			for(EntityVehiclePart part : new EntityVehiclePart[]{partWheelFront, partWheelBack, partFuelTank, partEngine})
				attackEntityFromPart(part, temp_source, amount);
			return true;
		}
		return super.attackEntityFrom(source, amount);
	}
}
