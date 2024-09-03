package pl.pabilo8.immersiveintelligence.client.gui.elements.storage;

import net.minecraft.client.gui.Gui;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiElementProgressBar extends Gui
{
	public int x;
	public int y;
	private final IIColor color1, color2;
	private final int w, h;

	public GuiElementProgressBar(int x, int y, int w, int h, IIColor color1, IIColor color2)
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
		return new GuiElementProgressBar(x, y, w, h, IIReference.COLOR_POWERBAR1, IIReference.COLOR_POWERBAR2);
	}

	public static GuiElementProgressBar createArmorBar(int x, int y, int w, int h)
	{
		return new GuiElementProgressBar(x, y, w, h, IIReference.COLOR_ARMORBAR1, IIReference.COLOR_ARMORBAR2);
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
