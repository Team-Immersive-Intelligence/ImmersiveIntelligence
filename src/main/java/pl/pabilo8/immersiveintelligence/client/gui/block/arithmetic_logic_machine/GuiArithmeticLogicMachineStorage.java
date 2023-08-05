package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiArithmeticLogicMachineStorage extends GuiArithmeticLogicMachineBase
{
	public GuiArithmeticLogicMachineStorage(EntityPlayer player, TileEntityArithmeticLogicMachine tile)
	{
		super(player, tile, IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE);
	}
}
