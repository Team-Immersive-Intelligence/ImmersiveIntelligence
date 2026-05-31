package pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term;

import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.04.2026
 */
public class RelationTerm implements DiplomaticAgreement.ITerm
{
	private final DiplomaticStatus relation;

	RelationTerm(DiplomaticStatus relation)
	{
		this.relation = relation;
	}

	@Override
	public void apply(DiplomaticAgreement agreement)
	{
		agreement.setActiveRelationChange(relation);
	}

	@Override
	public EasyNBT toNBT()
	{
		return EasyNBT.newNBT()
				.withString("type", "relation")
				.withEnum("relation", relation);
	}

	public static RelationTerm fromNBT(EasyNBT tag)
	{
		return new RelationTerm(tag.getEnum("relation", DiplomaticStatus.class));
	}
}
