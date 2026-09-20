package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.NodeLayoutInfo;
import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.Orientation;

/**
 * Renders nodes and connections for a {@link pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoTreeDisplay}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 09.12.2025
 */
public interface IDecoTreeNodeRenderer<T>
{
	/**
	 * Draws one tree node.
	 */
	void renderNode(@Nonnull IDecoTreeNode<T> node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable);

	/**
	 * @return the uniform node width used by the layout
	 */
	default int getNodeWidth()
	{
		return DefaultTreeNodeRenderer.NODE_WIDTH;
	}

	/**
	 * @return the uniform node height used by the layout
	 */
	default int getNodeHeight()
	{
		return DefaultTreeNodeRenderer.NODE_HEIGHT;
	}

	default void renderRootNode(int x, int y)
	{
	}

	default void renderConnection(NodeLayoutInfo parentInfo, NodeLayoutInfo childInfo, Orientation orientation, boolean isActive)
	{
	}

	@Nonnull
	default String getDisplayName(@Nonnull IDecoTreeNode<T> node)
	{
		T data = node.getUserData();
		return data!=null?String.valueOf(data): "";
	}

	@Nonnull
	default Collection<String> getTooltip(@Nonnull IDecoTreeNode<T> node)
	{
		return Collections.emptyList();
	}

	default FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRenderer;
	}
}
