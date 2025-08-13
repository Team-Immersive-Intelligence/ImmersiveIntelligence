package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.PrintingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.12.2025
 */
public class ContainerVulcanizer extends ContainerIIBase<TileEntityVulcanizer>
{
	public Slot slotInput, slotOutput;

	public ContainerVulcanizer(EntityPlayer player, TileEntityVulcanizer tile)
	{
		super(player, tile);
		//Input/Output Slots


		slotInput = this.addSlotToContainer(new Slot(this.inv, 0, 6, 19)
		{
			@Override
			public boolean isItemValid(@Nonnull ItemStack stack) {
				return VulcanizerRecipe.recipeList.values().stream()
						.anyMatch(recipe ->
								recipe.input.matches(stack) ||
										recipe.compoundInput.matches(stack) ||
										recipe.sulfurInput.matches(stack)
						);
			}
		});
		slotInput = this.addSlotToContainer(new Slot(this.inv, 1, 6, 39)
		{
			@Override
			public boolean isItemValid(@Nonnull ItemStack stack) {
				return VulcanizerRecipe.recipeList.values().stream()
						.anyMatch(recipe ->
								recipe.input.matches(stack) ||
										recipe.compoundInput.matches(stack) ||
										recipe.sulfurInput.matches(stack)
						);
			}
		});
		slotInput = this.addSlotToContainer(new Slot(this.inv, 2, 6, 59)
		{
			@Override
			public boolean isItemValid(@Nonnull ItemStack stack) {
				return VulcanizerRecipe.recipeList.values().stream()
						.anyMatch(recipe ->
								recipe.input.matches(stack) ||
										recipe.compoundInput.matches(stack) ||
										recipe.sulfurInput.matches(stack)
						);
			}
		});

		addPlayerInventory(player.inventory, 8, 141);
	}
}
