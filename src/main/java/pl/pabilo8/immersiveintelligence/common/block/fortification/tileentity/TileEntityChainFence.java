package pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity;

import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIBase;

public class TileEntityChainFence extends TileEntityIIBase
{
	@SyncNBT
	public boolean hasPost = false;
}
