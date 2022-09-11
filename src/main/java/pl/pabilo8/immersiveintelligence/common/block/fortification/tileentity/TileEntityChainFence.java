package pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityChainFence extends TileEntityIEBase
{
	public boolean hasPost = false;

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
