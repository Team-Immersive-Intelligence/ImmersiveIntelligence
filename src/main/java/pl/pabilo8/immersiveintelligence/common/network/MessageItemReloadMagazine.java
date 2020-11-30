package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIISubmachinegun;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class MessageItemReloadMagazine implements IMessage
{
	public MessageItemReloadMagazine()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{

	}

	public static class Handler implements IMessageHandler<MessageItemReloadMagazine, IMessage>
	{
		@Override
		public IMessage onMessage(MessageItemReloadMagazine message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() ->
			{
				ItemStack equipped = player.getHeldItem(EnumHand.MAIN_HAND);
				if(equipped.getItem() instanceof ItemIISubmachinegun)
					ItemNBTHelper.setBoolean(equipped, "shouldReload", true);
			});
			return null;
		}
	}
}
