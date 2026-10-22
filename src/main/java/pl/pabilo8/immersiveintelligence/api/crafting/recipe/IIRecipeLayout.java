package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe layout with components
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.10.2026
 */
public class IIRecipeLayout
{
	private final List<LayoutComponent> components, bottomBarComponents;
	private final int gridWidth;
	private final int gridHeight;
	private final boolean earlyGame;

	public IIRecipeLayout(List<LayoutComponent> components, List<LayoutComponent> bottomBarComponents, int gridWidth, int gridHeight, boolean earlyGame)
	{
		this.components = components;
		this.bottomBarComponents = bottomBarComponents;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.earlyGame = earlyGame;
	}

	public List<LayoutComponent> getComponents()
	{
		return components;
	}

	public List<LayoutComponent> getBottomBarComponents()
	{
		return bottomBarComponents;
	}

	public int getGridWidth()
	{
		return gridWidth;
	}

	public int getGridHeight()
	{
		return gridHeight;
	}

	public boolean isEarlyGame()
	{
		return earlyGame;
	}

	/**
	 * Copies slot alternatives, applies their counts, and expands wildcard metadata.
	 */
	@SideOnly(Side.CLIENT)
	public static List<ItemStack> getDisplayItems(Object data)
	{
		List<ItemStack> results = new ArrayList<>();
		if(data==null)
			return results;
		IngredientStack ingredient = data instanceof List?null: ApiUtils.createIngredientStack(data);
		Iterable<?> alternatives = ingredient==null?(List<?>)data: ingredient.getStackList();
		for(Object alternative : alternatives)
		{
			if(!(alternative instanceof ItemStack)||((ItemStack)alternative).isEmpty())
				continue;
			ItemStack source = (ItemStack)alternative;
			NonNullList<ItemStack> variants = NonNullList.create();
			if(source.getMetadata()==OreDictionary.WILDCARD_VALUE)
				source.getItem().getSubItems(CreativeTabs.SEARCH, variants);
			else
				variants.add(source);
			if(variants.isEmpty())
			{
				ItemStack fallback = source.copy();
				fallback.setItemDamage(0);
				variants.add(fallback);
			}
			for(ItemStack variant : variants)
			{
				ItemStack display = variant.copy();
				if(source.hasTagCompound())
					display.setTagCompound(source.getTagCompound().copy());
				display.setCount(ingredient==null?source.getCount(): ingredient.inputSize);
				results.add(display);
			}
		}
		return results;
	}

	@SideOnly(Side.CLIENT)
	public static ItemStack getDisplayedItem(Object data, long time)
	{
		List<ItemStack> stacks = getDisplayItems(data);
		return stacks.isEmpty()?ItemStack.EMPTY: stacks.get((int)((time/20)%stacks.size()));
	}

	public static String[] getNoteParts(LayoutComponent component)
	{
		return String.valueOf(component.getData()).split("\\|", -1);
	}

	public enum ComponentType
	{
		SLOT,
		FLUID_TANK,
		DUST_TANK,
		INFO_DISPLAY,
		MULTIBLOCK_MODEL
	}

	public enum IOType
	{
		INPUT,
		OUTPUT,
		NEUTRAL
	}
}
