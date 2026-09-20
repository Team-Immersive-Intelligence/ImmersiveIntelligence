package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.DefaultTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree.UpgradeTreeNode;

/**
 * Wraps an UpgradeTechTree for Deco tree rendering without rebuilding node collections each frame.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 09.12.2025
 */
public abstract class UpgradeTechTreeWrapper implements IDecoTree<Upgrade>
{
	private final IUpgradableDevice device;
	private final Map<UpgradeTreeNode, UpgradeTreeNodeWrapper> wrapperMap = new LinkedHashMap<>();
	private final List<IDecoTreeNode<Upgrade>> allNodes;
	private final List<IDecoTreeNode<Upgrade>> rootNodes;
	@Nullable
	private UpgradeTreeNodeWrapper hoveredNode;

	public UpgradeTechTreeWrapper(@Nonnull UpgradeTechTree techTree, @Nonnull IUpgradableDevice device)
	{
		this.device = device;
		for(UpgradeTreeNode node : techTree.getAllUpgrades())
			wrapperMap.put(node, new UpgradeTreeNodeWrapper(node));

		for(Map.Entry<UpgradeTreeNode, UpgradeTreeNodeWrapper> entry : wrapperMap.entrySet())
		{
			for(UpgradeTreeNode dependency : entry.getKey().getDependencies())
				entry.getValue().dependencies.add(wrapperMap.get(dependency));
			for(UpgradeTreeNode lockOut : entry.getKey().getLocksOut())
				entry.getValue().lockOuts.add(wrapperMap.get(lockOut));
		}

		allNodes = Collections.unmodifiableList(new ArrayList<>(wrapperMap.values()));
		List<IDecoTreeNode<Upgrade>> roots = new ArrayList<>();
		for(Map.Entry<UpgradeTreeNode, UpgradeTreeNodeWrapper> entry : wrapperMap.entrySet())
			if(entry.getKey().getDependencies().isEmpty())
				roots.add(entry.getValue());
		rootNodes = Collections.unmodifiableList(roots);
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<Upgrade>> getAllNodes()
	{
		return allNodes;
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<Upgrade>> getRootNodes()
	{
		return rootNodes;
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<Upgrade>> getActiveNodes()
	{
		List<IDecoTreeNode<Upgrade>> active = new ArrayList<>();
		for(IDecoTreeNode<Upgrade> node : allNodes)
			if(node.isActive())
				active.add(node);
		return active;
	}

	@Nullable
	@Override
	public IDecoTreeNode<Upgrade> getHoveredNode()
	{
		return hoveredNode;
	}

	@Override
	public void setHoveredNode(@Nullable IDecoTreeNode<Upgrade> node)
	{
		hoveredNode = (UpgradeTreeNodeWrapper)node;
	}

	@RequiredArgsConstructor
	private class UpgradeTreeNodeWrapper implements IDecoTreeNode<Upgrade>
	{
		private final UpgradeTreeNode original;
		@Getter
		private final Set<IDecoTreeNode<Upgrade>> dependencies = new LinkedHashSet<>();
		@Getter
		private final Set<IDecoTreeNode<Upgrade>> lockOuts = new LinkedHashSet<>();
		@Getter
		private int x;
		@Getter
		private int y;

		@Nonnull
		@Override
		public String getId()
		{
			return original.getUpgrade().getName();
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
			return DefaultTreeNodeRenderer.NODE_WIDTH;
		}

		@Override
		public int getHeight()
		{
			return DefaultTreeNodeRenderer.NODE_HEIGHT;
		}

		@Override
		public boolean isActive()
		{
			return device.isUpgradeInstalled(original.getUpgrade());
		}

		@Override
		public boolean isAvailable(@Nonnull Collection<IDecoTreeNode<Upgrade>> activeNodes)
		{
			for(IDecoTreeNode<Upgrade> dependency : dependencies)
				if(!activeNodes.contains(dependency))
					return false;
			for(IDecoTreeNode<Upgrade> lockOut : lockOuts)
				if(activeNodes.contains(lockOut))
					return false;
			return true;
		}

		@Nonnull
		@Override
		public Upgrade getUserData()
		{
			return original.getUpgrade();
		}
	}
}
