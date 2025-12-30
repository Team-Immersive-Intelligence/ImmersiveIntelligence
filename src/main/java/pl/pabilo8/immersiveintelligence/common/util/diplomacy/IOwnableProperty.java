package pl.pabilo8.immersiveintelligence.common.util.diplomacy;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public interface IOwnableProperty
{
	IOwnableProperty master();

	OwnerIdentity getOwnerIdentity();

	UUID getUUID();

	void setOwnerIdentity(OwnerIdentity ownerIdentity);

	BlockPos getPos();

	World getWorld();

	long getTicksExisted();

	default int getChunkOwnershipRadius()
	{
		return 0;
	}
}
