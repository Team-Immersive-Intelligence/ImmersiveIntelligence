package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.*;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.WheelType;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion.VehicleEngineFuelBased;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion.VehicleTransmission;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.07.2020
 */
@VehicleBlueprint(id = "immersiveintelligence:motorbike", mass = 4, type = VehicleType.MOTORBIKE)
public class EntityMotorbike extends EntityVehicleBase<EntityMotorbike>
{
	//--- AABBs ---//
	private static final AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
	private static final AxisAlignedBB AABB_TANK = new AxisAlignedBB(-0.35, 0d, 0.35, 0.35, 0.55d, -0.35);
	private static final AxisAlignedBB AABB_ENGINE = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 1d, -0.5);
	private static final AxisAlignedBB AABB_SEAT = new AxisAlignedBB(-0.3, -0.25d, 0.3, 0.3, 0.25d, -0.3);

	static
	{
		//Init upgrade tech tree
		UpgradeTechTree.getTreeFor(EntityMotorbike.class)
				.addUpgrade(IIContent.UPGRADE_VEHICLE_SMALL_STORAGE, UpgradeTier.TIER_1)
				.addUpgrade(IIContent.UPGRADE_VEHICLE_ADDITIONAL_PASSENGER_SEAT, UpgradeTier.TIER_1)
				.addUpgrade(IIContent.UPGRADE_VEHICLE_SMALL_ADDITIONAL_TANK, UpgradeTier.TIER_1)
				.addUpgrade(IIContent.UPGRADE_VEHICLE_WOODGAS, UpgradeTier.TIER_2)

				.addLockOut(IIContent.UPGRADE_VEHICLE_SMALL_STORAGE,
						IIContent.UPGRADE_VEHICLE_ADDITIONAL_PASSENGER_SEAT,
						IIContent.UPGRADE_VEHICLE_SMALL_ADDITIONAL_TANK,
						IIContent.UPGRADE_VEHICLE_WOODGAS
				);
	}

	public EntityVehicleWheel<EntityMotorbike> partWheelFront, partWheelBack;
	public EntityVehiclePart<EntityMotorbike> partFuelTank, partEngine;
	public EntityVehiclePart<EntityMotorbike> partSeat, partUpgradeSeat, partUpgradeCargo;
	public SeatInfo<EntityMotorbike> seatRider, seatPassenger, seatTowed;

	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public VehicleDurability frontWheelDurability, backWheelDurability, engineDurability, fuelTankDurability;

	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_FUEL, time = 40)
	public VehicleFuelTank<EntityMotorbike> fuelTank;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_CONTROLS)
	public VehicleControls driverControls;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_FUEL)
	public VehicleEngineFuelBased engine;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_FUEL)
	public VehicleTransmission<EntityMotorbike> transmission;

	public EntityMotorbike(World worldIn)
	{
		super(worldIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected EntityVehiclePart<EntityMotorbike>[] vehicleInit()
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
				.withStates("engine", "tow", "accelerate", "brake", "turnLeft", "turnRight", "gearUp", "gearDown", "honk");
		if(world.isRemote)
		{
			GameSettings settings = ClientUtils.mc().gameSettings;
			this.driverControls
					.withKeyBinding(ClientProxy.keybindVehicleEngine, "engine")
					.withKeyBinding(ClientProxy.keybindVehicleTowing, "tow")
					.withKeyBinding(settings.keyBindForward, "accelerate")
					.withKeyBinding(settings.keyBindBack, "brake")
					.withKeyBinding(settings.keyBindLeft, "turnLeft")
					.withKeyBinding(settings.keyBindRight, "turnRight")
					.withKeyBinding(ClientProxy.keybindVehicleGearUp, "gearUp")
					.withKeyBinding(ClientProxy.keybindVehicleGearDown, "gearDown");
		}

		//Seats
		this.seatRider = new SeatInfo<>(this, "rider")
				.withSettings(true, new Vec3d(-0.65f, 0.75, 0))
				.withYawAngleLimits(0, -45, 45)
				.withControls(this.driverControls);
		this.seatPassenger = new SeatInfo<>(this, "passenger")
				.withSettings(true, new Vec3d(0.75f, 0.75, 0))
				.withYawAngleLimits(0, -90, 90);
		this.seatTowed = new SeatInfo<>(this, "tow")
				.withSettings(true, new Vec3d(1f, 0, 0))
				.withYawAngleLimits(180, -75, 75);

		//Components
		this.partWheelBack = new EntityVehicleWheel<>(this, "wheel_back", new Vec3d(-1.5, 0, 0), AABB_WHEEL)
				.withType(WheelType.DRIVE)
				.withHitbox(backWheelDurability)
				.withWeightShare(0.6);
		this.partWheelFront = new EntityVehicleWheel<>(this, "wheel_front", new Vec3d(1.25, 0, 0), AABB_WHEEL)
				.withType(WheelType.STEERABLE)
				.withHitbox(frontWheelDurability)
				.withWeightShare(0.4);
		this.fuelTank = new VehicleFuelTank<>(this, 12000)
				.withDurability(fuelTankDurability);
		this.engine = new VehicleEngineFuelBased(fuelTank)
				.withDurability(engineDurability);
		this.transmission = new VehicleTransmission<EntityMotorbike>(this.engine)
				.withDurability(engineDurability)
				.withRatios(20, -0.25, 0.5, 1)
				.withReceivers(this.partWheelBack);

		//Parts
		return new EntityVehiclePart[]{
				partWheelBack, partWheelFront,

				partFuelTank = new EntityVehiclePart<>(this, "fuel_tank", new Vec3d(0.1, 1.175, 0), AABB_TANK)
						.withHitbox(fuelTankDurability),
				partEngine = new EntityVehiclePart<>(this, "engine", new Vec3d(0, 0.5, 0), AABB_ENGINE)
						.withHitbox(engineDurability),
				partSeat = new EntityVehiclePart<>(this, "seat", new Vec3d(-0.65, 1.5, 0), AABB_SEAT)
						.withHitbox(durabilityMain)
						.withSeat(seatRider)
		};
	}

	//--- Main ---//

	@Override
	protected void onVehicleUpdate()
	{
		//Apply Controls
		//TODO: 03.12.2025 gearbox operation
		/*if(driverControls.getKey("accelerate"))
			partWheelBack.setMovementFactors(2, 0);
		else if(driverControls.getKey("brake"))
			partWheelBack.setMovementFactors(-1f, 0);
		else
			partWheelBack.setMovementFactors(0f, 0);*/

		partWheelBack.setSteeringAngle(0);
		if(driverControls.getKey("turnLeft"))
			partWheelFront.setSteeringAngle(-45);
		else if(driverControls.getKey("turnRight"))
			partWheelFront.setSteeringAngle(45);
		else
			partWheelFront.setSteeringAngle(0);

		if(driverControls.getKey("gearUp"))
			transmission.shiftUp();
		else if(driverControls.getKey("gearDown"))
			transmission.shiftDown();

	}

	//--- Parts Handling ---//

	@Override
	public void onSeatDismount(String seatID, Entity passenger)
	{
		passenger.attackEntityFrom(IIDamageSources.causeVehicleDamageGetOut(this), (float)(4.5f*IIEntityUtils.getEntityMotion(this).lengthSquared()));
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
			return (T)fuelTank;
		return super.getCapability(capability, facing);
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
					if(DieselHandler.isValidFuel(f.getFluid()))
					{
						FluidUtil.interactWithFluidHandler(player, hand, fuelTank);
						if(!world.isRemote)
							updateEntityForEvent(SyncEvents.ENTITY_VEHICLE_FUEL);
					}
					return true;
				}
			}
			else if(!world.isRemote)
			{
				if(part==partSeat)
					return player.startRiding(EntityVehicleSeat.getOrCreateSeat(seatRider));
				else if(part==partUpgradeSeat)
					return player.startRiding(EntityVehicleSeat.getOrCreateSeat(seatPassenger));
			}
		return false;
	}

	@Override
	public String[] getOverlayTextOnPart(EntityVehiclePart<EntityMotorbike> part, EntityPlayer player, RayTraceResult mop)
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
