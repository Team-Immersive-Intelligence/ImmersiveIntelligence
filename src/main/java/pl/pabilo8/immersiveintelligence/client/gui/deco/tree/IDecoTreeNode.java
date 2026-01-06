package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * Interface for a node in a tree structure that can be displayed by DecoTreeDisplay.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public interface IDecoTreeNode
{
	/**
	 * @return Unique identifier for this node (used for equality checks)
	 */
	@Nonnull
	String getId();

	/**
	 * @return Display name for this node
	 */
	@Nonnull
	String getDisplayName();

	/**
	 * @return Nodes that must be unlocked/acquired before this node
	 */
	@Nonnull
	Set<IDecoTreeNode> getDependencies();

	/**
	 * @return Nodes that are incompatible with this node (mutual exclusion)
	 */
	@Nonnull
	Set<IDecoTreeNode> getLockOuts();

	/**
	 * @return Optional tooltip text for this node
	 */
	@Nullable
	Collection<String> getTooltip();

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
	boolean isAvailable(@Nonnull Collection<IDecoTreeNode> activeNodes);

	/**
	 * @return User data associated with this node
	 */
	@Nullable
	Object getUserData();
}
