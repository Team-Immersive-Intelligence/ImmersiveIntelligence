package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.render.MotorbikeRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 07.07.2020
 */
public class EntityMotorbike extends Entity implements IVehicleMultiPart, IEntitySpecialRepairable
{
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

	public EntityVehicleMultiPart[] partArray;

	public EntityVehicleWheel partWheelFront = new EntityVehicleWheel(this, "wheel_front", 1F, 1.0F);
	public EntityVehicleWheel partWheelBack = new EntityVehicleWheel(this, "wheel_back", 1F, 1.0F);
	public EntityVehicleMultiPart partFuelTank = new EntityVehicleMultiPart(this, "fuel_tank", 1.0F, 0.45F);
	public EntityVehicleMultiPart partEngine = new EntityVehicleMultiPart(this, "engine", 1.0F, 0.45F);

	public int destroyTimer = -1;

	public FluidTank tank = new FluidTank(12000)
	{

	};
	NonSidedFluidHandler fluidHandler = new NonSidedFluidHandler(this);

	public float acceleration = 0f, speed = 0f, tilt = 0f, brakeProgress = 0f;
	public boolean accelerated = false, brake = false, engineWorking = false, willRotate = false, turnLeft = false, turnRight = false, engineKeyPress = false;
	public int wheelTraverse = 0;
	public int frontWheelDurability, backWheelDurability, engineDurability, fuelTankDurability;

