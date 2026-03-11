package pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;

/**
 * Storage for chunk ownership capability
 *
 * @author Pabilo8
 */
public class ChunkOwnershipStorage implements Capability.IStorage<IChunkOwnership>
{
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IChunkOwnership> capability, IChunkOwnership instance, EnumFacing side)
	{
		if(instance instanceof ChunkOwnership)
			return ((ChunkOwnership)instance).serializeNBT().unwrap();
		return null;
	}

	@Override
	public void readNBT(Capability<IChunkOwnership> capability, IChunkOwnership instance, EnumFacing side, NBTBase nbt)
	{
		if(instance instanceof ChunkOwnership&&nbt instanceof NBTTagCompound)
			((ChunkOwnership)instance).deserializeNBT(EasyNBT.wrapNBT((NBTTagCompound)nbt));
	}
}
