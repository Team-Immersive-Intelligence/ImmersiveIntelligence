package pl.pabilo8.immersiveintelligence.client.gui.elements;

import net.minecraft.client.gui.Gui;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiElementProgressBar extends Gui
{
	public int x;
	public int y;
	private final int color1, color2, w, h;

	public GuiElementProgressBar(int x, int y, int w, int h, int color1, int color2)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.color1 = color1;
		this.color2 = color2;
	}

	public static GuiElementProgressBar createEnergyBar(int x, int y, int w, int h)
	{
		return new GuiElementProgressBar(x, y, w, h, IIReference.COLOR_POWERBAR_1, IIReference.COLOR_POWERBAR_2);
	}

	public static GuiElementProgressBar createArmorBar(int x, int y, int w, int h)
	{
		return new GuiElementProgressBar(x, y, w, h, IIReference.COLOR_ARMORBAR_1, IIReference.COLOR_ARMORBAR_2);
	}

	public void draw(float progress)
	{
		IIClientUtils.drawGradientBar(x, y, w, h, color1, color2, progress);
	}

	public boolean mouseOver(int mouseX, int mouseY)
	{
		return mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.w&&mouseY < this.y+this.h;
	}
}
