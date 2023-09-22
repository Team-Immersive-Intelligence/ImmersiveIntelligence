package pl.pabilo8.immersiveintelligence.api.utils.tools;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;

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
		return ItemNBTHelper.getString(stack, IISkinHandler.NBT_ENTRY);
	}

	/**
	 * Applies a skin to the ItemStack
	 *
	 * @param stack    of a skinnable item
	 * @param skinName skin ID
	 */
	default void applySkinnableSkin(ItemStack stack, String skinName)
	{
		ItemNBTHelper.setString(stack, IISkinHandler.NBT_ENTRY, skinName);
	}

	/**
	 * @param skin skin ID
	 * @return true if skin exists
	 * @// TODO: 8/12/2023 Replace all usages of this function with <code>IISkinHandler.isValidSkin()</code>
	 * @see pl.pabilo8.immersiveintelligence.common.util.IISkinHandler#isValidSkin(String)
	 * @deprecated Replaced by <code>IISkinHandler.isValidSkin()</code>
	 */
	default boolean isValidSkin(String skin)
	{
		return !skin.isEmpty()&&IISkinHandler.specialSkins.containsKey(skin);
	}

	default void addSkinTooltip(@Nonnull ItemStack stack, @Nonnull List<String> tooltip)
	{
		String skin = getSkinnableCurrentSkin(stack);
		if(IISkinHandler.isValidSkin(skin))
		{
			IISpecialSkin s = IISkinHandler.specialSkins.get(skin);
			tooltip.add(s.rarity.rarityColor+I18n.format(String.format("skin.%1$s.%2$s.name", ImmersiveIntelligence.MODID, skin)));
			tooltip.add(IIUtils.getItalicString(I18n.format(String.format("skin.%1$s.%2$s.desc", ImmersiveIntelligence.MODID, skin))));
		}
	}

	/**
	 * @param stack Itemstack to get skin from
	 * @return Skin rarity if found otherwise <code>null</code>
	 */
	@Nullable
	default IRarity getSkinRarity(@Nonnull ItemStack stack)
	{
		String skin = getSkinnableCurrentSkin(stack);
		if(IISkinHandler.isValidSkin(skin))
			return IISkinHandler.getSkin(skin).rarity;
		return null;
	}

	/**
	 * @return Name of the skinnable
	 */
	String getSkinnableName();

	/**
	 * @return Default texture location of the skinnable
	 */
	String getSkinnableDefaultTextureLocation();
}
