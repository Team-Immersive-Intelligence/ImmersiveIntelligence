package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 17.05.2021
 */
public class EntityParachute extends Entity
{
	public int time = 20;
	public int health = 20;

	public EntityParachute(World worldIn)
	{
		super(worldIn);
		width=4.5f;
		height=2f;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		Entity controllingPassenger = getControllingPassenger();

		if(!world.isRemote&&(controllingPassenger==null||controllingPassenger.getRidingEntity()!=this))
		{
			setDead();
		}

		if(controllingPassenger!=null)
			controllingPassenger.fallDistance = 0F;

		time=Math.max(time-1,0);

		//motionY = 0;
		motionY = time>0?-0.625:-0.2;

		if(controllingPassenger instanceof EntityLivingBase)
		{
			float speedMultiplier = 0.02F;
			double moveForwards = ((EntityLivingBase)this.getControllingPassenger()).moveForward;
			double moveStrafing = ((EntityLivingBase)this.getControllingPassenger()).moveStrafing;
			double sinYaw = -Math.sin((controllingPassenger.rotationYaw*(float)Math.PI/180.0F));
			double cosYaw = Math.cos((this.getControllingPassenger().rotationYaw*(float)Math.PI/180.0F));
			motionX += (moveForwards*sinYaw+moveStrafing*cosYaw)*speedMultiplier;
			motionZ += (moveForwards*cosYaw-moveStrafing*sinYaw)*speedMultiplier;
		}

		motionX *= 0.8F;
		motionZ *= 0.8F;

		move(MoverType.SELF, motionX, motionY, motionZ);

		IBlockState state = world.getBlockState(getPosition().down(7));
		if(!state.getBlock().isAir(state, world, getPosition().down(7)))
		{
			setDead();
			removePassengers();
		}
		else
			isAirBorne = true;
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
		List<Entity> list = this.getPassengers();
		return list.isEmpty()?null: list.get(0);
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
	protected void entityInit()
	{

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("health"))
			health=compound.getInteger("health");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("health",health);
	}

	@Override
	public void fall(float par1, float k)
	{
		//Ignore fall damage
		setDead();
		removePassengers();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float f)
	{
		health-=f;
		if(health<=0||source.isExplosion())
		{
			setDead();
			removePassengers();
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return this.getEntityBoundingBox().expand(0,-6,0);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
}
