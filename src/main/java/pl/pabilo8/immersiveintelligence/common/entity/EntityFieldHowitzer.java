package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 18.07.2020
 */
public class EntityFieldHowitzer extends Entity implements IVehicleMultiPart, IEntitySpecialRepairable, ITowable
{
	private static final DataParameter<Integer> dataMarkerWheelRightDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerWheelLeftDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerMainDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerGunDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> dataMarkerShieldDurability = EntityDataManager.createKey(EntityFieldHowitzer.class, DataSerializers.VARINT);
	public int rightWheelDurability, leftWheelDurability, mainDurability, gunDurability, shieldDurability;
	public int setupTime = 0;
	public boolean towingOperation = false;
	public int wheelTraverse = 0;
	//Yes
	public float acceleration = 0f, speed = 0f;

	public EntityVehicleMultiPart[] partArray;

	public EntityVehicleWheel partWheelRight = new EntityVehicleWheel(this, "wheel_right", 1F, 1.0F);
	public EntityVehicleWheel partWheelLeft = new EntityVehicleWheel(this, "wheel_left", 1F, 1.0F);
	public EntityVehicleMultiPart partMain = new EntityVehicleMultiPart(this, "main", 1F, 1.0F);
	public EntityVehicleMultiPart partGun = new EntityVehicleMultiPart(this, "gun", 1F, 1.0F);
	public EntityVehicleMultiPart partShieldRight = new EntityVehicleMultiPart(this, "shield_right", 1F, 1.0F);
	public EntityVehicleMultiPart partShieldLeft = new EntityVehicleMultiPart(this, "shield_left", 1F, 1.0F);
	public int destroyTimer = -1;


	static AxisAlignedBB aabb = new AxisAlignedBB(-1.5, 0, -1.5, 1.5, 1.75, 1.5);
	static AxisAlignedBB aabb_wheel = new AxisAlignedBB(-0.25, 0d, 0.5, 0.25, 1d, -0.5);
	static AxisAlignedBB aabb_main = new AxisAlignedBB(-0.5, 0.125d, 0.5, 0.5, 0.625, -1.25);
	static AxisAlignedBB aabb_gun = new AxisAlignedBB(-0.25, 0d, 0.75, 0.25, 1d, -0.75);
	static AxisAlignedBB aabb_shield = new AxisAlignedBB(-0.5, 0d, 0.5, 0.5, 0.45d, -0.5);

	Entity towingEntity = null;

