package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;

import java.util.UUID;

/**
 * Created by Pabilo8 on 04-07-2019.
 */
public class MessageGuiNBT implements IMessage
{
	//Used in changing tabs in guis (its being sent to server only)
	public int id, x, y, z;
	public String player;

	public MessageGuiNBT(int id, BlockPos pos, EntityPlayer player)
	{
		//The number of the opened gui
		this.id = id;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.player = player.getUniqueID().toString();
	}

	public MessageGuiNBT(IIGuiList id, BlockPos pos, EntityPlayer player)
	{
		this(id.ordinal(), pos, player);
	}

	public MessageGuiNBT()
	{
		this.id = -1;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
		this.player = "_";
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.id);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		ByteBufUtils.writeUTF8String(buf, player);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.id = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.player = ByteBufUtils.readUTF8String(buf);
	}

	public static class HandlerServer implements IMessageHandler<MessageGuiNBT, IMessage>
	{
		@Override
		public IMessage onMessage(MessageGuiNBT message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() ->
			{
				if(world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof IGuiTile)
				{
					if(message.player.equals("_")||world.getPlayerEntityByUUID(UUID.fromString(message.player))==null)
						return;

					EntityPlayer player = world.getPlayerEntityByUUID(UUID.fromString(message.player));

					ImmersiveIntelligence.proxy.onServerGuiChangeRequest(world.getTileEntity(new BlockPos(message.x, message.y, message.z)), message.id, player);
				}
			});
			return null;
		}
	}
}
