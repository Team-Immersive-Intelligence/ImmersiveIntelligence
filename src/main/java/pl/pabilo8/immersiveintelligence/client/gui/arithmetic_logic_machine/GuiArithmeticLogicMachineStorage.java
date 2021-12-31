package pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine;

import net.minecraft.entity.player.InventoryPlayer;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiArithmeticLogicMachineStorage extends GuiArithmeticLogicMachineBase
{
	public GuiArithmeticLogicMachineStorage(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile)
	{
		super(inventoryPlayer, tile, IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE);
	}
}
