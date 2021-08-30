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
		if(stack.getItem() instanceof ICorrosionProtectionEquipment)
			return ((ICorrosionProtectionEquipment)stack.getItem()).canCorrode(stack);
		return corrosionBlacklist.stream().noneMatch(stack1 -> ItemHandlerHelper.canItemStacksStack(stack, stack1));
	}

	public static void addItemToBlacklist(ItemStack stack)
	{
		corrosionBlacklist.add(stack);
	}

	public interface ICorrosionProtectionEquipment
	{
		boolean canCorrode(ItemStack stack);
	}

	public interface IAcidProtectionEquipment
	{
		boolean protectsFromAcid(ItemStack stack);
	}
}
