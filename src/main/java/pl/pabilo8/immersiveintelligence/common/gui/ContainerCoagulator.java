package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCoagulator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 12.12.2025
 */
public class ContainerCoagulator extends ContainerIIBase<TileEntityCoagulator>
{
	public Slot[] slotBucketIn, slotBucketOut;

	public ContainerCoagulator(EntityPlayer player, TileEntityCoagulator tile)
	{
		super(player, tile);

		//Fluid Container Slots
		this.slotBucketIn = new Slot[]{
				addSlot(176-32-12-64-16, 21-8, MultiblockCoagulator.SLOT_INPUT1, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(176-32-12, 21-8, MultiblockCoagulator.SLOT_INPUT2, getFluidContainerSlot(SideConfig.INPUT))
		};
		this.slotBucketOut = new Slot[]{
				addSlot(176-32-12-64-16, 57-8, MultiblockCoagulator.SLOT_OUTPUT1, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(176-32-12, 57-8, MultiblockCoagulator.SLOT_OUTPUT2, getFluidContainerSlot(SideConfig.OUTPUT))
		};
		addPlayerInventory(player.inventory, 8, 86+24);
	}
}
