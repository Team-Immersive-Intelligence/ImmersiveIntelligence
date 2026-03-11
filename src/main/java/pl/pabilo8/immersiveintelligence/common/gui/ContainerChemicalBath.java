package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockChemicalBath;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 11.12.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
public class ContainerChemicalBath extends ContainerIIBase<TileEntityChemicalBath>
{
	public final Slot slotInput, slotOutput;
	public final Slot slotBucketInput, slotBucketOutput;

	public ContainerChemicalBath(EntityPlayer player, TileEntityChemicalBath tile)
	{
		super(player, tile);
		//Input/Output Slots
		slotInput = addSlot(10, 39-10, MultiblockChemicalBath.ITEM_IN);
		slotOutput = addSlot(140, 39-10, MultiblockChemicalBath.ITEM_OUT, IESlot.Output::new);

		//Fluid Container Slots
		slotBucketInput = addSlot(44, 28-10, MultiblockChemicalBath.BUCKET_IN, getFluidContainerSlot(SideConfig.NONE));
		slotBucketOutput = addSlot(106, 28-10, MultiblockChemicalBath.BUCKET_OUT, getFluidContainerSlot(SideConfig.NONE));

		addPlayerInventory(player.inventory, 8, 86);
	}
}
