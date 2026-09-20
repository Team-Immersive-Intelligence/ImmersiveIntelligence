package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual;

import lombok.RequiredArgsConstructor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * Draws a straight separator line inside a Deco GUI.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.09.2026
 */
public class DecoLineSeparator extends DecoComponent<DecoLineSeparator>
{
	private LineDirection direction = LineDirection.HORIZONTAL;
	private IIColor color = DecoColors.H2;

	/**
	 * Creates a line separator at the specified position.
	 *
	 * @param x x position
	 * @param y y position
	 */
	public DecoLineSeparator(int x, int y)
	{
		super(x, y);
	}

	/**
	 * Sets the line direction.
	 *
	 * @param direction line direction
	 * @return this component
	 */
	public DecoLineSeparator withDirection(LineDirection direction)
	{
		this.direction = direction==null?LineDirection.HORIZONTAL: direction;
		return this;
	}

	/**
	 * Sets the line color.
	 *
	 * @param color line color
	 * @return this component
	 */
	public DecoLineSeparator withColor(IIColor color)
	{
		this.color = color==null?DecoColors.H2: color;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		IIDrawUtils.startColoredLines()
				.drawColorLine(x, y, x+direction.xMultiplier*width, y+direction.yMultiplier*height, color)
				.finish();
	}

	@Override
	public void cleanup()
	{

	}

	/**
	 * Supported separator directions.
	 */
	@RequiredArgsConstructor
	public enum LineDirection
	{
		HORIZONTAL(1, 0),
		VERTICAL(0, 1),
		DIAGONAL(1, 1);

		private final int xMultiplier;
		private final int yMultiplier;
	}
}