	static AxisAlignedBB aabb = new AxisAlignedBB(-2, 0, -2, 2, 1.35, 2);
	static AxisAlignedBB aabb_wheel = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
	static AxisAlignedBB aabb_tank = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 0.45d, -0.5);
	static AxisAlignedBB aabb_engine = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);

	public EntityMotorbike(World worldIn)
	{
		super(worldIn);
		setSize(1, 1.25f);
		stepHeight = 1f;
		partArray = new EntityVehicleMultiPart[]{partWheelFront, partWheelBack, partFuelTank, partEngine};
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
	}

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
	}

	@Override
	public boolean canPassengerSteer()
	{
		return super.canPassengerSteer();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(isPassenger(passenger))
		{
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
			float tylt = -tilt*45;
			double true_angle4 = Math.toRadians((tylt-90) > 180?360f-(tylt-90): (tylt-90));

			Vec3d pos2 = Utils.offsetPosDirection(-0.55f, true_angle, 0);
			Vec3d pos3 = Utils.offsetPosDirection(0.75f, true_angle2, -true_angle4);

			passenger.setPosition(posX+pos2.x+pos3.x, posY+pos3.y, posZ+pos2.z+pos3.z);
		}
	}

	@Override
	public void applyOrientationToEntity(Entity driving)
	{
		if(isPassenger(driving))
		{
			driving.setRenderYawOffset(this.rotationYaw);

			float f = MathHelper.wrapDegrees(driving.rotationYaw-this.rotationYaw);
			float f1 = MathHelper.clamp(f, -55.0F, 55.0F);
			driving.prevRotationYaw += f1-f;
			driving.rotationYaw += f1-f;
			driving.setRotationYawHead(driving.rotationYaw);
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(world.isRemote)
		{
			if(getPassengers().size() > 0&&getPassengers().get(0).equals(ClientUtils.mc().player))
			{
				//Handle, send to server
				handleClientKeyInput();
			}
			else
			{
				//Get from server
				handleClientKeyOutput();
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
			if(world.getTotalWorldTime()%20==0)
				updateTank(false);

			handleServerKeyInput();
		}

		//waste fuel and kill the passenger, try at least ^^
		if(destroyTimer==-1)
		{
			accelerated = accelerated&&getPassengers().size() > 0;
			if(engineDurability <= 0)
				destroyTimer = 100;
		}
		else
		{
			engineWorking = true;
			accelerated = true;
			destroyTimer -= 1;
		}
		if(destroyTimer==0)
			selfDestruct();

		engineWorking = engineWorking&&hasFuel();
		if(engineWorking&&tank.getFluid()!=null&&world.getTotalWorldTime()%DieselHandler.getBurnTime(tank.getFluid().getFluid())/4==0)
			tank.drain((int)Math.ceil(Motorbike.fuelBurnAmount*(speed/20f)), true);
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

		if(tilt!=0&&speed > 0)
		{
			rotationYaw += tilt*(speed/5f);
		}
		if(!engineWorking)
			if(turnLeft||turnRight)
			{
				rotationYaw += tilt*0.25;
				moveRelative(0, 0, -0.025f, 0.35f);
			}

		tilt = MathHelper.clamp(tilt, -1f, 1f);

		if(engineWorking&&accelerated)
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

		wheelTraverse += speed;

		if(world.isRemote)
		{
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw)); //z
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90)); //x

			if(engineWorking)
				spawnExhaustParticle(true_angle, true_angle2);
			if(hasFuel()&&engineDurability < Motorbike.engineDurability*0.85f)
				spawnEngineDamageParticle(true_angle, true_angle2);
		}
		updateParts();
		handleMovement();
		setFlag(7, false);

		//this.setPosition(posX+(partWheelFront.posX-ppx),posY+(partWheelFront.posY-ppy),posZ+(partWheelFront.posZ-ppz));

		// TODO: 08.07.2020 entity damage
		/*
		Vec3d currentPos = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
		RayTraceResult mop = this.world.rayTraceBlocks(currentPos, nextPos, false, true, false);

		if(mop!=null && mop.typeOfHit.equals(Type.ENTITY))
		{

		}
		 */

	}

	private void handleMovement()
	{
		// TODO: 09.07.2020 handle
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));

		Vec3d pos1_x = Utils.offsetPosDirection(-1.25f, true_angle, 0);
		Vec3d pos2_x = Utils.offsetPosDirection(1.25f, true_angle, 0);

		partWheelFront.rotationYaw = this.rotationYaw;
		partWheelFront.travel(0, 0, 1f, -0.2f, speed*0.0125*1.5f);

		if(partWheelFront.collidedHorizontally&&destroyTimer!=-1)
			destroyTimer = 1;
		/*

		partWheelBack.rotationYaw=this.rotationYaw;
		partWheelBack.travel(0,0,1f,-0.2f,speed*0.0125);
		 */

		if(!partWheelFront.isEntityInsideOpaqueBlock())
		{
			Vec3d currentPos = new Vec3d(partWheelFront.posX+pos1_x.x, partWheelFront.posY, partWheelFront.posZ+pos1_x.z);
			setPosition(currentPos.x, currentPos.y, currentPos.z);
		}


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
	}

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
		}

		if(a^accelerated||b^brake||tl^turnLeft||tr^turnRight||en^engineWorking)
			IIPacketHandler.INSTANCE.sendToServer(new MessageEntityNBTSync(this, updateKeys()));
	}

	private void selfDestruct()
	{
		if(!world.isRemote)
		{
			if(hasFuel())
			{
				world.newExplosion(null, posX, posY, posZ, tank.getFluidAmount()/12000f*4, false, false);
			}
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

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return aabb.offset(posX, posY, posZ);
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
		Vec3d exhaustVec = new Vec3d(0, 0.3+exhaustRandom, 0).add(Utils.offsetPosDirection(0.345f, angle2, 0)).add(Utils.offsetPosDirection(-1.5f, angle1, 0));
		Vec3d exhaustSpeedVec = Utils.offsetPosDirection(-0.25f, angle1, 0);

		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX+exhaustVec.x, posY+exhaustVec.y, posZ+exhaustVec.z, 0+exhaustSpeedVec.x, 0.015625, exhaustSpeedVec.z);
	}

	private void spawnEngineDamageParticle(double angle1, double angle2)
	{
		float worldRandom = Math.abs((world.getTotalWorldTime()%40/40f)-0.5f)/0.5f;

		Vec3d smokeVec = new Vec3d(0, 0.5, 0).add(Utils.offsetPosDirection(-0.25f+(worldRandom*0.015625f*8), angle2, 0));
		Vec3d modVec = Utils.offsetPosDirection(4, angle2, 0);
		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX+smokeVec.x, posY+smokeVec.y, posZ+smokeVec.z, worldRandom*0.015625*modVec.x, 0.015625*modVec.y, worldRandom*0.015625*modVec.z);

		if(engineDurability < Motorbike.engineDurability*0.65)
		{

			Vec3d fireVec = new Vec3d(0, 0.55, 0).add(Utils.offsetPosDirection(0.135f, angle2, 0));
			ParticleUtils.spawnFlameFX(posX+fireVec.x, posY+fireVec.y, posZ+fireVec.z, worldRandom*modVec.x, 2, worldRandom*modVec.z, 2.5f, 16);

			if(engineDurability < Motorbike.engineDurability*0.35)
			{
				modVec = Utils.offsetPosDirection(-4, angle2, 0);
				Vec3d fireVec2 = new Vec3d(0, 0.55, 0).add(Utils.offsetPosDirection(-0.2f, angle2, 0));
				ParticleUtils.spawnFlameFX(posX+fireVec2.x, posY+fireVec2.y, posZ+fireVec2.z, worldRandom*modVec.x, 2, worldRandom*modVec.z, 2.5f, 16);

				if(engineDurability < Motorbike.engineDurability*0.2)
				{
					smokeVec = new Vec3d(0, 0.5, 0).add(Utils.offsetPosDirection(-0.25f+(worldRandom*0.015625f*8), angle2, 0));
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX+smokeVec.x, posY+smokeVec.y, posZ+smokeVec.z, worldRandom*0.015625*modVec.x, 0.015625*modVec.y, worldRandom*0.015625*modVec.z);
				}
			}
		}

	}

	private void spawnExplosionParticles()
	{
		ParticleUtils.spawnFlameExplosion(posX, posY, posZ, 1f+(tank.getFluidAmount()/12000f), rand);
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float amount)
	{
		if(part==partEngine&&source.isProjectile()||source.isExplosion()||source.isFireDamage())
		{
			engineDurability -= amount*0.85;
			dataManager.set(dataMarkerEngineDurability, engineDurability);
			return true;
		}
		else if(part==partFuelTank&&source.isProjectile()||source.isExplosion()||source.isFireDamage())
		{
			fuelTankDurability -= amount*0.85;
			dataManager.set(dataMarkerFuelTankDurability, fuelTankDurability);
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
	public World getWorld()
	{
		return this.world;
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

	protected void updateTank(boolean read)
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

	public void writeTank(NBTTagCompound nbt)
	{
		boolean write = tank.getFluidAmount() > 0;
		NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
		if(write)
			nbt.setTag("tank", tankTag);
	}

	public void readTank(NBTTagCompound nbt)
	{
		if(nbt.hasKey("tank"))
		{
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}

	@Override
	public boolean onInteractWithPart(EntityVehicleMultiPart part, EntityPlayer player, EnumHand hand)
	{
		if(!isPassenger(player)&&(part==partFuelTank||part==partEngine))
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
			else if(!world.isRemote)
				player.startRiding(this);

			return true;
		}
		return false;
	}

	@Override
	public String[] getOverlayTextOnPart(EntityVehicleMultiPart part, EntityPlayer player, RayTraceResult mop, boolean hammer)
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
		if(tag.hasKey("accelerated"))
			accelerated = tag.getBoolean("accelerated");
		if(tag.hasKey("brake"))
			brake = tag.getBoolean("brake");
		if(tag.hasKey("turnLeft"))
			turnLeft = tag.getBoolean("turnLeft");
		if(tag.hasKey("turnRight"))
			turnRight = tag.getBoolean("turnRight");
		if(tag.hasKey("engineWorking"))
			engineWorking = tag.getBoolean("engineWorking");
	}

	public Entity[] getParts()
	{
		return partArray;
	}

	void updateParts()
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));

		Vec3d pos1_x = Utils.offsetPosDirection(1.25f, true_angle, 0);
		Vec3d pos2_x = Utils.offsetPosDirection(-1.25f, true_angle, 0);


		/*
		this.partSeatDriver.setLocationAndAngles(posX, posY, posZ, 0.0F, 0);
		this.partSeatPassenger.setLocationAndAngles(posX, posY, posZ, 0.0F, 0);
		this.partSeatTowable.setLocationAndAngles(posX, posY, posZ, 0.0F, 0);
		 */

		this.partWheelFront.setLocationAndAngles(posX+pos1_x.x, posY, posZ+pos1_x.z, 0.0F, 0);
		this.partWheelFront.setEntityBoundingBox(aabb_wheel.offset(this.partWheelFront.posX, this.partWheelFront.posY, this.partWheelFront.posZ));
		this.partWheelFront.onUpdate();

		this.partEngine.setLocationAndAngles(posX, posY, posZ, 0.0F, 0);
		this.partEngine.setEntityBoundingBox(aabb_engine.offset(this.partEngine.posX, this.partEngine.posY, this.partEngine.posZ));
		this.partEngine.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partEngine.onUpdate();

		this.partFuelTank.setLocationAndAngles(posX, posY+1, posZ, 0.0F, 0);
		this.partFuelTank.setEntityBoundingBox(aabb_tank.offset(this.partFuelTank.posX, this.partFuelTank.posY, this.partFuelTank.posZ));
		this.partFuelTank.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partFuelTank.onUpdate();

		this.partWheelBack.setLocationAndAngles(posX+pos2_x.x, posY, posZ+pos2_x.z, 0.0F, 0);
		this.partWheelBack.setEntityBoundingBox(aabb_wheel.offset(this.partWheelBack.posX, this.partWheelBack.posY, this.partWheelBack.posZ));
		this.partWheelBack.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partWheelBack.onUpdate();
	}

	@SideOnly(Side.CLIENT)
	public void spawnDebrisExplosion()
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
		float tylt = -tilt*45;
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
			Vec3d vx = Utils.offsetPosDirection((float)(mod.rotationPointX*0.0625), -true_angle, 0);
			Vec3d vz = Utils.offsetPosDirection((float)(-mod.rotationPointZ*0.0625), true_angle, 0);
			Vec3d vo = new Vec3d(posX, posY, posZ)
					.add(vx)
					.add(Utils.offsetPosDirection((float)(mod.rotationPointY*0.0625), -true_angle2, -true_angle4))
					.add(vz);
			Vec3d vecDir = new Vec3d(rand.nextGaussian()*0.25, rand.nextGaussian()*0.25, rand.nextGaussian()*0.025);

			ParticleUtils.spawnTMTModelFX(vo.x, vo.y, vo.z, (vx.x+vz.x+vecDir.x)/1.5f, (0.25+vecDir.y)/1.5f, (vx.z+vz.z+vecDir.z)/1.5f, 0.0625f, mod, MotorbikeRenderer.texture);
		}
	}
}
