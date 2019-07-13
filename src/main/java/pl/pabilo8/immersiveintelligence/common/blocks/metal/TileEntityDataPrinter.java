package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;

/**
 * Created by Pabilo8 on 11-06-2019.
 */
public class TileEntityDataPrinter extends TileEntityIEBase implements ITickable, IDataDevice
{

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{

	}

	@Override
	public void update()
	{

	}

	@Override
	public void onReceive(DataPacket packet)
	{
		if(packet.getPacketVariable('a') instanceof DataPacketTypeString)
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(
					new TextComponentString(packet.toNBT().toString())
			);
		}
	}

	@Override
	public void onSend()
	{

	}
}
