package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 23-04-2020
 */
@Optional.Interface(iface = "mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper", modid = "jei")
public class RecipeMinecart extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, IShapedCraftingRecipeWrapper
{
	private final ItemStack outputMinecart;
	private final ItemStack inputBlock;
	private final ArrayList<ItemStack> inputs;

	public static ArrayList<RecipeMinecart> listAllRecipes = new ArrayList<>();

	public RecipeMinecart(ItemStack outputMinecart, ItemStack inputBlock)
	{
		this.outputMinecart = outputMinecart;
		this.inputBlock = inputBlock;

		//JEI
		this.inputs = new ArrayList<>();
		this.inputs.add(inputBlock.copy());
		this.inputs.add(new ItemStack(Items.MINECART));

		listAllRecipes.add(this);
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack minecart = ItemStack.EMPTY;
		ItemStack crate = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(minecart.isEmpty()&&stackInSlot.getItem()==Items.MINECART)
				{
					minecart = stackInSlot;
				}
				else if(crate.isEmpty()&&stackInSlot.isItemEqual(inputBlock))
				{
					crate = stackInSlot;
				}
				else
					return false;
		}
		return !minecart.isEmpty()&&!crate.isEmpty();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack crate = ItemStack.EMPTY;
		ItemStack minecart = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(minecart.isEmpty()&&stackInSlot.getItem()==Items.MINECART)
				{
					minecart = stackInSlot;
				}
				else if(crate.isEmpty()&&stackInSlot.isItemEqual(inputBlock))
				{
					crate = stackInSlot;
				}
		}

		if(!minecart.isEmpty()&&!crate.isEmpty())
		{
			ItemStack output = outputMinecart.copy();
			output.setTagCompound(ItemNBTHelper.getTag(crate).copy());
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
		return width >= 2&&height >= 2;
	}

	/**
	 * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
	 * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
	 */
	@Override
	public ItemStack getRecipeOutput()
	{
		return outputMinecart.copy();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	@Override
	public int getWidth()
	{
		return 1;
	}

	@Override
	public int getHeight()
	{
		return 2;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(VanillaTypes.ITEM, this.inputs);
		ingredients.setOutput(VanillaTypes.ITEM, outputMinecart);
	}

}