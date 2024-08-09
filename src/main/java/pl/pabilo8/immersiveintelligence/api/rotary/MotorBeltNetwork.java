package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

import static blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.INSTANCE;
import static pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils.BELT_GENERAL_CATEGORY;

/**
 * @author Pabilo8
 * @updated 09.08.2024
 * @ii-approved 0.3.1
 * @since 2019-05-31
 */
public class MotorBeltNetwork
{
	/**
	 * All connectors in this rotary network
	 */
	public List<WeakReference<IMotorBeltConnector>> connectors = new ArrayList<>();
	/**
	 * ALl links between {@link #connectors} in this rotary network
	 */
	public List<Connection> connections = new ArrayList<>();
	/**
	 * Loss of speed in this network (in DPT)
	 */
	public float loss = 0f;
	/**
	 * Speed with which {@link #connectors} rotate in DPT (Degrees Per Tick)
	 */
	private float speed = 0f;
	/**
	 * Torque with which {@link #connectors} rotate, in Torque Units (TU)
	 */
	private float torque = 0f;

	public static void updateConnectors(BlockPos start, World world, MotorBeltNetwork network)
	{
		int dimension = world.provider.getDimension();
		Set<BlockPos> open = new HashSet<>();
		open.add(start);
		Set<BlockPos> closed = new HashSet<>();
		network.connectors.clear();
		network.loss = 0;
		while(!open.isEmpty())
		{
			Iterator<BlockPos> it = open.iterator();
			BlockPos next = it.next();
			it.remove();
			IImmersiveConnectable iic = ApiUtils.toIIC(next, world);
			closed.add(next);
			Set<Connection> connsAtBlock = INSTANCE.getConnections(dimension, next);

			if(connsAtBlock!=null&&iic!=null)
				for(Connection c : connsAtBlock)
				{
					if(iic.allowEnergyToPass(c)&&
							BELT_GENERAL_CATEGORY.equals(c.cableType.getCategory())&&
							!closed.contains(c.end))
					{
						open.add(c.end);
						network.connections.add(c);
					}
				}

			if(iic instanceof IMotorBeltConnector)
			{
				((IMotorBeltConnector)iic).setNetwork(network);
				network.connectors.add(new WeakReference<>((IMotorBeltConnector)iic));
			}

		}
		network.updateValues();
	}

	public MotorBeltNetwork add(IMotorBeltConnector connector)
	{
		connectors.add(new WeakReference<>(connector));
		return this;
	}

	public void mergeNetwork(MotorBeltNetwork wireNetwork)
	{
		List<WeakReference<IMotorBeltConnector>> conns = null;
		if(connectors.size() > 0)
			conns = connectors;
		else if(wireNetwork.connectors.size() > 0)
			conns = wireNetwork.connectors;
		if(conns==null)//No connectors to merge
			return;
		IMotorBeltConnector start = null;
		for(WeakReference<IMotorBeltConnector> conn : conns)
			if(conn.get()!=null)
			{
				start = conn.get();
				break;
			}
		if(start!=null)
		{
			BlockPos startPos = Utils.toCC(start);
			updateConnectors(startPos, start.getConnectorWorld(), this);
		}
		updateValues();
	}

	public void removeFromNetwork(IMotorBeltConnector removedConnector)
	{
		Iterator<WeakReference<IMotorBeltConnector>> iterator = connectors.iterator();
		Set<MotorBeltNetwork> knownNets = new HashSet<>();
		while(iterator.hasNext())
		{
			WeakReference<IMotorBeltConnector> conn = iterator.next();
			IMotorBeltConnector start = conn.get();
			if(start!=null&&!knownNets.contains(start.getNetwork()))
			{
				MotorBeltNetwork newNet = new MotorBeltNetwork();
				updateConnectors(Utils.toCC(start), start.getConnectorWorld(), newNet);
				knownNets.add(newNet);
			}
		}

	}

	public void updateValues()
	{
		speed = torque = 0;
		Set<IMotorBeltConnector> validReferences = connectors.stream().map(Reference::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		//rpm is the average (not counting 0)
		//torque is the sum
		validReferences.stream()
				.map(IMotorBeltConnector::getRotaryStorage)
				.forEach(storage -> {
					torque += storage.getTorque();
					if(storage.speed > speed)
						speed = storage.speed;
				});

		loss = (float)connections.stream().mapToDouble(Connection::getBaseLoss).sum();
		speed = Math.max(0, speed-(speed*loss));

		validReferences.forEach(IMotorBeltConnector::onChange);
	}

	//Use this when outputting energy with a connector
	public RotaryStorage getEnergyStorage()
	{
		return new RotaryStorage(torque, speed);
	}

	public double getNetworkSpeed()
	{
		return speed;
	}

	public double getNetworkTorque()
	{
		return torque;
	}

	@SideOnly(Side.CLIENT)
	public void setClient(float speed, float torque)
	{
		this.speed = speed;
		this.torque = torque;
	}
}
