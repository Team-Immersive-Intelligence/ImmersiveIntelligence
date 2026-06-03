package pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ICameraEntity;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.EntityVehicleTowable;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls.MouseBinding;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleType;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.WheelType;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunShootingHandler;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderSingle;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.07.2020
 */
@VehicleBlueprint(id = "immersiveintelligence:towed/field_howitzer", mass = 4, type = VehicleType.TOWED_WEAPON)
public class EntityFieldHowitzer extends EntityVehicleTowable<EntityFieldHowitzer> implements ICameraEntity
{
	//--- AABBs ---//
	static final AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.25, 0d, 0.25, 0.25, 1d, -0.25);
	static final AxisAlignedBB AABB_MAIN = new AxisAlignedBB(-0.5, 0.125d, -0.5, 0.5, 0.625, 0.5);
	static final AxisAlignedBB AABB_GUN = new AxisAlignedBB(-0.35, 0.25d, 0.35, 0.35, 1d, -0.35);
	static final AxisAlignedBB AABB_SHIELD = new AxisAlignedBB(-0.35, 0d, -0.35, 0.35, 1.25d, 0.35);

	//--- Entity Variables ---//
	private AmmoFactory<EntityAmmoArtilleryProjectile> ammoFactory;
	public SeatInfo<EntityFieldHowitzer> seatCommander, seatGunner;
	public EntityVehicleWheel<EntityFieldHowitzer> partWheelRight;
	public EntityVehicleWheel<EntityFieldHowitzer> partWheelLeft;

	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public SyncedDurability durabilityRightWheel, durabilityLeftWheel, durabilityGun, durabilityShield;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_CONTROLS)
	public VehicleControls commanderControls, gunnerControls;

	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public GunAimCoordinate aim;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public GunShootingHandler shootingHandler;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public GunAmmoProvider ammoProvider;
	private EntityVehiclePart<EntityFieldHowitzer> partGun;

	public EntityFieldHowitzer(World worldIn)
	{
		super(worldIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected EntityVehiclePart<EntityFieldHowitzer>[] vehicleInit()
	{
		//Gun internals
		this.ammoFactory = new AmmoFactory<>(this);
		this.aim = new GunAimCoordinate()
				.withPitchLimit(-75, 15);
		this.ammoProvider = new GunAmmoProviderSingle<>(this,
				() -> EntityVehicleSeat.getPassengerOnSeat(seatGunner),
				IIContent.itemAmmoLightArtillery, FieldHowitzer.reloadTime
		);
		this.shootingHandler = new GunShootingHandler()
				.withAmmoFactory(this.ammoFactory)
				.withAmmoProvider(this.ammoProvider)
				.withShootSound(IISounds.howitzerShot, 50);

		//Hitboxes
		this.durabilityRightWheel = new SyncedDurability(FieldHowitzer.wheelDurability, 4);
		this.durabilityLeftWheel = new SyncedDurability(FieldHowitzer.wheelDurability, 4);
		this.durabilityGun = new SyncedDurability(FieldHowitzer.gunDurability, 14)
				.withParent(this.durabilityMain);
		this.durabilityShield = new SyncedDurability(FieldHowitzer.shieldDurability, 32)
				.withParent(this.durabilityMain);

		//Controls
		this.commanderControls = new VehicleControls().withStates("forward", "backwards", "turnLeft", "turnRight");
		this.gunnerControls = new VehicleControls().withStates("up", "down", "fire", "reload");
		if(world.isRemote)
		{
			GameSettings settings = ClientUtils.mc().gameSettings;
			this.commanderControls
					.withKeyBinding(settings.keyBindForward, "forward")
					.withKeyBinding(settings.keyBindBack, "backwards")
					.withKeyBinding(settings.keyBindLeft, "turnLeft")
					.withKeyBinding(settings.keyBindRight, "turnRight");
			this.gunnerControls
					.withKeyBinding(settings.keyBindForward, "up")
					.withKeyBinding(settings.keyBindBack, "down")
					.withMouseBinding(MouseBinding.MOUSE_RIGHT, "fire")
					.withKeyBinding(ClientProxy.keybindZoom, "scope")
					.withKeyBinding(ClientProxy.keybindManualReload, "reload");
		}

		//Seats
		this.seatCommander = new SeatInfo<>(this, "commander")
				.withSettings(false, new Vec3d(-0.25, 0, 0.75))
				.withControls(this.commanderControls);
		this.seatGunner = new SeatInfo<>(this, "gunner")
				.withSettings(false, new Vec3d(-0.25, 0, -0.75))
				.withControls(this.gunnerControls);

		//Parts
		return new EntityVehiclePart[]{
				partWheelRight = new EntityVehicleWheel<>(this, "wheel_right", new Vec3d(0, 0, 0.75), AABB_WHEEL)
						.withHitbox(durabilityRightWheel)
						.withType(WheelType.STEERABLE_DRIVE),
				partWheelLeft = new EntityVehicleWheel<>(this, "wheel_left", new Vec3d(0, 0, -0.75), AABB_WHEEL)
						.withHitbox(durabilityLeftWheel)
						.withType(WheelType.STEERABLE_DRIVE),
				new EntityVehiclePart<>(this, "main", new Vec3d(0, 0.5, 0),
						new AxisAlignedBB(-0.5, -0.25, -0.5, 0.5, 0.25, 0.5))
						.withHitbox(durabilityMain)
						.withSeat(seatCommander),
				new EntityVehiclePart<>(this, "main2", new Vec3d(-0.75, 0.5, 0),
						new AxisAlignedBB(-0.5, -0.125, -0.5, 0.5, 0.125, 0.5))
						.withHitbox(durabilityMain)
						.withSeat(seatGunner),
				this.partGun = new EntityVehiclePart<>(this, "gun", new Vec3d(0.5, 0.65, 0), AABB_GUN)
						.withHitbox(durabilityGun),
				new EntityVehiclePart<>(this, "shield_right", new Vec3d(0.5, 0.385+0.125, 0.75), AABB_SHIELD)
						.withHitbox(durabilityShield),
				new EntityVehiclePart<>(this, "shield_left", new Vec3d(0.5, 0.385+0.125, -0.75), AABB_SHIELD)
						.withHitbox(durabilityShield),
		};
	}

	//--- Main ---//

	@Override
	protected void onVehicleUpdate()
	{
		this.aim.update();
		this.shootingHandler.update();
		this.ammoFactory.setShooterAndGun(EntityVehicleSeat.getPassengerOnSeat(seatGunner), this)
				.setPositionAndVelocity(partGun.getPositionVector(), this.aim, 1f, 1f);

		float left = 0, right = 0;
		if(EntityVehicleSeat.getPassengerOnSeat(seatCommander)!=null)
		{
			//Intentionally swapped, because it's a push-gun, and that's how pushing works
			right = this.commanderControls.getKey("turnLeft")?0.5f: 0;
			left = this.commanderControls.getKey("turnRight")?0.5f: 0;
			boolean backwards = this.commanderControls.getKey("backwards");
			if(backwards||this.commanderControls.getKey("forward"))
			{
				left = Math.max(0.5f, left+0.25f)*(backwards?-1: 1);
				right = Math.max(0.5f, right+0.25f)*(backwards?-1: 1);
			}
		}
		partWheelLeft.setRotationSpeed(360f*left);
		partWheelLeft.setTorque(5f*left);
		partWheelRight.setRotationSpeed(360f*right);
		partWheelRight.setTorque(5f*right);

		//Gun elevation
		this.aim.withCenterYaw(this.rotationYaw);
		if(this.gunnerControls.getKey("up"))
			this.aim.setTarget(0, this.aim.getTargetPitch()-0.5f);
		else if(this.gunnerControls.getKey("down"))
			this.aim.setTarget(0, this.aim.getTargetPitch()+0.5f);

		if(this.gunnerControls.getKey("reload"))
			this.shootingHandler.startReloading();
		else if(this.gunnerControls.getKey("fire"))
			this.shootingHandler.fire();
	}


	//--- Parts Handling ---//

	@Override
	public void onSeatDismount(String seatID, Entity passenger)
	{

	}

	@Override
	public boolean onInteractWithPart(EntityVehiclePart<EntityFieldHowitzer> part, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote&&!towingOperation)
		{
			if(part.partName.contains("right"))
				EntityVehicleSeat.enterSeat(player, seatCommander);
			else if(part.partName.contains("left"))
				EntityVehicleSeat.enterSeat(player, seatGunner);
			return true;
		}
		return false;
	}

	//--- ICameraEntity ---//

	@Override
	public float getCameraPitch(EntityPlayer cameraPlayer, float partialTicks)
	{
		return (float)IIMath.clampedLerp(cameraPlayer.prevCameraYaw, cameraPlayer.cameraYaw, partialTicks);
	}

	@Override
	public float getCameraYaw(EntityPlayer cameraPlayer, float partialTicks)
	{
		return (float)IIMath.clampedLerp(cameraPlayer.prevCameraYaw, cameraPlayer.cameraYaw, partialTicks);
	}

	@Override
	public Vec3d getCameraPos(EntityPlayer cameraPlayer, float partialTicks)
	{
		return cameraPlayer.getLook(partialTicks);
	}

	@Override
	public boolean isCameraEnabled(EntityPlayer player)
	{
		return false;//seatGunner!=null&&seatGunner.isClientPlayerOnSeat();
	}
}
