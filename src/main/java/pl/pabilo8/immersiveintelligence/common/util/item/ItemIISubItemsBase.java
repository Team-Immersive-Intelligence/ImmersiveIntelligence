package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Use this class on items with sub-items (meta)
 *
 * @author Pabilo8
 * @since 01.09.2022
 */
public class ItemIISubItemsBase<E extends Enum<E> & IIItemEnum> extends ItemIIBase
{
	private E[] subItems;
	private String[] subNames;
	private int[] subMaxAmounts;
	private boolean[] isMetaHidden;

	public ItemIISubItemsBase(String name, int stackSize, @Nonnull E[] subItems)
	{
		super(name, stackSize);
		this.hasSubtypes = true;
		updateValues(subItems);
	}

	public final void updateValues(@Nonnull E[] subItems)
	{
		this.subItems = subItems;

		//get names or null
		this.subNames = Arrays.stream(subItems)
				.map(e -> (IIItemEnum)e)
				.map(IIItemEnum::getName)
				.toArray(String[]::new);

		//get if meta should be hidden in creative tabs / jei
		this.isMetaHidden = new boolean[this.subNames.length];

		for(E subItem : subItems)
			if(subItem.isHidden())
				this.isMetaHidden[subItem.getMeta()] = true;

		//get item amounts for sub items
		this.subMaxAmounts = ArrayUtils.toPrimitive(
				Arrays.stream(subItems)
						.map(e -> (IIItemEnum)e)
						.map(IIItemEnum::getStackSize)
						.map(i -> i==-1?maxStackSize: i) //if -1 was returned, it means the sub item uses the default stack size
						.toArray(Integer[]::new)
		);
	}

	@Override
	public Item setMaxStackSize(int maxStackSize)
	{
		super.setMaxStackSize(maxStackSize);
		if(subItems!=null)
			updateValues(subItems);
		return this;
	}

	//--- SubItem Names ---//

	public E[] getSubItems()
	{
		return subItems;
	}

	public String[] getSubNames()
	{
		return subNames;
	}

	/**
	 * Returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(tab))
		{
			if(subItems!=null)
			{
				for(int i = 0; i < subItems.length; i++)
					if(!isMetaHidden(i))
						list.add(new ItemStack(this, 1, i));
			}
			else
				list.add(new ItemStack(this));
		}
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different
	 * names based on their damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String subName = stack.getMetadata() < getSubNames().length?getSubNames()[stack.getMetadata()]: "";
		return this.getUnlocalizedName()+"."+subName;
	}

	//--- SubItem Data ---//

	public ItemIISubItemsBase<E> setMetaUnhidden(E... meta)
	{
		for(E subItem : meta)
			this.isMetaHidden[subItem.ordinal()] = false;
		return this;
	}

	public boolean isMetaHidden(int meta)
	{
		return this.isMetaHidden[Math.max(0, Math.min(meta, this.isMetaHidden.length-1))];
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return subMaxAmounts[MathHelper.clamp(stack.getMetadata(), 0, subItems.length-1)];
	}

	//--- SubItem Conversion ---//

	public E stackToSub(ItemStack stack)
	{
		return subItems[MathHelper.clamp(stack.getMetadata(), 0, subItems.length-1)];
	}

	public E nameToSub(String name)
	{
		for(int i = 0, subNamesLength = subNames.length; i < subNamesLength; i++)
			if(subNames[i].equals(name))
				return subItems[i];
		return subItems[0];
	}

	//--- Crafting Utilities ---//

	public ItemStack getStack(E subItem)
	{
		return new ItemStack(this, 1, subItem.ordinal());
	}

	public ComparableItemStack getComparableStack(E subItem)
	{
		return new ComparableItemStack(getStack(subItem));
	}

	public ItemStack getStack(E subItem, int amount)
	{
		return new ItemStack(this, amount, subItem.ordinal());
	}

	public IngredientStack getIngredientStack(E subItem, int amount)
	{
		return new IngredientStack(getStack(subItem, amount));
	}
}
