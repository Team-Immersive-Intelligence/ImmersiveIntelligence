package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerLeggings;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class MessageItemKeybind implements IMessage
{
	int id = 0;

	public MessageItemKeybind()
	{
	}

	public MessageItemKeybind(int id)
	{
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<MessageItemKeybind, IMessage>
	{
		@Override
		public IMessage onMessage(MessageItemKeybind message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() ->
			{
				switch(message.id)
				{
					//smg
					case 0:
					{
						ItemStack equipped = player.getHeldItem(EnumHand.MAIN_HAND);
						if(equipped.getItem() instanceof ItemIISubmachinegun)
							ItemNBTHelper.setBoolean(equipped, "shouldReload", true);
					}
					break;
					//headgear
					case 1:
					{
						ItemStack equipped = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
						if(equipped.getItem() instanceof ItemIILightEngineerHelmet)
						{
							boolean newState = !ItemNBTHelper.getBoolean(equipped, "headgearActive");
							ItemNBTHelper.setBoolean(equipped, "headgearActive", newState);
							if(!newState)
								ctx.getServerHandler().player.removePotionEffect(MobEffects.NIGHT_VISION);

							ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(
									IILib.DESCRIPTION_KEY+(newState?
											"infrared_enabled": "infrared_disabled"))), player);

						}
					}
					break;
					//exoskeleton
					case 2:
					{
						ItemStack equipped = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
						if(equipped.getItem() instanceof ItemIILightEngineerLeggings)
						{
							int mode = ItemNBTHelper.getInt(equipped, "exoskeletonMode");
							ItemNBTHelper.setInt(equipped, "exoskeletonMode", mode = IIUtils.cycleInt(!player.isSneaking(), mode, 0, 2));
							ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(IILib.INFO_KEY+"exoskeleton."+mode)), player);
						}
					}
					break;
				}
			});
			return null;
		}
	}
}
