package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

/**
 * Created by Pabilo8 on 27-12-2019.
 */
public interface IEntityOverlayText
{
	String[] getOverlayText(EntityPlayer player, RayTraceResult mop, boolean hammer);

	boolean useNixieFont(EntityPlayer player, RayTraceResult mop);
}
