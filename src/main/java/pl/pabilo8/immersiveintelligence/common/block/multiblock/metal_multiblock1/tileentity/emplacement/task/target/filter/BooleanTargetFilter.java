package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Implements target conditions with an expected boolean state.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
@Getter
public abstract class BooleanTargetFilter implements TargetFilter
{
	private boolean expected;

	protected BooleanTargetFilter()
	{
		this(true);
	}

	protected BooleanTargetFilter(boolean expected)
	{
		this.expected = expected;
	}

	public void setExpected(boolean expected)
	{
		this.expected = expected;
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return getActual(context)==expected;
	}

	protected abstract boolean getActual(@Nonnull TargetEvaluationContext context);

	@Nonnull
	@Override
	public String getSummary()
	{
		return Boolean.toString(expected);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withBoolean("value", expected).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("value", EasyNBT.TAG_BYTE))
			throw new IllegalArgumentException("Invalid boolean target filter");
		this.expected = nbt.getBoolean("value");
	}
}
