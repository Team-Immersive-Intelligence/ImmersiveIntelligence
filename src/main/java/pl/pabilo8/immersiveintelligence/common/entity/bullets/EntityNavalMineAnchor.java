package pl.pabilo8.immersiveintelligence.common.entity.bullets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

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
	}

	public void updatePassenger(Entity passenger)
	{
		if (this.isPassenger(passenger))
		{
			double diff = Math.abs(passenger.posY-this.posY);
			diff = passenger instanceof EntityNavalMine?Math.min(((EntityNavalMine)passenger).maxLength,diff):0;
			if(diff>0)
				ImmersiveIntelligence.logger.info("o");
			passenger.setPosition(passenger.posX, passenger.posY-diff, passenger.posZ);
		}
	}
}
