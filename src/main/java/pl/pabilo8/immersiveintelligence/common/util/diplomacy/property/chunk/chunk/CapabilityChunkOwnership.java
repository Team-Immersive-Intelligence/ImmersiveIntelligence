package pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provider for chunk ownership capability
 *
 * @author Pabilo8
 */
public class CapabilityChunkOwnership implements ICapabilitySerializable<NBTBase>
{
	@CapabilityInject(IChunkOwnership.class)
	public static Capability<IChunkOwnership> CHUNK_OWNERSHIP_CAP = null;
	private final IChunkOwnership instance;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IChunkOwnership.class, new ChunkOwnershipStorage(), () -> null);
	}

	public CapabilityChunkOwnership(IChunkOwnership instance)
	{
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability==CHUNK_OWNERSHIP_CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability==CHUNK_OWNERSHIP_CAP?CHUNK_OWNERSHIP_CAP.cast(instance): null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return CHUNK_OWNERSHIP_CAP.getStorage().writeNBT(CHUNK_OWNERSHIP_CAP, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		CHUNK_OWNERSHIP_CAP.getStorage().readNBT(CHUNK_OWNERSHIP_CAP, instance, null, nbt);
	}
}
