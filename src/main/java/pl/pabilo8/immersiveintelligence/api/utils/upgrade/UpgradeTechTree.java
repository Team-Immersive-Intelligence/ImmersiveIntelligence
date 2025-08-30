package pl.pabilo8.immersiveintelligence.api.utils.upgrade;

import pl.pabilo8.immersiveintelligence.api.utils.upgrade.UpgradeUtils.UpgradeTier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2025
 */
public class UpgradeTechTree
{
	private static final Map<Class<? extends IUpgradableDevice>, UpgradeTechTree> UPGRADE_TECH_TREES = new HashMap<>();
	private final List<UpgradeTreeNode> nodes = new ArrayList<>();

	public static UpgradeTechTree getTreeFor(IUpgradableDevice machine)
	{
		return getTreeFor(machine.getClass());
	}

	public static UpgradeTechTree getTreeFor(Class<? extends IUpgradableDevice> klass)
	{
		return UPGRADE_TECH_TREES.computeIfAbsent(klass, c -> new UpgradeTechTree());
	}

	public UpgradeTreeNode addUpgrade(Upgrade upgrade, UpgradeTier tier)
	{
		UpgradeTreeNode node = new UpgradeTreeNode(upgrade, tier);
		nodes.add(node);
		return node;
	}

	public void addDependency(Upgrade from, Upgrade to)
	{

	}

	public void addLockOut(Upgrade between)
	{

	}

	/**
	 * @param upgrade upgrade to check for
	 * @return whether the upgrade is present in this tech tree
	 */
	public boolean isUpgradePresent(Upgrade upgrade)
	{
		return nodes.stream().map(UpgradeTreeNode::getUpgrade).anyMatch(upgrade::equals);
	}

	/**
	 * Checks if the given machine upgrade can be installed, based on already installed upgrades.
	 *
	 * @param installed list of already installed upgrades
	 * @param upgrade   upgrade to check for
	 * @return if the upgrade can be installed
	 */
	public boolean isUpgradeAvailable(@Nonnull List<Upgrade> installed, @Nullable Upgrade upgrade)
	{
		//Upgrade invalid
		if(upgrade==null)
			return false;
		//Upgrade already installed
		if(installed.contains(upgrade))
			return false;

		//Find the node
		UpgradeTreeNode node = nodes.stream()
				.filter(n -> n.upgrade==upgrade)
				.findFirst().orElse(null);
		if(node==null)
			return false;

		//Check if all dependencies are installed
		if(node.dependencies.stream().anyMatch(dependency -> !installed.contains(dependency.upgrade)))
			return false;
		//Check if no upgrades locking this one are installed
		return node.locksOut.stream().noneMatch(dependency -> installed.contains(dependency.upgrade));
	}

	/**
	 * Represents a node in the upgrade tech tree of a machine. Can be connected to other nodes via a dependency or lock relationship.
	 */
	public static class UpgradeTreeNode
	{
		private final Upgrade upgrade;
		private final UpgradeTier tier;
		private final Set<UpgradeTreeNode> dependencies = new HashSet<>();
		private final Set<UpgradeTreeNode> locksOut = new HashSet<>();

		public UpgradeTreeNode(Upgrade upgrade, UpgradeTier tier)
		{
			this.upgrade = upgrade;
			this.tier = tier;
		}

		public Upgrade getUpgrade()
		{
			return upgrade;
		}

		public UpgradeTier getTier()
		{
			return tier;
		}

		public Set<UpgradeTreeNode> getDependencies()
		{
			return dependencies;
		}

		public Set<UpgradeTreeNode> getLocksOut()
		{
			return locksOut;
		}
	}
}

