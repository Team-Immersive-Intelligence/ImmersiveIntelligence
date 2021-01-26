package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;

/**
 * @author Pabilo8
 * @since 20.01.2021
 */
public class MessageParticleEffect implements IMessage
{
	String id;
	public double x, y, z;

	public MessageParticleEffect(double x, double y, double z, String id)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

	public MessageParticleEffect()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.id = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		ByteBufUtils.writeUTF8String(buf, id);
	}

	public static class HandlerClient implements IMessageHandler<MessageParticleEffect, IMessage>
	{
		@Override
		public IMessage onMessage(MessageParticleEffect message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				WorldClient world = ClientUtils.mc().world;
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					switch(message.id)
					{
						case "white_phosphorus":
						{
							ParticleUtils.spawnExplosionPhosphorusFX(message.x, message.y, message.z);
						}
						break;
					}
				}

			});
			return null;
		}
	}
}