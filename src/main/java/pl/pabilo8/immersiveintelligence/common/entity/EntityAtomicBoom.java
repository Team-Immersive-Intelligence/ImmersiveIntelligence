package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.ParticleUtils;

/**
 * @author Pabilo8
 * @since 19.12.2020
 */
public class EntityAtomicBoom extends Entity
{
	public float size;

	public EntityAtomicBoom(World worldIn)
	{
		super(worldIn);
	}

	public EntityAtomicBoom(World worldIn, float size)
	{
		this(worldIn);
		this.size = size;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote)
		{
			if(ticksExisted < 40)
			{
				size = 1f;
				ParticleUtils.spawnShockwave(posX, posY+(1.5*size), posZ, 20f, 2.5f);
			}
			if(ticksExisted > 10)
			{
				ParticleUtils.spawnFog(posX, posY+(0.5*size), posZ, 12f, 0.85f, -0.125f);
				if(ticksExisted < 30)
					ParticleUtils.spawnAtomicBoomCore(posX, posY+(0.5*size), posZ, 10f, 0, 0.5f);
			}
			if(ticksExisted > 20)
			{
				ParticleUtils.spawnFog(posX, posY+(0.5*size), posZ, 20f, 0.5f, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(posX, posY+(3.5*size), posZ, 20f, 0.05f, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(posX, posY+(1.5*size), posZ, 10f, 0, 0.5f);
			}
			if(ticksExisted > 20&&ticksExisted < 380)
			{
				ParticleUtils.spawnFog(posX, posY+(1.5*size), posZ, 25f, 0.125f, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(posX, posY+(1.5*size), posZ, 20f, -0.01f, 0.25f);
				if(ticksExisted < 35)
					ParticleUtils.spawnAtomicBoomCore(posX, posY+(3.5*size), posZ, 10f, 0, 0.5f);
			}
			if(ticksExisted > 25&&ticksExisted < 360)
			{
				ParticleUtils.spawnFog(posX, posY+(6.5*size), posZ, 20f, 0.1f, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(posX, posY+(6.5*size), posZ, 20f, -0.01f, 0.25f);
				if(ticksExisted < 40)
					ParticleUtils.spawnAtomicBoomCore(posX, posY+(6.5*size), posZ, 10f, 0, 0.5f);
			}
			if(ticksExisted > 30&&ticksExisted < 340)
			{
				ParticleUtils.spawnAtomicBoomCore(posX, posY+(12.5*size), posZ, 15f, -0.01f, 0.125f);
			}
			if(ticksExisted > 40&&ticksExisted < 240)
			{
				ParticleUtils.spawnAtomicBoomRing(posX, posY+(18.5*size), posZ, 25f, 0.25f, -0.05f);
			}
			return;
		}

		if(ticksExisted > 400*size)
		{
			setDead();
			return;
		}
		//apply half a second
		if(world.getTotalWorldTime()%10==0)
		{
			AxisAlignedBB aabb = new AxisAlignedBB(getPosition()).grow(40*size);
			EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb).toArray(new EntityLivingBase[0]);
			for(EntityLivingBase e : entities)
			{
				e.addPotionEffect(new PotionEffect(IEPotions.flammable, 400, 8));
				e.setFire(30);
			}
			entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb.grow(20*size)).toArray(new EntityLivingBase[0]);
			for(EntityLivingBase e : entities)
			{
				e.addPotionEffect(new PotionEffect(IEPotions.stunned, 40, 20));
				e.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 20));
			}
		}
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		size = compound.getFloat("size");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("size", size);
	}

	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		return true;
	}
}
