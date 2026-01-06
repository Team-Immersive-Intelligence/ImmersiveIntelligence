package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Interface for a tree structure that can be displayed by DecoTreeDisplay.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public interface IDecoTree
{
	/**
	 * @return All nodes in this tree
	 */
	@Nonnull
	Collection<IDecoTreeNode> getAllNodes();

	/**
	 * @return Root nodes (nodes with no dependencies)
	 */
	@Nonnull
	Collection<IDecoTreeNode> getRootNodes();

	/**
	 * @return Currently active/selected nodes
	 */
	@Nonnull
	Collection<IDecoTreeNode> getActiveNodes();

	/**
	 * @return Node currently being hovered, or null
	 */
	@Nullable
	IDecoTreeNode getHoveredNode();

	/**
	 * Sets the currently hovered node.
	 */
	void setHoveredNode(@Nullable IDecoTreeNode node);

	/**
	 * Called when a node is clicked.
	 */
	void onNodeClicked(@Nonnull IDecoTreeNode node);
}
