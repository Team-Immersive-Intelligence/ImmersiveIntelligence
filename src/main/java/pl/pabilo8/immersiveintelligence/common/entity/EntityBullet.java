package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class EntityBullet extends EntityIEProjectile
{
	private static final DataParameter<ItemStack> dataMarker_ammo = EntityDataManager.createKey(EntityBullet.class, DataSerializers.ITEM_STACK);
	public ItemStack bullet = ItemStack.EMPTY;
	public float mass = 1f;
	public float penetrationPower = 0f;
	public boolean setNotDead = true;

	public EntityBullet(World world)
	{
		super(world);
		setParams();
		this.pickupStatus = PickupStatus.DISALLOWED;
	}

	public EntityBullet(World world, double x, double y, double z, double ax, double ay, double az, ItemStack stack)
	{
		super(world);
		this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
		this.setPosition(x, y, z);
		this.bullet = stack.copy();
		setParams();
		this.pickupStatus = PickupStatus.DISALLOWED;
		ImmersiveIntelligence.logger.info("otak1!");
	}

	public EntityBullet(World world, EntityLivingBase living, double ax, double ay, double az, ItemStack stack)
	{
		super(world);
		this.shootingEntity = living;
		setShooterSynced();
		this.setLocationAndAngles(living.posX+ax, living.posY+living.getEyeHeight()+ay, living.posZ+az, living.rotationYaw, living.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.bullet = stack.copy();
		setParams();
		this.pickupStatus = PickupStatus.DISALLOWED;

	}

	private void setParams()
	{
		if(!bullet.isEmpty()&&bullet.getItem() instanceof ItemIIBullet)
		{
			this.mass = ItemIIBullet.getMass(bullet);
			this.penetrationPower = ItemIIBullet.getCasing(bullet).getPenetration();

			float first_pen = 0f, second_pen = 0f;
			penetrationPower = ItemIIBullet.getCasing(bullet).getPenetration();
			if(ItemIIBullet.hasFirstComponent(bullet))
				first_pen = ItemIIBullet.getFirstComponent(bullet).getPenetrationModifier(ItemIIBullet.getFirstComponentNBT(bullet));
			if(ItemIIBullet.hasSecondComponent(bullet))
				second_pen = ItemIIBullet.getSecondComponent(bullet).getPenetrationModifier(ItemIIBullet.getSecondComponentNBT(bullet));
			penetrationPower += first_pen+second_pen;

			this.setTickLimit(Math.round(320f*ItemIIBullet.getCasing(bullet).getSize()));
			this.setSize(.625f*ItemIIBullet.getCasing(bullet).getSize(), .625f*ItemIIBullet.getCasing(bullet).getSize());
		}
	}

	public void setAmmoSynced()
	{
		if(!this.getAmmo().isEmpty())
			this.dataManager.set(dataMarker_ammo, getAmmo());
	}

	public ItemStack getAmmoSynced()
	{
		return this.dataManager.get(dataMarker_ammo);
	}

	public ItemStack getAmmo()
	{
		return bullet;
	}


	@Override
	public void onUpdate()
	{
		ImmersiveIntelligence.logger.info("otak2!");
		super.onUpdate();
		ImmersiveIntelligence.logger.info(getPosition());
		if(setNotDead)
			isDead = false;
	}

	@Override
	protected ItemStack getArrowStack()
	{
		return bullet;
	}


	@Override
	public double getGravity()
	{
		return 0.35F*mass;
	}

	@Override
	public void onImpact(RayTraceResult mop)
	{
		if(!this.world.isRemote&&!bullet.isEmpty()&&bullet.getItem() instanceof ItemIIBullet)
		{
			//Simulate the penetration

			float damage = ItemIIBullet.getCasing(bullet).getDamage();
			float first_dmg = ItemIIBullet.hasFirstComponent(bullet)?ItemIIBullet.getFirstComponent(bullet).getDamageModifier(ItemIIBullet.getFirstComponentNBT(bullet)): 0f;
			float second_dmg = ItemIIBullet.hasSecondComponent(bullet)?ItemIIBullet.getSecondComponent(bullet).getDamageModifier(ItemIIBullet.getSecondComponentNBT(bullet)): 0f;
			damage += first_dmg+second_dmg;

			if(penetrationPower > 0)
			{
				if(this.inGround)
				{
					BlockPos pos = new BlockPos(this.blockX, this.blockY, this.blockZ);
					float hardness = world.getBlockState(pos).getBlockHardness(world, pos)/4f;

					if(penetrationPower > hardness)
					{
						world.destroyBlock(pos, false);
					}

					penetrationPower = Math.max(0f, penetrationPower-hardness/3f);
				}

				if(mop.entityHit!=null)
				{
					mop.entityHit.attackEntityFrom(IIDamageSources.causeBulletDamage(this, getShooter()), damage);
					Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
					penetrationPower = Math.max(0f, penetrationPower-damage/3f);
					moveToBlockPosAndAngles(new BlockPos(nextPos), rotationYaw, rotationPitch);
					setNotDead = true;
				}
			}
			else
			{
				if(ItemIIBullet.hasFirstComponent(bullet))
					ItemIIBullet.getFirstComponent(bullet).onExplosion(ItemIIBullet.getFirstComponentQuantity(bullet), ItemIIBullet.getFirstComponentNBT(bullet), world, getPosition(), this);
				if(ItemIIBullet.hasSecondComponent(bullet))
					ItemIIBullet.getSecondComponent(bullet).onExplosion(ItemIIBullet.getSecondComponentQuantity(bullet), ItemIIBullet.getSecondComponentNBT(bullet), world, getPosition(), this);

				this.setDead();
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if(!this.bullet.isEmpty())
			nbt.setTag("bullet", this.bullet.writeToNBT(new NBTTagCompound()));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.bullet = new ItemStack(nbt.getCompoundTag("bullet"));
		setParams();
	}
}
