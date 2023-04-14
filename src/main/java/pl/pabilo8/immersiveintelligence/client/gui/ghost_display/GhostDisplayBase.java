package pl.pabilo8.immersiveintelligence.client.gui.ghost_display;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8
 * @since 13.04.2023
 */
public abstract class GhostDisplayBase extends Gui
{
	public abstract boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver);

	public abstract void draw(EntityPlayer player, RayTraceResult mouseOver, Vec3d pos);
}
