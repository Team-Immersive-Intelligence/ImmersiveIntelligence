package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 23-06-2019
 */
public class RadioNetwork
{
	public static RadioNetwork INSTANCE = new RadioNetwork();

	ArrayList<IRadioDevice> devices = new ArrayList<>();
	ArrayList<IRadioDevice> toRemove = new ArrayList<>();

	/**
	 * Adds a Radio device to the global network
	 *
	 * @param device the device to be added
	 * @return if the device was added
	 */
	public boolean addDevice(IRadioDevice device)
	{
		if(!devices.contains(device))
		{
			devices.add(device);
			return true;
		}
		return false;
	}

	/**
	 * Removes a Radio device from the global network
	 *
	 * @param pos the device to be removed
	 * @return if the device was removed
	 */
	public boolean removeDevice(IRadioDevice pos)
	{
		if(!toRemove.contains(pos))
		{
			toRemove.add(pos);
			return true;
		}
		return false;
	}


	public void clearDevices()
	{
		devices.clear();
	}

	public ArrayList<IRadioDevice> getDevices()
	{
		return devices;
	}


	//Simulates radio transmission in a recursive way
	public void sendPacket(DataPacket packet, IRadioDevice sender, ArrayList<IRadioDevice> list)
	{
		if(toRemove.size() > 0)
		{
			//prevents crashing by recursion
			list.addAll(toRemove);
			devices.removeAll(toRemove);
		}
		if(!list.contains(sender))
		{
			sender.onRadioSend(packet);
			list.add(sender);
		}
		for(IRadioDevice dev : getDevices())
		{
			if(!list.contains(dev))
			{
				if(dev.getFrequency()==sender.getFrequency()&&distanceCheck(sender, dev))
				{
					if(dev.onRadioReceive(packet))
					{
						list.add(dev);
						INSTANCE.sendPacket(packet, dev, list);
					}
				}
			}
		}

	}

	//TODO:Radio Item
	public void sendPacketItem()
	{

	}

	/**
	 * @param device1 the sending radio
	 * @param device2 the receiving radio
	 * @return if the distance between the two radios is less than the sending radio's range
	 */
	public boolean distanceCheck(IRadioDevice device1, IRadioDevice device2)
	{
		DimensionBlockPos pos1 = device1.getDevicePosition();
		DimensionBlockPos pos2 = device2.getDevicePosition();
		if(pos1.dimension!=pos2.dimension)
			return false;
		float range = device1.getRange();
		return pos1.distanceSq(device2.getDevicePosition()) <= range*range;
	}

}
