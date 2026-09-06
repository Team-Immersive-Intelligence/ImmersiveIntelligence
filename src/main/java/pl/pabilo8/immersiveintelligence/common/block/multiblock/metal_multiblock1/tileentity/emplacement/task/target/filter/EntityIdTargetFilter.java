package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetingLimits;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matches an entity registry identifier.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
public class EntityIdTargetFilter implements TargetFilter
{
	@Getter
	private String entityId;
	@Nullable
	private ResourceLocation parsedId;

	public EntityIdTargetFilter()
	{
		this("");
	}

	public EntityIdTargetFilter(String entityId)
	{
		setEntityId(entityId);
	}

	public void setEntityId(String entityId)
	{
		this.entityId = TargetingLimits.clampString(entityId==null?"": entityId.trim());
		try
		{
			parsedId = this.entityId.isEmpty()?null: new ResourceLocation(this.entityId);
		} catch(RuntimeException ignored)
		{
			parsedId = null;
		}
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return parsedId!=null&&parsedId.equals(context.getRegistryId());
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.ENTITY_ID;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return entityId;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("value", entityId).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		String value = nbt.getString("value");
		if(value.length() > TargetingLimits.MAX_STRING_LENGTH)
			throw new IllegalArgumentException("Invalid entity ID target filter");
		setEntityId(value);
		if(!value.isEmpty()&&parsedId==null)
			throw new IllegalArgumentException("Invalid entity ID target filter");
	}
}
