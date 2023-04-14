package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IRarity;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.CustomSkinHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-06-2020
 */
public interface ISkinnable
{
	/**
	 * @param stack of a skinnable item
	 * @return skin ID or empty string
	 */
	default String getSkinnableCurrentSkin(ItemStack stack)
	{
		return ItemNBTHelper.getTag(stack).getString("contributorSkin");
	}

	/**
	 * Applies a skin to the ItemStack
	 * @param stack of a skinnable item
	 * @param skinName skin ID
	 */
	default void applySkinnableSkin(ItemStack stack, String skinName)
	{
		ItemNBTHelper.setString(stack, "contributorSkin", skinName);
	}

	/**
	 * @param skin skin ID
	 * @return true if skin exists
	 */
	default boolean isValidSkin(String skin)
	{
		return !skin.isEmpty()&&CustomSkinHandler.specialSkins.containsKey(skin);
	}


	default void addSkinTooltip(@Nonnull ItemStack stack, @Nonnull List<String> tooltip)
	{
		String skin;
		if(isValidSkin(skin = getSkinnableCurrentSkin(stack)))
		{
			tooltip.add(TextFormatting.WHITE+I18n.format(String.format("skin.%1$s.%2$s.name", ImmersiveIntelligence.MODID, skin)));
			tooltip.add(IIUtils.getItalicString(I18n.format(String.format("skin.%1$s.%2$s.desc", ImmersiveIntelligence.MODID, skin))));
		}
	}

	@Nullable
	default IRarity getSkinRarity(@Nonnull ItemStack stack)
	{
		String skin = getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty()&&CustomSkinHandler.specialSkins.containsKey(skin))
			return CustomSkinHandler.specialSkins.get(skin).rarity;
		return null;
	}

	String getSkinnableName();

	String getSkinnableDefaultTextureLocation();
}
