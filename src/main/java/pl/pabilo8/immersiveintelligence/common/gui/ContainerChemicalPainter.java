package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
public class ContainerChemicalPainter extends ContainerIIBase<TileEntityChemicalPainter>
{
	public Slot[] inputSlot, outputSlot;

	public ContainerChemicalPainter(EntityPlayer player, TileEntityChemicalPainter tile)
	{
		super(player, tile);

			this.inputSlot = new Slot[2];
			this.outputSlot = new Slot[2];

			//Item Slots
			inputSlot[0] = this.addSlotToContainer(new Slot(this.inv, 0, 13, 13));
			outputSlot[0] = this.addSlotToContainer(new Slot(this.inv, 1, 13, 59));

			//Fluid Slots
			inputSlot[1] = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 137, 21, 0));
			outputSlot[1] = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 137, 57, 0));

			//Invin Slots
			addPlayerInventory(player.inventory, 8 ,141);
	}
}
