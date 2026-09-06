package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetingLimits;

import javax.annotation.Nonnull;

/**
 * Matches squared distance from the Emplacement targeting origin.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class DistanceTargetFilter extends NumericTargetFilter
{
	public DistanceTargetFilter()
	{
		this(NumericComparison.LESS_OR_EQUAL, 32);
	}

	public DistanceTargetFilter(NumericComparison comparison, double value)
	{
		super(comparison, value);
	}

	@Override
	protected double getEntityValue(@Nonnull TargetEvaluationContext context)
	{
		return context.getDistanceSq();
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		double threshold = getValue();
		return getComparison().test(context.getDistanceSq(), threshold*threshold);
	}

	@Override
	protected double normalizeValue(double value)
	{
		return TargetingLimits.clampDistance(value);
	}

	@Override
	protected boolean isValidValue(double value)
	{
		return super.isValidValue(value)&&value >= 0&&value <= TargetingLimits.MAX_DISTANCE;
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.DISTANCE;
	}
}
