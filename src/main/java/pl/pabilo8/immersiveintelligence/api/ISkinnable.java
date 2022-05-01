package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 30-06-2020
 */
public interface ISkinnable
{
	default String getSkinnableCurrentSkin(ItemStack stack)
	{
		return ItemNBTHelper.getTag(stack).getString("contributorSkin");
	}

	default void applySkinnableSkin(ItemStack stack, String skinName)
	{
		ItemNBTHelper.setString(stack, "contributorSkin", skinName);
	}

	String getSkinnableName();

	String getSkinnableDefaultTextureLocation();
}
