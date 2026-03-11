package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.12.2025
 */
public class MapRect
{
	public final int fromX, fromZ, toX, toZ;
	public final IIColor color;

	MapRect(int fromX, int fromZ, int toX, int toZ, IIColor color)
	{
		this.fromX = fromX;
		this.fromZ = fromZ;
		this.toX = toX;
		this.toZ = toZ;
		this.color = color;
	}
}
