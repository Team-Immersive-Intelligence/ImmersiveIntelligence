package pl.pabilo8.immersiveintelligence.common.util.diplomacy.property;


import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IEntityProof;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.common.util.IWorldPosProvider;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

import java.util.UUID;

/**
 * Properties are ownable structures that can lay claim to and load chunks around them.
 *
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

	default int getChunkLoadingRange()
	{
		return 0;
	}

	@Override
	default boolean canEntityDestroy(Entity entity)
	{
		if(!(entity instanceof EntityLivingBase))
			return false;
		IOwnableProperty master = master();
		//Likely a bugged block
		if(master==null)
			return true;
		//Get master block's identity and check if the entity is parmitted
		OwnerIdentity ownerIdentity = master.getOwnerIdentity();
		if(ownerIdentity==null)
			return true;
		return !ownerIdentity.isPermitted(((EntityLivingBase)entity), PermissionCategory.BREAKING_STRUCTURES);
	}

	boolean isValid();
}
