package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;

import javax.annotation.Nullable;

/**
 * Created by Pabilo8 on 22-06-2019.
 */
public class Utils
{
	public static double getDistanceBetweenPos(BlockPos pos1, BlockPos pos2, boolean center)
	{
		double deltaX = (pos1.getX()+(center?0d: 0.5d))-(pos2.getX()+(center?0d: 0.5d));
		double deltaY = (pos1.getY()+(center?0d: 0.5d))-(pos2.getY()+(center?0d: 0.5d));
		double deltaZ = (pos1.getZ()+(center?0d: 0.5d))-(pos2.getZ()+(center?0d: 0.5d));

		return Math.sqrt((deltaX*deltaX)+(deltaY*deltaY)+(deltaZ*deltaZ));
	}

	@Nullable
	public static IDataConnector findConnectorAround(BlockPos pos, World world)
	{
		BlockPos newpos = pos.add(1, 0, 0);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.WEST)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		newpos = pos.add(-1, 0, 0);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.EAST)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		newpos = pos.add(0, 0, 1);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.NORTH)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		newpos = pos.add(0, 0, -1);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.SOUTH)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		return null;
	}

	public static TargetPoint targetPointFromPos(BlockPos pos, World world, int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}

	public static TargetPoint targetPointFromTile(TileEntity tile, int range)
	{
		return new TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), range);
	}

	//https://stackoverflow.com/a/52284357/9876980
	public static double root(double num, double root)
	{
		double d = Math.pow(num, 1.0/root);
		long rounded = Math.round(d);
		return Math.abs(rounded-d) < 0.00000000000001?rounded: d;
	}

	//CMY is actually reverse RGB (i tested that out in GIMP ^^)... adding black makes colour darker (less means lighter)
	//Black is actually the limit of darkness (less value - darker) in RGB
	//But because everything is reverse, we get the color with greater value.
	public static int[] rgbToCmyk(int red, int green, int blue)
	{
		return new int[]{255-red, 255-green, 255-blue, 255-Math.min(red, Math.max(green, blue))};
	}

	public static int[] cmykToRgb(int cyan, int magenta, int yellow, int black)
	{
		return new int[]{Math.min(255-black, 255-cyan), Math.min(255-black, 255-magenta), Math.min(255-black, 255-yellow)};
	}

}
