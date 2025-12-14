package pl.pabilo8.immersiveintelligence.client.gui.overlay;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2022
 */
public abstract class GuiOverlayBase extends Gui
{
	protected static final ResourceLocation TEXTURE_HUD = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/hud_elements.png");
	protected static final ResourceLocation TEXTURE_IE_HUD = new ResourceLocation("immersiveengineering:textures/gui/hud_elements.png");

	public abstract boolean shouldDraw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver);

	public abstract void draw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver, int width, int height);

	public final void bindHUDTexture()
	{
		IIClientUtils.bindTexture(TEXTURE_HUD);
	}

	public enum GuiOverlayLayer
	{
		ITEM,
		VEHICLE
	}
}
