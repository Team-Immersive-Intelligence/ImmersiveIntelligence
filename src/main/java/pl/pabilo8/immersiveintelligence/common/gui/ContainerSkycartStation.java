package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 08.14.2025
 */
public class ContainerSkycartStation extends ContainerIIBase<TileEntitySkyCartStation>
{

	public Slot inputSlot;

	public ContainerSkycartStation(EntityPlayer player, TileEntitySkyCartStation tile)
	{
		super(player, tile);

		inputSlot = this.addSlotToContainer(new Slot(this.inv, 0, 70, 47)
			{
				@Override
				public int getSlotStackLimit()
				{
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof IMotorGear;
				}
			});

		addPlayerInventory(player.inventory, 8, 141);

	}
}
