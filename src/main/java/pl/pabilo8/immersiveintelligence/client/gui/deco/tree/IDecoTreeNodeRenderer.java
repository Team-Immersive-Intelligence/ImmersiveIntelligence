package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.NodeLayoutInfo;
import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.Orientation;

/**
 * Interface for rendering tree nodes in DecoTreeDisplay.
 * <p>
 * Renderer is responsible for all visuals: nodes, connections, root node, display name, tooltip, etc.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public interface IDecoTreeNodeRenderer<T>
{
	void renderNode(@Nonnull IDecoTreeNode<T> node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable);

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
