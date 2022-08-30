package pl.pabilo8.immersiveintelligence.common.entity.bullet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 11.02.2021
 */
public class EntityNavalMineAnchor extends Entity
{
	public EntityNavalMineAnchor(World worldIn)
	{
		super(worldIn);
		setSize(0.5f,0.5f);
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

	@Override
	public void onUpdate()
	{
		handleWaterMovement();
		move(MoverType.SELF,0,-0.0625f,0);

		if(ticksExisted>400&&getPassengers().size()==0)
			setDead();
	}

	public void updatePassenger(Entity passenger)
	{
		if (this.isPassenger(passenger))
		{
			if(passenger instanceof EntityNavalMine)
			{
				double dd = 0;
				double diff = Math.abs(passenger.posY-(this.posY)-0.5);
				if(diff>((EntityNavalMine)passenger).maxLength)
					dd=Math.min(diff-((EntityNavalMine)passenger).maxLength,0.125f);
				passenger.setPosition(passenger.posX, passenger.posY-dd, passenger.posZ);
			}
			else
				passenger.setPosition(passenger.posX, passenger.posY+0.5f, passenger.posZ);
		}
	}
}
