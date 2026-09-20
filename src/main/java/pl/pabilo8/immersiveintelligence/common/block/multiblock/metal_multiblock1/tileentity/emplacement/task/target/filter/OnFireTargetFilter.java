package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;

import javax.annotation.Nonnull;

/**
 * Matches the expected burning state.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class OnFireTargetFilter extends BooleanTargetFilter
{
	public OnFireTargetFilter()
	{
	}

	public OnFireTargetFilter(boolean expected)
	{
		super(expected);
	}

	@Override
	protected boolean getActual(@Nonnull TargetEvaluationContext context)
	{
		return context.isOnFire();
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.ON_FIRE;
	}
}
