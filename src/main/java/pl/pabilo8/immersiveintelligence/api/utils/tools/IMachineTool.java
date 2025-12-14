package pl.pabilo8.immersiveintelligence.api.utils.tools;


import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 11.12.2025
 */
public interface IMachineTool
{
	String getToolID(ItemStack stack);

	void damageTool(ItemStack stack, int amount);

	int getToolDamage(ItemStack stack);

	int getToolMaxDamage(ItemStack stack);
}
