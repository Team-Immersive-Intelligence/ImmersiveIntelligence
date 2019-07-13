package pl.pabilo8.immersiveintelligence.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;

/**
 * Created by Pabilo8 on 05-07-2019.
 */
public interface ITabbedGui
{
	default boolean positionEqual(ClientProxy proxy, TileEntity tile)
	{
		NBTTagCompound gdata = proxy.storedGuiData;
		BlockPos p = tile.getPos();
		if(gdata.hasKey("x")&&gdata.hasKey("y")&&gdata.hasKey("z")&&gdata.hasKey("dim"))
		{
			if(p.getX()==gdata.getInteger("x")&&p.getY()==gdata.getInteger("y")&&p.getZ()==gdata.getInteger("z")&&tile.getWorld().provider.getDimension()==gdata.getInteger("dim"))
			{
				return true;
			}
		}
		return false;
	}

	default void saveBasicData(ClientProxy proxy, TileEntity tile)
	{
		proxy.storedGuiData.setInteger("x", tile.getPos().getX());
		proxy.storedGuiData.setInteger("y", tile.getPos().getY());
		proxy.storedGuiData.setInteger("z", tile.getPos().getZ());
		proxy.storedGuiData.setInteger("dim", tile.getWorld().provider.getDimension());

	}
}
