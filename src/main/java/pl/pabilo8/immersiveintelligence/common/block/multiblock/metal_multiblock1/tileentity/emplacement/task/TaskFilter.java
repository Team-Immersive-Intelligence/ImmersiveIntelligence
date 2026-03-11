package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class TaskFilter
{
	public EnumTargetType type;
	public boolean negation;
	public String filter;

	public TaskFilter(EnumTargetType type, boolean negation, String filter)
	{
		this.type = type;
		this.negation = negation;
		this.filter = filter;
	}

	public TaskFilter(NBTTagCompound tag)
	{
		this(EnumTargetType.valueOf(tag.getString("type").toUpperCase()), tag.getBoolean("negation"), tag.getString("filter"));
	}
}
