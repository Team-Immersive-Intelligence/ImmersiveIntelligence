package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import lombok.AllArgsConstructor;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

/**
 * Defines who can interact with a worn backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.11.2026
 */
@AllArgsConstructor
public enum BackpackAccess implements ISerializableEnum
{
	PUBLIC(IIColor.fromPackedRGB(0x52617d)),
	ALLIED(IIColor.fromPackedRGB(0x417243)),
	FACTION(IIColor.fromPackedRGB(0x8e633f)),
	PRIVATE(IIColor.fromPackedRGB(0x6d3a3a));

	final IIColor color;

	public boolean canAccess(EntityLivingBase wearer, EntityLivingBase visitor)
	{
		if(wearer==visitor||this==PUBLIC)
			return true;
		if(this==PRIVATE)
			return false;

		//Check for faction relation
		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(wearer.world.isRemote);
		if(!diplomacy.diplomacyInitialized)
			return false;
		OwnerIdentity identity = diplomacy.getOwnerIdentityForEntity(wearer);
		if(identity.isInvalid()||DiplomacyHandler.NEUTRAL_UUID.equals(identity.getUUID()))
			return false;
		return identity.getRelationTowards(visitor).atLeast(this==ALLIED?DiplomaticStatus.ALLIED: DiplomaticStatus.MEMBER);
	}
}
