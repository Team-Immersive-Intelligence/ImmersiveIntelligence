package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;

public class MessageExplosion implements IMessage
{
	boolean flaming, damagesTerrain;
	float radius, strength;
	public double x, y, z;

	public MessageExplosion(boolean flaming, boolean damagesTerrain, float radius, float strength, double x, double y, double z)
	{
		this.flaming = flaming;
		this.damagesTerrain = damagesTerrain;
		this.radius = radius;
		this.strength = strength;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public MessageExplosion()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.flaming = buf.readBoolean();
		this.damagesTerrain = buf.readBoolean();

		this.radius = buf.readFloat();
		this.strength = buf.readFloat();

		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(flaming);
		buf.writeBoolean(damagesTerrain);

		buf.writeFloat(radius);
		buf.writeFloat(strength);

		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

	public static class HandlerClient implements IMessageHandler<MessageExplosion, IMessage>
	{
		@Override
		public IMessage onMessage(MessageExplosion message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				World world = ImmersiveEngineering.proxy.getClientWorld();
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					/*
					for(int i = 0; i < 20; i++)
					{
						Vec3d vecDir = new Vec3d(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f+(Math.floor(i/16f)*0.35f), Utils.RAND.nextGaussian()*.25f).scale(8f);
						ParticleUtils.spawnTracerFX(message.x, message.y, message.z, vecDir.x, vecDir.y, vecDir.z, 0.25f,0xffffff);
					}
					 */
					ParticleUtils.spawnExplosionBoomFX(world, message.x, message.y, message.z, message.radius, message.strength, message.flaming, message.damagesTerrain);
				}

			});
			return null;
		}
	}
}