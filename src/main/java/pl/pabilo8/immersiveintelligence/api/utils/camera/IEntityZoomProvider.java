package pl.pabilo8.immersiveintelligence.api.utils.camera;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public interface IEntityZoomProvider
{
	IAdvancedZoomTool getZoom();

	default ItemStack getZoomStack()
	{
		return ItemStack.EMPTY;
	}
}
