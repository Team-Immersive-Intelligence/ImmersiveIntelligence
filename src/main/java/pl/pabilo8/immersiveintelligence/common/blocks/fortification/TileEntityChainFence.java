package pl.pabilo8.immersiveintelligence.common.blocks.fortification;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityChainFence extends TileEntityIEBase
{
	boolean hasPost = false;

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setBoolean("hasPost", hasPost);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		hasPost = nbt.getBoolean("hasPost");
	}
}
