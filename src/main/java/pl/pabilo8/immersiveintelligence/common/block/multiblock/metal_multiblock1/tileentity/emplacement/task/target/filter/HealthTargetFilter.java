package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;

import javax.annotation.Nonnull;

/**
 * Matches current entity health or vehicle durability.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
public class HealthTargetFilter extends NumericTargetFilter
{
	public HealthTargetFilter()
	{
		this(NumericComparison.GREATER_OR_EQUAL, 0);
	}

	public HealthTargetFilter(NumericComparison comparison, double value)
	{
		super(comparison, value);
	}

	@Override
	protected double getEntityValue(@Nonnull TargetEvaluationContext context)
	{
		return context.getHealth();
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.HEALTH;
	}
}
