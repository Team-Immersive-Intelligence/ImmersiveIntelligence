package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 08.15.2025
 */
public class ContainerRepairCrate extends ContainerIIBase<TileEntityRepairCrate>
{

	public Slot inputSlot;

	public ContainerRepairCrate(EntityPlayer player, TileEntityRepairCrate tile)
	{
		//Normal bullet slots

		super(player, tile);
		int shift = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER)?0: 27;
		for(int i = 0; i < tile.getInventory().size(); i++)

			inputSlot = this.addSlotToContainer(new Slot(this.inv, i, shift+16+(i%4)*21, 7+(i/4)*18)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return Utils.compareToOreName(stack, "plateSteel");
				}
			});

		addPlayerInventory(player.inventory, 8, 141);
	}
}
