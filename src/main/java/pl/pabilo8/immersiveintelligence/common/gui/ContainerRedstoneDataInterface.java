package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataInputMachine.FilteredDataInput;

/**
 * @author Pabilo8
 * @since 30-06-2019
 */
public class ContainerRedstoneDataInterface extends ContainerIEBase<TileEntityRedstoneInterface>
{
	public ContainerRedstoneDataInterface(EntityPlayer player, TileEntityRedstoneInterface tile)
	{
		super(player.inventory, tile);

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		this.addSlotToContainer(new FilteredDataInput(this, this.inv, 0, 5, 21));
		this.addSlotToContainer(new Output(this, this.inv, 1, 5, 100));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 199));
	}

}
