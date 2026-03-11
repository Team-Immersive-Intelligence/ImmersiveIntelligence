package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 20.08.2025
 * @updated 28.01.2026
 * @ii-approved 0.3.1
 * @since 17.05.2019
 */
public class ContainerPacker extends ContainerIIBase<TileEntityPacker>
{
	//Used by fluid and energy containers
	public Slot[] slotsInput, slotsOutput;

	public ContainerPacker(EntityPlayer player, TileEntityPacker tile)
	{
		super(player, tile);
		this.slotsInput = this.slotsOutput = new Slot[0];

		//Add packer slots depending on mode
		if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
		{
			this.slotsInput = new Slot[]{
					addSlot(147+128+8-1+12, 21-8, 1, getFluidContainerSlot(SideConfig.INPUT)),
					addSlot(147+128+8+16+4-1+16+32, 21-8, 2, getFluidContainerSlot(SideConfig.INPUT))
			};
			this.slotsOutput = new Slot[]{
					addSlot(147+128+8-1+12, 57+96-32-8+2, 3, getFluidContainerSlot(SideConfig.OUTPUT)),
					addSlot(147+128+8+16+4-1+16+32, 57+96-32-8+2, 4, getFluidContainerSlot(SideConfig.OUTPUT))
			};
		}
		else if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
		{
			this.slotsInput = new Slot[]{
					addSlot(147+128+8-1+12-8-4, 21-8, 1, getFluidContainerSlot(SideConfig.INPUT))
			};
			this.slotsOutput = new Slot[]{
					addSlot(147+128+8-1+12-8-4, 57+96-32-8+2+2, 3, getFluidContainerSlot(SideConfig.OUTPUT))
			};
		}
		else
		{
			int total = tile.getInventory().size()-1;
			this.slotsInput = addVirtualSlots(1, total/2, CrateSlot::new);
			this.slotsOutput = addVirtualSlots(1+total/2, total/2, CrateSlot::new);
		}
		//Add player inventory
		this.addPlayerInventory(player.inventory, 8+64+32-16+16+8-2, 86+32+12+32+12);
	}
}
