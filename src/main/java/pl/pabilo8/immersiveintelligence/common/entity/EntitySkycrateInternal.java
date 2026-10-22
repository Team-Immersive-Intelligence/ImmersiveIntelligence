package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;

/**
 * Holds a station minecart at its synchronised position.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.10.2026
 * @since 10.11.2019
 */
public class EntitySkycrateInternal extends Entity
{
	private static final DataParameter<Float> dataMarkerRidingX = EntityDataManager.createKey(EntitySkycrateInternal.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerRidingY = EntityDataManager.createKey(EntitySkycrateInternal.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> dataMarkerRidingZ = EntityDataManager.createKey(EntitySkycrateInternal.class, DataSerializers.FLOAT);
	public float riding_x = 0, riding_y = 0, riding_z = 0;
	public BlockPos origin_pos = new BlockPos(0, 0, 0);

	public EntitySkycrateInternal(World worldIn)
	{
		super(worldIn);
	}

	public EntitySkycrateInternal(World worldIn, BlockPos pos)
	{
		super(worldIn);
		origin_pos = pos;
		setPosition(pos.getX(), pos.getY(), pos.getZ());
		riding_x = pos.getX()+.5f;
		riding_y = pos.getY()+.125f;
		riding_z = pos.getZ()+.5f;
		updateValues();
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(dataMarkerRidingX, 1f);
		this.dataManager.register(dataMarkerRidingY, 1f);
		this.dataManager.register(dataMarkerRidingZ, 1f);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(world.isRemote)
		{
			riding_x = dataManager.get(dataMarkerRidingX);
			riding_y = dataManager.get(dataMarkerRidingY);
			riding_z = dataManager.get(dataMarkerRidingZ);
		}
		else if(ticksExisted%20==0&&world.isBlockLoaded(origin_pos))
		{
			net.minecraft.tileentity.TileEntity owner = world.getTileEntity(origin_pos);
			if(!(owner instanceof TileEntitySkyCartStation)||!((TileEntitySkyCartStation)owner).formed)
				setDead();
		}
	}

	@Override
	public void setDead()
	{
		if(!world.isRemote)
			for(Entity passenger : new java.util.ArrayList<>(getPassengers()))
			{
				passenger.dismountRidingEntity();
				if(passenger instanceof net.minecraft.entity.item.EntityMinecart)
					((net.minecraft.entity.item.EntityMinecart)passenger).setCanUseRail(true);
			}
		super.setDead();
	}

	@Override
	public boolean canPassengerSteer()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return true;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn)
	{
		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("riding_x")&&compound.hasKey("riding_y")&&compound.hasKey("riding_z"))
		{
			riding_x = compound.getFloat("riding_x");
			riding_y = compound.getFloat("riding_y");
			riding_z = compound.getFloat("riding_z");
		}

		if(compound.hasKey("origin_x")&&compound.hasKey("origin_y")&&compound.hasKey("origin_z"))
		{
			double x = compound.getInteger("origin_x");
			double y = compound.getInteger("origin_y");
			double z = compound.getInteger("origin_z");

			origin_pos = new BlockPos(x, y, z);
		}

		updateValues();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("riding_x", riding_x);
		compound.setFloat("riding_y", riding_y);
		compound.setFloat("riding_z", riding_z);

		compound.setInteger("origin_x", origin_pos.getX());
		compound.setInteger("origin_y", origin_pos.getY());
		compound.setInteger("origin_z", origin_pos.getZ());
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			passenger.setPosition(riding_x, riding_y, riding_z);
		}
	}

	public void updateValues()
	{
		this.dataManager.set(dataMarkerRidingX, riding_x);
		this.dataManager.set(dataMarkerRidingY, riding_y);
		this.dataManager.set(dataMarkerRidingZ, riding_z);
	}

	@Override
	public boolean isInvisible()
	{
		return true;
	}
}
