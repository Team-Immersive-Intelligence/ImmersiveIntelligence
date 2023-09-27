package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerPrecissionAssembler extends ContainerIEBase<TileEntityPrecisionAssembler>
{
	public ContainerPrecissionAssembler(EntityPlayer player, TileEntityPrecisionAssembler tile)
	{
		super(player.inventory, tile);

		//tool slots
		for(int i = 0; i < 3; i++)
			this.addSlotToContainer(new Slot(this.inv, i, 62+(i*18), 59)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof IPrecisionTool;
				}
			});

		//scheme slot
		this.addSlotToContainer(new Slot(this.inv, 3, 80, 24)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemIIAssemblyScheme;
			}
		});

		//ingredient slots
		this.addSlotToContainer(new Slot(this.inv, 4, 30, 39));

		this.addSlotToContainer(new Slot(this.inv, 5, 9, 19));
		this.addSlotToContainer(new Slot(this.inv, 6, 9, 39));
		this.addSlotToContainer(new Slot(this.inv, 7, 9, 59));

		//output slots
		this.addSlotToContainer(new Output(this, this.inv, 8, 137, 39));
		this.addSlotToContainer(new Output(this, this.inv, 9, 137, 59));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 144));

	}
}
