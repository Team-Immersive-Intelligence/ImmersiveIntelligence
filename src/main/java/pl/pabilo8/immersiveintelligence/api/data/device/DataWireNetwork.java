package pl.pabilo8.immersiveintelligence.api.data.device;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import java.lang.ref.WeakReference;
import java.util.*;

import static blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.INSTANCE;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class DataWireNetwork
{
	public List<WeakReference<IDataConnector>> connectors = new ArrayList<>();

	public DataWireNetwork add(IDataConnector connector)
	{
		connectors.add(new WeakReference<>(connector));
		return this;
	}

	public void mergeNetwork(DataWireNetwork wireNetwork)
	{
		List<WeakReference<IDataConnector>> conns = null;
		if(connectors.size() > 0)
			conns = connectors;
		else if(wireNetwork.connectors.size() > 0)
			conns = wireNetwork.connectors;
		if(conns==null)//No connectors to merge
			return;
		IDataConnector start = null;
		for(WeakReference<IDataConnector> conn : conns)
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
	}

	public void removeFromNetwork(IDataConnector removedConnector)
	{
		Iterator<WeakReference<IDataConnector>> iterator = connectors.iterator();
		Set<DataWireNetwork> knownNets = new HashSet<>();
		while(iterator.hasNext())
		{
			WeakReference<IDataConnector> conn = iterator.next();
			IDataConnector start = conn.get();
			if(start!=null&&!knownNets.contains(start.getDataNetwork()))
			{
				DataWireNetwork newNet = new DataWireNetwork();
				updateConnectors(Utils.toCC(start), start.getConnectorWorld(), newNet);
				knownNets.add(newNet);
			}
		}

	}

	public static void updateConnectors(BlockPos start, World world, DataWireNetwork network)
	{
		int dimension = world.provider.getDimension();
		Set<BlockPos> open = new HashSet<>();
		open.add(start);
		Set<BlockPos> closed = new HashSet<>();
		network.connectors.clear();
		while(!open.isEmpty())
		{
			Iterator<BlockPos> it = open.iterator();
			BlockPos next = it.next();
			it.remove();
			IImmersiveConnectable iic = ApiUtils.toIIC(next, world);
			closed.add(next);
			Set<Connection> connsAtBlock = INSTANCE.getConnections(dimension, next);

			if(iic instanceof IDataConnector)
			{
				((IDataConnector)iic).setDataNetwork(network);
				network.connectors.add(new WeakReference<>((IDataConnector)iic));
			}
			if(connsAtBlock!=null&&iic!=null)
				for(Connection c : connsAtBlock)
				{
					if(Objects.equals(c.cableType.getCategory(), IIDataWireType.DATA_CATEGORY)&&
							iic.allowEnergyToPass(c)&&
							!closed.contains(c.end))
						open.add(c.end);
				}
		}
	}

	//Ethernet-like data packet sending
	public void sendPacket(DataPacket packet, IDataConnector sender)
	{
		for(WeakReference<IDataConnector> connectorRef : connectors)
		{
			IDataConnector connector = connectorRef.get();
			if(connector!=null&&!connector.equals(sender))
			{
				connector.onPacketReceive(packet);
			}
		}
	}
}
