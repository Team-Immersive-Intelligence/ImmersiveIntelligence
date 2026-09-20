package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Matches one of the existing Emplacement entity categories.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
@Getter
public class EntityTypeTargetFilter implements TargetFilter
{
	private TargetEntityType targetType;

	public EntityTypeTargetFilter()
	{
		this(TargetEntityType.MOB);
	}

	public EntityTypeTargetFilter(TargetEntityType type)
	{
		this.targetType = type==null?TargetEntityType.MOB: type;
	}

	public void setTargetType(TargetEntityType type)
	{
		this.targetType = type==null?TargetEntityType.MOB: type;
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return targetType.matches(context.getEntityTypeMask());
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.ENTITY_TYPE;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return targetType.getName();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("value", targetType.getName()).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		TargetEntityType parsed = TargetEntityType.fromName(nbt.getString("value"));
		if(parsed==null)
			throw new IllegalArgumentException("Invalid entity type target filter");
		this.targetType = parsed;
	}
}
