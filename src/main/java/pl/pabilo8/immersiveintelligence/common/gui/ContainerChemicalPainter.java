package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
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

		inputSlot = this.addSlotToContainer(new Slot(this.inv, 0, 13, 13));
		outputSlot = this.addSlotToContainer(new Slot(this.inv, 1, 13, 59));

		inputFluidSlot = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 137, 21,0));
		outputFluidSlot = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 137, 57, 0));

		addPlayerInventory(player.inventory, 8, 141);
	}
}
