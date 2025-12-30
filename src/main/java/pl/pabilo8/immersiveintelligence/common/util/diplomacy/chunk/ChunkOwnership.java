package pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk;

import net.minecraft.world.chunk.Chunk;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils.NEUTRAL;
import static pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils.getIdentityByName;

/**
 * Implementation of chunk ownership capability
 *
 * @author Pabilo8
 */
public class ChunkOwnership implements IChunkOwnership
{
	private final Chunk chunk;
	@Nullable
	private OwnerIdentity owner;
	@Nullable
	private ChunkClaimData claimData;

	public ChunkOwnership(Chunk chunk)
	{
		this.chunk = chunk;
	}

	@Override
	@Nonnull
	public OwnerIdentity getOwner()
	{
		return owner==null?NEUTRAL: owner;
	}

	@Override
	public void setOwner(@Nullable OwnerIdentity owner)
	{
		this.owner = owner;
		markDirty();
	}

	@Override
	@Nullable
	public ChunkClaimData getClaimData()
	{
		return claimData;
	}

	@Override
	public void setClaimData(@Nullable ChunkClaimData claimData)
	{
		this.claimData = claimData;
		markDirty();
	}

	public EasyNBT serializeNBT()
	{
		EasyNBT nbt = EasyNBT.newNBT();

		if(owner!=null)
			nbt.withString("owner", owner.getDisplayName());
		if(claimData!=null)
			nbt.withTag("claimData", claimData.toNBT());
		return nbt;
	}

	public void deserializeNBT(EasyNBT nbt)
	{
		if(nbt.hasKey("owner"))
		{
			String ownerName = nbt.getString("owner");
			this.owner = getIdentityByName(ownerName);
		}
		if(nbt.hasKey("claimData"))
			this.claimData = ChunkClaimData.fromNBT(nbt.getEasyCompound("claimData"));

	}

	private void markDirty()
	{
		chunk.markDirty();
	}
}
