package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Pabilo8 on 23-06-2019.
 */
public class RadioNetwork
{
	public static RadioNetwork INSTANCE = new RadioNetwork();

	Set<DimensionBlockPos> devices = new HashSet<>();

	public boolean addDevice(DimensionBlockPos pos)
	{
		if(!devices.contains(pos))
		{
			devices.add(pos);
			return true;
		}
		return false;
	}

	public boolean removeDevice(DimensionBlockPos pos)
	{
		if(devices.contains(pos))
		{
			devices.remove(pos);
			return true;
		}
		return false;
	}

	//Don't use that pls
	public void clearDevices()
	{
		devices.clear();
	}

	public Set<DimensionBlockPos> getDevices()
	{
		return devices;
	}


	//Simulates radio transmission in a recursive way
	public void sendPacket(DataPacket packet, DimensionBlockPos sender, World world, List<DimensionBlockPos> list)
	{
		if(list.isEmpty())
			list.add(sender);

		if(world.provider.getDimension()!=sender.dimension||!world.isBlockLoaded(sender)||!(world.getTileEntity(sender) instanceof IRadioDevice))
			return;

		IRadioDevice dvc = (IRadioDevice)world.getTileEntity(sender);
		dvc.onRadioSend(packet);

		for(DimensionBlockPos device : devices)
		{
			if(device.dimension!=sender.dimension||!world.isBlockLoaded(device)||list.contains(device))
				continue;

			if(!(world.getTileEntity(device) instanceof IRadioDevice))
			{
				//Concurrent what?!
				//removeDevice(device);
				ImmersiveIntelligence.logger.info("A device is not a radio!");
				continue;
			}

			IRadioDevice dvc2 = (IRadioDevice)world.getTileEntity(device);

			//ImmersiveIntelligence.logger.info(dvc2.getFrequency());
			//Todo: Investigate why is the frequency not loading / why is the radio facing not saving sometimes

			if(world.getTileEntity(sender).serializeNBT().getInteger("Frequency")!=world.getTileEntity(device).serializeNBT().getInteger("Frequency"))
				continue;

			if(Utils.getDistanceBetweenPos(sender, device, true) < (dvc.getRange()*(world.isRainingAt(sender)?dvc.getWeatherRangeDecrease(): 1f))+(dvc2.getRange())*(world.isRainingAt(device)?dvc2.getWeatherRangeDecrease(): 1f))
			{
				list.add(device);
				dvc2.onRadioReceive(packet);
				sendPacket(packet, device, world, list);
			}

		}
	}

	//TODO:Radio Item
	public void sendPacketItem()
	{

	}

}
