package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerLeggings;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class MessageItemKeybind extends IIMessage
{
	public static final int KEYBIND_GUN_RELOAD = 0;
	public static final int KEYBIND_HEADGEAR = 1;
	public static final int KEYBIND_EXOSKELETON = 2;

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

		ItemStack equipped;
		switch(id)
		{
			case KEYBIND_GUN_RELOAD:
			{
				if((equipped = player.getHeldItem(EnumHand.MAIN_HAND)).getItem() instanceof ItemIIGunBase)
					ItemNBTHelper.setBoolean(equipped, "shouldReload", true);
			}
			break;
			case KEYBIND_HEADGEAR:
			{
				if((equipped = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)).getItem() instanceof ItemIILightEngineerHelmet)
				{
					boolean newState = !ItemNBTHelper.getBoolean(equipped, "headgearActive");
					ItemNBTHelper.setBoolean(equipped, "headgearActive", newState);
					if(!newState)
						player.removePotionEffect(MobEffects.NIGHT_VISION);

					IIPacketHandler.sendChatTranslation(player, IIReference.DESCRIPTION_KEY+(newState?"infrared_enabled": "infrared_disabled"));
				}
			}
			break;
			case KEYBIND_EXOSKELETON:
			{
				if((equipped = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS)).getItem() instanceof ItemIILightEngineerLeggings)
				{
					int mode = ItemNBTHelper.getInt(equipped, "exoskeletonMode");
					ItemNBTHelper.setInt(equipped, "exoskeletonMode", mode = IIUtils.cycleInt(!player.isSneaking(), mode, 0, 2));
					IIPacketHandler.sendChatTranslation(player, IIReference.INFO_KEY+"exoskeleton."+mode);
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
