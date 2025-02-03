package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8
 * @since 30-06-2019
 */
public class ContainerRedstoneDataInterface extends ContainerIIBase<TileEntityRedstoneInterface>
{
	public Slot dataInput, dataOutput;

	public ContainerRedstoneDataInterface(EntityPlayer player, TileEntityRedstoneInterface tile)
	{
		super(player, tile);

		dataInput = this.addSlotToContainer(new FilteredDataInput(this, this.inv, 0, 8, 21));
		dataOutput = this.addSlotToContainer(new Output(this, this.inv, 1, 8, 100));

		addPlayerInventory(player.inventory, 8, 141+8);
	}
}
