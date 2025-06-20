package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Represents a GUI tile using a background image and a shape mask applied to it.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.01.2025
 **/
@ParametersAreNonnullByDefault
public class DecoBackgroundTile
{
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final IIColor color;
	public final ResLoc style;
	public final ResLoc mask;
	@Nullable
	public DecoFrame frame;

	public DecoBackgroundTile(int x, int y, int width, int height, IIColor color, ResLoc style, ResLoc mask)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.style = style;
		this.mask = mask;
	}
}
