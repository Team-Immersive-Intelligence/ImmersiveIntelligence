package pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.EntityVehicleTowable;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleType;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.WheelType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.07.2020
 */
@VehicleBlueprint(id = "immersiveintelligence:towed/field_howitzer", mass = 4, type = VehicleType.TOWED_WEAPON)
public class EntityFieldHowitzer extends EntityVehicleTowable<EntityFieldHowitzer> implements IEntityZoomProvider
{
	//--- AABBs ---//
	static final AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.25, 0d, 0.25, 0.25, 1d, -0.25);
	static final AxisAlignedBB AABB_MAIN = new AxisAlignedBB(-0.5, 0.125d, -0.5, 0.5, 0.625, 0.5);
	static final AxisAlignedBB AABB_GUN = new AxisAlignedBB(-0.35, 0.25d, 0.35, 0.35, 1d, -0.35);
	static final AxisAlignedBB AABB_SHIELD = new AxisAlignedBB(-0.35, 0d, -0.35, 0.35, 1.25d, 0.35);

	//--- Entity Variables ---//
	private AmmoFactory<EntityAmmoArtilleryProjectile> ammoFactory;
	public SeatInfo<EntityFieldHowitzer> seatCommander, seatGunner;

	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public VehicleDurability durabilityRightWheel, durabilityLeftWheel, durabilityGun, durabilityShield;
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_CONTROLS)
	public VehicleControls commanderControls, gunnerControls;

	@SyncNBT
	public boolean alreadyShot = false;
	@SyncNBT
	public ItemStack shell = ItemStack.EMPTY;
	@SyncNBT
	public float shootingProgress = 0f, reloadProgress = 0f, gunPitch = 0f;

	public EntityVehicleWheel<EntityFieldHowitzer> partWheelRight;
	public EntityVehicleWheel<EntityFieldHowitzer> partWheelLeft;
	public EntityVehiclePart<EntityFieldHowitzer> partMain;
	public EntityVehiclePart<EntityFieldHowitzer> partMain2;
	public EntityVehiclePart<EntityFieldHowitzer> partGun;
	public EntityVehiclePart<EntityFieldHowitzer> partShieldRight;
	public EntityVehiclePart<EntityFieldHowitzer> partShieldLeft;

	public EntityFieldHowitzer(World worldIn)
	{
		super(worldIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected EntityVehiclePart<EntityFieldHowitzer>[] vehicleInit()
	{
		//Projectile entity spawner
		this.ammoFactory = new AmmoFactory<>(this);

		//Hitboxes
		this.durabilityRightWheel = new VehicleDurability(FieldHowitzer.wheelDurability, 4);
		this.durabilityLeftWheel = new VehicleDurability(FieldHowitzer.wheelDurability, 4);
		this.durabilityGun = new VehicleDurability(FieldHowitzer.gunDurability, 14)
				.withParent(this.durabilityMain);
		this.durabilityShield = new VehicleDurability(FieldHowitzer.shieldDurability, 32)
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
					.withKeyBinding(settings.keyBindJump, "fire")
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
				partMain = new EntityVehiclePart<>(this, "main", new Vec3d(0, 0.5, 0),
						new AxisAlignedBB(-0.5, -0.25, -0.5, 0.5, 0.25, 0.5))
						.withHitbox(durabilityMain)
						.withSeat(seatCommander),
				partMain2 = new EntityVehiclePart<>(this, "main2", new Vec3d(-0.75, 0.5, 0),
						new AxisAlignedBB(-0.5, -0.125, -0.5, 0.5, 0.125, 0.5))
						.withHitbox(durabilityMain)
						.withSeat(seatGunner),
				partGun = new EntityVehiclePart<>(this, "gun", new Vec3d(0.5, 0.65, 0), AABB_GUN)
						.withHitbox(durabilityGun),
				partShieldRight = new EntityVehiclePart<>(this, "shield_right", new Vec3d(0.5, 0.385+0.125, 0.75), AABB_SHIELD)
						.withHitbox(durabilityShield),
				partShieldLeft = new EntityVehiclePart<>(this, "shield_left", new Vec3d(0.5, 0.385+0.125, -0.75), AABB_SHIELD)
						.withHitbox(durabilityShield),
		};
	}

	//--- Main ---//

	@Override
	protected void onVehicleUpdate()
	{
		boolean backwards = this.commanderControls.getKey("backwards");
		float right = 0, left = 0;

		//
		if(this.commanderControls.getKey("turnLeft"))
			right += 0.25f;
		else if(this.commanderControls.getKey("turnRight"))
			left += 0.25f;
		if(this.commanderControls.getKey("forward")||backwards)
		{
			left = 0.25f;
			right = 0.25f;
			if(backwards)
			{
				left *= -1;
				right *= -1;
			}
		}

		//Gun elevation
		if(this.gunnerControls.getKey("up"))
			gunPitch = Math.min(gunPitch+1f, 76.5f);
		else if(this.gunnerControls.getKey("down"))
			gunPitch = Math.max(gunPitch-1f, -12.5f-12.5f);

		//TODO: 03.12.2025 moving
		/*partWheelLeft.setMovementFactors(left, 0);
		partWheelRight.setMovementFactors(right, 0);*/
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
			if(part==partShieldRight||part==partWheelRight)
				player.startRiding(EntityVehicleSeat.getOrCreateSeat(seatCommander));
			else if(part==partShieldLeft||part==partWheelLeft)
				player.startRiding(EntityVehicleSeat.getOrCreateSeat(seatGunner));
			return true;
		}
		return false;
	}

	//--- Binoculars Zoom Feature ---//

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
}
