package pl.pabilo8.immersiveintelligence.api.data.radio;

import pl.pabilo8.immersiveintelligence.common.IIUtils;
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

	public boolean addDevice(IRadioDevice pos)
	{
		if(!devices.contains(pos))
		{
			devices.add(pos);
			return true;
		}
		return false;
	}

	public boolean removeDevice(IRadioDevice pos)
	{
		if(!toRemove.contains(pos))
		{
			toRemove.add(pos);
			return true;
		}
		return false;
	}

	//Don't use that pls
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

	public boolean distanceCheck(IRadioDevice device1, IRadioDevice device2)
	{
		float range1 = device1.getRange()*device1.getWeatherRangeDecrease();
		float range2 = device2.getRange()*device2.getWeatherRangeDecrease();

		return IIUtils.getDistanceBetweenPos(device1.getDevicePosition(), device2.getDevicePosition(), true) <= (range1+range2)/2f;
	}

}
