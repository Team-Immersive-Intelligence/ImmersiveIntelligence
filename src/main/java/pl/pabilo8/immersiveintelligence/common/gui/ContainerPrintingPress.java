package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.PrintingRecipe;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.06.2025
 * @since 10.07.2019
 */
public class ContainerPrintingPress extends ContainerIIBase<TileEntityPrintingPress>
{
	public Slot slotInput, slotOutput;
	public Slot slotBucketIn, slotBucketOut;

	public ContainerPrintingPress(EntityPlayer player, TileEntityPrintingPress tile)
	{
		super(player, tile);

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
		this.slotBucketIn = addSlot(147, 21-8, 2, getFluidContainerSlot(SideConfig.INPUT));
		this.slotBucketOut = addSlot(147, 57-8, 3, getFluidContainerSlot(SideConfig.INPUT));

		addPlayerInventory(player.inventory, 8, 86);
	}
}
