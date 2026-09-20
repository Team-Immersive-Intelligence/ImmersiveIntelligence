package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetingLimits;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Matches the visible entity name without case sensitivity.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
@Getter
public class NameTargetFilter implements TargetFilter
{
	@Nonnull
	private String name;

	public NameTargetFilter()
	{
		this("");
	}

	public NameTargetFilter(String name)
	{
		this.name = TargetingLimits.clampString(name);
	}

	public void setName(String name)
	{
		this.name = TargetingLimits.clampString(name);
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return !name.isEmpty()&&name.equalsIgnoreCase(context.getName());
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.NAME;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return name;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("value", name).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		String value = nbt.getString("value");
		if(value.length() > TargetingLimits.MAX_STRING_LENGTH)
			throw new IllegalArgumentException("Invalid name target filter");
		this.name = value;
	}
}
