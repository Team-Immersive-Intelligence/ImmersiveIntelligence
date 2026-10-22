package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Keeps the flight path of a projectile loaded on the server.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.10.2026
 */
public abstract class EntityAmmoChunkLoadingProjectile extends EntityAmmoProjectile
{
	private Ticket chunkLoadingTicket;
	private int nextTicketRequest;
	private int loadedMinX = Integer.MAX_VALUE;
	private int loadedMaxX;
	private int loadedMinZ;
	private int loadedMaxZ;

	protected EntityAmmoChunkLoadingProjectile(World world)
	{
		super(world);
	}

	@Override
	public void onAddedToWorld()
	{
		super.onAddedToWorld();
		if(!world.isRemote&&!isDead)
		{
			requestChunkLoadingTicket();
			forceCurrentChunk();
		}
	}

	@Override
	public void onUpdate()
	{
		if(isDead)
			return;
		if(!world.isRemote)
			requestChunkLoadingTicket();
		super.onUpdate();
	}

	@Override
	protected void doProjectileMotion()
	{
		if(isDead)
			return;
		if(!world.isRemote&&chunkLoadingTicket!=null)
			loadFlightChunks();
		super.doProjectileMotion();
		if(!world.isRemote&&!isDead)
			forceCurrentChunk();
	}

	/**
	 * Uses a saved ticket for this projectile.
	 */
	public final void restoreChunkLoadingTicket(Ticket ticket)
	{
		if(world.isRemote||isDead||ticket.world!=world||ticket.getEntity()!=this)
		{
			ForgeChunkManager.releaseTicket(ticket);
			return;
		}
		if(chunkLoadingTicket!=ticket)
			releaseChunkLoadingTicket();
		chunkLoadingTicket = ticket;
		loadedMinX = Integer.MAX_VALUE;
		forceCurrentChunk();
	}

	@Override
	public void setDead()
	{
		releaseChunkLoadingTicket();
		super.setDead();
	}

	@Override
	public void onRemovedFromWorld()
	{
		releaseChunkLoadingTicket();
		super.onRemovedFromWorld();
	}

	private void requestChunkLoadingTicket()
	{
		if(chunkLoadingTicket!=null||ticksExisted < nextTicketRequest)
			return;
		chunkLoadingTicket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, world, ForgeChunkManager.Type.ENTITY);
		if(chunkLoadingTicket!=null)
		{
			chunkLoadingTicket.bindEntity(this);
			loadedMinX = Integer.MAX_VALUE;
		}
		else
			nextTicketRequest = ticksExisted+20;
	}

	private void loadFlightChunks()
	{
		//Include the tracer bounds and the world entity search margin.
		double margin = width*0.5+0.125+World.MAX_ENTITY_RADIUS;
		int minX = MathHelper.floor(Math.min(posX, posX+motionX)-margin)>>4;
		int maxX = MathHelper.floor(Math.max(posX, posX+motionX)+margin)>>4;
		int minZ = MathHelper.floor(Math.min(posZ, posZ+motionZ)-margin)>>4;
		int maxZ = MathHelper.floor(Math.max(posZ, posZ+motionZ)+margin)>>4;
		if(minX==loadedMinX&&maxX==loadedMaxX&&minZ==loadedMinZ&&maxZ==loadedMaxZ)
			return;

		//Retain the current and destination chunks first when the ticket limit is small.
		Set<ChunkPos> chunks = new LinkedHashSet<>();
		chunks.add(new ChunkPos(MathHelper.floor(posX)>>4, MathHelper.floor(posZ)>>4));
		chunks.add(new ChunkPos(MathHelper.floor(posX+motionX)>>4, MathHelper.floor(posZ+motionZ)>>4));
		int depth = chunkLoadingTicket.getChunkListDepth();
		Set<ChunkPos> retained = new LinkedHashSet<>();
		for(int x = minX; x <= maxX; x++)
			for(int z = minZ; z <= maxZ; z++)
				chunks.add(new ChunkPos(x, z));
		for(ChunkPos chunk : chunks)
			if(depth <= 0||retained.size() < depth)
				retained.add(chunk);

		//Release the previous path before forcing the next path.
		Set<ChunkPos> forced = chunkLoadingTicket.getChunkList();
		for(ChunkPos chunk : forced)
			if(!retained.contains(chunk))
				ForgeChunkManager.unforceChunk(chunkLoadingTicket, chunk);
		for(ChunkPos chunk : retained)
			if(!forced.contains(chunk))
				ForgeChunkManager.forceChunk(chunkLoadingTicket, chunk);

		//Forcing a chunk does not load it synchronously. Load all trace chunks before movement.
		for(ChunkPos chunk : chunks)
			world.getChunkFromChunkCoords(chunk.x, chunk.z);
		loadedMinX = retained.size()==chunks.size()?minX: Integer.MAX_VALUE;
		loadedMaxX = maxX;
		loadedMinZ = minZ;
		loadedMaxZ = maxZ;
	}

	private void forceCurrentChunk()
	{
		if(chunkLoadingTicket==null)
			return;
		ChunkPos current = new ChunkPos(MathHelper.floor(posX)>>4, MathHelper.floor(posZ)>>4);
		Set<ChunkPos> forced = chunkLoadingTicket.getChunkList();
		if(forced.contains(current))
			return;
		int depth = chunkLoadingTicket.getChunkListDepth();
		if(depth > 0&&forced.size() >= depth)
			ForgeChunkManager.unforceChunk(chunkLoadingTicket, forced.iterator().next());
		ForgeChunkManager.forceChunk(chunkLoadingTicket, current);
		world.getChunkFromChunkCoords(current.x, current.z);
		loadedMinX = Integer.MAX_VALUE;
	}

	private void releaseChunkLoadingTicket()
	{
		if(chunkLoadingTicket==null)
			return;
		ForgeChunkManager.releaseTicket(chunkLoadingTicket);
		chunkLoadingTicket = null;
		loadedMinX = Integer.MAX_VALUE;
	}
}
