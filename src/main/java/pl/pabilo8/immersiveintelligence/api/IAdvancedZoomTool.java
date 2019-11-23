package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Pabilo8 on 27-09-2019.
 */
public interface IAdvancedZoomTool extends IZoomTool
{
	String getZoomOverlayTexture(ItemStack stack, EntityPlayer player);
}
