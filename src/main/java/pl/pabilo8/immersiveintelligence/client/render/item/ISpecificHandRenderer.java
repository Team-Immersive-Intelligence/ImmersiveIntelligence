package pl.pabilo8.immersiveintelligence.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public interface ISpecificHandRenderer
{
	/**
	 * Used to render or mark a value in held item's renderer
	 *
	 * @return true if the hand <b>should not</b> be rendered
	 */
	boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks);

	/**
	 * Used to render crosshair for held item
	 *
	 * @return true if regular crosshair should be rendered
	 */
	default boolean renderCrosshair(ItemStack stack, EnumHand hand)
	{
		return true;
	}
}
