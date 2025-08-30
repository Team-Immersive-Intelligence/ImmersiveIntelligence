package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@itteam.net)
 * @updated 30.08.2025
 * @since 17.05.2019
 */
public class ContainerPrecisionAssembler extends ContainerIIBase<TileEntityPrecisionAssembler>
{
	public Slot ingredientSlot, outputSlot, toolSlot,  schemeSlot;

	public ContainerPrecisionAssembler(EntityPlayer player, TileEntityPrecisionAssembler tile)
	{
		super(player, tile);

		//tool slots
		for(int i = 0; i < 3; i++)
			this.toolSlot = addSlotToContainer(new Slot(this.inv, i, 62+(i*18), 59)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof IPrecisionTool;
				}
			});

		//scheme slot
		this.schemeSlot = addSlotToContainer(new Slot(this.inv, 3, 80, 24)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemIIAssemblyScheme;
			}
		});

		//ingredient slots
		this.ingredientSlot = addSlotToContainer(new Slot(this.inv, 4, 30, 39));

		this.ingredientSlot = addSlotToContainer(new Slot(this.inv, 5, 9, 19));
		this.ingredientSlot = addSlotToContainer(new Slot(this.inv, 6, 9, 39));
		this.ingredientSlot = addSlotToContainer(new Slot(this.inv, 7, 9, 59));

		//output slots
		this.outputSlot = addSlotToContainer(new Output(this, this.inv, 8, 137, 39));
		this.outputSlot = addSlotToContainer(new Output(this, this.inv, 9, 137, 59));

		addPlayerInventory(player.inventory, 8, 86);

	}
}
