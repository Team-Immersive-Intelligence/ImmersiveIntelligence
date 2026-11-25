package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase.WornBackpack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Carries a descending rider and remains available for recovery after landing.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.11.2026
 * @since 17.05.2021
 */
public class EntityParachute extends Entity
{
	public static final int GROUND_LIFETIME = 640;
	private static final DataParameter<Integer> LANDED_AT = EntityDataManager.createKey(EntityParachute.class, DataSerializers.VARINT);
	public int time = 20;
	public int health = 20;

	public EntityParachute(World world)
	{
		super(world);
		setSize(4.5f, 2f);
	}

	public boolean hasLanded()
	{
		return dataManager.get(LANDED_AT)!=-1;
	}

	public int getLandingAge()
	{
		return hasLanded()?Math.max(0, (int)world.getTotalWorldTime()-dataManager.get(LANDED_AT)): 0;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(hasLanded())
		{
			motionX = motionY = motionZ = 0;
			if(!world.isRemote&&getLandingAge() >= GROUND_LIFETIME)
				setDead();
			return;
		}

		Entity passenger = getControllingPassenger();
		if(passenger!=null)
			passenger.fallDistance = 0;
		time = Math.max(time-1, 0);
		motionY = time > 0?-0.625: -0.2;
		if(passenger instanceof EntityLivingBase living)
		{
			double sinYaw = -Math.sin(Math.toRadians(passenger.rotationYaw));
			double cosYaw = Math.cos(Math.toRadians(passenger.rotationYaw));
			motionX += (living.moveForward*sinYaw+living.moveStrafing*cosYaw)*0.02;
			motionZ += (living.moveForward*cosYaw-living.moveStrafing*sinYaw)*0.02;
		}
		motionX *= 0.8;
		motionZ *= 0.8;
		move(MoverType.SELF, motionX, motionY, motionZ);
		if(hasLanded()||world.isRemote)
			return;

		double riderY = posY+getMountedYOffset()+(passenger==null?-0.35: passenger.getYOffset());
		RayTraceResult ground = world.rayTraceBlocks(new Vec3d(posX, riderY+0.75, posZ),
				new Vec3d(posX, riderY-0.75, posZ), false, true, false);
		if(ground!=null&&ground.typeOfHit==RayTraceResult.Type.BLOCK)
			land(ground.hitVec.y);
		else
			isAirBorne = true;
	}

	private void land(double groundY)
	{
		if(world.isRemote||hasLanded())
			return;
		Entity passenger = getControllingPassenger();
		removePassengers();
		if(passenger!=null)
		{
			passenger.fallDistance = 0;
			passenger.setPositionAndUpdate(posX, groundY, posZ);
		}
		dataManager.set(LANDED_AT, (int)world.getTotalWorldTime());
		setSize(3.5f, 0.25f);
		setPosition(posX, groundY+0.01, posZ);
		motionX = motionY = motionZ = 0;
		onGround = true;
		fallDistance = 0;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		super.notifyDataManagerChange(key);
		if(LANDED_AT.equals(key)&&hasLanded())
			setSize(3.5f, 0.25f);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		if(!hasLanded()||isDead||player.isSpectator())
			return false;
		for(int slot = 0; slot < 2; slot++)
		{
			WornBackpack worn = ItemIIBackpackBase.getWornBackpack(player, slot);
			if(worn!=null&&refill(player, worn.getStack(), worn))
				return true;
		}
		for(EnumHand heldHand : EnumHand.values())
			if(refill(player, player.getHeldItem(heldHand), null))
				return true;
		return false;
	}

	private boolean refill(EntityPlayer player, ItemStack stack, @Nullable WornBackpack worn)
	{
		if(stack.getItem()!=IIContent.itemParachutePack)
			return false;
		if(!world.isRemote)
		{
			IIContent.itemParachutePack.setUsed(stack, false);
			if(worn!=null)
				worn.save(stack);
			player.inventory.markDirty();
			setDead();
		}
		return true;
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Nullable
	@Override
	public Entity getControllingPassenger()
	{
		List<Entity> passengers = getPassengers();
		return passengers.isEmpty()?null: passengers.get(0);
	}

	@Override
	public double getMountedYOffset()
	{
		return -6.5;
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		super.updatePassenger(passenger);
		applyOrientationToEntity(passenger);
	}

	@Override
	public void applyOrientationToEntity(Entity entity)
	{
		entity.setRenderYawOffset(rotationYaw);
		float yaw = MathHelper.wrapDegrees(entity.rotationYaw-rotationYaw);
		float clamped = MathHelper.clamp(yaw, -75f, 75f);
		entity.prevRotationYaw += clamped-yaw;
		entity.rotationYaw += clamped-yaw;
		entity.setRotationYawHead(entity.rotationYaw);
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(LANDED_AT, -1);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("health"))
			health = compound.getInteger("health");
		time = compound.hasKey("opening_time")?MathHelper.clamp(compound.getInteger("opening_time"), 0, 20): 20;
		if(compound.getBoolean("landed"))
		{
			dataManager.set(LANDED_AT, (int)world.getTotalWorldTime()-MathHelper.clamp(compound.getInteger("ground_age"), 0, GROUND_LIFETIME));
			setSize(3.5f, 0.25f);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("health", health);
		compound.setInteger("opening_time", time);
		compound.setBoolean("landed", hasLanded());
		compound.setInteger("ground_age", getLandingAge());
	}

	@Override
	public void fall(float distance, float multiplier)
	{
		land(posY);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(world.isRemote||isDead||isEntityInvulnerable(source))
			return false;
		health = (int)(health-amount);
		if(health <= 0||source.isExplosion())
		{
			removePassengers();
			setDead();
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return hasLanded()?getEntityBoundingBox().grow(0.5): getEntityBoundingBox().expand(0, -7, 0);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !isDead;
	}
}
