package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.06.2019
 */
public class GuiArithmeticLogicMachineStorage extends GuiArithmeticLogicMachineBase
{
	public GuiArithmeticLogicMachineStorage(EntityPlayer player, TileEntityArithmeticLogicMachine tile)
	{
		super(player, tile, IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE);
	}
}
