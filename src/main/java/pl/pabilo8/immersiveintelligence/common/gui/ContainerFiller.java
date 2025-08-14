package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.12.2025
 */
public class ContainerFiller extends ContainerIIBase<TileEntityFiller>
{
	public Slot inputSlot;

	public ContainerFiller(EntityPlayer player, TileEntityFiller tile)
	{
		super(player, tile);
		//Dust Input slot
		inputSlot = this.addSlotToContainer(new Slot(this.inv, 0, 120, 5));

		//player inventory
		addPlayerInventory(player.inventory, 8, 86);

	}
}
