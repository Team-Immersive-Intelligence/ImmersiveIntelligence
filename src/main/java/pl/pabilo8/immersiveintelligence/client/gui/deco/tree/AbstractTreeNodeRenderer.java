package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;

/**
 * Provides orthogonal connection rendering for Deco tree node renderers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 09.12.2025
 */
public abstract class AbstractTreeNodeRenderer<T> implements IDecoTreeNodeRenderer<T>
{
	public static final int CONNECTION_WIDTH = 2;
	public static final int ARROW_SIZE = 3;

	protected final IIColor connectionColor;
	protected final IIColor connectionActiveColor;

	protected AbstractTreeNodeRenderer()
	{
		this(IIColor.fromHex("888888"), IIColor.fromHex("497d49"));
	}

	protected AbstractTreeNodeRenderer(IIColor connectionColor, IIColor connectionActiveColor)
	{
		this.connectionColor = connectionColor;
		this.connectionActiveColor = connectionActiveColor;
	}

	@Override
	public abstract void renderNode(@Nonnull IDecoTreeNode<T> node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable);

	@Override
	public void renderConnection(TreeLayout.NodeLayoutInfo parentInfo, TreeLayout.NodeLayoutInfo childInfo,
								 TreeLayout.Orientation orientation, boolean isActive)
	{
		IIColor color = isActive?connectionActiveColor: connectionColor;
		IIDrawUtils draw = IIDrawUtils.startColored();

		switch(orientation)
		{
			case HORIZONTAL_LEFT_TO_RIGHT:
				renderHorizontalConnection(draw, parentInfo.getRight(), parentInfo.getCenterY(),
						childInfo.x, childInfo.getCenterY(), color, false);
				break;
			case HORIZONTAL_RIGHT_TO_LEFT:
				renderHorizontalConnection(draw, parentInfo.x, parentInfo.getCenterY(),
						childInfo.getRight(), childInfo.getCenterY(), color, true);
				break;
			case VERTICAL_TOP_TO_BOTTOM:
				renderVerticalConnection(draw, parentInfo.getCenterX(), parentInfo.getBottom(),
						childInfo.getCenterX(), childInfo.y, color, false);
				break;
			case VERTICAL_BOTTOM_TO_TOP:
				renderVerticalConnection(draw, parentInfo.getCenterX(), parentInfo.y,
						childInfo.getCenterX(), childInfo.getBottom(), color, true);
				break;
		}

		draw.finish();
	}

	protected void renderHorizontalConnection(IIDrawUtils draw, int fromX, int fromY, int toX, int toY, IIColor color, boolean reverse)
	{
		int midX = (fromX+toX)/2;
		drawHorizontalSegment(draw, fromX, midX, fromY, color);
		drawVerticalSegment(draw, fromY, toY, midX, color);
		drawHorizontalSegment(draw, midX, toX, toY, color);
		if(reverse)
			drawArrowLeft(draw, toX, toY, color);
		else
			drawArrowRight(draw, toX, toY, color);
	}

	protected void renderVerticalConnection(IIDrawUtils draw, int fromX, int fromY, int toX, int toY, IIColor color, boolean reverse)
	{
		int midY = (fromY+toY)/2;
		drawVerticalSegment(draw, fromY, midY, fromX, color);
		drawHorizontalSegment(draw, fromX, toX, midY, color);
		drawVerticalSegment(draw, midY, toY, toX, color);
		if(reverse)
			drawArrowUp(draw, toX, toY, color);
		else
			drawArrowDown(draw, toX, toY, color);
	}

	private void drawHorizontalSegment(IIDrawUtils draw, int fromX, int toX, int y, IIColor color)
	{
		int x = Math.min(fromX, toX);
		draw.drawColorRect(x, y-CONNECTION_WIDTH/2, Math.abs(toX-fromX), CONNECTION_WIDTH, color);
	}

	private void drawVerticalSegment(IIDrawUtils draw, int fromY, int toY, int x, IIColor color)
	{
		int y = Math.min(fromY, toY);
		draw.drawColorRect(x-CONNECTION_WIDTH/2, y, CONNECTION_WIDTH, Math.abs(toY-fromY), color);
	}

	protected void drawArrowRight(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x, y-ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x+ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE*2, color);
	}

	protected void drawArrowLeft(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE, y-ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE*2, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE*2, color);
	}

	protected void drawArrowDown(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE/2, y, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y+ARROW_SIZE, ARROW_SIZE*2, ARROW_SIZE, color);
	}

	protected void drawArrowUp(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE/2, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y-ARROW_SIZE*2, ARROW_SIZE*2, ARROW_SIZE, color);
	}
}
