package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.12.2019
 */
public interface IAdvancedTextOverlay
{
	@SideOnly(Side.CLIENT)
	String[] getOverlayText(EntityPlayer player, RayTraceResult mop);
}
