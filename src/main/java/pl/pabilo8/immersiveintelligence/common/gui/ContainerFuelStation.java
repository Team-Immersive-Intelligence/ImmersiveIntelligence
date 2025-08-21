package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
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
		inputFluidSlot = addSlot(39, 14-4, 0, getFluidContainerSlot(SideConfig.INPUT));
		outputFluidSlot = addSlot(39, 42-4+1, 1, getFluidContainerSlot(SideConfig.OUTPUT));

		//Player Inventory
		addPlayerInventory(player.inventory, 8, 87);
	}
}
