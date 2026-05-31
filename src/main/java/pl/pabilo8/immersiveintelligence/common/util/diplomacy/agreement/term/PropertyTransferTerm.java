package pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term;

import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.04.2026
 */
public class PropertyTransferTerm implements DiplomaticAgreement.ITerm
{
	private final UUID propertyId;

	PropertyTransferTerm(UUID propertyId)
	{
		this.propertyId = propertyId;
	}

	@Override
	public void apply(DiplomaticAgreement agreement)
	{
		agreement.addTransferredProperty(propertyId);
		// The actual transfer is executed by DiplomacyUtils when the agreement is activated
	}

	@Override
	public EasyNBT toNBT()
	{
		return EasyNBT.newNBT()
				.withString("type", "transfer")
				.withString("propertyId", propertyId.toString());
	}

	public static PropertyTransferTerm fromNBT(EasyNBT tag)
	{
		return new PropertyTransferTerm(UUID.fromString(tag.getString("propertyId")));
	}
}
