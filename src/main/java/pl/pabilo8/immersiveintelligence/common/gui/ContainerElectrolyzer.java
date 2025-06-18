package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.06.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
public class ContainerElectrolyzer extends ContainerIIBase<TileEntityElectrolyzer>
{
	public Slot[] slotsInput, slotsOutput;

	public ContainerElectrolyzer(EntityPlayer player, TileEntityElectrolyzer tile)
	{
		super(player, tile);
		this.slotsInput = new Slot[3];
		this.slotsOutput = new Slot[3];

		this.slotsInput[0] = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 10, 14, 0));
		this.slotsOutput[0] = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 1, 10, 52, 0));
		this.slotsInput[1] = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 61, 14, 0));
		this.slotsOutput[1] = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 61, 52-2, 0));
		this.slotsInput[2] = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 4, 140+4, 14, 0));
		this.slotsOutput[2] = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 5, 140+4, 52-2, 0));

		this.addPlayerInventory(player.inventory, 8, 86);
	}
}
