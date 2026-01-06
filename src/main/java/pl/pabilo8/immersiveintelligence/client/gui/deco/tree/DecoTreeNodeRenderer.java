package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;

/**
 * Default implementation of IDecoTreeNodeRenderer.
 * Renders nodes as simple boxes with text and connection lines.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public class DecoTreeNodeRenderer implements IDecoTreeNodeRenderer
{
	public static final int NODE_WIDTH = 20;
	public static final int NODE_HEIGHT = 20;
	public static final int CONNECTION_WIDTH = 2;
	public static final int ARROW_SIZE = 3;

	private final IIColor activeColor = IIColor.fromHex("497d49");
	private final IIColor availableColor = IIColor.fromHex("888888");
	private final IIColor unavailableColor = IIColor.fromHex("3c3c3c");
	private final IIColor hoverColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.75f);

	public final IIColor connectionColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.75f);
	public final IIColor connectionActiveColor = IIColor.fromHex("497d49");

	@Override
	public void render(@Nonnull IDecoTreeNode node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable)
	{
		// Determine color based on state
		IIColor color;
		if(isActive)
			color = activeColor;
		else if(isHovered)
			color = hoverColor;
		else if(isAvailable)
			color = availableColor;
		else
			color = unavailableColor;

		// Draw node background
		ClientUtils.bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		draw.drawConnectedTexColorRect(x, y, NODE_WIDTH, NODE_HEIGHT, color, DecoTextures.RES_TEXTURES_DECO_IE_SLOT, 32, 32, 4, 4);
		draw.finish();

		// Draw node text (centered)
		String text = node.getDisplayName();
		if(!text.isEmpty())
		{
			FontRenderer font = getFontRenderer();

			// Truncate text if too long
			if(font.getStringWidth(text) > NODE_WIDTH-4)
				text = font.trimStringToWidth(text, NODE_WIDTH-6)+"...";

			int textWidth = font.getStringWidth(text);
			int textX = x+(NODE_WIDTH-textWidth)/2;
			int textY = y+(NODE_HEIGHT-font.FONT_HEIGHT)/2;

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 1); // Ensure text is above background
			font.drawString(text, textX, textY, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void renderRootNode(int x, int y)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);

		IIDrawUtils draw = IIDrawUtils.startColored();
		// Draw a circle for the virtual root
		draw.drawColorRect(-3, -3, 6, 6, IIColor.fromHex("888888"));
		draw.finish();

		GlStateManager.popMatrix();
	}

	/**
	 * Renders a connection between parent and child nodes.
	 */
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

	private void renderHorizontalConnection(IIDrawUtils draw, int fromX, int fromY, int toX, int toY, IIColor color, boolean reverse)
	{
		// Calculate mid point
		int midX = (fromX+toX)/2;

		if(!reverse)
		{
			// Left to right connection
			// Horizontal segment from parent to mid
			draw.drawColorRect(fromX, fromY-CONNECTION_WIDTH/2,
					midX-fromX, CONNECTION_WIDTH, color);

			// Vertical segment at mid
			int verticalStartY = Math.min(fromY, toY);
			int verticalEndY = Math.max(fromY, toY);
			draw.drawColorRect(midX-CONNECTION_WIDTH/2, verticalStartY,
					CONNECTION_WIDTH, verticalEndY-verticalStartY, color);

			// Horizontal segment from mid to child
			draw.drawColorRect(midX, toY-CONNECTION_WIDTH/2,
					toX-midX, CONNECTION_WIDTH, color);

			// Draw arrow at target
			drawArrowRight(draw, toX, toY, color);
		}
		else
		{
			// Right to left connection
			// Horizontal segment from parent to mid
			draw.drawColorRect(fromX, fromY-CONNECTION_WIDTH/2,
					midX-fromX, CONNECTION_WIDTH, color);

			// Vertical segment at mid
			int verticalStartY = Math.min(fromY, toY);
			int verticalEndY = Math.max(fromY, toY);
			draw.drawColorRect(midX-CONNECTION_WIDTH/2, verticalStartY,
					CONNECTION_WIDTH, verticalEndY-verticalStartY, color);

			// Horizontal segment from mid to child
			draw.drawColorRect(midX, toY-CONNECTION_WIDTH/2,
					toX-midX, CONNECTION_WIDTH, color);

			// Draw arrow at target
			drawArrowLeft(draw, toX, toY, color);
		}
	}

	private void renderVerticalConnection(IIDrawUtils draw, int fromX, int fromY, int toX, int toY, IIColor color, boolean reverse)
	{
		// Calculate mid point
		int midY = (fromY+toY)/2;

		if(!reverse)
		{
			// Top to bottom connection
			// Vertical segment from parent to mid
			draw.drawColorRect(fromX-CONNECTION_WIDTH/2, fromY,
					CONNECTION_WIDTH, midY-fromY, color);

			// Horizontal segment at mid
			int horizontalStartX = Math.min(fromX, toX);
			int horizontalEndX = Math.max(fromX, toX);
			draw.drawColorRect(horizontalStartX, midY-CONNECTION_WIDTH/2,
					horizontalEndX-horizontalStartX, CONNECTION_WIDTH, color);

			// Vertical segment from mid to child
			draw.drawColorRect(toX-CONNECTION_WIDTH/2, midY,
					CONNECTION_WIDTH, toY-midY, color);

			// Draw arrow at target
			drawArrowDown(draw, toX, toY, color);
		}
		else
		{
			// Bottom to top connection
			// Vertical segment from parent to mid
			draw.drawColorRect(fromX-CONNECTION_WIDTH/2, fromY,
					CONNECTION_WIDTH, midY-fromY, color);

			// Horizontal segment at mid
			int horizontalStartX = Math.min(fromX, toX);
			int horizontalEndX = Math.max(fromX, toX);
			draw.drawColorRect(horizontalStartX, midY-CONNECTION_WIDTH/2,
					horizontalEndX-horizontalStartX, CONNECTION_WIDTH, color);

			// Vertical segment from mid to child
			draw.drawColorRect(toX-CONNECTION_WIDTH/2, midY,
					CONNECTION_WIDTH, toY-midY, color);

			// Draw arrow at target
			drawArrowUp(draw, toX, toY, color);
		}
	}

	private void drawArrowRight(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x, y-ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x+ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE*2, color);
	}

	private void drawArrowLeft(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x, y-ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE, ARROW_SIZE*2, color);
	}

	private void drawArrowDown(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE/2, y, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y+ARROW_SIZE, ARROW_SIZE*2, ARROW_SIZE, color);
	}

	private void drawArrowUp(IIDrawUtils draw, int x, int y, IIColor color)
	{
		draw.drawColorRect(x-ARROW_SIZE/2, y, ARROW_SIZE, ARROW_SIZE, color)
				.drawColorRect(x-ARROW_SIZE, y-ARROW_SIZE, ARROW_SIZE*2, ARROW_SIZE, color);
	}
}
