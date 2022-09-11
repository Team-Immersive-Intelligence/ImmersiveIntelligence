package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * Use this class on items without sub-items (meta)
 *
 * @Author Pabilo8
 * @Since 2019-05-07
 */
public class ItemIIBase extends Item implements IColouredItem
{
	public final String itemName;

	public ItemIIBase(String name, int stackSize)
	{
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);
		//set common parameters
		this.setHasSubtypes(false);
		this.setMaxStackSize(stackSize);
		this.itemName = name;

		//add item to list
		IIContent.ITEMS.add(this);
	}

	//--- Crafting Utilities ---//

	public ItemStack getStack(int amount)
	{
		return new ItemStack(this, 0, amount);
	}

	public IngredientStack getIngredientStack(int amount)
	{
		return new IngredientStack(getStack(amount));
	}
}