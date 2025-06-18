package pl.pabilo8.immersiveintelligence.client.gui.deco.component;

/**
 * Enum for constraints in Deco GUI layout.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.01.2025
 */
public enum DecoAlignment
{
	TOP_LEFT,
	TOP,
	TOP_RIGHT,

	LEFT,
	CENTER,
	RIGHT,

	BOTTOM_LEFT,
	BOTTOM,
	BOTTOM_RIGHT;

	/**
	 * Gets the x offset of an element aligned within a parent space.
	 *
	 * @param x           x offset
	 * @param width       width of the element
	 * @param parentWidth width of the parent space
	 * @return aligned x offset
	 */
	public int getAlignX(int x, int width, int parentWidth)
	{
		switch(this)
		{
			case TOP:
			case CENTER:
			case BOTTOM:
				return x+((parentWidth-width)/2);
			case TOP_RIGHT:
			case RIGHT:
			case BOTTOM_RIGHT:
				return x+Math.max(parentWidth-width, 0);
			default:
			case TOP_LEFT:
			case LEFT:
			case BOTTOM_LEFT:
				return x;
		}
	}

	/**
	 * Gets the y offset of an element aligned within a parent space.
	 *
	 * @param y            y offset
	 * @param height       height of the element
	 * @param parentHeight height of the parent space
	 * @return aligned y offset
	 */
	public int getAlignY(int y, int height, int parentHeight)
	{
		switch(this)
		{
			case LEFT:
			case CENTER:
			case RIGHT:
				return y+(parentHeight-height)/2;
			case BOTTOM_LEFT:
			case BOTTOM:
			case BOTTOM_RIGHT:
				return y+Math.max(parentHeight-height, 0);
			case TOP_LEFT:
			case TOP:
			case TOP_RIGHT:
			default:
				return y;
		}
	}
}
