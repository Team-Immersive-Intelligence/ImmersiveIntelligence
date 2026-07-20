package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 20.07.2026
 * @ii-approved 0.3.1
 * @since 29.12.2019
 */
public abstract class TileEntityWheelBase extends TileEntityMechanicalConnectable implements IBlockBounds
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL_TOWARDS_CLICKED);
	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityMechanicalConnectable> loopSound;

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

	@SideOnly(Side.CLIENT)
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
				loopSound = new ConditionCompoundSound<>(((MotorBeltType)connection.cableType).getLoopSound(),
						new Vec3d(pos).addVector(0.5, 0.5, 0.5), this, o -> !o.isInvalid()&&o.getNetwork().getNetworkSpeed() > 1);
				break;
			}

		}
		else
			loopSound.setPitch(((float)MathHelper.clamp(getNetwork().getNetworkSpeed()/80f, 0, 2)));
	}

	@Nonnull
	@Override
	public FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5);
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		super.onConnectivityUpdate(pos, dimension);
		refreshBeltNetwork = false;
	}

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

	@Override
	public float getDisplayedRotationProgress(boolean belt, float partialTicks)
	{
		double rotation = prevRotations+(rotations-prevRotations)*partialTicks;
		double maximum = belt?getBeltRotationMaximum(): 1d;
		double progress = rotation%maximum;
		return (float)((progress < 0?progress+maximum: progress)/maximum);
	}

	private double getBeltRotationMaximum()
	{
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
		if(connections!=null)
			for(Connection connection : connections)
				if(connection.cableType instanceof MotorBeltType)
				{
					double x = connection.end.getX()-connection.start.getX();
					double y = connection.end.getY()-connection.start.getY();
					double z = connection.end.getZ()-connection.start.getZ();
					double circumference = 2*Math.PI*(getRadius()+1)/16d;
					double beltLength = 2*Math.sqrt(x*x+y*y+z*z)+circumference;
					return beltLength/circumference;
				}
		return 1d;
	}

	/**
	 * Only for visuals
	 */
	@Override
	public double getOutputSpeed()
	{
		return getNetwork().getNetworkSpeed();
	}

	@Override
	public Axis getConnectionAxis()
	{
		if(facing.getAxis()==Axis.Y)
			return Axis.Y;
		return facing.rotateY().getAxis();
	}
}
