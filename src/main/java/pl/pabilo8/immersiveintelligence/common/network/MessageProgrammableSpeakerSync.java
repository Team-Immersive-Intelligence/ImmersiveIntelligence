package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAlarmSiren;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityProgrammableSpeaker;

/**
 * Created by Pabilo8 on 16-06-2019.
 */
public class MessageProgrammableSpeakerSync implements IMessage
{
	public boolean active;
	public int x, y, z;
	public float pitch,volume;
	public String soundID;

	public MessageProgrammableSpeakerSync(boolean active, BlockPos pos, float pitch, float volume, String soundID)
	{
		this.active = active;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.pitch = pitch;
		this.volume = volume;
		this.soundID=soundID;
	}

	public MessageProgrammableSpeakerSync()
	{
		this.active = false;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
		this.pitch = 1f;
		this.volume = 1f;
		this.soundID="";
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.active);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeFloat(this.pitch);
		buf.writeFloat(this.volume);
		ByteBufUtils.writeUTF8String(buf,this.soundID);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.active = buf.readBoolean();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.pitch=buf.readFloat();
		this.volume=buf.readFloat();
		this.soundID=ByteBufUtils.readUTF8String(buf);
	}

	public static class Handler implements IMessageHandler<MessageProgrammableSpeakerSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageProgrammableSpeakerSync message, MessageContext ctx)
		{
			ClientUtils.mc().addScheduledTask(() -> onMessageMain(message));
			return null;
		}

		private void onMessageMain(MessageProgrammableSpeakerSync message)
		{
			if(ClientUtils.mc().world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof TileEntityProgrammableSpeaker)
			{
				TileEntityProgrammableSpeaker te = (TileEntityProgrammableSpeaker)ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
				if(te!=null)
				{
					te.active = message.active;
					te.tone = message.pitch;
					te.soundVolume = message.volume;
					te.soundID =message.soundID;
				}
			}
		}
	}

}