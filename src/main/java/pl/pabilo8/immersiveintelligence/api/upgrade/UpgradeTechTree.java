package pl.pabilo8.immersiveintelligence.api.upgrade;

import lombok.Getter;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores upgrade relationships and preview models for an upgradable device.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 29.09.2026
 * @ii-approved 0.3.1
 * @since 29.08.2025
 */
public class UpgradeTechTree
{
	private static final Map<Class<? extends IUpgradableDevice>, UpgradeTechTree> UPGRADE_TECH_TREES = new HashMap<>();
	private final List<UpgradeTreeNode> nodes = new ArrayList<>();

	@Nullable
	@SideOnly(Side.CLIENT)
	private AMTModel model;

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
		{
			Set<UpgradeTreeNode> dependencies = new HashSet<>();
			fromNode.collectDependencies(dependencies);
			if(fromNode==toNode||dependencies.contains(toNode))
				IILogger.warn("[Upgrade System] Could not add dependency from "+from.getName()+" to "+to.getName()+" because it creates a cycle");
			else
				toNode.dependencies.add(fromNode);
		}
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

	/**
	 * Sets the base model for the upgrade preview.
	 *
	 * @param model base preview model
	 * @return this tech tree
	 */
	@SideOnly(Side.CLIENT)
	public UpgradeTechTree withBaseModel(@Nonnull AMTModel model)
	{
		if(this.model!=null&&this.model!=model)
			this.model.disposeOf();
		this.model = model;
		return this;
	}

	/**
	 * Creates and sets the base model for the upgrade preview.
	 *
	 * @param modelLocation base preview model location
	 * @return this tech tree
	 */
	@SideOnly(Side.CLIENT)
	public UpgradeTechTree withBaseModelLocation(@Nonnull ResLoc modelLocation)
	{
		return withBaseModel(new AMTModel(DefaultVertexFormats.ITEM, modelLocation));
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

	private Set<UpgradeTreeNode> getAllDependentNodes(Collection<UpgradeTreeNode> roots)
	{
		Set<UpgradeTreeNode> resultNodes = new LinkedHashSet<>(roots);
		Deque<UpgradeTreeNode> queue = new ArrayDeque<>(resultNodes);
		while(!queue.isEmpty())
		{
			UpgradeTreeNode current = queue.removeFirst();
			for(UpgradeTreeNode candidate : nodes)
				if(candidate.dependencies.contains(current)&&resultNodes.add(candidate))
					queue.addLast(candidate);
		}
		return resultNodes;
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

		//Include direct lockouts + all their dependent (child) nodes
		Set<UpgradeTreeNode> resultNodes = getAllDependentNodes(node.locksOut);
		resultNodes.remove(node);
		return resultNodes.stream().map(UpgradeTreeNode::getUpgrade).distinct().collect(Collectors.toList());
	}

	/**
	 * @param upgrade upgrade to check for
	 * @return all upgrades required to install the given upgrade
	 */
	public List<Upgrade> getAllParents(@Nonnull Upgrade upgrade)
	{
		UpgradeTreeNode node = getUpgradeNodeFor(upgrade);
		if(node==null)
			return Collections.emptyList();

		Set<UpgradeTreeNode> resultNodes = new LinkedHashSet<>();
		resultNodes.add(node);
		node.collectDependencies(resultNodes);
		resultNodes.remove(node);
		return resultNodes.stream().map(UpgradeTreeNode::getUpgrade).distinct().collect(Collectors.toList());
	}

	/**
	 * @param upgrade upgrade to check for
	 * @return all upgrades that can be installed after the given upgrade is installed
	 */
	public List<Upgrade> getAllChildren(@Nonnull Upgrade upgrade)
	{
		UpgradeTreeNode node = getUpgradeNodeFor(upgrade);
		if(node==null)
			return Collections.emptyList();

		Set<UpgradeTreeNode> resultNodes = getAllDependentNodes(Collections.singleton(node));
		resultNodes.remove(node);
		return resultNodes.stream().map(UpgradeTreeNode::getUpgrade).distinct().collect(Collectors.toList());
	}

	/**
	 * @return all upgrades in this tech tree
	 */
	public List<UpgradeTreeNode> getAllUpgrades()
	{
		return Collections.unmodifiableList(nodes);
	}

	/**
	 * @return all base level upgrades in this tech tree (upgrades without dependencies)
	 */
	public List<UpgradeTreeNode> getAllBaseUpgrades()
	{
		return nodes.stream().filter(nodes -> nodes.dependencies.isEmpty()).collect(Collectors.toList());
	}

	/**
	 * @return the 3D model to render in the upgrade GUI, or null for no model
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	public AMTModel getModel()
	{
		return model;
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
		@Getter
		private final Upgrade upgrade;
		@Getter
		private final UpgradeTier tier;
		private final Set<UpgradeTreeNode> dependencies = new LinkedHashSet<>();
		private final Set<UpgradeTreeNode> locksOut = new LinkedHashSet<>();
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

		/**
		 * @return nodes that must be installed before this node
		 */
		public Set<UpgradeTreeNode> getDependencies()
		{
			return Collections.unmodifiableSet(dependencies);
		}

		/**
		 * @return nodes that cannot be installed together with this node
		 */
		public Set<UpgradeTreeNode> getLocksOut()
		{
			return Collections.unmodifiableSet(locksOut);
		}

		private void collectDependencies(Set<UpgradeTreeNode> result)
		{
			for(UpgradeTreeNode dependency : dependencies)
				if(result.add(dependency))
					dependency.collectDependencies(result);
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
