package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.utils.IPrecissionTool;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityPrecissionAssembler;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIAssemblyScheme;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerPrecissionAssembler extends ContainerIEBase<TileEntityPrecissionAssembler>
{
	public ContainerPrecissionAssembler(InventoryPlayer inventoryPlayer, TileEntityPrecissionAssembler tile)
	{
		//Normal bullet slots

		super(inventoryPlayer, tile);

		for(int i = 0; i < 3; i++)
			this.addSlotToContainer(new Slot(this.inv, i, 60+(i*18), 57)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof IPrecissionTool;
				}
			});

		this.addSlotToContainer(new Slot(this.inv, 3, 80, 24)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemIIAssemblyScheme;
			}
		});

		this.addSlotToContainer(new Slot(this.inv, 4, 30, 39));

		this.addSlotToContainer(new Slot(this.inv, 5, 9, 19));
		this.addSlotToContainer(new Slot(this.inv, 6, 9, 39));
		this.addSlotToContainer(new Slot(this.inv, 7, 9, 59));

		this.addSlotToContainer(new Output(this, this.inv, 8, 137, 39));
		this.addSlotToContainer(new Output(this, this.inv, 9, 137, 59));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 144));

	}
}
