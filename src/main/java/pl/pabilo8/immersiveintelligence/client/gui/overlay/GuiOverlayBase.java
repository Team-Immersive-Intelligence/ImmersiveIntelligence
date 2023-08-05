package pl.pabilo8.immersiveintelligence.client.gui.overlay;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public abstract class GuiOverlayBase extends Gui
{
	protected static final ResourceLocation TEXTURE_HUD = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/hud_elements.png");
	protected static final ResourceLocation TEXTURE_IE_HUD = new ResourceLocation("immersiveengineering:textures/gui/hud_elements.png");

	public abstract boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver);

	public abstract void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height);

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
