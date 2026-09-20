package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.AnyTargetFilter;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

/**
 * Stores one named Emplacement target-tree preset.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class TargetDecisionTreePreset implements INBTSerializable<NBTTagCompound>
{
	@Getter
	private String id = "";
	@Getter
	private String name = "";
	@Getter
	private boolean builtin;
	@Getter
	private TargetDecisionTree tree = new TargetDecisionTree();
	private boolean valid;

	public TargetDecisionTreePreset()
	{
	}

	public TargetDecisionTreePreset(@Nonnull String id, @Nonnull String name, boolean builtin,
	                                @Nonnull TargetDecisionTree tree)
	{
		this.id = Objects.requireNonNull(id, "id");
		this.name = TargetingLimits.clampString(Objects.requireNonNull(name, "name"));
		this.builtin = builtin;
		this.tree = Objects.requireNonNull(tree, "tree");
		this.valid = !id.isEmpty()&&id.length() <= TargetingLimits.MAX_STRING_LENGTH
				&&!this.name.isEmpty()&&tree.isValid();
	}

	/**
	 * Creates a custom preset with an independent Any tree.
	 */
	public static TargetDecisionTreePreset createCustom(String name)
	{
		return new TargetDecisionTreePreset(UUID.randomUUID().toString(), TargetingLimits.clampString(name), false,
				new TargetDecisionTree(new TargetDecisionTreeNode(new AnyTargetFilter(), 0)));
	}

	public void setName(String name)
	{
		if(!builtin)
		{
			this.name = TargetingLimits.clampString(name);
			this.valid = !id.isEmpty()&&!this.name.isEmpty()&&tree.isValid();
		}
	}

	public boolean isValid()
	{
		return valid&&!id.isEmpty()&&id.length() <= TargetingLimits.MAX_STRING_LENGTH
				&&!name.isEmpty()&&name.length() <= TargetingLimits.MAX_STRING_LENGTH&&tree.isValid();
	}

	public void setTree(@Nonnull TargetDecisionTree tree)
	{
		this.tree = Objects.requireNonNull(tree, "tree");
		this.valid = !id.isEmpty()&&!name.isEmpty()&&tree.isValid();
	}

	/**
	 * Creates a custom duplicate with a new stable identifier.
	 */
	public TargetDecisionTreePreset duplicate(String duplicateName)
	{
		return new TargetDecisionTreePreset(UUID.randomUUID().toString(), duplicateName, false, tree.copy());
	}

	public TargetDecisionTreePreset copy()
	{
		TargetDecisionTreePreset copy = new TargetDecisionTreePreset();
		copy.deserializeNBT(serializeNBT());
		return copy;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withString("id", id)
				.withString("name", name)
				.withBoolean("builtin", builtin)
				.withSerializable("tree", tree)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.valid = false;
		String readId = nbt.getString("id");
		String readName = nbt.getString("name");
		if(readId.isEmpty()||readName.isEmpty()||readId.length() > TargetingLimits.MAX_STRING_LENGTH
				||readName.length() > TargetingLimits.MAX_STRING_LENGTH||!nbt.hasKey("tree", EasyNBT.TAG_COMPOUND))
			return;
		TargetDecisionTree readTree = new TargetDecisionTree();
		readTree.deserializeNBT(nbt.getCompoundTag("tree"));
		if(!readTree.isValid())
			return;
		this.id = readId;
		this.name = readName;
		this.builtin = nbt.getBoolean("builtin");
		this.tree = readTree;
		this.valid = true;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
