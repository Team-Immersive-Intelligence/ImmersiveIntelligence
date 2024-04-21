package pl.pabilo8.immersiveintelligence.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:16 PM
 */
public class EventIIItemCreated extends Event
{
	private EntityPlayer entityPlayer;
	private ItemStack itemStack;

	public EventIIItemCreated(EntityPlayer entityPlayer, ItemStack itemStack)
	{
		this.itemStack = itemStack;
		this.entityPlayer = entityPlayer;
	}

	public EntityPlayer getEntityPlayer()
	{
		return this.entityPlayer;
	}
	public ItemStack getItemStack()
	{
		return this.itemStack;
	}

	/**
	 * Fire {@link EventIIItemCreated} event.
	 * @param entityPlayer Player that created the item
	 * @param itemStack Item that was created
	 * @return New instance of {@link EventIIItemCreated}
	 */
	public static EventIIItemCreated fireItemCreatedEvent(EntityPlayer entityPlayer, ItemStack itemStack)
	{
		EventIIItemCreated event = new EventIIItemCreated(entityPlayer, itemStack);
		MinecraftForge.EVENT_BUS.post(event);
		return event;
	}
}
