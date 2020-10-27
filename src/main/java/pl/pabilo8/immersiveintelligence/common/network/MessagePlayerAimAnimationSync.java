/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;

public class MessagePlayerAimAnimationSync implements IMessage
{
	int entityID;
	boolean aiming;

	public MessagePlayerAimAnimationSync(Entity entity, boolean aiming)
	{
		this.entityID = entity.getEntityId();
		this.aiming = aiming;
	}

	public MessagePlayerAimAnimationSync()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.aiming = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.entityID);
		buf.writeBoolean(aiming);
	}

	public static class HandlerClient implements IMessageHandler<MessagePlayerAimAnimationSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessagePlayerAimAnimationSync message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				World world = ImmersiveEngineering.proxy.getClientWorld();
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					Entity entity = world.getEntityByID(message.entityID);
					if(entity instanceof EntityPlayer)
					{
						if(message.aiming)
							ClientEventHandler.aimingPlayers.add((EntityPlayer)entity);
						else
							ClientEventHandler.aimingPlayers.remove(entity);
					}
				}
			});
			return null;
		}
	}
}