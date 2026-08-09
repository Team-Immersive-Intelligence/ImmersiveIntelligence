package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.ArrayList;

/**
 * Connects {@link IRadioDevice Radio Devices} of the same {@link IRadioDevice#getFrequency() frequency} and routes data packets through them.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 23.06.2019
 */
public class RadioNetwork
{
	public static RadioNetwork INSTANCE = new RadioNetwork();

	ArrayList<IRadioDevice> devices = new ArrayList<>();
	ArrayList<IRadioDevice> toRemove = new ArrayList<>();

	public boolean addDevice(IRadioDevice device)
	{
		if(!devices.contains(device))
		{
			devices.add(device);
			return true;
		}
		return false;
	}

	public boolean removeDevice(IRadioDevice device)
	{
		if(!toRemove.contains(device))
		{
			toRemove.add(device);
			return true;
		}
		return false;
	}

	public void clearDevices()
	{
		devices.clear();
		toRemove.clear();
	}

	public ArrayList<IRadioDevice> getDevices()
	{
		return devices;
	}

	/**
	 * Sends a packet through all available radios that can receive it.
	 */
	public void sendPacket(DataPacket packet, IRadioDevice sender, ArrayList<IRadioDevice> visited)
	{
		flushRemovedDevices();
		if(sender==null||!sender.isRadioAvailable())
			return;

		if(!visited.contains(sender))
		{
			sender.onRadioSend(packet);
			visited.add(sender);
		}

		for(IRadioDevice device : getDevices())
			if(!visited.contains(device)&&device.isRadioAvailable()&&
					device.getFrequency()==sender.getFrequency()&&distanceCheck(sender, device)&&
					device.onRadioReceive(packet))
			{
				visited.add(device);
				sendPacket(packet, device, visited);
			}
	}

	/**
	 * Adds a usability cooldown to radio devices within the specified radius.
	 */
	public void disruptDevices(World world, Vec3d position, float radius, int cooldown)
	{
		if(world==null||world.isRemote||radius <= 0||cooldown <= 0)
			return;

		flushRemovedDevices();
		double radiusSq = radius*radius;
		int dimension = world.provider.getDimension();
		for(IRadioDevice device : devices)
		{
			DimensionBlockPos devicePosition = device.getDevicePosition();
			if(devicePosition.dimension!=dimension)
				continue;
			double x = devicePosition.getX()+0.5-position.x;
			double y = devicePosition.getY()+0.5-position.y;
			double z = devicePosition.getZ()+0.5-position.z;
			if(x*x+y*y+z*z <= radiusSq)
				device.addRadioCooldown(cooldown);
		}
	}

	public void sendPacketItem()
	{
	}

	public boolean distanceCheck(IRadioDevice device1, IRadioDevice device2)
	{
		DimensionBlockPos pos1 = device1.getDevicePosition();
		DimensionBlockPos pos2 = device2.getDevicePosition();
		if(pos1.dimension!=pos2.dimension)
			return false;
		float range = device1.getRange();
		return pos1.distanceSq(pos2) <= range*range;
	}

	private void flushRemovedDevices()
	{
		if(toRemove.isEmpty())
			return;
		devices.removeAll(toRemove);
		toRemove.clear();
	}
}
