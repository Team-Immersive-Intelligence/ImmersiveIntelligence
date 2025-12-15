package pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.04.2023
 */
@SideOnly(Side.CLIENT)
public abstract class InWorldOverlayBase extends Gui
{
	public abstract void draw(@Nonnull EntityPlayer player, @Nonnull World world, @Nullable RayTraceResult mouseOver, float partialTicks);
}
