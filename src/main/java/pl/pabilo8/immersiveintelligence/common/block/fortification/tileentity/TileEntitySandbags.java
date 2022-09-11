package pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 16-08-2019
 */
public class TileEntitySandbags extends TileEntityIEBase implements IDirectionalTile, IAdvancedCollisionBounds, IAdvancedSelectionBounds
{
	public EnumFacing facing = EnumFacing.NORTH;

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("facing", facing.ordinal());
	}

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return true;
	}

	@Override
	public float[] getBlockBounds()
	{
		return null;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	public boolean hasNeighbour()
	{
		BlockPos pos = getPos().offset(facing.rotateY());
		return world.getTileEntity(pos) instanceof TileEntitySandbags;
	}

	public boolean isLower()
	{
		BlockPos up = getPos().up();
		TileEntity te = world.getTileEntity(up);
		return te instanceof TileEntitySandbags;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List<AxisAlignedBB> aabb = new ArrayList<>();
		// TODO: 28.12.2021 new aabb

		switch(facing)
		{
			case NORTH:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0f, 0f, 0.5f));
			}
			break;
			case SOUTH:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0f, 0f, 0.5f).offset(0f, 0f, 0.5f));
			}
			break;
			case EAST:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0.5f, 0f, 0f).offset(0.5f, 0f, 0f));
			}
			break;
			case WEST:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0.5f, 0f, 0f));
			}
			break;
		}

		return aabb;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}
}