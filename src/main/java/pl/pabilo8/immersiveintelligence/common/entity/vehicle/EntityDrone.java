package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.drone.AIDroneTarget;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.12.2022
 */
public class EntityDrone extends EntityFlying implements ISyncNBTEntity<EntityDrone>, IVehicleMultiPart<EntityDrone>, IEntitySpecialRepairable
{
	private final AmmoFactory<EntityAmmoArtilleryProjectile> ammoFactory;

	//--- Parts ---//
	private final EntityVehiclePart<EntityDrone>[] partArray;

	//Sub-entities for colision and hitboxes
	private EntityVehiclePart<EntityDrone> partMain, partTankRight1, partTankRight2, partTankLeft1, partTankLeft2, partEngine;
	private EntityVehiclePart<EntityDrone> partRotorFrontRight, partRotorFrontLeft, partRotorBackRight, partRotorBackLeft;
	private EntityVehiclePart<EntityDrone> exhaust1, exhaust2;

	//Part durability (health)
	@SyncNBT
	public VehicleDurability durabilityMain, durabilityTankLeft, durabilityTankRight, durabilityEngine;
	@SyncNBT
	public VehicleDurability durabilityRotorFrontRight, durabilityRotorFrontLeft, durabilityRotorBackRight, durabilityRotorBackLeft;

	public EntityDrone(World world)
	{
		super(world);
		setSize(4, 2.5f);

		//Set ammo factory (bomb dropping)
		this.ammoFactory = new AmmoFactory<>(this);
		ammoFactory.setStack(IIContent.itemAmmoGuidedMissile.getAmmoStack(IIContent.ammoCoreIron, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentRDX));

		//Set durability and armor
		durabilityMain = new VehicleDurability(100, 4);
		durabilityTankLeft = new VehicleDurability(45, 6);
		durabilityTankRight = new VehicleDurability(45, 6);
		durabilityEngine = new VehicleDurability(40, 2);

		durabilityRotorFrontRight = new VehicleDurability(20, 24);
		durabilityRotorFrontLeft = new VehicleDurability(20, 24);
		durabilityRotorBackRight = new VehicleDurability(20, 24);
		durabilityRotorBackLeft = new VehicleDurability(20, 24);

		//Set parts
		partArray = new EntityVehiclePart[]{
				partMain = new EntityVehiclePart<>(this, "main",
						Vec3d.ZERO, 0.65, 0.4)
						.withHitbox(durabilityMain),
				partEngine = new EntityVehiclePart<>(this, "engine",
						new Vec3d(0.25, 1, 0), 0.65, 0.25)
						.withHitbox(durabilityEngine),

				partTankRight1 = new EntityVehiclePart<>(this, "fuel_tank_r1",
						new Vec3d(-0.45, 0.3125, 0.65), 0.45)
						.withHitbox(durabilityTankRight),
				partTankRight2 = new EntityVehiclePart<>(this, "fuel_tank_r2",
						new Vec3d(0.65, 0.3125, 0.65), 0.45)
						.withHitbox(durabilityTankRight),
				partTankLeft1 = new EntityVehiclePart<>(this, "fuel_tank_l1",
						new Vec3d(-0.45, 0.3125, -0.65), 0.45)
						.withHitbox(durabilityTankLeft),
				partTankLeft2 = new EntityVehiclePart<>(this, "fuel_tank_l2",
						new Vec3d(0.65, 0.3125, -0.65), 0.45)
						.withHitbox(durabilityTankLeft),

				partRotorFrontRight = new EntityVehiclePart<>(this, "rotor_fr",
						new Vec3d(1.8, 1.125, 1.7), 0.35, 0.4)
						.withHitbox(durabilityRotorFrontRight),
				partRotorFrontLeft = new EntityVehiclePart<>(this, "rotor_fl",
						new Vec3d(1.8, 1.125, -1.7), 0.35, 0.4)
						.withHitbox(durabilityRotorFrontLeft),
				partRotorBackRight = new EntityVehiclePart<>(this, "rotor_br",
						new Vec3d(-1.625, 1.125, 1.7), 0.35, 0.4)
						.withHitbox(durabilityRotorBackRight),
				partRotorBackLeft = new EntityVehiclePart<>(this, "rotor_bl",
						new Vec3d(-1.625, 1.125, -1.7), 0.35, 0.4)
						.withHitbox(durabilityRotorBackLeft),

				exhaust1 = new EntityVehiclePart<>(this, "exhaust1",
						new Vec3d(-0.125, 1.85, -0.8), 0.25),
				exhaust2 = new EntityVehiclePart<>(this, "exhaust2",
						new Vec3d(0.25+0.125, 1.85, 0.8), 0.25)
		};

	}

