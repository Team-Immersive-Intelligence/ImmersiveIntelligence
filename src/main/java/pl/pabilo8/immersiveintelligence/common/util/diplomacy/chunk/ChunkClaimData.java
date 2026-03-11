package pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk;

import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * Data about a chunk claim
 *
 * @author Pabilo8
 */
public class ChunkClaimData
{
	private final BlockPos claimBlockPos;
	private final long claimTime;

	public ChunkClaimData(IOwnableProperty owner)
	{
		this(owner.getIIPos(), owner.getTicksExisted());
	}

	public ChunkClaimData(BlockPos claimBlockPos, long claimTime)
	{
		this.claimBlockPos = claimBlockPos;
		this.claimTime = claimTime;
	}

	public BlockPos getClaimBlockPos()
	{
		return claimBlockPos;
	}

	public long getClaimTime()
	{
		return claimTime;
	}

	public EasyNBT toNBT()
	{
		return EasyNBT.newNBT()
				.withPos("pos", claimBlockPos)
				.withLong("time", claimTime);
	}

	public static ChunkClaimData fromNBT(EasyNBT nbt)
	{
		return new ChunkClaimData(
				nbt.getPos("pos"),
				nbt.getLong("time")
		);
	}
}
