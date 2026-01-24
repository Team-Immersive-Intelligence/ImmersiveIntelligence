package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade;

import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.DefaultTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree.UpgradeTreeNode;

/**
 * Wraps the server-side UpgradeTechTree for display in DecoTreeDisplay.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public abstract class UpgradeTechTreeWrapper implements IDecoTree<Upgrade>
{
	private final UpgradeTechTree techTree;
	private final IUpgradableDevice device;
	private final Map<UpgradeTreeNode, UpgradeTreeNodeWrapper> wrapperMap = new HashMap<>();
	@Nullable
	private UpgradeTreeNodeWrapper hoveredNode = null;

	public UpgradeTechTreeWrapper(@Nonnull UpgradeTechTree techTree, @Nonnull IUpgradableDevice device)
	{
		this.techTree = techTree;
		this.device = device;

		//Create wrapper for each node
		for(UpgradeTreeNode node : techTree.getAllUpgrades())
			wrapperMap.put(node, new UpgradeTreeNodeWrapper(node));

		//Setup dependencies and lockouts in wrappers
		for(Map.Entry<UpgradeTreeNode, UpgradeTreeNodeWrapper> entry : wrapperMap.entrySet())
		{
			UpgradeTreeNode original = entry.getKey();
			UpgradeTreeNodeWrapper wrapper = entry.getValue();
			//Convert dependencies
			for(UpgradeTreeNode dep : original.getDependencies())
				wrapper.dependencies.add(wrapperMap.get(dep));
			//Convert lockouts
			for(UpgradeTreeNode lock : original.getLocksOut())
				wrapper.lockOuts.add(wrapperMap.get(lock));
		}
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<Upgrade>> getAllNodes()
	{
		return new ArrayList<>(wrapperMap.values());
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<Upgrade>> getRootNodes()
	{
		return wrapperMap.entrySet().stream()
				.filter(entry -> entry.getKey().getDependencies().isEmpty())
				.map(Map.Entry::getValue).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode<Upgrade>> getActiveNodes()
	{
		List<Upgrade> installedUpgrades = device.getAllInstalledUpgrades();
		return wrapperMap.entrySet().stream()
				.filter(entry -> installedUpgrades.contains(entry.getKey().getUpgrade()))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
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
		this.hoveredNode = (UpgradeTreeNodeWrapper)node;
	}

	private class UpgradeTreeNodeWrapper implements IDecoTreeNode<Upgrade>
	{
		private final UpgradeTreeNode original;
		final Set<IDecoTreeNode<Upgrade>> dependencies = new HashSet<>();
		final Set<IDecoTreeNode<Upgrade>> lockOuts = new HashSet<>();
		private int x = 0;
		private int y = 0;

		public UpgradeTreeNodeWrapper(UpgradeTreeNode original)
		{
			this.original = original;
		}

		@Nonnull
		@Override
		public String getId()
		{
			return original.getUpgrade().getName();
		}

		@Nonnull
		@Override
		public Set<IDecoTreeNode<Upgrade>> getDependencies()
		{
			return dependencies;
		}

		@Nonnull
		@Override
		public Set<IDecoTreeNode<Upgrade>> getLockOuts()
		{
			return lockOuts;
		}

		@Override
		public int getX()
		{
			return x;
		}

		@Override
		public int getY()
		{
			return y;
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
			return device.getAllInstalledUpgrades().contains(original.getUpgrade()); //Fixed method name
		}

		@Override
		public boolean isAvailable(@Nonnull Collection<IDecoTreeNode<Upgrade>> activeNodes)
		{
			//Check if all dependencies are active
			for(IDecoTreeNode<Upgrade> dep : dependencies)
				if(!activeNodes.contains(dep))
					return false;

			//Check if no lockouts are active
			for(IDecoTreeNode<Upgrade> lock : lockOuts)
				if(activeNodes.contains(lock))
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
