package pl.pabilo8.immersiveintelligence.api.utils.tools;


import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;

import static pl.pabilo8.immersiveintelligence.common.util.IIReference.NBT_DAMAGE;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 11.12.2025
 */
public interface IMachineTool
{
	String getToolID(ItemStack stack);

	default void damageTool(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_DAMAGE))
			ItemNBTHelper.setInt(stack, NBT_DAMAGE, getToolMaxDamage(stack));

		ItemNBTHelper.setInt(stack, NBT_DAMAGE, getToolDamage(stack)-amount);

		if(getToolDamage(stack) < 0)
			stack.setCount(0);
	}

	default int getToolDamage(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_DAMAGE))
			return getToolMaxDamage(stack);
		return ItemNBTHelper.getInt(stack, NBT_DAMAGE);
	}

	int getToolMaxDamage(ItemStack stack);

}
