package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 04-07-2019
 */
public class MessageGuiNBT extends IIMessage
{
	//Used in changing tabs in guis (its being sent to server only)
	private int id;
	private BlockPos pos;

	public MessageGuiNBT(int id, BlockPos pos)
	{
		//The number of the opened gui
		this.id = id;
		this.pos = pos;
	}

	public MessageGuiNBT(IIGuiList id, TileEntity te)
	{
		this(id.ordinal(), te.getPos());
	}

	@Deprecated
	public MessageGuiNBT(IIGuiList id, BlockPos pos)
	{
		this(id.ordinal(), pos);
	}

	public MessageGuiNBT()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		TileEntity te;
		if(handler.player!=null&&world.isBlockLoaded(pos)&&(te = world.getTileEntity(pos)) instanceof IGuiTile)
			ImmersiveIntelligence.proxy.onServerGuiChangeRequest(te, id, handler.player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.id);
		writePos(buf, pos);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.id = buf.readInt();
		this.pos = readPos(buf);
	}
}
