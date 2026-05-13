package pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term;

import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.04.2026
 */
public class PermissionGrantTerm implements DiplomaticAgreement.ITerm
{
	private final PermissionCategory category;
	private final boolean allowed;

	PermissionGrantTerm(PermissionCategory category, boolean allowed)
	{
		this.category = category;
		this.allowed = allowed;
	}

	@Override
	public void apply(DiplomaticAgreement agreement)
	{
		agreement.addGrantedPermission(category, allowed);
	}

	@Override
	public EasyNBT toNBT()
	{
		return EasyNBT.newNBT()
				.withString("type", "permission")
				.withEnum("category", category)
				.withBoolean("allowed", allowed);
	}

	public static PermissionGrantTerm fromNBT(EasyNBT tag)
	{
		return new PermissionGrantTerm(
				tag.getEnum("category", PermissionCategory.class),
				tag.getBoolean("allowed")
		);
	}
}
