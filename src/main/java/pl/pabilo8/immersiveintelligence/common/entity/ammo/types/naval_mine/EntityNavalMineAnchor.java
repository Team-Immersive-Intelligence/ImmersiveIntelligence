package pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
		setSize(1f, 1f-0.25f);
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound compound)
	{

	}

	@Override
	public void onUpdate()
	{
		handleWaterMovement();
		IBlockState state = world.getBlockState(getPosition());
		float fallSpeed = 0.2f;
		if(state.getMaterial().isLiquid())
			fallSpeed = 0.0625f;
		move(MoverType.SELF, 0, -fallSpeed, 0);

		if(ticksExisted > 400&&getPassengers().isEmpty())
			setDead();
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Nonnull
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
	public void updatePassenger(@Nonnull Entity passenger)
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
