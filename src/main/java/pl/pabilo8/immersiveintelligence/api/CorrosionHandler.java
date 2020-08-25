package pl.pabilo8.immersiveintelligence.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
public class CorrosionHandler
{

	static List<ItemStack> corrosionBlacklist = new ArrayList<>();

	public static boolean canCorrode(ItemStack stack)
	{
		return corrosionBlacklist.stream().noneMatch(stack1 -> ItemHandlerHelper.canItemStacksStack(stack, stack1));
	}

	public static void addItemToBlacklist(ItemStack stack)
	{
		corrosionBlacklist.add(stack);
	}
}
