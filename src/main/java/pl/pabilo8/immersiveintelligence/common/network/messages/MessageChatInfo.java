package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.common.util.ChatUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 23.09.2022
 */
public class MessageChatInfo extends IIMessage
{
	ITextComponent[] chatMessages;

	public MessageChatInfo(ITextComponent... chatMessages)
	{
		this.chatMessages = chatMessages;
	}

	public MessageChatInfo()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		Minecraft.getMinecraft().addScheduledTask(() -> ChatUtils.sendClientNoSpamMessages(chatMessages));
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int l = buf.readInt();
		chatMessages = new ITextComponent[l];
		for(int i = 0; i < l; i++)
			chatMessages[i] = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(chatMessages.length);
		for(ITextComponent component : chatMessages)
			ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(component));
	}
}
