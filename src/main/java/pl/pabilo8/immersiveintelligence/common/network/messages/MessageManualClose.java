package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 20.07.2021
 */
public class MessageManualClose extends IIMessage
{
	private String skin;

	public MessageManualClose(String skin)
	{
		this.skin = skin;
	}

	public MessageManualClose()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		if(handler.player==null)
			return;

		ItemStack mainItem = handler.player.getHeldItemMainhand();
		ItemStack offItem = handler.player.getHeldItemOffhand();

		ItemStack target = isManual(mainItem)?mainItem: isManual(offItem)?offItem: null;

		if(target!=null)
			if((skin==null||skin.isEmpty())&&ItemNBTHelper.hasKey(target, "lastSkin"))
				ItemNBTHelper.remove(target, "lastSkin");
			else if(skin!=null)
				ItemNBTHelper.setString(target, "lastSkin", skin);
	}

	private boolean isManual(ItemStack stack)
	{
		return stack.getItem()==IEContent.itemTool&&stack.getItemDamage()==3;
	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.skin = readString(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeString(buf, skin);
	}
}
