package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Pabilo8 on 10-11-2019.
 */
public class EntityCamera extends Entity
{
	public float rotationRoll = 0;

	public EntityCamera(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{

	}
}
