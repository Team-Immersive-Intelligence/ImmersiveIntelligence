package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

/**
 * @author Pabilo8
 * @since 12.12.2023
 */
public class MultiblockRedstoneNetwork<T extends TileEntityMultiblockIIBase<T> & IRedstoneConnector>
{
	private T tile;
	private RedstoneWireNetwork wireNetwork;
	private boolean refreshWireNetwork = false;
	private boolean redstoneChanged = false;

	public MultiblockRedstoneNetwork(T tile)
	{
		this.tile = tile;
		wireNetwork = new RedstoneWireNetwork().add(tile);
	}

	public RedstoneWireNetwork getNetwork()
	{
		return wireNetwork;
	}

	public void setNetwork(RedstoneWireNetwork wireNetwork)
	{
		this.wireNetwork = wireNetwork;
	}
}
