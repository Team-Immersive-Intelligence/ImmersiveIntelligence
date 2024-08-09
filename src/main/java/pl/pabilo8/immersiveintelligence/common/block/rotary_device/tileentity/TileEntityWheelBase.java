package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;

import java.util.Set;

/**
 * @author Pabilo8
 * @since 29-12-2019
 */
public abstract class TileEntityWheelBase extends TileEntityMechanicalConnectable implements IBlockBounds
{
	private ConditionCompoundSound loopSound = null;
	public EnumFacing facing = EnumFacing.NORTH;

	@Override
	public void update()
	{
		super.update();

		//Play belt's loop sound
		if(world.isRemote)
		{
			updateSound();
			return;
		}

		if(!(limitType instanceof MotorBeltType))
			return;
		MotorBeltType belt = (MotorBeltType)limitType;
		//Throw off motor belts if max torque is exceeded
		if(getNetwork().getNetworkTorque() > belt.getMaxTorque())
		{
			Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
			if(connections!=null)
			{
				BlockPos p = pos;
				for(Connection c : connections)
				{
					ImmersiveNetHandler.INSTANCE.removeConnection(world, c);
					p = new BlockPos(new Vec3d(c.start).add(new Vec3d(c.end.subtract(c.start)).scale(0.5)));
				}
				Utils.dropStackAtPos(world, p, belt.getBrokenDrop().copy());
				world.playSound(null, p, belt.getBreakSound(), SoundCategory.BLOCKS, 2f, 1f);
			}
		}
	}

	private void updateSound()
	{
		//Stop playing sound if the wheel is not moving
		if(limitType==null||getNetwork().getNetworkSpeed() <= 0)
		{
			if(loopSound!=null)
				loopSound.forceStop();
			loopSound = null;
			return;
		}

		//Start playing sound if the wheel is moving
		if(loopSound==null)
		{
			Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
			if(connections==null)
				return;

			for(Connection connection : connections)
			{
				if(!(connection.cableType instanceof MotorBeltType))
					continue;
				ClientUtils.mc().getSoundHandler().playSound(loopSound = new ConditionCompoundSound(((MotorBeltType)connection.cableType).getLoopSound(), SoundCategory.BLOCKS,
						new Vec3d(pos).addVector(0.5, 0.5, 0.5), 1f, 1f, () -> !tileEntityInvalid));
				break;
			}

		}

		//Update sound
		if(loopSound!=null)
		{
			loopSound.update();
			loopSound.setPitch(((float)MathHelper.clamp(getNetwork().getNetworkSpeed()/80f, 0, 2)));
		}
	}

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
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		int inc = getRenderRadiusIncrease();
		return new AxisAlignedBB(this.pos.getX()-inc, this.pos.getY()-inc, this.pos.getZ()-inc, this.pos.getX()+inc+1, this.pos.getY()+inc+1, this.pos.getZ()+inc+1);
	}

	protected abstract int getRenderRadiusIncrease();

	@Override
	public float[] getBlockBounds()
	{
		float length = .625f;
		float wMin = 0.5f-getRadius()/16f;
		float wMax = 0.5f+getRadius()/16f;
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
		return getNetwork().getNetworkSpeed();
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
		return Axis.Y;
	}


	@Override
	public BlockPos getConnectionPos()
	{
		return getPos().offset(facing);
	}
}
