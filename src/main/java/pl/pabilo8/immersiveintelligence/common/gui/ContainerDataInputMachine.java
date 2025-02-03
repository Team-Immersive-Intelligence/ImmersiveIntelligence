package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase.FilteredDataInput;

/**
 * @author Pabilo8
 * @since 30-06-2019
 */
public class ContainerDataInputMachine extends ContainerIEBase<TileEntityDataInputMachine>
{
	public ContainerDataInputMachine(EntityPlayer player, TileEntityDataInputMachine tile, boolean storage)
	{
		super(player.inventory, tile);

		this.addSlotToContainer(new FilteredDataInput(this, this.inv, 0, 5, 21));
		this.addSlotToContainer(new Output(this, this.inv, 1, 5, 100));

		if(storage)
			for(int i = 2; i < 26; i++)
				this.addSlotToContainer(new FilteredDataInput(this, this.inv, i, 30+((i-2)%8)*18, 28+((i-2)/8)*18));

		this.slotCount = storage?tile.getInventory().size(): 2;
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 199));
	}

}
