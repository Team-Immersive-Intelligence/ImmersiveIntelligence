package pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityMineSign extends TileEntityIEBase implements IDirectionalTile, IBlockBounds
{
	public EnumFacing facing = EnumFacing.NORTH;
	private static final float[] boundsX = new float[]{0.4375f, 0, 0, 0.5625f, 1, 1};
	private static final float[] boundsY = new float[]{0, 0, 0.4375f, 1, 1, 0.5625f};

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
		if(facing.getAxis().isVertical())
			this.facing = EnumFacing.NORTH;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return true;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return !axis.getAxis().isVertical();
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		setFacing(EnumFacing.getFront(nbt.getInteger("facing")));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("facing", facing.getIndex());
	}

	@Override
	public float[] getBlockBounds()
	{
		return facing.getAxis()==Axis.X?boundsX: boundsY;
	}
}
