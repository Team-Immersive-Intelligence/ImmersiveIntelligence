package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.13.2025
 */
public class ContainerChemicalPainter extends ContainerIIBase<TileEntityChemicalPainter>
{

	public Slot inputSlot, outputSlot, inputFluidSlot, outputFluidSlot;

	public ContainerChemicalPainter(EntityPlayer player, TileEntityChemicalPainter tile)
	{
		super(player, tile);

		inputSlot = addSlot(13, 13, 0, DefaultInputSlot::new);
		outputSlot = addSlot(13, 59, 1, IESlot.Output::new);

		inputFluidSlot = addSlot(137, 21, 2, getFluidContainerSlot(SideConfig.INPUT));
		outputFluidSlot = addSlot(137, 57, 3, getFluidContainerSlot(SideConfig.OUTPUT));

		addPlayerInventory(player.inventory, 8, 141);
	}
}
