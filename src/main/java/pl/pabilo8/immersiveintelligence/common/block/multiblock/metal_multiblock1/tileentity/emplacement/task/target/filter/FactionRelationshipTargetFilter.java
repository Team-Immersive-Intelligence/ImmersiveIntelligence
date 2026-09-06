package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Matches the exact diplomatic relation towards a living target.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class FactionRelationshipTargetFilter implements TargetFilter
{
	@Getter
	private DiplomaticStatus relationship;

	public FactionRelationshipTargetFilter()
	{
		this(DiplomaticStatus.ENEMY);
	}

	public FactionRelationshipTargetFilter(DiplomaticStatus relationship)
	{
		this.relationship = relationship==null?DiplomaticStatus.ENEMY: relationship;
	}

	public void setRelationship(DiplomaticStatus relationship)
	{
		this.relationship = relationship==null?DiplomaticStatus.NEUTRAL: relationship;
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		return context.getDiplomaticStatus()==relationship;
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.FACTION_RELATIONSHIP;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return relationship.getName();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("value", relationship.getName()).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		String value = nbt.getString("value");
		for(DiplomaticStatus status : DiplomaticStatus.values())
			if(status.getName().equals(value))
			{
				this.relationship = status;
				return;
			}
		throw new IllegalArgumentException("Invalid faction relationship target filter");
	}
}
