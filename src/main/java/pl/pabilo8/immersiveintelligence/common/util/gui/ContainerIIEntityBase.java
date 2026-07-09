package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Entity-backed II container base for entities that expose an {@link IIEInventory}.
 *
 * @param <E> entity type
 */
public abstract class ContainerIIEntityBase<E extends Entity & IIEInventory> extends ContainerIIBase
{
	public final E entity;

	public ContainerIIEntityBase(EntityPlayer player, E entity)
	{
		super(player.inventory, new InventoryIIEInventory<>(entity, entity::getDisplayName, entityPlayer ->
				!entity.isDead&&entityPlayer.getDistanceSq(entity.posX, entity.posY, entity.posZ) <= 64.0D
		), entity.getInventory().size());
		this.entity = entity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return inv!=null&&inv.isUsableByPlayer(player);
	}

	@Override
	protected boolean isStackValid(int slot, ItemStack stack)
	{
		return entity.isStackValid(slot, stack);
	}

	@Override
	protected int getSlotLimit(int slot)
	{
		return entity.getSlotLimit(slot);
	}

	public E getEntity()
	{
		return entity;
	}
}
