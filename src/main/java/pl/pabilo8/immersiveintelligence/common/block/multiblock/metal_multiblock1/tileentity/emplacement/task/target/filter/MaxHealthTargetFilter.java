package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;

import javax.annotation.Nonnull;

/**
 * Matches maximum entity health or maximum vehicle durability.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
public class MaxHealthTargetFilter extends NumericTargetFilter
{
	public MaxHealthTargetFilter()
	{
		this(NumericComparison.GREATER_OR_EQUAL, 0);
	}

	public MaxHealthTargetFilter(NumericComparison comparison, double value)
	{
		super(comparison, value);
	}

	@Override
	protected double getEntityValue(@Nonnull TargetEvaluationContext context)
	{
		return context.getMaxHealth();
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.MAX_HEALTH;
	}
}
