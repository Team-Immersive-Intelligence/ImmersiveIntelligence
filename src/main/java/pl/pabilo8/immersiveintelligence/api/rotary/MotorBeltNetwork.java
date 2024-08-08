package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.*;

import static blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.INSTANCE;
import static pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils.BELT_GENERAL_CATEGORY;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class MotorBeltNetwork
{
	public List<WeakReference<IMotorBeltConnector>> connectors = new ArrayList<>();
	public List<Connection> connections = new ArrayList<>();
	public float loss = 0f;
	private float rpm = 0f, torque = 0f;

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
		double oldRPM = rpm;
		rpm = 0;
		torque = 0;
		List<Float> rpm_values = new ArrayList<>();
		List<Float> torque_values = new ArrayList<>();
		for(WeakReference<IMotorBeltConnector> connectorRef : connectors)
		{
			IMotorBeltConnector connector = connectorRef.get();
			if(connector!=null)
			{
				rpm_values.add(connector.getRotaryStorage().getRotationSpeed());
				torque_values.add(connector.getRotaryStorage().getTorque());

			}
		}

		//rpm is the average (not counting 0)
		//torque is the sum
		rpm_values.forEach(value ->
		{
			if(value > rpm)
				rpm = value;
		});
		torque_values.forEach(value -> torque += value);

		loss = 0;

		for(Connection conn : connections)
			loss += conn.getBaseLoss();

		rpm = Math.max(0, rpm-(rpm*loss));

		for(WeakReference<IMotorBeltConnector> connectorRef : connectors)
		{
			IMotorBeltConnector connector = connectorRef.get();
			if(connector!=null)
				connector.onChange();
		}
	}

	//Use this when outputting energy with a connector
	public RotaryStorage getEnergyStorage()
	{
		return new RotaryStorage(torque, rpm);
	}

	public double getNetworkRPM()
	{
		return rpm;
	}

	public double getNetworkTorque()
	{
		return torque;
	}

	@SideOnly(Side.CLIENT)
	public void setClient(float rpm, float torque)
	{
		this.rpm = rpm;
		this.torque = torque;
	}
}
