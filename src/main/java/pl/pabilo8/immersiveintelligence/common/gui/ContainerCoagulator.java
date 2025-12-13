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

	public Slot slotInput, slotOutput;
	public Slot slotBucketIn1, slotBucketOut1;
	public Slot slotBucketIn2, slotBucketOut2;

	public ContainerCoagulator(EntityPlayer player, TileEntityCoagulator tile)
	{
		super(player, tile);
		{

			//Recipe item input (empty pages) slot
			this.slotInput = addSlotToContainer(new Slot(this.inv, 0, 13-4+6, 39-8)
			{
				@Override
				public boolean isItemValid(@Nonnull ItemStack stack)
				{
					return PrintingRecipe.streamRecipes(PrintingRecipe.class)
							.anyMatch(recipe -> recipe.getInput().matchesItemStackIgnoringSize(stack));
				}
			});

			//Output slot
			this.slotOutput = addSlotToContainer(new IESlot.Output(this, this.inv, 1, 88+8-6, 39-8));

			//Fluid Container Slots
			this.slotBucketIn1 = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 147, 21-8, 0));
			this.slotBucketOut1 = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 147, 57-8, 0));

			this.slotBucketIn2 = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 147, 21-8-10, 0));
			this.slotBucketOut2 = addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 147, 57-8-10, 0));

			addPlayerInventory(player.inventory, 8, 86);
		}
	}
}
