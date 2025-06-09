package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenCrate;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
@ChestContainer
public class ContainerIICrate<T extends TileEntityWoodenCrate> extends ContainerIIBase<T>
{
	public ContainerIICrate(EntityPlayer player, T tile)
	{
		super(player, tile);
		for(int i = 0; i < tile.getInventory().size(); i++)
			this.addSlotToContainer(new Slot(this.inv, i, 8+(i%9)*18, 14+(i/9)*18)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return IEApi.isAllowedInCrate(stack);
				}
			});

		addPlayerInventory(player.inventory, 8, 87);
	}
}