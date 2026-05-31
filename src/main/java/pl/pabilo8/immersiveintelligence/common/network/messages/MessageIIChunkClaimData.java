package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.CapabilityChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.ChunkClaimData;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.IChunkOwnership;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

/**
 * Send by server to update {@link CapabilityChunkOwnership a chunk's terrain faction ownership Capability}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
public class MessageIIChunkClaimData extends IIMessage implements IPositionBoundMessage
{
	private World world;
	private BlockPos pos;
	@Nullable
	private UUID ownerIdentity;
	@Nullable
	private ChunkClaimData claimData;

	public MessageIIChunkClaimData()
	{
	}

	@ParametersAreNonnullByDefault
	public MessageIIChunkClaimData(World world, BlockPos pos, OwnerIdentity ownerIdentity)
	{
		this(world, pos, ownerIdentity, null);
	}

	@ParametersAreNonnullByDefault
	public MessageIIChunkClaimData(World world, BlockPos pos, ChunkClaimData claimData)
	{
		this(world, pos, null, claimData);
	}

	@ParametersAreNonnullByDefault
	public MessageIIChunkClaimData(World world, BlockPos pos, @Nullable OwnerIdentity ownerIdentity, @Nullable ChunkClaimData claimData)
	{
		this.world = world;
		this.pos = pos;
		this.ownerIdentity = ownerIdentity==null?null: ownerIdentity.getUUID();
		this.claimData = claimData;
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		//Get chunk
		ChunkPos cp = new ChunkPos(pos);
		Chunk chunk = world.getChunkProvider().getLoadedChunk(cp.x, cp.z);
		if(chunk==null)
			return;

		//Get capability
		if(!chunk.hasCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null))
			return;
		IChunkOwnership cap = chunk.getCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null);
		assert cap!=null;
		//Set fields
		if(ownerIdentity!=null)
			cap.setOwner(DiplomacyHandler.getInstance(true).getIdentityByUUID(ownerIdentity));
		if(claimData!=null)
			cap.setClaimData(claimData);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = readPos(buf);
		byte mutex = buf.readByte();
		this.ownerIdentity = (mutex&1)!=0?readUUID(buf): null;
		this.claimData = (mutex&2)!=0?ChunkClaimData.fromNBT(readEasyNBT(buf)): null;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writePos(buf, this.pos);
		byte mutex = (byte)((ownerIdentity!=null?1: 0)+(claimData!=null?2: 0));
		buf.writeByte(mutex);
		if(ownerIdentity!=null)
			writeUUID(buf, ownerIdentity);
		if(claimData!=null)
			writeEasyNBT(buf, claimData.toNBT());
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Vec3d getPosition()
	{
		return new Vec3d(pos);
	}
}
