package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine.CircuitSlot;

/**
 * @author Pabilo9
 * @since 30-06-2019
 * * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 * @author Avalon
 * @since 18-12-2024
 */
public class GuiArithmeticLogicMachineStorage extends GuiArithmeticLogicMachineBase
{
	public GuiArithmeticLogicMachineStorage(EntityPlayer player, TileEntityArithmeticLogicMachine tile)
	{
		super(player, tile, IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE);
	}

	@Override
	public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		if (slotIn instanceof CircuitSlot) {
			refreshStoredData();  // Ensure stored data is reloaded
		}
	}
}
