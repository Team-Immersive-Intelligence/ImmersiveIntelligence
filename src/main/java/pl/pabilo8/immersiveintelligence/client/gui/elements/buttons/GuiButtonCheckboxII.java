package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 18.07.2021
 */
public class GuiButtonCheckboxII extends GuiButtonState
{
	public GuiButtonCheckboxII(int buttonId, int x, int y, String name, boolean state)
	{
		super(buttonId, x, y, 9, 9, name, state, "immersiveintelligence:textures/gui/emplacement_icons.png", 44, 52, -1);
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			ClientUtils.bindTexture(texture);
			FontRenderer fontrenderer = mc.fontRenderer;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.width&&mouseY < this.y+this.height;
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			int u = texU+(!state?0: offsetDir==0?width: offsetDir==2?-width: 0);
			int v = texV+(!state?0: offsetDir==1?height: offsetDir==3?-height: 0);
			this.drawTexturedModalRect(x, y, u, v, width, height);
			this.mouseDragged(mc, mouseX, mouseY);
			if(displayString!=null&&!displayString.isEmpty())
			{
				this.drawString(fontrenderer, displayString, x+textOffset[0]+2, y+textOffset[1], IIReference.COLOR_H1.getPackedRGB());
			}
		}
		if(this.visible&&state)
			this.drawCenteredString(mc.fontRenderer, "\u2714", x+width/2+2, y-1, Lib.COLOUR_I_ImmersiveOrange);
	}


	/**
	 * Renders the specified text to the screen, center-aligned.
	 * Without shadow.
	 */
	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
	{
		fontRendererIn.drawString(text, x-fontRendererIn.getStringWidth(text)/2, y, color);
	}

	/**
	 * Renders the specified text to the screen.
	 * Without shadow.
	 */
	@Override
	public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
	{
		fontRendererIn.drawString(text, x, y, color);
	}
}
