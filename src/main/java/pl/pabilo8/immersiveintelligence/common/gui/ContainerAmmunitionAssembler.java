package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.13.2025
 */
public class ContainerAmmunitionAssembler extends ContainerIIBase<TileEntityAmmunitionAssembler>
{
	public Slot inputSlot, outputSlot;

	public ContainerAmmunitionAssembler(EntityPlayer player, TileEntityAmmunitionAssembler tile)
	{
		super(player, tile);

		inputSlot = this.addSlotToContainer(new Slot(this.inv, 0, 8, 20)
		{
			@Override
			public boolean isItemValid(ItemStack itemStack)
			{
				return tile.isStackValid(0, itemStack);
			}
		});

		outputSlot = this.addSlotToContainer(new Slot(this.inv, 1, 8, 60)
		{
			@Override
			public boolean isItemValid(ItemStack itemStack)
			{
				return tile.isStackValid(1, itemStack);
			}
		});

		addPlayerInventory(player.inventory, 8, 141);

	}
}