	public EntityFieldHowitzer(World worldIn)
	{
		super(worldIn);
		partArray = new EntityVehicleMultiPart[]{partWheelRight, partWheelLeft, partMain, partGun, partShieldRight, partShieldLeft};
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(dataMarkerWheelRightDurability, rightWheelDurability);
		this.dataManager.register(dataMarkerWheelLeftDurability, leftWheelDurability);
		this.dataManager.register(dataMarkerMainDurability, mainDurability);
		this.dataManager.register(dataMarkerGunDurability, gunDurability);
		this.dataManager.register(dataMarkerShieldDurability, shieldDurability);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{

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

	@Override
	public void onUpdate()
	{

		super.onUpdate();

		updateParts();
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
		return towingEntity;
	}

	@Override
	public boolean startTowing(Entity tower)
	{
		if(towingEntity==null&&getPassengers().size()==0)
		{
			towingOperation = true;
			setupTime = 0;
			towingEntity = tower;
			return true;
		}
		return false;
	}

	@Override
	public boolean stopTowing()
	{
		if(towingEntity!=null&&getPassengers().size()==0)
		{
			towingOperation = true;
			setupTime = 0;
			towingEntity = null;
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
	public void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));

			Vec3d pos2 = Utils.offsetPosDirection(-0.65f, true_angle, 0);
			Vec3d pos3 = Utils.offsetPosDirection(0.75f, true_angle2, 0);

			passenger.setPosition(posX+pos2.x+pos3.x, posY+pos3.y, posZ+pos2.z+pos3.z);
		}
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.setRenderYawOffset(this.rotationYaw);

		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw-this.rotationYaw);
		float f1 = MathHelper.clamp(f, -75.0F, 75.0F);
		entityToUpdate.prevRotationYaw += f1-f;
		entityToUpdate.rotationYaw += f1-f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	public boolean onInteractWithPart(EntityVehicleMultiPart part, EntityPlayer player, EnumHand hand)
	{
		if(part==partMain&&!world.isRemote&&!towingOperation)
		{
			player.startRiding(this);
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
		if(part==partGun&&source.isProjectile()||source.isExplosion()||source.isFireDamage())
		{
			gunDurability -= amount*0.85;
			dataManager.set(dataMarkerGunDurability, gunDurability);
			return true;
		}
		else if(part==partMain&&source.isProjectile()||source.isExplosion()||source.isFireDamage())
		{
			mainDurability -= amount*0.85;
			dataManager.set(dataMarkerMainDurability, mainDurability);
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
		else if((part==partShieldLeft||part==partShieldRight)&&(source.isProjectile()||source.isExplosion()||source.isFireDamage())&&amount > 8)
		{
			shieldDurability -= amount;
			dataManager.set(dataMarkerShieldDurability, shieldDurability);
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

	void updateParts()
	{
		double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
		double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));

		Vec3d pos1_x = Utils.offsetPosDirection(0.5f, true_angle, 0);
		Vec3d pos2_x = pos1_x.scale(1);
		Vec3d pos1_z = Utils.offsetPosDirection(0.75f, true_angle2, 0);
		Vec3d pos2_z = Utils.offsetPosDirection(-0.75f, true_angle2, 0);

		aabb_shield = new AxisAlignedBB(-0.5, 0d, 0.125, 0.5, 1.325d, -0.125);

		this.partWheelLeft.setLocationAndAngles(posX+pos1_z.x, posY, posZ+pos1_z.z, 0.0F, 0);
		this.partWheelLeft.setEntityBoundingBox(aabb_wheel.offset(this.partWheelLeft.posX, this.partWheelLeft.posY, this.partWheelLeft.posZ));
		this.partWheelLeft.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partWheelLeft.onUpdate();

		this.partWheelRight.setLocationAndAngles(posX+pos2_z.x, posY, posZ+pos2_z.z, 0.0F, 0);
		this.partWheelRight.setEntityBoundingBox(aabb_wheel.offset(this.partWheelRight.posX, this.partWheelRight.posY, this.partWheelRight.posZ));
		this.partWheelRight.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partWheelRight.onUpdate();

		this.partMain.setLocationAndAngles(posX, posY, posZ, 0.0F, 0);
		this.partMain.setEntityBoundingBox(aabb_main.offset(this.partMain.posX, this.partMain.posY, this.partMain.posZ));
		this.partMain.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partMain.onUpdate();

		this.partGun.setLocationAndAngles(posX+pos1_x.x, posY+0.65, posZ+pos1_x.z, 0.0F, 0);
		this.partGun.setEntityBoundingBox(aabb_gun.offset(this.partGun.posX, this.partGun.posY, this.partGun.posZ));
		this.partGun.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partGun.onUpdate();

		this.partShieldLeft.setLocationAndAngles(posX+pos1_z.x+pos2_x.x, posY+0.375, posZ+pos1_z.z+pos2_x.z, 0.0F, 0);
		this.partShieldLeft.setEntityBoundingBox(aabb_shield.offset(this.partShieldLeft.posX, this.partShieldLeft.posY, this.partShieldLeft.posZ));
		this.partShieldLeft.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partShieldLeft.onUpdate();

		this.partShieldRight.setLocationAndAngles(posX+pos2_z.x+pos2_x.x, posY+0.375, posZ+pos2_z.z+pos2_x.z, 0.0F, 0);
		this.partShieldRight.setEntityBoundingBox(aabb_shield.offset(this.partShieldRight.posX, this.partShieldRight.posY, this.partShieldRight.posZ));
		this.partShieldRight.setVelocity(this.motionX, this.motionY, this.motionZ);
		this.partShieldRight.onUpdate();
	}
}
