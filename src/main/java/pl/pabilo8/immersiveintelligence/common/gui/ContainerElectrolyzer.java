package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
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
	public final Slot[] slotsInput, slotsOutput;

	public ContainerElectrolyzer(EntityPlayer player, TileEntityElectrolyzer tile)
	{
		super(player, tile);

		this.slotsInput = new Slot[]{
				addSlot(10, 14, 0, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(61, 14, 2, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(144, 14, 4, getFluidContainerSlot(SideConfig.INPUT))
		};
		this.slotsOutput = new Slot[]{
				addSlot(10, 52, 1, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(61, 50, 3, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(144, 50, 5, getFluidContainerSlot(SideConfig.OUTPUT))
		};

		this.addPlayerInventory(player.inventory, 8, 86);
	}
}
