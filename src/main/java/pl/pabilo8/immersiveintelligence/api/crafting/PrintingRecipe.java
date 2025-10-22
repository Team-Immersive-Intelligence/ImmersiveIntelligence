package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import javax.annotation.Nullable;

/**
 * Data of a singular print order
 */
public class PrintingRecipe extends IIMultiblockRecipe
{
	private final IngredientStack input;
	private final String categoryName;
	private final PrintFunction function;

	/**
	 * @param input        the recipe input item
	 * @param categoryName a category name, in addition to the
	 * @param function     a {@link PrintFunction} determining the ink required and the recipe's outcome
	 */
	public PrintingRecipe(IngredientStack input, String categoryName, PrintFunction function)
	{
		super(categoryName, input);
		this.setTimeAndEnergy(PrintingPress.printTime, PrintingPress.energyUsage);
		this.input = input;
		this.categoryName = categoryName;
		this.function = function;
	}

	public IngredientStack getInput()
	{
		return input;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public PrintFunction getFunction()
	{
		return function;
	}

	/**
	 * Handles the printing process ink cost math and applies the recipe to the input item stack.
	 */
	public interface PrintFunction
	{
		/**
		 * Applies the printing recipe to the input item stack, returning the resulting item stack.
		 *
		 * @param input the input item stack
		 * @param data  determines what is printed on the recipe
		 * @return the resulting item stack after applying the recipe
		 */
		ItemStack apply(ItemStack input, DataPacket data);

		/**
		 * @return an array of cyan, magenta, yellow and black (in this order) ink types required for this recipe
		 */
		int[] getInkTypesRequired(DataPacket data);

		@Nullable
		default Upgrade getUpgradeRequired()
		{
			return null;
		}
	}
}
