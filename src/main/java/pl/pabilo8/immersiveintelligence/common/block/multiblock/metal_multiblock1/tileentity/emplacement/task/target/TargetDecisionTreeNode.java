package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.InvalidTargetFilter;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetFilter;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetFilterType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Stores one weighted condition and its child conditions in a target decision tree.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2026
 * @since 24.08.2026
 */
public class TargetDecisionTreeNode implements INBTSerializable<NBTTagCompound>
{
	@Getter
	private String id;
	@Getter
	private int weight;
	@Getter
	@Nonnull
	private TargetFilter filter;
	@Nullable
	@Getter
	private TargetDecisionTreeNode parent;
	private final List<TargetDecisionTreeNode> children = new ArrayList<>();

	/**
	 * Creates a fail-closed detached node for NBT reconstruction.
	 */
	public TargetDecisionTreeNode()
	{
		this(UUID.randomUUID().toString(), new InvalidTargetFilter(), 0);
	}

	/**
	 * Creates a node with an automatically generated identifier.
	 */
	public TargetDecisionTreeNode(@Nonnull TargetFilter filter, int weight)
	{
		this(UUID.randomUUID().toString(), filter, weight);
	}

	/**
	 * Creates a node with a stable identifier.
	 */
	public TargetDecisionTreeNode(@Nonnull String id, @Nonnull TargetFilter filter, int weight)
	{
		this.id = Objects.requireNonNull(id, "id");
		this.filter = Objects.requireNonNull(filter, "filter");
		this.weight = TargetingLimits.clampWeight(weight);
	}

	public void setWeight(int weight)
	{
		this.weight = TargetingLimits.clampWeight(weight);
	}

	/**
	 * Replaces the condition without changing tree structure.
	 */
	public void setFilter(@Nonnull TargetFilter filter)
	{
		this.filter = Objects.requireNonNull(filter, "filter");
	}

	/**
	 * @return an immutable view of child nodes
	 */
	@Nonnull
	public List<TargetDecisionTreeNode> getChildren()
	{
		return Collections.unmodifiableList(children);
	}

	void attachChild(@Nonnull TargetDecisionTreeNode child)
	{
		children.add(child);
		child.parent = this;
	}

	void detachChild(@Nonnull TargetDecisionTreeNode child)
	{
		if(children.remove(child))
			child.parent = null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound filterTag = EasyNBT.newNBT()
				.withString("type", filter.getType().getId())
				.withTag("data", filter.serializeNBT())
				.unwrap();
		NBTTagList childTags = new NBTTagList();
		for(TargetDecisionTreeNode child : children)
			childTags.appendTag(child.serializeNBT());
		return EasyNBT.newNBT()
				.withString("id", id)
				.withInt("weight", weight)
				.withTag("filter", filterTag)
				.withTag("children", childTags)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		try
		{
			copyFrom(readValidated(nbt));
		} catch(RuntimeException ignored)
		{
			setInvalid();
		}
	}

	static TargetDecisionTreeNode readValidated(NBTTagCompound nbt)
	{
		return readValidated(nbt, 0, new ReadState());
	}

	private static TargetDecisionTreeNode readValidated(NBTTagCompound nbt, int depth, ReadState state)
	{
		if(nbt==null||depth >= TargetingLimits.MAX_TREE_DEPTH||++state.nodes > TargetingLimits.MAX_NODES)
			throw new IllegalArgumentException("Target tree exceeds its structural limits");

		String id = nbt.getString("id");
		if(id.isEmpty()||id.length() > TargetingLimits.MAX_NODE_ID_LENGTH||!state.ids.add(id))
			throw new IllegalArgumentException("Invalid or duplicate target node ID");
		int weight = nbt.getInteger("weight");
		if(weight < -TargetingLimits.MAX_WEIGHT||weight > TargetingLimits.MAX_WEIGHT)
			throw new IllegalArgumentException("Target node weight is outside its limit");
		if(!nbt.hasKey("filter", EasyNBT.TAG_COMPOUND))
			throw new IllegalArgumentException("Target node has no filter");

		NBTTagCompound filterTag = nbt.getCompoundTag("filter");
		TargetFilterType type = TargetFilterType.fromId(filterTag.getString("type"));
		if(type==null||type==TargetFilterType.INVALID||!filterTag.hasKey("data", EasyNBT.TAG_COMPOUND))
			throw new IllegalArgumentException("Unknown target filter type");
		TargetFilter filter = type.createDefault();
		filter.deserializeNBT(filterTag.getCompoundTag("data"));

		NBTTagList childTags = nbt.hasKey("children", EasyNBT.TAG_LIST)?
				nbt.getTagList("children", EasyNBT.TAG_COMPOUND): new NBTTagList();
		if(childTags.tagCount() > TargetingLimits.MAX_DIRECT_CHILDREN)
			throw new IllegalArgumentException("Target node has too many direct children");

		TargetDecisionTreeNode result = new TargetDecisionTreeNode(id, filter, weight);
		for(int i = 0; i < childTags.tagCount(); i++)
			result.attachChild(readValidated(childTags.getCompoundTagAt(i), depth+1, state));
		return result;
	}

	private void copyFrom(TargetDecisionTreeNode source)
	{
		this.id = source.id;
		this.weight = source.weight;
		this.filter = source.filter;
		this.parent = null;
		this.children.clear();
		for(TargetDecisionTreeNode child : source.children)
			attachChild(child);
	}

	private void setInvalid()
	{
		this.id = UUID.randomUUID().toString();
		this.weight = 0;
		this.filter = new InvalidTargetFilter();
		this.parent = null;
		this.children.clear();
	}

	@Override
	public String toString()
	{
		String summary = filter.getSummary();
		return filter.getType().getId()+(summary.isEmpty()?"": ": "+summary)+" ["+(weight >= 0?"+": "")+weight+"]";
	}

	private static class ReadState
	{
		private int nodes;
		private final Set<String> ids = new HashSet<>();
	}
}
