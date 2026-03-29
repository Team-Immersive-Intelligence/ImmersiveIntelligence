package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.*;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.*;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion.VehicleEngineFuelBased;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion.VehicleTransmission;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.07.2020
 */
@VehicleBlueprint(id = "immersiveintelligence:tracked_motorbike", mass = 6, type = VehicleType.MOTORBIKE)
public class EntityTrackedMotorbike extends EntityVehicleBase<EntityTrackedMotorbike>
{
	static
	{
		//Init upgrade tech tree
		UpgradeTechTree.getTreeFor(EntityTrackedMotorbike.class);
	}

	public EntityVehicleWheel<EntityTrackedMotorbike> partWheelFront;
	public EntityVehicleWheel<EntityTrackedMotorbike> partWheelLeftFront, partWheelLeft1, partWheelLeft2, partWheelLeftBack;
	public EntityVehicleWheel<EntityTrackedMotorbike> partWheelRightFront, partWheelRight1, partWheelRight2, partWheelRightBack;
	public EntityVehiclePart<EntityTrackedMotorbike> partFuelTank, partEngine;
	public EntityVehiclePart<EntityTrackedMotorbike> partDriverSeat, partPassengerSeat;
	public SeatInfo<EntityTrackedMotorbike> seatDriver, seatPassenger, seatTowed;

	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public VehicleDurability frontWheelDurability, backWheelDurability, engineDurability, fuelTankDurability;

	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_FUEL, time = 40)
	public VehicleFuelTank<EntityTrackedMotorbike> fuelTank;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_CONTROLS)
	public VehicleControls driverControls;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_FUEL)
	public VehicleEngineFuelBased engine;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_FUEL)
	public VehicleTransmission<EntityTrackedMotorbike> transmission1, transmission2;

	public EntityTrackedMotorbike(World worldIn)
	{
		super(worldIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected EntityVehiclePart<EntityTrackedMotorbike>[] vehicleInit()
	{
		//Hitboxes
		this.frontWheelDurability = new VehicleDurability(Motorbike.wheelDurability, 0);
		this.backWheelDurability = new VehicleDurability(Motorbike.wheelDurability, 0);
		this.engineDurability = new VehicleDurability(Motorbike.engineDurability, 7)
				.withParent(this.durabilityMain);
		this.fuelTankDurability = new VehicleDurability(Motorbike.fuelTankDurability, 4)
				.withParent(this.durabilityMain);

		//Controls
		this.driverControls = new VehicleControls()
				.withStates("engine", "tow", "forward", "backward", "turnLeft", "turnRight", "gearUp", "gearDown", "reduction", "honk");
		if(world.isRemote)
		{
			GameSettings settings = ClientUtils.mc().gameSettings;
			this.driverControls
					.withKeyBinding(ClientProxy.keybindVehicleEngine, "engine")
					.withKeyBinding(ClientProxy.keybindVehicleTowing, "tow")
					.withKeyBinding(settings.keyBindForward, "forward")
					.withKeyBinding(settings.keyBindBack, "backward")
					.withKeyBinding(settings.keyBindLeft, "turnLeft")
					.withKeyBinding(settings.keyBindRight, "turnRight")
					.withKeyBinding(ClientProxy.keybindVehicleGearUp, "gearUp")
					.withKeyBinding(ClientProxy.keybindVehicleGearDown, "gearDown")
					.withKeyBinding(ClientProxy.keybindVehicleReductionSwitch, "reduction");
		}

		//Seats
		this.seatDriver = new SeatInfo<>(this, "rider")
				.withSettings(true, new Vec3d(-4/16f, 27/16f-1.25f-0.125, 0))
				.withYawAngleLimits(0, -45, 45)
				.withControls(this.driverControls);
		this.seatPassenger = new SeatInfo<>(this, "passenger")
				.withSettings(true, new Vec3d(-2.25+0.25, 0.25-0.0625, 0))
				.withYawAngleLimits(180, -90, 90);
		this.seatTowed = new SeatInfo<>(this, "tow")
				.withSettings(true, new Vec3d(-3, 0, 0))
				.withYawAngleLimits(180, -75, 75);

		//Wheels
		AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
		AxisAlignedBB AABB_WHEEL_TRACK = new AxisAlignedBB(-0.385, 0.125, 0.385, 0.385, 0.875, -0.385);
		this.partWheelFront = new EntityVehicleWheel<>(this, "wheel_front", new Vec3d(1.25, 0, 0), AABB_WHEEL)
				.withType(WheelType.STEERABLE)
				.withHitbox(frontWheelDurability)
				.withWeightShare(0.3);

		this.partWheelLeftFront = new EntityVehicleWheel<>(this, "wheel_right_front", new Vec3d(0.5, 0.25, -0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.DRIVE)
				.withHitbox(frontWheelDurability)
				.withWeightShare(0);
		this.partWheelRightFront = new EntityVehicleWheel<>(this, "wheel_left_front", new Vec3d(0.5, 0.25, 0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.DRIVE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0);

		this.partWheelLeft1 = new EntityVehicleWheel<>(this, "wheel_right_1", new Vec3d(-0.5, 0, -0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.STEERABLE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0.175);
		this.partWheelRight1 = new EntityVehicleWheel<>(this, "wheel_left_1", new Vec3d(-0.5, 0, 0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.STEERABLE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0.175);
		this.partWheelLeft2 = new EntityVehicleWheel<>(this, "wheel_right_2", new Vec3d(-1.27, 0, -0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.DRIVE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0.175);
		this.partWheelRight2 = new EntityVehicleWheel<>(this, "wheel_left_2", new Vec3d(-1.27, 0, 0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.DRIVE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0.175);

		this.partWheelLeftBack = new EntityVehicleWheel<>(this, "wheel_right_back", new Vec3d(-2.25, 0.25, -0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.DRIVE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0);
		this.partWheelRightBack = new EntityVehicleWheel<>(this, "wheel_left_back", new Vec3d(-2.25, 0.25, 0.75), AABB_WHEEL_TRACK)
				.withType(WheelType.DRIVE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0);

		//Components
		this.fuelTank = new VehicleFuelTank<>(this, 12000)
				.withDurability(fuelTankDurability);
		this.engine = new VehicleEngineFuelBased(fuelTank)
				.withDurability(engineDurability);
		this.transmission1 = new VehicleTransmission<EntityTrackedMotorbike>(this.engine)
				.withDurability(engineDurability)
				.withRatios(20, 0.5, 1)
				.withCurrentGear(0);
		this.transmission2 = new VehicleTransmission<EntityTrackedMotorbike>(this.transmission1)
				.withDurability(engineDurability)
				.withRatios(20, -0.75, 0.5, 1, 1.25)
				.withCurrentGear(1);
		this.transmission1.withReceivers(this.transmission2);
		this.transmission2.withReceivers(
				partWheelLeftFront, partWheelRightFront,
				partWheelLeft1, partWheelLeft2, partWheelRight1, partWheelRight2,
				partWheelLeftBack, partWheelRightBack
		);

		AxisAlignedBB SIDEBOX_AABB = new AxisAlignedBB(-0.3225, -0.25, -0.3225, 0.3225, 0.25, 0.3225);

		this.style.withColor(IIColor.fromHSV(19/64f, 0.35f, 0.85f));

		this.components = new IVehicleComponent[]{
				this.fuelTank, this.engine, this.transmission1, this.transmission2
		};

		//Parts
		return new EntityVehiclePart[]{
				partWheelFront,
				partWheelLeftFront, partWheelRightFront,
				partWheelLeft1, partWheelLeft2, partWheelRight1, partWheelRight2,
				partWheelLeftBack, partWheelRightBack,

				partFuelTank = new EntityVehiclePart<>(this, "fuel_tank", new Vec3d(-1.385-0.0625, 0.935-0.0625-0.25, 0),
						new AxisAlignedBB(-0.3225, 0d, -0.3225, 0.3225, 0.55d, 0.3225))
						.withHitbox(fuelTankDurability),
				partEngine = new EntityVehiclePart<>(this, "engine", new Vec3d(-1.25, 0.935-0.0625, 0),
						new AxisAlignedBB(-0.5, -0.5625, 0.5, 0.5, 0.5625, -0.5))
						.withHitbox(engineDurability),
				partDriverSeat = new EntityVehiclePart<>(this, "seat_driver", new Vec3d(0, 0.5, 0),
						new AxisAlignedBB(-0.5, -0.25d, -0.5, 0.5, 0.25d, 0.5))
						.withHitbox(durabilityMain, false)
						.withSeat(seatDriver),
				partPassengerSeat = new EntityVehiclePart<>(this, "seat_passenger", new Vec3d(-2, 0.5, 0),
						new AxisAlignedBB(-0.425, -0.25d, 0.425, 0.425, 0.25d, -0.425))
						.withHitbox(durabilityMain, false)
						.withSeat(seatPassenger),

				new EntityVehiclePart<>(this, "sidebox_right2", new Vec3d(-0.3225, 1, -0.75-0.0625), SIDEBOX_AABB)
						.withHitbox(durabilityMain),
				new EntityVehiclePart<>(this, "sidebox_right3", new Vec3d(-0.3225*3, 1, -0.75-0.0625), SIDEBOX_AABB)
						.withHitbox(durabilityMain),
				new EntityVehiclePart<>(this, "sidebox_right4", new Vec3d(-0.3225*5, 1, -0.75-0.0625), SIDEBOX_AABB)
						.withHitbox(durabilityMain),

				new EntityVehiclePart<>(this, "sidebox_left2", new Vec3d(-0.3225, 1, 0.75+0.0625), SIDEBOX_AABB)
						.withHitbox(durabilityMain),
				new EntityVehiclePart<>(this, "sidebox_left3", new Vec3d(-0.3225*3, 1, 0.75+0.0625), SIDEBOX_AABB)
						.withHitbox(durabilityMain),
				new EntityVehiclePart<>(this, "sidebox_left4", new Vec3d(-0.3225*5, 1, 0.75+0.0625), SIDEBOX_AABB)
						.withHitbox(durabilityMain),

				new EntityVehiclePart<>(this, "sidebox_front", new Vec3d(0.3225*3-0.125, 1, 0), SIDEBOX_AABB.grow(0.0625))
						.withHitbox(durabilityMain),
		};
	}

	//--- Main ---//

	@Override
	protected void onVehicleUpdate()
	{
		//Toggle engine
		if(driverControls.getKey("engine"))
		{
			driverControls.setKey("engine", false);
			engine.toggle();
		}

		//Primary Transmission
		if(driverControls.getKey("gearUp"))
			transmission2.shiftUp();
		else if(driverControls.getKey("gearDown"))
			transmission2.shiftDown();
		//Secondary Transmission
		if(driverControls.getKey("reduction"))
		{
			driverControls.setKey("reduction", false);
			if(!transmission1.shiftDown())
				transmission1.shiftUp();
		}

		//Apply Controls
		if(driverControls.getKey("turnLeft"))
			partWheelFront.setSteeringAngle(MathHelper.clamp(partWheelFront.getSteeringAngle()-5, -45, 45));
		else if(driverControls.getKey("turnRight"))
			partWheelFront.setSteeringAngle(MathHelper.clamp(partWheelFront.getSteeringAngle()+5, -45, 45));
		else
			partWheelFront.setSteeringAngle(partWheelFront.getSteeringAngle()*0.9f);

		//Handle engine accelleration
		boolean drivingBackwards = transmission2.getDirection()==AxisDirection.NEGATIVE;

		engine.accelerate(driverControls.getKey(drivingBackwards?"backward": "forward"));
		//Handle braking
		float brakeValue = driverControls.getKey(drivingBackwards?"forward": "backward")?1f: 0f;
		partWheelLeft1.setBrakeFactor(brakeValue);
		partWheelLeft2.setBrakeFactor(brakeValue);
		partWheelRight1.setBrakeFactor(brakeValue);
		partWheelRight2.setBrakeFactor(brakeValue);

		//Update components
		engine.onUpdate();
		transmission1.onUpdate();
		transmission2.onUpdate();
		fuelTank.onUpdate();
	}

	//--- Parts Handling ---//

	@Override
	public void onSeatDismount(String seatID, Entity passenger)
	{
		passenger.attackEntityFrom(IIDamageSources.causeVehicleDamageGetOut(this), (float)(4.5f*IIEntityUtils.getEntityMotion(this).lengthSquared()));
	}

	@Override
	public boolean onInteractWithPart(EntityVehiclePart part, EntityPlayer player, EnumHand hand)
	{
		if(!getRecursivePassengers().contains(player))
			if((part==partFuelTank||part==partEngine))
			{
				FluidStack f = FluidUtil.getFluidContained(player.getHeldItem(EnumHand.MAIN_HAND));
				if(f==null)
					f = FluidUtil.getFluidContained(player.getHeldItem(EnumHand.OFF_HAND));

				if(f!=null)
				{
					FluidUtil.interactWithFluidHandler(player, hand, fuelTank);
					return true;
				}
			}
			else
			{
				if(part==partDriverSeat)
					return EntityVehicleSeat.enterSeat(player, seatDriver);
				else if(part==partPassengerSeat)
					return EntityVehicleSeat.enterSeat(player, seatPassenger);
			}
		return false;
	}

	@Override
	public String[] getOverlayTextOnPart(EntityVehiclePart<EntityTrackedMotorbike> part, EntityPlayer player, RayTraceResult mop)
	{
		if(!isPassenger(player)&&(part==partEngine||part==partFuelTank))
			if(Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
			{
				FluidStack fluidStack = fuelTank.getFluid();
				if(fluidStack==null||fluidStack.amount==0)
					return new String[]{I18n.format(Lib.GUI+"empty")};
				return new String[]{fluidStack.getLocalizedName()+": "+fluidStack.amount+"mB"};
			}
		return null;
	}

}
