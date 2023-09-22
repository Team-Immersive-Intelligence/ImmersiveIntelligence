package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
public interface IAdvancedTextOverlay
{
	String[] getOverlayText(EntityPlayer player, RayTraceResult mop);
}
