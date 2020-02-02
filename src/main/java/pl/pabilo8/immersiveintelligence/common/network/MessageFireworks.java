/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFireworks implements IMessage
{
	NBTTagCompound tag;
	public float x, y, z;

	public MessageFireworks(NBTTagCompound tag, float x, float y, float z)
	{
		this.tag = tag;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public MessageFireworks()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.tag = ByteBufUtils.readTag(buf);
		this.x = buf.readFloat();
		this.y = buf.readFloat();
		this.z = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.tag);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeFloat(z);
	}

	public static class HandlerClient implements IMessageHandler<MessageFireworks, IMessage>
	{
		@Override
		public IMessage onMessage(MessageFireworks message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				WorldClient world = ClientUtils.mc().world;
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					NBTTagList list = new NBTTagList();
					list.appendTag(message.tag.getTag("Explosion"));
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setTag("Explosions", list);
					world.makeFireworks(message.x, message.y, message.z, 0, 0, 0, nbt);
					//{Flight:1b,Explosions:[{Type:0b,Colors:[I;3887386]}]}
				}
			});
			return null;
		}
	}
}