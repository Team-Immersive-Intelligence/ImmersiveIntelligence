package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
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
	public float volume;

	public MessageAlarmSirenSync(boolean active, float volume, BlockPos pos)
	{
		this.active = active;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.volume=volume;
	}

	public MessageAlarmSirenSync()
	{
		this.active = false;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
		this.volume=1f;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.active);
		buf.writeFloat(this.volume);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.active = buf.readBoolean();
		this.volume=buf.readFloat();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

	}

	public static class Handler implements IMessageHandler<MessageAlarmSirenSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageAlarmSirenSync message, MessageContext ctx)
		{
			ClientUtils.mc().addScheduledTask(() -> onMessageMain(message));
			return null;
		}

		private void onMessageMain(MessageAlarmSirenSync message)
		{
			if(ClientUtils.mc().world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof TileEntityAlarmSiren)
			{
				TileEntityAlarmSiren te = (TileEntityAlarmSiren)ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
				te.active = message.active;
				te.soundVolume = message.volume;
			}
		}
	}

}