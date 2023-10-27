package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8
 * @since 27-09-2019
 */
public interface IAdvancedZoomTool
{
	/**
	 * @return whether this item is valid for zooming in
	 */
	boolean shouldZoom(ItemStack stack, EntityPlayer player);

	default float getZoomProgress(ItemStack stack, EntityPlayer player)
	{
		return 1;
	}

	/**
	 * @return the different steps of zoom the item has, sorted from low to high
	 */
	float[] getZoomSteps(ItemStack stack, EntityPlayer player);

	@SideOnly(Side.CLIENT)
	ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player);
}
