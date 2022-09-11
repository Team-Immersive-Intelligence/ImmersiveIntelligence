package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8
 * @since 29-12-2019
 */
public class TileEntityMechanicalWheel extends TileEntityMechanicalConnectable
{
	public EnumFacing facing = EnumFacing.NORTH;

	@Override
	public EnumFacing getFacing()
	{
		return this.facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 5;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return false;
	}


	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setInteger("facing", facing.ordinal());
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5);
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		refreshBeltNetwork = false;
	}

	@SideOnly(Side.CLIENT)
	private AxisAlignedBB renderAABB;

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		int inc = getRenderRadiusIncrease();
		return new AxisAlignedBB(this.pos.getX()-inc, this.pos.getY()-inc, this.pos.getZ()-inc, this.pos.getX()+inc+1, this.pos.getY()+inc+1, this.pos.getZ()+inc+1);
	}

	int getRenderRadiusIncrease()
	{
		return limitType!=null?limitType.getMaxLength(): 2;
	}

	@Override
	public float[] getBlockBounds()
	{
		float length = .75f;
		float wMin = .25f;
		float wMax = .75f;
		switch(facing.getOpposite())
		{
			case SOUTH:
				return new float[]{wMin, wMin, 0, wMax, wMax, length};
			case NORTH:
				return new float[]{wMin, wMin, 1-length, wMax, wMax, 1};
			case EAST:
				return new float[]{0, wMin, wMin, length, wMax, wMax};
			case WEST:
				return new float[]{1-length, wMin, wMin, 1, wMax, wMax};
		}
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	/**
	 * Only for visuals
	 */
	@Override
	public double getOutputRPM()
	{
		return getNetwork().getNetworkRPM();
	}

	@Override
	public Axis getConnectionAxis()
	{
		switch(facing)
		{
			case NORTH:
			case SOUTH:
				return Axis.X;
			case EAST:
			case WEST:
				return Axis.Z;
		}
		return Axis.Z;
	}

	@Override
	public float getRadius()
	{
		return 6;
		//Try it if you want ^^
		//return (int)(world.getTotalWorldTime()%40>20?(20-(world.getTotalWorldTime()%20)):(world.getTotalWorldTime()%20));
	}

	@Override
	public BlockPos getConnectionPos()
	{
		return getPos().offset(facing);
	}
}
