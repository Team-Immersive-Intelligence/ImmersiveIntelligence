package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerLeggings;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class MessageItemKeybind extends IIMessage
{
	private int id;

	public MessageItemKeybind(int id)
	{
		this.id = id;
	}

	public MessageItemKeybind()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		EntityPlayerMP player = handler.player;
		if(player==null||player.isDead)
			return;

		switch(id)
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
						player.removePotionEffect(MobEffects.NIGHT_VISION);

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
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{

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
}
