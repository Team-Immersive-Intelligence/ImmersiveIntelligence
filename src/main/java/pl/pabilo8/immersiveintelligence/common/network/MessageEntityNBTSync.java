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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMotorbike;

public class MessageEntityNBTSync implements IMessage
{
	int entityID;
	NBTTagCompound tag;

	public MessageEntityNBTSync(Entity entity, NBTTagCompound tag)
	{
		this.entityID = entity.getEntityId();
		this.tag = tag;
	}

	public MessageEntityNBTSync()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.entityID);
		ByteBufUtils.writeTag(buf, this.tag);
	}

	public static class HandlerClient implements IMessageHandler<MessageEntityNBTSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageEntityNBTSync message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				World world = ImmersiveEngineering.proxy.getClientWorld();
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					Entity entity = world.getEntityByID(message.entityID);
					if(entity instanceof EntityMachinegun)
						((EntityMachinegun)entity).readEntityFromNBT(message.tag);
				}
			});
			return null;
		}
	}

	public static class HandlerServer implements IMessageHandler<MessageEntityNBTSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageEntityNBTSync message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() ->
			{
				Entity entity = world.getEntityByID(message.entityID);
				if(entity instanceof EntityMachinegun)
					((EntityMachinegun)entity).readEntityFromNBT(message.tag);
				else if(entity instanceof EntityMotorbike)
					((EntityMotorbike)entity).syncKeyPress(message.tag);
			});
			return null;
		}
	}
}