package pl.pabilo8.immersiveintelligence.common.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * Refills a used parachute pack while retaining its settings and custom name.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.11.2026
 */
public class RecipeParachuteRefill extends ShapedOreRecipe
{
	public RecipeParachuteRefill()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "parachute_refill"), new ItemStack(IIContent.itemParachutePack),
				"FFF", "FBF", "FFF", 'F', "fabricHemp", 'B', IIContent.itemParachutePack);
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		return inventory.getWidth()==3&&inventory.getHeight()==3&&super.matches(inventory, world)
				&&IIContent.itemParachutePack.isUsed(inventory.getStackInRowAndColumn(1, 1));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory)
	{
		if(!matches(inventory, null))
			return ItemStack.EMPTY;
		ItemStack result = inventory.getStackInRowAndColumn(1, 1).copy();
		result.setCount(1);
		IIContent.itemParachutePack.setUsed(result, false);
		return result;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}
}
