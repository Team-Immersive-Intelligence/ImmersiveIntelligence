package pl.pabilo8.immersiveintelligence.api.api.protection;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry and capability query for item corrosion.
 *
 * @since 24.05.2019
 */
public final class CorrosionHandler
{
	private static final List<ItemStack> CORROSION_BLACKLIST = new ArrayList<>();

	private CorrosionHandler()
	{
	}

	public static boolean canCorrode(ItemStack stack)
	{
		if(stack.isEmpty()||ProtectionHandler.isProtectedFromCorrosion(stack))
			return false;
		return CORROSION_BLACKLIST.stream().noneMatch(blacklisted ->
				ItemHandlerHelper.canItemStacksStack(stack, blacklisted));
	}

	public static void addItemToBlacklist(ItemStack stack)
	{
		if(!stack.isEmpty())
			CORROSION_BLACKLIST.add(stack.copy());
	}

	public static List<ItemStack> getCorrosionBlacklist()
	{
		return Collections.unmodifiableList(CORROSION_BLACKLIST);
	}
}
