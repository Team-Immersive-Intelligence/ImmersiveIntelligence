package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;

public abstract class AbstractTreeNodeRenderer<T> implements IDecoTreeNodeRenderer<T>
{
	public static final int CONNECTION_WIDTH = 2;
	public static final int ARROW_SIZE = 3;

	protected final IIColor connectionColor = IIColor.fromHex("888888");
	protected final IIColor connectionActiveColor = IIColor.fromHex("497d49");

	@Override
	public abstract void renderNode(@Nonnull IDecoTreeNode<T> node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable);

	@Override
	public void renderConnection(TreeLayout.NodeLayoutInfo parentInfo, TreeLayout.NodeLayoutInfo childInfo,
								 TreeLayout.Orientation orientation, boolean isActive)
	{
		IIColor color = isActive?connectionActiveColor: connectionColor;

		int parentCenterX = parentInfo.getCenterX();
		int parentCenterY = parentInfo.getCenterY();
		int childCenterX = childInfo.getCenterX();
		int childCenterY = childInfo.getCenterY();

		IIDrawUtils draw = IIDrawUtils.startColored();

		switch(orientation)
		{
			case HORIZONTAL_LEFT_TO_RIGHT:
				renderHorizontalConnection(draw, parentCenterX, parentCenterY, childCenterX, childCenterY, color, false);
				break;
			case HORIZONTAL_RIGHT_TO_LEFT:
				renderHorizontalConnection(draw, parentCenterX, parentCenterY, childCenterX, childCenterY, color, true);
				break;
			case VERTICAL_TOP_TO_BOTTOM:
				renderVerticalConnection(draw, parentCenterX, parentCenterY, childCenterX, childCenterY, color, false);
				break;
			case VERTICAL_BOTTOM_TO_TOP:
				renderVerticalConnection(draw, parentCenterX, parentCenterY, childCenterX, childCenterY, color, true);
				break;
		}

		draw.finish();
	}

	protected void renderHorizontalConnection(IIDrawUtils draw, int fromX, int fromY, int toX, int toY, IIColor color, boolean reverse)
	{
		int midX = (fromX+toX)/2;

		draw.drawColorRect(fromX, fromY-CONNECTION_WIDTH/2, midX-fromX, CONNECTION_WIDTH, color);

		int verticalStartY = Math.min(fromY, toY);
		int verticalEndY = Math.max(fromY, toY);
		draw.drawColorRect(midX-CONNECTION_WIDTH/2, verticalStartY, CONNECTION_WIDTH, verticalEndY-verticalStartY, color);

		draw.drawColorRect(midX, toY-CONNECTION_WIDTH/2, toX-midX, CONNECTION_WIDTH, color);

		if(reverse) drawArrowLeft(draw, toX, toY, color);
		else drawArrowRight(draw, toX, toY, color);
	}

	protected void renderVerticalConnection(IIDrawUtils draw, int fromX, int fromY, int toX, int toY, IIColor color, boolean reverse)
	{
		int midY = (fromY+toY)/2;

		draw.drawColorRect(fromX-CONNECTION_WIDTH/2, fromY, CONNECTION_WIDTH, midY-fromY, color);

		int horizontalStartX = Math.min(fromX, toX);
		int horizontalEndX = Math.max(fromX, toX);
		draw.drawColorRect(horizontalStartX, midY-CONNECTION_WIDTH/2, horizontalEndX-horizontalStartX, CONNECTION_WIDTH, color);

		draw.drawColorRect(toX-CONNECTION_WIDTH/2, midY, CONNECTION_WIDTH, toY-midY, color);

		if(reverse) drawArrowUp(draw, toX, toY, color);
		else drawArrowDown(draw, toX, toY, color);
	}

	protected void drawArrowRight(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x, y-ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x+ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE*2, color);
	}

	protected void drawArrowLeft(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x, y-ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE*2, color);
	}

	protected void drawArrowDown(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE/2, y, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y+ARROW_SIZE, ARROW_SIZE*2, ARROW_SIZE, color);
	}

	protected void drawArrowUp(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE/2, y, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE*2, ARROW_SIZE, color);
	}
}

