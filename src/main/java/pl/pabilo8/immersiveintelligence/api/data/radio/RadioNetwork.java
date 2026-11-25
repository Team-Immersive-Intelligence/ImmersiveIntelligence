package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;

import java.util.*;

/**
 * Connects {@link IRadioDevice Radio Devices} of the same {@link IRadioDevice#getFrequency() frequency} and routes data packets through them.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.11.2026
 * @ii-approved 0.3.1
 * @since 23.06.2019
 */
public class RadioNetwork
{
	public static RadioNetwork INSTANCE = new RadioNetwork();

	@CapabilityInject(IRadioDevice.class)
	public static Capability<IRadioDevice> RADIO_CAPABILITY = null;

	@Getter
	ArrayList<IRadioDevice> devices = new ArrayList<>();
	ArrayList<IRadioDevice> toRemove = new ArrayList<>();

	/**
	 * Registers the capability for entity and item radio providers.
	 */
	public static void registerCapability()
	{
		CapabilityManager.INSTANCE.register(IRadioDevice.class, new Capability.IStorage<>()
		{
			@Override
			public NBTBase writeNBT(Capability<IRadioDevice> capability, IRadioDevice instance, EnumFacing side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<IRadioDevice> capability, IRadioDevice instance, EnumFacing side, NBTBase nbt)
			{
			}
		}, () -> null);
	}

	public boolean addDevice(IRadioDevice device)
	{
		World world = device.getIIWorld();
		if(world!=null&&world.isRemote)
			return false;
		boolean pendingRemoval = toRemove.remove(device);
		if(devices.contains(device))
			return pendingRemoval;
		devices.add(device);
		return true;
	}

	public boolean removeDevice(IRadioDevice device)
	{
		World world = device.getIIWorld();
		if(world!=null&&world.isRemote||toRemove.contains(device))
			return false;
		return toRemove.add(device);
	}

	public void clearDevices()
	{
		devices.clear();
		toRemove.clear();
	}

	/**
	 * Sends a packet through all available radios that can receive it.
	 */
	public void sendPacket(DataPacket packet, IRadioDevice sender, ArrayList<IRadioDevice> visited)
	{
		if(sender==null)
			return;
		World world = sender.getIIWorld();
		if(world!=null&&world.isRemote)
			return;

		flushRemovedDevices();
		sendPacket(packet, sender, sender, visited, world, new ArrayList<>(devices), new HashMap<>());
	}

	private void sendPacket(DataPacket packet, IRadioDevice sender, IRadioDevice origin, ArrayList<IRadioDevice> visited,
							World world, Collection<IRadioDevice> fixed, Map<Entity, Set<IRadioDevice>> portable)
	{
		if(!sender.isRadioAvailable()||!sender.canRelayRadio())
			return;

		if(!visited.contains(sender))
		{
			visited.add(sender);
			sender.onRadioSend(packet);
		}

		Collection<IRadioDevice> nearby = new LinkedHashSet<>(fixed);
		if(world!=null)
			for(Entity entity : world.getEntitiesWithinAABB(Entity.class,
					new AxisAlignedBB(sender.getRadioDevicePosition()).grow(sender.getRange())))
				nearby.addAll(portable.computeIfAbsent(entity, RadioNetwork::getEntityRadios));

		for(IRadioDevice device : nearby)
			if(!visited.contains(device)&&!toRemove.contains(device)&&device.isRadioAvailable()&&
					device.getFrequency()==sender.getFrequency()&&distanceCheck(sender, device)&&
					device.onRadioReceive(packet, origin))
			{
				visited.add(device);
				if(device.canRelayRadio())
					sendPacket(packet, device, origin, visited, world, fixed, portable);
			}
	}

	/**
	 * Gets radio devices provided by an entity or its equipment.
	 */
	public static Set<IRadioDevice> getEntityRadios(Entity entity)
	{
		Set<IRadioDevice> radios = new LinkedHashSet<>();
		if(RADIO_CAPABILITY==null||entity.isDead)
			return radios;

		addRadio(radios, entity, entity, ItemStack.EMPTY);
		ItemStack chest = entity instanceof EntityLivingBase?
				((EntityLivingBase)entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST): ItemStack.EMPTY;
		addRadio(radios, ItemIIBackpackBase.getAttachedBackpack(chest), entity, chest);
		if(IICompatModule.baubles&&entity instanceof EntityPlayer)
		{
			ItemStack body = BaublesHelper.getBodyBauble((EntityPlayer)entity);
			addRadio(radios, body, entity, body);
		}
		for(ItemStack stack : entity.getEquipmentAndArmor())
			if(stack==chest||!(stack.getItem() instanceof ItemIIBackpackBase))
				addRadio(radios, stack, entity, stack);
		return radios;
	}

	private static void addRadio(Set<IRadioDevice> radios, ICapabilityProvider provider, Entity wearer, ItemStack equipment)
	{
		IRadioDevice radio = provider.getCapability(RADIO_CAPABILITY, null);
		if(radio!=null)
			radios.add(radio.bindTo(wearer, equipment));
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
		Set<IRadioDevice> nearby = new LinkedHashSet<>(devices);
		for(Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.x, position.y, position.z,
				position.x, position.y, position.z).grow(radius)))
			nearby.addAll(getEntityRadios(entity));

		for(IRadioDevice device : nearby)
		{
			DimensionBlockPos devicePosition = device.getRadioDevicePosition();
			if(devicePosition.dimension==dimension&&devicePosition.distanceSqToCenter(position.x, position.y, position.z) <= radiusSq)
				device.addRadioCooldown(cooldown);
		}
	}

	public boolean distanceCheck(IRadioDevice device1, IRadioDevice device2)
	{
		DimensionBlockPos pos1 = device1.getRadioDevicePosition();
		DimensionBlockPos pos2 = device2.getRadioDevicePosition();
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
