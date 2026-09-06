package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.target;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTree;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTreeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Adapts a TargetDecisionTree to the client-only Deco tree interfaces.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
public class TargetDecisionTreeWrapper implements IDecoTree<TargetDecisionTreeNode>
{
	private final TargetDecisionTree tree;
	private final Consumer<TargetDecisionTreeNode> onSelected;
	private final Map<TargetDecisionTreeNode, WrapperNode> wrappers = new LinkedHashMap<>();
	private List<IDecoTreeNode<TargetDecisionTreeNode>> allNodes = Collections.emptyList();
	private List<IDecoTreeNode<TargetDecisionTreeNode>> rootNodes = Collections.emptyList();
	@Nullable
	private WrapperNode hoveredNode;
	@Nullable
	@Getter
	private TargetDecisionTreeNode selected;

	public TargetDecisionTreeWrapper(@Nonnull TargetDecisionTree tree, @Nonnull Consumer<TargetDecisionTreeNode> onSelected)
	{
		this.tree = tree;
		this.onSelected = onSelected;
		refreshStructure();
	}

	/**
	 * Rebuilds wrappers after nodes are added or removed.
	 */
	public void refreshStructure()
	{
		TargetDecisionTreeNode previousSelection = selected;
		wrappers.clear();
		for(TargetDecisionTreeNode node : tree.getAllNodes())
			wrappers.put(node, new WrapperNode(node));
		for(Map.Entry<TargetDecisionTreeNode, WrapperNode> entry : wrappers.entrySet())
		{
			TargetDecisionTreeNode parent = entry.getKey().getParent();
			if(parent!=null)
				entry.getValue().dependencies.add(wrappers.get(parent));
		}
		allNodes = Collections.unmodifiableList(new ArrayList<>(wrappers.values()));
		rootNodes = Collections.singletonList(wrappers.get(tree.getRoot()));
		selected = wrappers.containsKey(previousSelection)?previousSelection: tree.getRoot();
		hoveredNode = null;
	}

	/**
	 * Selects a logical node without emulating a mouse click.
	 */
	public void select(@Nullable TargetDecisionTreeNode node)
	{
		if(node!=null&&wrappers.containsKey(node))
		{
			selected = node;
			onSelected.accept(node);
		}
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<TargetDecisionTreeNode>> getAllNodes()
	{
		return allNodes;
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<TargetDecisionTreeNode>> getRootNodes()
	{
		return rootNodes;
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<TargetDecisionTreeNode>> getActiveNodes()
	{
		WrapperNode wrapper = wrappers.get(selected);
		return wrapper==null?Collections.emptyList(): Collections.singletonList(wrapper);
	}

	@Nullable
	@Override
	public IDecoTreeNode<TargetDecisionTreeNode> getHoveredNode()
	{
		return hoveredNode;
	}

	@Override
	public void setHoveredNode(@Nullable IDecoTreeNode<TargetDecisionTreeNode> node)
	{
		hoveredNode = (WrapperNode)node;
	}

	@Override
	public void onNodeClicked(@Nonnull IDecoTreeNode<TargetDecisionTreeNode> node)
	{
		select(node.getUserData());
	}

	@RequiredArgsConstructor
	private class WrapperNode implements IDecoTreeNode<TargetDecisionTreeNode>
	{
		private final TargetDecisionTreeNode original;
		@Getter
		private final Set<IDecoTreeNode<TargetDecisionTreeNode>> dependencies = new LinkedHashSet<>();
		@Getter
		private final Set<IDecoTreeNode<TargetDecisionTreeNode>> lockOuts = Collections.emptySet();
		@Getter
		private int x;
		@Getter
		private int y;

		@Nonnull
		@Override
		public String getId()
		{
			return original.getId();
		}

		@Override
		public void setPosition(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		@Override
		public int getWidth()
		{
			return TargetDecisionTreeNodeRenderer.NODE_WIDTH;
		}

		@Override
		public int getHeight()
		{
			return TargetDecisionTreeNodeRenderer.NODE_HEIGHT;
		}

		@Override
		public boolean isActive()
		{
			return selected==original;
		}

		@Override
		public boolean isAvailable(@Nonnull Collection<IDecoTreeNode<TargetDecisionTreeNode>> activeNodes)
		{
			return true;
		}

		@Nonnull
		@Override
		public TargetDecisionTreeNode getUserData()
		{
			return original;
		}
	}
}
