package pl.pabilo8.immersiveintelligence.common.util.diplomacy;


import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IEntityProof;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.common.util.IWorldPosProvider;

import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public interface IOwnableProperty extends IEntityProof, IWorldPosProvider
{
	IOwnableProperty master();

	OwnerIdentity getOwnerIdentity();

	UUID getUUID();

	void setOwnerIdentity(OwnerIdentity ownerIdentity);

	long getTicksExisted();

	default int getChunkOwnershipRadius()
	{
		return 0;
	}

	@Override
	default boolean canEntityDestroy(Entity entity)
	{
		if(!(entity instanceof EntityLivingBase))
			return false;
		OwnerIdentity ownerIdentity = this.getOwnerIdentity();
		if(ownerIdentity!=null)
			return !ownerIdentity.isPermitted(((EntityLivingBase)entity), PermissionCategory.BREAKING_STRUCTURES);
		return false;
	}
}
