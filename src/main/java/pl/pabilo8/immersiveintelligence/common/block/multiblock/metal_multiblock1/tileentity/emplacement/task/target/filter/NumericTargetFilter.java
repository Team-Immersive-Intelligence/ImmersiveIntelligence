package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Base class for numeric entity-property conditions.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
@Getter
public abstract class NumericTargetFilter implements TargetFilter
{
	@Nonnull
	private NumericComparison comparison;
	private double value;

	protected NumericTargetFilter(NumericComparison comparison, double value)
	{
		this.comparison = comparison==null?NumericComparison.GREATER_OR_EQUAL: comparison;
		setValue(value);
	}

	public void setComparison(NumericComparison comparison)
	{
		this.comparison = comparison==null?NumericComparison.GREATER_OR_EQUAL: comparison;
	}

	public void setValue(double value)
	{
		this.value = Double.isNaN(value)||Double.isInfinite(value)?0: normalizeValue(value);
	}

	protected double normalizeValue(double value)
	{
		return value;
	}

	protected boolean isValidValue(double value)
	{
		return !Double.isNaN(value)&&!Double.isInfinite(value);
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		double actual = getEntityValue(context);
		return !Double.isNaN(actual)&&!Double.isInfinite(actual)&&comparison.test(actual, value);
	}

	protected abstract double getEntityValue(@Nonnull TargetEvaluationContext context);

	@Nonnull
	@Override
	public String getSummary()
	{
		return comparison.getSymbol()+" "+formatValue(value);
	}

	protected static String formatValue(double value)
	{
		return value==(long)value?Long.toString((long)value): Double.toString(value);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withString("comparison", comparison.getName())
				.withDouble("value", value)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		NumericComparison parsed = NumericComparison.fromName(nbt.getString("comparison"));
		double parsedValue = nbt.getDouble("value");
		if(parsed==null||!isValidValue(parsedValue))
			throw new IllegalArgumentException("Invalid numeric target filter");
		this.comparison = parsed;
		setValue(parsedValue);
	}
}
