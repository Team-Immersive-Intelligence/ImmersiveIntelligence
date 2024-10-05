package pl.pabilo8.immersiveintelligence.common.crafting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.registries.IForgeRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 23-04-2020
 */
@Optional.Interface(iface = "mezz.jei.api.recipe.IRecipeWrapper", modid = "jei")
public class RecipeCrateConversion extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, IRecipeWrapper
{
	private final ItemStack outputCrate;
	private final ItemStack inputCrate;

	public static ArrayList<RecipeCrateConversion> listAllRecipes = new ArrayList<>();

	public RecipeCrateConversion(ItemStack outputCrate, ItemStack inputCrate)
	{
		this.outputCrate = outputCrate;
		this.inputCrate = inputCrate;
		listAllRecipes.add(this);
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack crate = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty()&&crate.isEmpty()&&stackInSlot.isItemEqual(inputCrate))
			{
				crate = stackInSlot;
			}
			else if(!stackInSlot.isEmpty())
			{
				return false;
			}
		}
		return !crate.isEmpty();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack crate = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty()&&stackInSlot.isItemEqual(inputCrate))
			{
				crate = stackInSlot;
				break;
			}
		}

		if(!crate.isEmpty())
		{
			ItemStack output = outputCrate.copy();
			output.setTagCompound(crate.getTagCompound());
			return output;
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 1&&height >= 1;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return outputCrate.copy();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	/**
	 * Method to create recipes for cycling between crates.
	 */
	public static void createCrateConversionRecipes(IForgeRegistry<IRecipe> registry, String baseName, ItemStack... crates)
	{
		for(int i = 0; i < crates.length; i++)
		{
			int nextIndex = (i+1)%crates.length;
			registry.register(new RecipeCrateConversion(crates[nextIndex], crates[i])
					.setRegistryName(ImmersiveIntelligence.MODID, baseName+i));
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(VanillaTypes.ITEM, inputCrate);
		ingredients.setOutput(VanillaTypes.ITEM, outputCrate);
	}
}