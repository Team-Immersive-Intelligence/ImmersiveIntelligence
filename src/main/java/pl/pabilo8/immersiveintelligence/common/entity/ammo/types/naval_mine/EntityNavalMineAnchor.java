package pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.02.2021
 */
public class EntityNavalMineAnchor extends Entity
{
	public EntityNavalMineAnchor(World worldIn)
	{
		super(worldIn);
		setSize(0.5f, 0.5f);
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
		move(MoverType.SELF, 0, -0.0625f, 0);

		if(ticksExisted > 400&&getPassengers().size()==0)
			setDead();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		List<Entity> passengers = this.getPassengers();
		if(passengers.isEmpty())
			return super.getRenderBoundingBox();

		return this.getEntityBoundingBox().grow(0, passengers.get(0).posY-this.posY, 0);
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			if(passenger instanceof EntityNavalMine)
			{
				double dd = 0;
				double diff = Math.abs(passenger.posY-(this.posY)-0.5);
				if(diff > ((EntityNavalMine)passenger).maxLength)
					dd = Math.min(diff-((EntityNavalMine)passenger).maxLength, 0.125f);
				passenger.setPosition(passenger.posX, passenger.posY-dd, passenger.posZ);
			}
			else
				passenger.setPosition(passenger.posX, passenger.posY+0.5f, passenger.posZ);
		}
	}
}