	//--- Main ---//
	@Override
	protected void initEntityAI()
	{
		//Attack mobs and enemies focused on the Hans first
		this.targetTasks.addTask(1, new AIDroneTarget<EntityLiving>(this, EntityLiving.class,
						input -> input.isEntityAlive()&&(input instanceof IMob||input.getAttackTarget()==this))
				{
					@Override
					protected AxisAlignedBB getTargetableArea(double targetDistance)
					{
						return this.drone.getEntityBoundingBox().grow(targetDistance, targetDistance*0.66f, targetDistance);
					}
				}
		);
		//Attack entities with different team, stay neutral on default
		this.targetTasks.addTask(2, new AIDroneTarget<EntityLivingBase>(this, EntityLivingBase.class, input -> input!=null&&isValidTarget(input))
				{
					@Override
					protected AxisAlignedBB getTargetableArea(double targetDistance)
					{
						return this.drone.getEntityBoundingBox().grow(targetDistance, targetDistance*0.66f, targetDistance);
					}
				}
		);

		//this.tasks.addTask(1, new <>());
	}

	// TODO: 19.12.2022 expand
	public boolean isValidTarget(Entity entity)
	{
		return entity instanceof IMob||((entity instanceof EntityPlayer||entity instanceof EntityHans||entity instanceof EntityEmplacementWeapon||entity instanceof EntityIronGolem)&&entity.getTeam()!=this.getTeam());
	}

	@Override
	public EntityMoveHelper getMoveHelper()
	{
		return new EntityFlyHelper(this);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(160.0D);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		updateParts();

		/*List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().grow(30));
		if(!players.isEmpty())
			getLookHelper().setLookPositionWithEntity(players.get(0), 5, 5);*/

		List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, getEntityBoundingBox().grow(30));
		if(!mobs.isEmpty()&&!moveHelper.isUpdating())
		{
			EntityMob mob = mobs.get(0);
			getLookHelper().setLookPositionWithEntity(mob, 5, 5);
			moveHelper.setMoveTo(
					mob.posX+(this.posX-mob.posX)/mob.getDistance(this)*15,
					mob.posY,
					mob.posZ+(this.posZ-mob.posZ)/mob.getDistance(this)*15,
					5
			);
		}

		//setRotation(rotationYaw+1f, rotationPitch);


		//setRotation(0, rotationPitch);

		//Vec3d dir = getLookVec().scale(0.4);
		//move(MoverType.SELF, dir.x, dir.y, dir.z);
		//setRotation(rotationYaw+4f, rotationPitch);

		if(!world.isRemote&&ticksExisted%40==0)
		{
			AxisAlignedBB target = getEntityBoundingBox().offset(0, -height-1, 0).grow(20).expand(0, -40, 0);
			List<EntityMob> targets = world.getEntitiesWithinAABB(EntityMob.class, target);
			if(!targets.isEmpty())
			{
				Vec3d weapon = getPositionVector().subtract(0, 1.5, 0);
				ammoFactory
						.setPosition(weapon)
						.setDirection(targets.get(0).getPositionEyes(0).subtract(weapon).normalize())
						.setOwner(this)
						.create();
			}
		}

		if(world.isRemote)//&&world.getTotalWorldTime()%2==0
		{
			Vec3d smokeDir = this.getVectorForRotation(0, this.rotationYaw).scale(-0.25);
			spawnExhaustParticle(exhaust1.getPositionVector(), smokeDir);
			spawnExhaustParticle(exhaust2.getPositionVector(), smokeDir);
		}

	}

	@SideOnly(Side.CLIENT)
	private void spawnExhaustParticle(Vec3d position, Vec3d smokeDir)
	{
		float exhaustRandom = (world.getTotalWorldTime()%20/20f)*0.2f;
		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
				position.x, position.y, position.z,
				0+smokeDir.x, 0.015625*3, smokeDir.z
		);
	}

	@Override
	public float getEyeHeight()
	{
		return -0.55f;
	}

	//--- Colision ---//

	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio)
	{

	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	//disabled for multipart
	@Override
	protected void collideWithNearbyEntities()
	{

	}

	@Override
	public float getCollisionBorderSize()
	{
		return 0;
	}

	//--- Part Handling ---//

	@Nullable
	@Override
	public Entity[] getParts()
	{
		return partArray;
	}

	@Override
	public EntityVehiclePart<EntityDrone>[] getVehicleParts()
	{
		return partArray;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList()
	{
		return NonNullList.create();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{

	}

	@Override
	public EnumHandSide getPrimaryHand()
	{
		return EnumHandSide.RIGHT;
	}

	@Override
	public boolean canRepair()
	{
		return false;
	}

	@Override
	public boolean repair(int repairPoints)
	{
		return false;
	}

	@Override
	public int getRepairCost()
	{
		return 1;
	}

	@Override
	public boolean onInteractWithPart(EntityVehiclePart part, EntityPlayer player, EnumHand hand)
	{
		return false;
	}

	@Override
	public void onSeatDismount(String seatID, Entity passenger)
	{

	}

	@Override
	public Vec3d getVelocity()
	{
		return Vec3d.ZERO;
	}

	@Override
	public VehicleBlueprint getVehicleBlueprint()
	{
		return null;
	}

	@Override
	public SeatInfo<?> getSeatInfo(String seatID)
	{
		return null;
	}
}
