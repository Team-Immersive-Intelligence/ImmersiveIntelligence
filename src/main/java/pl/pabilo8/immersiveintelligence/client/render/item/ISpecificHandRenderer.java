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
	 * Used to cancel rendering of a crosshair for held item
	 *
	 * @return false if regular crosshair should be rendered
	 */
	default boolean shouldCancelCrosshair(ItemStack stack, EnumHand hand)
	{
		return false;
	}
}
