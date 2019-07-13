package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;

/**
 * Created by Pabilo8 on 11-06-2019.
 */
public class TileEntityDataSender extends TileEntityIEBase implements ITickable, IRedstoneOutput, IDataDevice
{
	public boolean toggle = false;

	@Override
	public void update()
	{
		if(!world.isRemote&&(world.isBlockIndirectlyGettingPowered(getPos()) > 0)&&!toggle)
		{
			toggle = !toggle;
			DataPacket pack = new DataPacket();
			pack.setVariable('a', new DataPacketTypeString("lolle"));
			if(world.getTileEntity(pos.up())!=null&&world.getTileEntity(pos.up()) instanceof TileEntityDataConnector)
			{
				TileEntityDataConnector conn = (TileEntityDataConnector)world.getTileEntity(pos.up());
				conn.sendPacket(pack);
				ImmersiveIntelligence.logger.info("Actually Sending Packet!");
			}
			ImmersiveIntelligence.logger.info("Sending Packet!");
		}
		else if(!world.isRemote&&(world.isBlockIndirectlyGettingPowered(getPos()) > 0)&&toggle)
		{
			toggle = !toggle;
		}
	}

	@Override
	public int getWeakRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}

	@Override
	public int getStrongRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, EnumFacing side)
	{
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{

	}

	@Override
	public void onReceive(DataPacket packet)
	{

	}

	@Override
	public void onSend()
	{

	}
}
