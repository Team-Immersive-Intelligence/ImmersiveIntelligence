package pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk;

import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Capability for chunk ownership data
 *
 * @author Pabilo8
 * @since 09.09.2025
 */
public interface IChunkOwnership
{
	/**
	 * Get the owner of this chunk.
	 *
	 * @implNote In case there is no owner, return {@link DiplomacyHandler#NEUTRAL}
	 */
	@Nonnull
	OwnerIdentity getOwner();

	/**
	 * Set the owner of this chunk
	 */
	void setOwner(@Nullable OwnerIdentity owner);

	/**
	 * Get the claiming block position (if any)
	 */
	@Nullable
	ChunkClaimData getClaimData();

	/**
	 * Set the claiming block position
	 */
	void setClaimData(@Nullable ChunkClaimData claimData);
}
