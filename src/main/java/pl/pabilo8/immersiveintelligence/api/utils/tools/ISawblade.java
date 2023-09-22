package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 19-08-2019
 */
public interface ISawblade
{
	String getMaterialName(ItemStack stack);

	void damageSawblade(ItemStack stack, int amount);

	int getSawbladeDamage(ItemStack stack);

	int getSawbladeMaxDamage(ItemStack stack);

	int getHardness(ItemStack stack);

	@Nonnull
	ItemStack getToolPresentationStack(String toolName);

	ResourceLocation getSawbladeTexture(ItemStack stack);
}
