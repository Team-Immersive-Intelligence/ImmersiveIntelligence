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
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.07.2019
 */
public class MessageGuiNBT extends IIMessage
{
	//Used in changing tabs in guis (its being sent to server only)
	private IIGUI id;
	private BlockPos pos;

	public MessageGuiNBT(IIGUI id, TileEntity te)
	{
		this.id = id;
		this.pos = te.getPos();
	}

	public MessageGuiNBT()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		TileEntity te;
		if(handler.player!=null&&world.isBlockLoaded(pos)&&(te = world.getTileEntity(pos)) instanceof IGuiTile)
			ImmersiveIntelligence.proxy.onServerGuiChangeRequest(te, id.ordinal(), handler.player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeEnum(buf, id);
		writePos(buf, pos);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.id = readEnum(buf, IIGUI.class);
		this.pos = readPos(buf);
	}
}
