package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.07.2021
 */
public class MessageManualClose extends IIMessage
{
	private EnumHand enumHand;
	private EasyNBT easyNBT;

	public MessageManualClose()
	{
	}

	public MessageManualClose(@Nonnull EnumHand enumHand, @Nonnull EasyNBT easyNBT)
	{
		this.enumHand = enumHand;
		this.easyNBT = easyNBT;
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		if(handler.player==null)
			return;

		ItemStack manualStack = handler.player.getHeldItem(enumHand);
		if(!IIItemUtils.isEngineersManual(manualStack))
			return;

		String lastMultiblock = ItemNBTHelper.getString(manualStack, "lastMultiblock");
		if(!easyNBT.hasKey("lastMultiblock")&&!lastMultiblock.isEmpty()&&!lastMultiblock.startsWith("II"))
			easyNBT = EasyNBT.newNBT().withString("lastMultiblock", lastMultiblock);
		manualStack.setTagCompound(easyNBT.unwrap());
	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.enumHand = readEnum(buf, EnumHand.class);
		this.easyNBT = readEasyNBT(buf);
	}


	@Override
	public void toBytes(ByteBuf buf)
	{
		writeEnum(buf, enumHand);
		writeEasyNBT(buf, easyNBT);
	}
}
