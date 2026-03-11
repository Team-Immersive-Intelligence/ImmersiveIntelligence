package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.CapabilityChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.IChunkOwnership;

/**
 * Send by server to update {@link CapabilityChunkOwnership a chunk's terrain faction ownership Capability}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
public class MessageIIRequestChunkClaimData extends IIMessage
{
	private int chunkX, chunkZ;

	public MessageIIRequestChunkClaimData()
	{
	}

	public MessageIIRequestChunkClaimData(Chunk chunk)
	{
		this.chunkX = chunk.x;
		this.chunkZ = chunk.z;
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		Chunk chunk = world.getChunkProvider().getLoadedChunk(chunkX, chunkZ);
		if(chunk==null)
		{
			IILogger.error("MessageIIRequestChunkClaimData: Chunk at "+new ChunkPos(chunkX, chunkZ)+" is not loaded!");
			return;
		}

		if(!chunk.hasCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null))
		{
			IILogger.error("MessageIIRequestChunkClaimData: Chunk at "+new ChunkPos(chunkX, chunkZ)+" has no ChunkOwnership capability!");
			return;
		}

		IChunkOwnership cap = chunk.getCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null);
		assert cap!=null;

		//Send a reply
		IIPacketHandler.sendToClient(handler.player, new MessageIIChunkClaimData(world, chunk.getPos().getBlock(8, 8, 8),
				cap.getOwner(), cap.getClaimData()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.chunkX = buf.readInt();
		this.chunkZ = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.chunkX);
		buf.writeByte(this.chunkZ);
	}
}
