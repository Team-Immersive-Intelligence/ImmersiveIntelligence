package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * Interface for a node in a tree structure that can be displayed by DecoTreeDisplay.
 * <p>
 * Node is responsible for tree-logic related properties (deps, lockouts, active/available and layout position).
 * Visual representation (name, tooltip, rendering) is handled by IDecoTreeNodeRenderer.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public interface IDecoTreeNode<T>
{
	/**
	 * @return Unique identifier for this node (used for equality checks)
	 */
	@Nonnull
	String getId();

	/**
	 * @return Nodes that must be unlocked/acquired before this node
	 */
	@Nonnull
	Set<IDecoTreeNode<T>> getDependencies();

	/**
	 * @return Nodes that are incompatible with this node (mutual exclusion)
	 */
	@Nonnull
	Set<IDecoTreeNode<T>> getLockOuts();

	/**
	 * @return X position for rendering (set by layout algorithm)
	 */
	int getX();

	/**
	 * @return Y position for rendering (set by layout algorithm)
	 */
	int getY();

	/**
	 * Sets the position for rendering.
	 */
	void setPosition(int x, int y);

	/**
	 * @return Width for rendering
	 */
	int getWidth();

	/**
	 * @return Height for rendering
	 */
	int getHeight();

	/**
	 * @return Whether this node is currently selected/active
	 */
	boolean isActive();

	/**
	 * @return Whether this node is available (all dependencies met, no lockouts)
	 */
	boolean isAvailable(@Nonnull Collection<IDecoTreeNode<T>> activeNodes);

	/**
	 * @return User data associated with this node
	 */
	@Nonnull
	T getUserData();
}
