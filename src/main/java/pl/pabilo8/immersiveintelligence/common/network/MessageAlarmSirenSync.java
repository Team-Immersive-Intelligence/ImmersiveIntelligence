package pl.pabilo8.immersiveintelligence.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAlarmSiren;

/**
 * Created by Pabilo8 on 16-06-2019.
 */
public class MessageAlarmSirenSync implements IMessage
{
	public boolean active;
	public int x, y, z;

	public MessageAlarmSirenSync(boolean active, BlockPos pos)
	{
		this.active = active;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public MessageAlarmSirenSync()
	{
		this.active = false;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.active);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.active = buf.readBoolean();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

	}

	public static class Handler implements IMessageHandler<MessageAlarmSirenSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageAlarmSirenSync message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> onMessageMain(message));
			return null;
		}

		private void onMessageMain(MessageAlarmSirenSync message)
		{
			if(Minecraft.getMinecraft().world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof TileEntityAlarmSiren)
			{
				TileEntityAlarmSiren te = (TileEntityAlarmSiren)Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
				te.active = message.active;
			}
		}
	}

}