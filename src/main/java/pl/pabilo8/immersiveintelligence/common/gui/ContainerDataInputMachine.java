package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.02.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
public class ContainerDataInputMachine extends ContainerIIBase<TileEntityDataInputMachine>
{
	public Slot dataInput, dataOutput;
	public Slot[] punchtapeStorage;
	public boolean hasStorage;

	public ContainerDataInputMachine(EntityPlayer player, TileEntityDataInputMachine tile, boolean hasStorage)
	{
		super(player, tile);
		this.hasStorage = hasStorage;

		dataInput = this.addSlotToContainer(new FilteredDataInput(this, this.inv, 0, 8, 21));
		dataOutput = this.addSlotToContainer(new Output(this, this.inv, 1, 8, 100));

		if(hasStorage)
			punchtapeStorage = addSlotArray(32+2+1, 11+11, 2, 24, 6, FilteredDataInput::new);

		addPlayerInventory(player.inventory, 8, 141+8);
	}
}
