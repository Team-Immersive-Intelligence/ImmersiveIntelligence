package pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term;

import pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.RelationTerm;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.04.2026
 */
public class TermFactory
{
	static DiplomaticAgreement.ITerm fromNBT(EasyNBT tag)
	{
		String termType = tag.getString("type");
		switch(termType)
		{
			case "relation":
				return RelationTerm.fromNBT(tag);
			case "permission":
				return PermissionGrantTerm.fromNBT(tag);
			case "transfer":
				return ChunkTransferTerm.fromNBT(tag);
			default:
				throw new IllegalArgumentException("Unknown term type: "+termType);
		}
	}

}
