package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.14.2025
 */
public class ContainerFuelStation extends ContainerIIBase<TileEntityFuelStation>
{

	public Slot inputFluidSlot, outputFluidSlot;

	public ContainerFuelStation(EntityPlayer player, TileEntityFuelStation tile)
	{
		super(player, tile);

		//Fluid Container Slots
		inputFluidSlot = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 39, 14, 0));
		outputFluidSlot = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 39, 42, 0));

		addPlayerInventory(player.inventory, 8, 141);
	}
}
