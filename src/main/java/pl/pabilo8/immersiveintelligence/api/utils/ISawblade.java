package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Pabilo8 on 19-08-2019.
 */
public interface ISawblade
{
	String getMaterialName(ItemStack stack);

	void damageSawblade(ItemStack stack, int amount);

	int getSawbladeDamage(ItemStack stack);

	int getSawbladeMaxDamage(ItemStack stack);

	int getHardness(ItemStack stack);

	@Nonnull
	ItemStack getToolPresentationStack(String tool_name);

	//A hacky way of registering models, works tho
	int getSawbladeDisplayMeta(ItemStack stack);
}
