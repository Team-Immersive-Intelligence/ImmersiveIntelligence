package pl.pabilo8.immersiveintelligence.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.api.utils.IItemScrollable;

/**
 * Created by Pabilo8 on 24-06-2019.
 */
public class MessageItemScrollableSwitch implements IMessage
{
	boolean forward;

	public MessageItemScrollableSwitch(boolean forward)
	{
		this.forward = forward;
	}

	public MessageItemScrollableSwitch()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.forward = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.forward);
	}

	public static class Handler implements IMessageHandler<MessageItemScrollableSwitch, IMessage>
	{
		@Override
		public IMessage onMessage(MessageItemScrollableSwitch message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() ->
			{
				ItemStack equipped = player.getHeldItem(EnumHand.MAIN_HAND);
				if(equipped.getItem() instanceof IItemScrollable)
					((IItemScrollable)equipped.getItem()).onScroll(equipped, message.forward, player);
			});
			return null;
		}
	}
}
