package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Matches the candidate's horizontal direction relative to the weapon origin.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 02.10.2026
 */
@Getter
public class RelativeDirectionFilter implements TargetFilter
{
	private EnumFacing direction;

	public RelativeDirectionFilter()
	{
		this(EnumFacing.NORTH);
	}

	public RelativeDirectionFilter(EnumFacing direction)
	{
		setDirection(direction);
	}

	public void setDirection(EnumFacing direction)
	{
		if(direction==null||direction.getAxis()==EnumFacing.Axis.Y)
			throw new IllegalArgumentException("Relative direction must be horizontal");
		this.direction = direction;
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return direction==context.getRelativeDirection();
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.RELATIVE_DIRECTION;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return direction.getName();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("value", direction.getName()).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		setDirection(EnumFacing.byName(nbt.getString("value")));
	}
}
