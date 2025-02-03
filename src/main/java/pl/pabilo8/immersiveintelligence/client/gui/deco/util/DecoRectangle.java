package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.01.2025
 **/
public class DecoRectangle
{
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final ResLoc style;
	public final ResLoc mask;

	public DecoRectangle(int x, int y, int width, int height, ResLoc style, ResLoc mask)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.style = style;
		this.mask = mask;
	}
}
