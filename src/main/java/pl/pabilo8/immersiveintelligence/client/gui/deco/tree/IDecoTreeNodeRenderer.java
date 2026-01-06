package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nonnull;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.NodeLayoutInfo;
import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.Orientation;

/**
 * Interface for rendering tree nodes in DecoTreeDisplay.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public interface IDecoTreeNodeRenderer
{
	/**
	 * Renders a tree node.
	 *
	 * @param node        The node to render
	 * @param x           X position
	 * @param y           Y position
	 * @param isHovered   Whether the node is currently hovered
	 * @param isActive    Whether the node is active/selected
	 * @param isAvailable Whether the node is available (dependencies met)
	 */
	void render(@Nonnull IDecoTreeNode node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable);

	/**
	 * Renders a virtual root node that is not a part of the tree structure.
	 *
	 * @param x the x position
	 * @param y the y position
	 */
	default void renderRootNode(int x, int y)
	{

	}

	/**
	 * Renders a connection between two nodes using the specified layout orientation.
	 * Default implementation does nothing - renderers that support connections should override this.
	 */
	default void renderConnection(NodeLayoutInfo parentInfo, NodeLayoutInfo childInfo, Orientation orientation, boolean isActive)
	{

	}

	/**
	 * Utility method to get the default FontRenderer.
	 */
	default FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRenderer;
	}
}
