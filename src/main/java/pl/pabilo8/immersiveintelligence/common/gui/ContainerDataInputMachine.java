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
public class ContainerDataInputMachine extends ContainerIITileBase<TileEntityDataInputMachine>
{
	public final Slot dataInput, dataOutput;
	public final Slot[] punchtapeStorage;
	public final boolean hasStorage;

	public ContainerDataInputMachine(EntityPlayer player, TileEntityDataInputMachine tile, boolean hasStorage)
	{
		super(player, tile);
		this.hasStorage = hasStorage;

		dataInput = addSlot(8, 21, 0, FilteredDataInput::new);
		dataOutput = addSlot(8, 100, 1, Output::new);

		punchtapeStorage = hasStorage?addSlotArray(35, 22, 2, 24, 6, FilteredDataInput::new): null;

		addPlayerInventory(player.inventory, 8, 149);
	}
}
