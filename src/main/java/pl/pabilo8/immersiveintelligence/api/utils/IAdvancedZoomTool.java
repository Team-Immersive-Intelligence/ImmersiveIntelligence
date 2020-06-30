package pl.pabilo8.immersiveintelligence.api.utils;

import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 27-09-2019
 */
public interface IAdvancedZoomTool extends IZoomTool
{
	String getZoomOverlayTexture(ItemStack stack, EntityPlayer player);
}
