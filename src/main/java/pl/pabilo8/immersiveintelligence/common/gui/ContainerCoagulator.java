package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.PrintingRecipe;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 12.12.2025
 */
public class ContainerCoagulator extends ContainerIIBase<TileEntityCoagulator>
{

	public Slot slotOutput;
	public Slot slotBucketIn, slotBucketOut;
;

	//no Input slot, liquid latex is the crafting recipe input

	public ContainerCoagulator(EntityPlayer player, TileEntityCoagulator tile)
	{
		super(player, tile);
		{

			//Recipe item input (empty pages) slot
			this.slotOutput = addSlotToContainer(new Slot(this.inv, 0, 13+80+20-20, 39-8)
			{
				@Override
				public boolean isItemValid(@Nonnull ItemStack stack)
				{
					return PrintingRecipe.streamRecipes(PrintingRecipe.class)
							.anyMatch(recipe -> recipe.getInput().matchesItemStackIgnoringSize(stack));
				}
			});

			//Fluid Container Slots
			this.slotBucketIn = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 147-20-40+3-20, 21-8, 0));
			this.slotBucketOut = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 147-20-40+3-20, 57-8, 0));

			addPlayerInventory(player.inventory, 8, 86);
		}
	}
}
