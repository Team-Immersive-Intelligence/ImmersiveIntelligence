package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.12.2025
 */
public class MapLine extends MapRect
{
	public final float width;

	public MapLine(int fromX, int fromZ, int toX, int toZ, IIColor color, float width)
	{
		super(fromX, fromZ, toX, toZ, color);
		this.width = width;
	}
}
