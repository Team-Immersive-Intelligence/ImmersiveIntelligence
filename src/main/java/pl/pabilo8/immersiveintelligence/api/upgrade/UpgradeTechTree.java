package pl.pabilo8.immersiveintelligence.api.upgrade;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2025
 */
public class UpgradeTechTree
{
	private static final Map<Class<? extends IUpgradableDevice>, UpgradeTechTree> UPGRADE_TECH_TREES = new HashMap<>();
	private final List<UpgradeTreeNode> nodes = new ArrayList<>();

	@Nullable
	private ResLoc modelLocation = null;

	public static UpgradeTechTree getTreeFor(IUpgradableDevice machine)
	{
		return getTreeFor(machine.getClass());
	}

	public static UpgradeTechTree getTreeFor(Class<? extends IUpgradableDevice> klass)
	{
		return UPGRADE_TECH_TREES.computeIfAbsent(klass, c -> new UpgradeTechTree());
	}

	public UpgradeTechTree reset()
	{
		nodes.clear();
		return this;
	}

	public UpgradeTechTree withUpgrade(Upgrade upgrade, UpgradeTier tier)
	{
		nodes.add(new UpgradeTreeNode(upgrade, tier));
		return this;
	}

	public UpgradeTechTree withDependency(Upgrade from, Upgrade to)
	{
		//Get from node
		UpgradeTreeNode fromNode = getUpgradeNodeFor(from);
		if(fromNode==null)
			IILogger.warn("Could not find upgrade "+from.getName()+" in tech tree to add dependency to "+to.getName());
		//Get to node
		UpgradeTreeNode toNode = getUpgradeNodeFor(to);
		if(toNode==null)
			IILogger.warn("Could not find upgrade "+to.getName()+" in tech tree to add dependency from "+from.getName());

		//Add dependency
		if(toNode!=null&&fromNode!=null)
			toNode.dependencies.add(fromNode);
		return this;
	}

	public UpgradeTechTree withLockOut(Upgrade... between)
	{
		//Get all upgrade nodes
		List<UpgradeTreeNode> lockOutNodes = Arrays.stream(between)
				.map(this::getUpgradeNodeFor)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		//Add lock outs
		for(UpgradeTreeNode thisNode : lockOutNodes)
			for(UpgradeTreeNode otherNode : lockOutNodes)
				if(thisNode!=otherNode)
					thisNode.locksOut.add(otherNode);

		return this;
	}

	public UpgradeTechTree withLockOut(UpgradePurpose purpose)
	{
		//Get all upgrade nodes
		List<UpgradeTreeNode> lockOutNodes = nodes.stream()
				.filter(n -> n.upgrade.getPurpose()==purpose)
				.collect(Collectors.toList());

		//Add lock outs
		for(UpgradeTreeNode thisNode : lockOutNodes)
			for(UpgradeTreeNode otherNode : lockOutNodes)
				if(thisNode!=otherNode)
					thisNode.locksOut.add(otherNode);

		return this;
	}

	public UpgradeTechTree withBaseModelLocation(@Nonnull ResLoc modelLocation)
	{
		this.modelLocation = modelLocation;
		return this;
	}

	public UpgradeTechTree withUpgradeModelLocation(@Nonnull Upgrade upgrade, @Nonnull ResLoc modelLocation)
	{
		UpgradeTreeNode node = getUpgradeNodeFor(upgrade);
		if(node==null)
			IILogger.error("[Upgrade System] Attempting to set model location for upgrade "+upgrade.getLocalizedName()+" which is not present in the tech tree!");
		else
			node.withModelLocation(modelLocation);
		return this;
	}

	private UpgradeTreeNode getUpgradeNodeFor(Upgrade upgrade)
	{
		return nodes.stream().filter(n -> n.upgrade==upgrade).findFirst().orElse(null);
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
	 * @param upgrade upgrade to check for
	 * @return all upgrades incompatible with the given upgrade
	 */
	public List<Upgrade> getAllIncompatibleUpgrades(Upgrade upgrade)
	{
		UpgradeTreeNode node = getUpgradeNodeFor(upgrade);
		if(node==null)
			return Collections.emptyList();
		return node.locksOut.stream().map(n -> n.upgrade).collect(Collectors.toList());
	}

	/**
	 * @param upgrade upgrade to check for
	 * @return all upgrades required to install the given upgrade
	 */
	public List<Upgrade> getAllRequiredUpgrades(UpgradePurpose upgrade)
	{
		return nodes.stream()
				.filter(n -> n.upgrade.getPurpose()==upgrade)
				.flatMap(n -> n.dependencies.stream())
				.map(n -> n.upgrade)
				.distinct()
				.collect(Collectors.toList());
	}

	/**
	 * @return all upgrades in this tech tree
	 */
	public List<UpgradeTreeNode> getAllUpgrades()
	{
		return nodes;
	}

	/**
	 * @return all base level upgrades in this tech tree (upgrades without dependencies)
	 */
	public List<UpgradeTreeNode> getAllBaseUpgrades()
	{
		return nodes.stream().filter(nodes -> nodes.dependencies.isEmpty()).collect(Collectors.toList());
	}

	/**
	 * @return the 3D model location to render in the upgrade GUI, or null for no model
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	public ResLoc getModelLocation()
	{
		return modelLocation;
	}

	/**
	 * @return the 3D model location of an upgrade model
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	public ResLoc getUpgradeModelLocation(Upgrade upgrade)
	{
		UpgradeTreeNode node = getUpgradeNodeFor(upgrade);
		if(node!=null)
			return node.getModelLocation();
		return null;
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
		@Nullable
		private ResLoc modelLocation = null;

		public UpgradeTreeNode(Upgrade upgrade, UpgradeTier tier)
		{
			this.upgrade = upgrade;
			this.tier = tier;
		}

		public UpgradeTreeNode withModelLocation(@Nullable ResLoc modelLocation)
		{
			this.modelLocation = modelLocation;
			return this;
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

		/**
		 * @return the 3D model location to render in the upgrade GUI, or null for no model
		 */
		@Nullable
		@SideOnly(Side.CLIENT)
		public ResLoc getModelLocation()
		{
			return modelLocation;
		}
	}
}

