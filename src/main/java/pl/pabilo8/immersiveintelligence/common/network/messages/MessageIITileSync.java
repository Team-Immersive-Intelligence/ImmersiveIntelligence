package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 22.09.2022
 */
public class MessageIITileSync extends IIMessage
{
	BlockPos pos;
	NBTTagCompound nbt;

	public MessageIITileSync(TileEntityIEBase tile, NBTTagCompound nbt)
	{
		this.pos = tile.getPos();
		this.nbt = nbt;
	}

	public MessageIITileSync(TileEntityIEBase tile, EasyNBT nbt)
	{
		this(tile, nbt.unwrap());
	}

	public MessageIITileSync(TileEntityIEBase tile)
	{
		this(tile, getNBT(tile));
	}

	public static NBTTagCompound getNBT(TileEntityIEBase tile)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		tile.writeCustomNBT(nbt, false);
		return nbt;
	}

	public MessageIITileSync()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		if(world.isBlockLoaded(pos))
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEntityIEBase)
				((TileEntityIEBase)tile).receiveMessageFromClient(nbt);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(world.isBlockLoaded(pos))
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEntityIEBase)
				((TileEntityIEBase)tile).receiveMessageFromServer(nbt);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = readPos(buf);
		this.nbt = readTagCompound(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writePos(buf, pos);
		writeTagCompound(buf, nbt);
	}
}
