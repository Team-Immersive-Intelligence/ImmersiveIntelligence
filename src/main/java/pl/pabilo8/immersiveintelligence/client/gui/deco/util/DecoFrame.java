package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.06.2025
 */
public class DecoFrame
{
	/**
	 * Resource location of the frame texture
	 */
	public final ResLoc style;
	/**
	 * If true, only the corners of the frame will be drawn.
	 * This is useful for frames that are not meant to have a full border.
	 */
	public final boolean cornersOnly;
	/**
	 * Thickness of the frame in pixels.
	 * This is used to determine how thick the frame should be drawn.
	 */
	public final int frameThickness;
	/**
	 * Sides of the frame, in order: top, bottom, left, right.
	 * If a side is true, it will be drawn.
	 */
	public boolean[] sides = new boolean[]{true, true, true, true};

	public DecoFrame(ResLoc style, boolean cornersOnly, int frameThickness)
	{
		this.style = style;
		this.cornersOnly = cornersOnly;
		this.frameThickness = frameThickness;
	}

	/**
	 * Sets all sides of the frame to be drawn.
	 *
	 * @return this DecoFrame instance for method chaining
	 */
	public DecoFrame withSides(boolean top, boolean bottom, boolean left, boolean right)
	{
		sides[0] = top;
		sides[1] = bottom;
		sides[2] = left;
		sides[3] = right;
		return this;
	}

	/**
	 * Disables the top border
	 *
	 * @return this DecoFrame instance for method chaining
	 */
	public DecoFrame withoutTop()
	{
		sides[0] = false;
		return this;
	}

	/**
	 * Disables the bottom border
	 *
	 * @return this DecoFrame instance for method chaining
	 */
	public DecoFrame withoutBottom()
	{
		sides[1] = false;
		return this;
	}

	/**
	 * Disables the left border
	 *
	 * @return this DecoFrame instance for method chaining
	 */
	public DecoFrame withoutLeft()
	{
		sides[2] = false;
		return this;
	}

	/**
	 * Disables the right border
	 *
	 * @return this DecoFrame instance for method chaining
	 */
	public DecoFrame withoutRight()
	{
		sides[3] = false;
		return this;
	}
}
