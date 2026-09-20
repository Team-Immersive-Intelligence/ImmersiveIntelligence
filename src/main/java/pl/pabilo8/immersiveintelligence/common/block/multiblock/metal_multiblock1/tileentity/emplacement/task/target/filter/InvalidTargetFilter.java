package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;

import javax.annotation.Nonnull;

/**
 * Rejects every candidate when target configuration is invalid.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class InvalidTargetFilter implements TargetFilter
{
	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return false;
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.INVALID;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return "";
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
	}
}
