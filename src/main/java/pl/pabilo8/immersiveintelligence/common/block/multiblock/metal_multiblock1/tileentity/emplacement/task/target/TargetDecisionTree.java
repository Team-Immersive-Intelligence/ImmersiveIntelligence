package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.InvalidTargetFilter;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetFilter;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Scores entities by summing weights of all matching branches in a weighted condition tree.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2026
 * @since 24.08.2026
 */
public class TargetDecisionTree implements INBTSerializable<NBTTagCompound>
{
	public static final long NO_MATCH = Long.MIN_VALUE;

	@Getter
	private TargetDecisionTreeNode root;
	@Getter
	private boolean valid;
	private CompiledNode[] compiled = new CompiledNode[0];
	private List<TargetDecisionTreeNode> allNodes = Collections.emptyList();

	/**
	 * Creates a fail-closed tree for NBT reconstruction.
	 */
	public TargetDecisionTree()
	{
		setInvalid();
	}

	/**
	 * Creates a decision tree from a detached root node.
	 */
	public TargetDecisionTree(@Nonnull TargetDecisionTreeNode root)
	{
		if(root==null||root.getParent()!=null)
			throw new IllegalArgumentException("root must be detached");
		this.root = root;
		this.valid = root.getFilter().getType().isUserSelectable();
		compile();
	}

	/**
	 * Creates and attaches a child node.
	 */
	@Nonnull
	public TargetDecisionTreeNode addChild(@Nonnull TargetDecisionTreeNode parent, @Nonnull TargetFilter filter, int weight)
	{
		TargetDecisionTreeNode child = new TargetDecisionTreeNode(filter, weight);
		if(!addChild(parent, child))
			throw new IllegalArgumentException("parent cannot accept another target node");
		return child;
	}

	/**
	 * Attaches a detached subtree below a node in this tree.
	 */
	public boolean addChild(@Nonnull TargetDecisionTreeNode parent, @Nonnull TargetDecisionTreeNode child)
	{
		if(parent==null||child==null||child==root||child.getParent()!=null||!contains(parent)||contains(child)||contains(child, parent))
			return false;
		if(allNodes.size()+countNodes(child) > TargetingLimits.MAX_NODES
				||parent.getChildren().size() >= TargetingLimits.MAX_DIRECT_CHILDREN
				||getDepth(parent)+getHeight(child) >= TargetingLimits.MAX_TREE_DEPTH)
			return false;
		parent.attachChild(child);
		compile();
		return true;
	}

	/**
	 * Removes a node and its complete subtree. The root cannot be removed.
	 */
	public boolean removeSubtree(@Nonnull TargetDecisionTreeNode node)
	{
		if(node==null||node==root||!contains(node)||node.getParent()==null)
			return false;
		node.getParent().detachChild(node);
		compile();
		return true;
	}

	/**
	 * @return all nodes in depth-first order
	 */
	@Nonnull
	public List<TargetDecisionTreeNode> getAllNodes()
	{
		return allNodes;
	}

	/**
	 * Scores one prepared evaluation context.
	 */
	public long score(@Nonnull TargetEvaluationContext context)
	{
		if(!valid)
			return NO_MATCH;
		long score = 0;
		for(int i = 0; i < compiled.length; )
		{
			CompiledNode entry = compiled[i];
			if(entry.node.getFilter().matches(context))
			{
				score += entry.node.getWeight();
				i++;
			}
			else
			{
				if(i==0)
					return NO_MATCH;
				i = entry.subtreeEnd;
			}
		}
		return score;
	}

	/**
	 * Scores an entity without owner context. Intended for cold utility and test paths.
	 */
	public long score(@Nonnull Entity entity)
	{
		TargetEvaluationContext context = new TargetEvaluationContext()
				.reset(entity.world, null, Vec3d.ZERO, entity);
		return score(context);
	}

	/**
	 * Creates an independent NBT round-trip copy.
	 */
	public TargetDecisionTree copy()
	{
		TargetDecisionTree copy = new TargetDecisionTree();
		copy.deserializeNBT(serializeNBT());
		return copy;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound result = new NBTTagCompound();
		result.setTag("root", root.serializeNBT());
		return result;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		try
		{
			if(nbt==null||!nbt.hasKey("root", EasyNBT.TAG_COMPOUND))
				throw new IllegalArgumentException("Target tree has no root");
			this.root = TargetDecisionTreeNode.readValidated(nbt.getCompoundTag("root"));
			this.valid = true;
			compile();
		} catch(RuntimeException ignored)
		{
			setInvalid();
		}
	}

	private boolean contains(TargetDecisionTreeNode node)
	{
		return allNodes.contains(node);
	}

	private boolean contains(TargetDecisionTreeNode subtree, TargetDecisionTreeNode searched)
	{
		if(subtree==searched)
			return true;
		for(TargetDecisionTreeNode child : subtree.getChildren())
			if(contains(child, searched))
				return true;
		return false;
	}

	private int countNodes(TargetDecisionTreeNode node)
	{
		int result = 1;
		for(TargetDecisionTreeNode child : node.getChildren())
			result += countNodes(child);
		return result;
	}

	private int getDepth(TargetDecisionTreeNode node)
	{
		int depth = 0;
		while(node.getParent()!=null)
		{
			depth++;
			node = node.getParent();
		}
		return depth;
	}

	private int getHeight(TargetDecisionTreeNode node)
	{
		int height = 1;
		for(TargetDecisionTreeNode child : node.getChildren())
			height = Math.max(height, 1+getHeight(child));
		return height;
	}

	private void setInvalid()
	{
		this.root = new TargetDecisionTreeNode(new InvalidTargetFilter(), 0);
		this.valid = false;
		compile();
	}

	private void compile()
	{
		List<CompiledNode> entries = new ArrayList<>();
		List<TargetDecisionTreeNode> nodes = new ArrayList<>();
		compile(root, entries, nodes);
		compiled = entries.toArray(new CompiledNode[0]);
		allNodes = Collections.unmodifiableList(nodes);
	}

	private void compile(TargetDecisionTreeNode node, List<CompiledNode> entries, List<TargetDecisionTreeNode> nodes)
	{
		int index = entries.size();
		entries.add(new CompiledNode(node, -1));
		nodes.add(node);
		for(TargetDecisionTreeNode child : node.getChildren())
			compile(child, entries, nodes);
		entries.get(index).subtreeEnd = entries.size();
	}

	@AllArgsConstructor
	private static class CompiledNode
	{
		private final TargetDecisionTreeNode node;
		private int subtreeEnd;
	}
}
