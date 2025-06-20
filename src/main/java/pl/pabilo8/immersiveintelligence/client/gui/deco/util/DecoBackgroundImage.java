package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * Represents a tile displaying an image instead of taking part in the standard GUI building process.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.06.2025
 */
public class DecoBackgroundImage extends DecoBackgroundTile
{
	public DecoBackgroundImage(int x, int y, int width, int height, ResLoc style)
	{
		super(x, y, width, height, IIColor.WHITE, style, null);
	}
}
