package pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.EntityVehicleTowable;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleType;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel.WheelType;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.07.2020
 */
@VehicleBlueprint(id = "immersiveintelligence:towed/field_howitzer", mass = 16, type = VehicleType.TOWED_WEAPON)
public class EntityFieldHowitzer extends EntityVehicleTowable<EntityFieldHowitzer> implements IEntityZoomProvider
{
	//--- AABBs ---//
	static final AxisAlignedBB AABB_WHEEL = new AxisAlignedBB(-0.25, 0d, 0.25, 0.25, 1d, -0.25);
	static final AxisAlignedBB AABB_MAIN = new AxisAlignedBB(-0.5, 0.125d, -0.5, 0.5, 0.625, 0.5);
	static final AxisAlignedBB AABB_GUN = new AxisAlignedBB(-0.35, 0.25d, 0.35, 0.35, 1d, -0.35);
	static final AxisAlignedBB AABB_SHIELD = new AxisAlignedBB(-0.35, 0d, -0.35, 0.35, 1.25d, 0.35);

	//--- Entity Variables ---//
	private AmmoFactory<EntityAmmoArtilleryProjectile> ammoFactory;
	public EntityVehicleSeat seatCommander, seatGunner;

	@SyncNBT
	public VehicleDurability durabilityRightWheel, durabilityLeftWheel, durabilityGun, durabilityShield;

	@SyncNBT
	public boolean alreadyShot = false;
	@SyncNBT
	public ItemStack shell = ItemStack.EMPTY;
	@SyncNBT
	public float shootingProgress = 0f, reloadProgress = 0f, gunPitch = 0f;

	//Keyboard keys
	@SyncNBT
	public boolean forward = false, backward = false, turnLeft = false, turnRight = false, reloadKeyPress = false, fireKeyPress = false, gunPitchUp = false, gunPitchDown = false;

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
		this.durabilityGun = new VehicleDurability(FieldHowitzer.gunDurability, 14);
		this.durabilityShield = new VehicleDurability(FieldHowitzer.shieldDurability, 32);

		//Seats
		this.seatCommander = EntityVehicleSeat.getOrCreateSeat(this, "commander");
		this.seatGunner = EntityVehicleSeat.getOrCreateSeat(this, "gunner");

		//Parts
		return new EntityVehiclePart[]{

				partWheelRight = new EntityVehicleWheel<>(this, "wheel_right", new Vec3d(0, 0, 0.75), AABB_WHEEL)
						.withHitbox(durabilityRightWheel)
						.withType(WheelType.STEERABLE_DRIVE),
				partWheelLeft = new EntityVehicleWheel<>(this, "wheel_left", new Vec3d(0, 0, -0.75), AABB_WHEEL)
						.withHitbox(durabilityLeftWheel)
						.withType(WheelType.STEERABLE_DRIVE),
				partMain = new EntityVehiclePart<>(this, "main", Vec3d.ZERO, AABB_MAIN)
						.withHitbox(durabilityMain),
				partMain2 = new EntityVehiclePart<>(this, "main2", new Vec3d(-0.75, 0, 0), AABB_MAIN)
						.withHitbox(durabilityMain),
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
		partWheelLeft.setMovementFactors(0, 0);
		partWheelLeft.setSteeringAngle(0);
		partWheelRight.setMovementFactors(0, 0);
		partWheelRight.setSteeringAngle(0);
	}


	//--- Parts Handling ---//

	@Override
	public void getSeatRidingPosition(String seatID, Entity passenger)
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));

		Vec3d pos2 = IIMath.offsetPosDirection(-0.65f, true_angle, 0);
		Vec3d pos3;

		switch(seatID)
		{
			case "commander":
				pos3 = IIMath.offsetPosDirection(-0.75f, true_angle2, 0);
				if(shootingProgress > FieldHowitzer.fireTime*0.3f)
					pos3 = pos3.addVector(0, -0.2, 0);
				else if((gunPitchDown||gunPitchUp))
					pos3 = pos3.addVector(0, -0.2, 0);
				else if(reloadProgress > 0.2f&&reloadProgress < 0.4f)
					pos3 = pos3.add(pos3.scale(-0.1875*((reloadProgress-0.4)/0.1)));
				else if(reloadProgress > 0.4f&&reloadProgress < 0.5f)
					pos3 = pos3.add(pos3.scale(-0.1875*(1f-(reloadProgress-0.4)/0.1)));

				break;
			case "gunner":
				pos3 = IIMath.offsetPosDirection(0.75f, true_angle2, 0);
				if(shootingProgress > 0)
					pos3 = pos3.addVector(0, -0.2, 0);
				break;
			default:
				pos3 = Vec3d.ZERO;
				break;
		}

		if(setupTime > 0)
		{
			double ticks = MathHelper.clamp((setupTime/(FieldHowitzer.setupTime*0.2)), 0, 1);
			pos3 = pos3.subtract(pos3.scale(ticks*0.55))
					.addVector(0, -0.2, 0);
		}

		passenger.setPosition(posX+pos2.x+pos3.x, posY+pos3.y, posZ+pos2.z+pos3.z);
	}

	@Override
	public void getSeatRidingAngle(String seatID, Entity passenger)
	{
		float yy = this.rotationYaw;
		if(setupTime > 0)
		{
			yy += MathHelper.clamp((setupTime/(FieldHowitzer.setupTime*0.2)), 0, 1)*(seatID.equals("commander")?65: -65);
		}
		else if(seatID.equals("commander")&&reloadProgress > 0)
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
	public boolean shouldSeatPassengerSit(String seatID, Entity passenger)
	{
		return false;
	}

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
				player.startRiding(seatCommander = EntityVehicleSeat.getOrCreateSeat(this, "commander"));
			else if(part==partShieldLeft||part==partWheelLeft)
				player.startRiding(seatGunner = EntityVehicleSeat.getOrCreateSeat(this, "gunner"));
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
