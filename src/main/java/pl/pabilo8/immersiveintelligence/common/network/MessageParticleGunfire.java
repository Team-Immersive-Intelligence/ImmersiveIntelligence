package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;

/**
 * @author Pabilo8
 * @since 20.07.2021
 */
public class MessageParticleGunfire implements IMessage
{
	public Vec3d pos;
	public Vec3d dir;
	public float size;

	public MessageParticleGunfire(Vec3d pos, Vec3d dir, float size)
	{
		this.pos = pos;
		this.dir = dir;
		this.size = size;
	}

	public MessageParticleGunfire()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.dir = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.size = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);

		buf.writeDouble(dir.x);
		buf.writeDouble(dir.y);
		buf.writeDouble(dir.z);

		buf.writeFloat(size);
	}

	public static class HandlerClient implements IMessageHandler<MessageParticleGunfire, IMessage>
	{
		@Override
		public IMessage onMessage(MessageParticleGunfire message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				World world = ImmersiveEngineering.proxy.getClientWorld();
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					ParticleUtils.spawnGunfireFX(message.pos.x, message.pos.y, message.pos.z, message.dir.x, message.dir.y, message.dir.z, message.size);
				}

			});
			return null;
		}
	}
}
