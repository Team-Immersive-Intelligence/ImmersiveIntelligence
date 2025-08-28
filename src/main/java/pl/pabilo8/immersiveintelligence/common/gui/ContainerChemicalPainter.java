package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockChemicalPainter;
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

		inputFluidSlot = addSlot(5, 13+4, MultiblockChemicalPainter.SLOT_BUCKET_INPUT, getFluidContainerSlot(SideConfig.INPUT));
		outputFluidSlot = addSlot(5, 59-4, MultiblockChemicalPainter.SLOT_BUCKET_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT));

		inputSlot = addSlot(137+10, 13, MultiblockChemicalPainter.SLOT_INPUT, DefaultInputSlot::new);
		outputSlot = addSlot(137+10, 59, MultiblockChemicalPainter.SLOT_OUTPUT, IESlot.Output::new);

		addPlayerInventory(player.inventory, 8, 128+12);
	}
}
