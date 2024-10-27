package pl.pabilo8.immersiveintelligence.api.data.device;

import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public interface IDataConnector extends IImmersiveConnectable
{
	/**
	 * Sets the DataWireNetwork this connector is connected to.
	 *
	 * @param net the new network for this connector.
	 */
	void setDataNetwork(DataWireNetwork net);

	/**
	 * @return the DataWireNetwork this connector is connected to.
	 */
	DataWireNetwork getDataNetwork();

	/**
	 * Called whenever the DataWireNetwork is changed in some way (both adding/removing connectors and changes in RS values).
	 */
	void onDataChange();

	/**
	 * @return the world that this connector is in
	 */
	World getConnectorWorld();

	/**
	 * Called when received a packet (this does not apply to the connector which send the packet).
	 *
	 * @param packet the data packet.
	 */
	void onPacketReceive(DataPacket packet);

	/**
	 * Used to send a packet to a network
	 *
	 * @param packet the data packet.
	 */
	void sendPacket(DataPacket packet);

	/**
	 * @return whether this connector is capable of query-reply type communications
	 */
	default boolean isCallbackCapable()
	{
		return false;
	}
}
