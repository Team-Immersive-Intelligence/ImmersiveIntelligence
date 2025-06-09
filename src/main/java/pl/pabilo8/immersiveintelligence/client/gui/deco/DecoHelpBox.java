package pl.pabilo8.immersiveintelligence.client.gui.deco;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

/**
 * A class for displaying help information when hovering over a part of the gui
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.01.2024
 */
public class DecoHelpBox
{
	/**
	 * The x position of the box
	 */
	public int x;
	/**
	 * The y position of the box
	 */
	public int y;
	/**
	 * The width of the box
	 */
	public int width;
	/**
	 * The height of the box
	 */
	public int height;
	/**
	 * The translated text to display in the box
	 */
	public String text;

	public DecoHelpBox(int x, int y, int width, int height, String helpText)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = I18n.format(helpText);
	}

	/**
	 * @param gui component this box is attached to
	 */
	public DecoHelpBox(GuiButton gui, String helpText)
	{
		this(gui.x, gui.y, gui.width, gui.height, helpText);
	}
}
