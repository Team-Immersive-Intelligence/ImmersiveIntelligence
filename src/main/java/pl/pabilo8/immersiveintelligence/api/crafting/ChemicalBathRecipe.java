package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.10.2026
 * @ii-approved 0.3.1
 * @since 08.08.2019
 */
public class ChemicalBathRecipe extends IIMultiblockRecipe
{
	public final IngredientStack itemInput;
	public final FluidStack fluidInput;
	public final ItemStack itemOutput;
	public final boolean isWashing;

	public ChemicalBathRecipe(ItemStack itemOutput, Object itemInput, FluidStack fluidInput, int energy, int time, boolean isWashing)
	{
		super(isWashing?"washing": "bathing", itemOutput, fluidInput);
		setTimeAndEnergy(time, energy);

		this.itemOutput = itemOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.fluidInput = fluidInput;
		this.isWashing = isWashing;
		completeRegistration(isWashing?"washing": "bathing", this.itemInput, this.itemOutput, this.fluidInput);
	}

	public static ChemicalBathRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, FluidStack fluidInput, int energy, int time)
	{
		return new ChemicalBathRecipe(itemOutput, itemInput, fluidInput, energy, time, false);
	}

	public static boolean isValidFluid(FluidStack fluidStack)
	{
		return getRecipes(ChemicalBathRecipe.class).stream()
				.anyMatch(recipe -> recipe.fluidInput.isFluidEqual(fluidStack));
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(148, 64)
				.withSlot(2, 16+2, itemInput, IOType.INPUT, "frame")
				.withInputFluidTank(2+20, 3, fluidInput)
				.withSlot(128, 16+2, itemOutput, IOType.OUTPUT, "frame")
				.withTimeInfo()
				.withPowerInfo()
				.withMultiblockModel(32+8, -8)
				.build();
	}

	@Override
	public boolean matchesSubCategory(String subCategory)
	{
		return isWashing^(!subCategory.equals("ii.washing"));
	}
}
