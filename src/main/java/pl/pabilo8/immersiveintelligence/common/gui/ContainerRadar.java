package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerRadar extends ContainerIEBase<TileEntityRadar>
{
	public ContainerRadar(EntityPlayer player, TileEntityRadar tile)
	{
		super(player.inventory, tile);

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 33+j*18, 173+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 33+i*18, 216+15));
	}
}
