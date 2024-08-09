package pl.pabilo8.immersiveintelligence.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerBoots;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:20 PM
 */
public class LightEngineerEventHandler extends IIBaseEventHandler
{
	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event)
	{
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer)event.getEntityLiving();
		Iterable<ItemStack> armor = player.getArmorInventoryList();

		for (ItemStack piece : armor)
		{
			if (!(piece.getItem() instanceof ItemIILightEngineerBoots)) continue;
			ItemIILightEngineerBoots boots = (ItemIILightEngineerBoots)piece.getItem();
			if (boots.hasUpgrade(piece, "internal_springs"))
			{
				event.setDistance(0);
			}
		}
	}
}
