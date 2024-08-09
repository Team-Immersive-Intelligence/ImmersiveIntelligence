package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IWrench;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

public class IIItemUtil
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
}
