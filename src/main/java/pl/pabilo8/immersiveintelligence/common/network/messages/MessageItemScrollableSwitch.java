package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class MessageItemScrollableSwitch extends IIMessage
{
	private boolean forward;

	public MessageItemScrollableSwitch(boolean forward)
	{
		this.forward = forward;
	}

	public MessageItemScrollableSwitch()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		ItemStack equipped = handler.player.getHeldItem(EnumHand.MAIN_HAND);
		if(equipped.getItem() instanceof IItemScrollable)
			((IItemScrollable)equipped.getItem()).onScroll(equipped, forward, handler.player);
	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
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
}
