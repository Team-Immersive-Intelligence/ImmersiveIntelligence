package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;

/**
 * Compares numeric entity properties against configured thresholds.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
@Getter
@RequiredArgsConstructor
public enum NumericComparison implements ISerializableEnum
{
	LESS_THAN("<"),
	LESS_OR_EQUAL("<="),
	EQUAL("="),
	GREATER_OR_EQUAL(">="),
	GREATER_THAN(">");

	private final String symbol;

	public boolean test(double actual, double expected)
	{
		switch(this)
		{
			case LESS_THAN:
				return actual < expected;
			case LESS_OR_EQUAL:
				return actual <= expected;
			case EQUAL:
				return Double.compare(actual, expected)==0;
			case GREATER_OR_EQUAL:
				return actual >= expected;
			case GREATER_THAN:
				return actual > expected;
			default:
				return false;
		}
	}

	@Nullable
	public static NumericComparison fromName(String name)
	{
		if(name==null)
			return null;
		for(NumericComparison comparison : values())
			if(comparison.getName().equals(name))
				return comparison;
		return null;
	}
}
