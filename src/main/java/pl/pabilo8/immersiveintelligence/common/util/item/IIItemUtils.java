package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IWrench;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

public class IIItemUtils
{
	public static boolean isWrench(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_WRENCH)&&stack.getItem() instanceof IWrench;
	}

	public static boolean isTachometer(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_TACHOMETER);
	}

	public static boolean isCrowbar(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_CROWBAR);
	}

	public static boolean isVoltmeter(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return OreDictionary.itemMatches(new ItemStack(IEContent.itemTool, 1, 2), stack, true);
	}

	public static boolean isAdvancedHammer(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_ADVANCED_HAMMER);
	}

	public static NonNullList<ItemStack> trimInventory(NonNullList<ItemStack> inventory, int start, int end)
	{
		if(inventory.size() <= end)
			return inventory;

		NonNullList<ItemStack> trimmed = NonNullList.create();
		for(int i = start; i < end; i++)
		{
			if(i < inventory.size())
				trimmed.add(inventory.get(i));
			else
				trimmed.add(ItemStack.EMPTY);
		}
		return trimmed;
	}

	public static void fixupItem(Item item, String itemName)
	{
		// First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=item)
			throw new IllegalStateException("fixupItem was not called at the appropriate time");

		// Now, reconfigure the block to match our mod.
		item.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+itemName);
		item.setCreativeTab(IIContent.II_CREATIVE_TAB);

		// And add it to our registries.
		IIContent.ITEMS.add(item);
	}

	public static boolean canUpgradeFreeOfCharge(EntityPlayer player)
	{
		return player.isCreative()&&Tools.instantCreativeUpgrading;
	}

	public static boolean canConstructFreeOfCharge(EntityPlayer player)
	{
		return player.isCreative()&&Tools.instantCreativeConstruction;
	}
}
