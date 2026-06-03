package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.02.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
public class ContainerDataInputMachineEditing extends ContainerIITileBase<TileEntityDataInputMachine>
{
	public Slot dataInput, dataOutput;

	public ContainerDataInputMachineEditing(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		super(player, tile);

		dataInput = addSlot(8, 21, 0, FilteredDataInput::new);
		dataOutput = addSlot(8, 100, 1, Output::new);


		addPlayerInventory(player.inventory, 40, 141+8+32);
	}
}
