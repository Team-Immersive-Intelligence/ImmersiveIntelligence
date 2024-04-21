package pl.pabilo8.immersiveintelligence.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:19 PM
 */
public class IIItemCreatedEventHandler extends IIBaseEventHandler
{
	private static List<ItemStack> previousTick = new ArrayList<>();

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent e)
	{
		if (e.phase==Phase.START&&e.side==Side.SERVER)
		{
			List<ItemStack> temp = new ArrayList<>();
			EntityPlayer player = e.player;
			InventoryPlayer inventory = player.inventory;

			for (int i = 0; i < inventory.getSizeInventory(); i++)
			{
				ItemStack stack = inventory.getStackInSlot(i);
				if (!previousTick.contains(stack)) stack.getItem().onCreated(stack, e.player.world, e.player);
				temp.add(stack);
			}

			previousTick.clear();
			previousTick.addAll(temp);
		}
	}
}
