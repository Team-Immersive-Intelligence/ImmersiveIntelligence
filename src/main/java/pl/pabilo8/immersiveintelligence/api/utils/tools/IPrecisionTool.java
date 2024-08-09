package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 19-08-2019
 */
public interface IPrecisionTool
{
	String getPrecisionToolType(ItemStack stack);

	void damagePrecisionTool(ItemStack stack, int amount);

	int getPrecisionToolDamage(ItemStack stack);

	int getPrecisionToolMaxDamage(ItemStack stack);

	int getWorkTime(String tool_name);

	@Nonnull
	ItemStack getToolPresentationStack(String tool_name);

	@SideOnly(Side.CLIENT)
	void renderInMachine(ItemStack stack, float progress, float angle, float maxProgress, ItemStack renderedStack);
}
