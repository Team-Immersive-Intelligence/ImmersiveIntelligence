package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.CustomMapData;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Scanner for detecting specific block types.
 */
public class BlockTypeScanner extends MapScanner
{
	private final List<Predicate<IBlockState>> blockFilters = new ArrayList<>();
	private final List<Predicate<TileEntity>> tileFilters = new ArrayList<>();
	private boolean drawBoundingBox = false;

	public BlockTypeScanner(String layerName)
	{
		super(layerName);
	}

	/**
	 * Add a block filter - blocks that match will be marked.
	 */
	public BlockTypeScanner withBlockFilter(Predicate<IBlockState> filter)
	{
		blockFilters.add(filter);
		return this;
	}

	/**
	 * Filter by block class or interface.
	 */
	public BlockTypeScanner withBlockClass(Class<?> blockClass)
	{
		return withBlockFilter(state -> blockClass.isAssignableFrom(state.getBlock().getClass()));
	}

	/**
	 * Filter by block instance.
	 */
	public BlockTypeScanner withBlock(Block block)
	{
		return withBlockFilter(state -> state.getBlock()==block);
	}

	/**
	 * Filter by tile entity instance.
	 */
	public BlockTypeScanner withTileEntityFilter(Predicate<TileEntity> filter)
	{
		tileFilters.add(filter);
		return this;
	}

	/**
	 * Filter by multiblock master block - only the master block of the multiblock will be marked.
	 */
	public <T extends TileEntityMultiblockIIBase<T>> BlockTypeScanner withMultiblockFilter(Class<T> klass)
	{
		return withTileEntityFilter(te -> {
			if(klass.isInstance(te))
			{
				TileEntityMultiblockIIBase<?> mb = (TileEntityMultiblockIIBase<?>)te;
				//Check if it's the top block over master()
				return mb.offset[0]==0&&mb.offset[2]==0;
			}
			return false;
		});
	}

	/**
	 * Draw bounding boxes around detected areas instead of markers.
	 */
	public BlockTypeScanner withBoundingBox(boolean draw)
	{
		this.drawBoundingBox = draw;
		return this;
	}

	@Override
	protected void scanArea(DecoMapDisplay mapDisplay, World world, CustomMapData mapData,
							MapLayerBuilder layer, int minX, int maxX, int minZ, int maxZ)
	{
		if(blockFilters.isEmpty()&&tileFilters.isEmpty())
			return;

		//Scan each chunk in the area
		int minChunkX = minX>>4;
		int maxChunkX = maxX>>4;
		int minChunkZ = minZ>>4;
		int maxChunkZ = maxZ>>4;

		for(int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++)
			for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++)
			{
				Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
				if(!chunk.isLoaded()) continue;

				//Scan blocks in this chunk
				for(int x = 0; x < 16; x++)
					for(int z = 0; z < 16; z++)
					{
						int worldX = (chunkX<<4)+x;
						int worldZ = (chunkZ<<4)+z;

						//Skip if outside scan area
						if(worldX < minX||worldX > maxX||worldZ < minZ||worldZ > maxZ)
							continue;

						//Get height at this position
						int topY = world.getHeight(worldX, worldZ);

						//Scan from top to bottom (or vice versa depending on needs)
						for(int y = topY; y > 0; y--)
						{
							BlockPos pos = new BlockPos(worldX, y, worldZ);
							IBlockState state = world.getBlockState(pos);

							//Check if block matches any filter
							boolean matches = false;
							for(Predicate<IBlockState> filter : blockFilters)
								if(filter.test(state))
								{
									matches = true;
									break;
								}

							if(!matches&&!tileFilters.isEmpty())
							{
								TileEntity tileEntity = world.getTileEntity(pos);
								if(tileEntity!=null)
									for(Predicate<TileEntity> filter : tileFilters)
										if(filter.test(tileEntity))
										{
											matches = true;
											break;
										}
							}

							if(matches)
							{
								//Add marker at block position
								//Skip further down if we found a match (optional)
								//break;
								if(drawBoundingBox)
								{
									//Find the extent of this block group
									AxisAlignedBB bounds = findBlockGroupBounds(world, pos, state);
									if(bounds!=null)
									{
										layer.addRectangle(
												(int)bounds.minX, (int)bounds.minZ,
												(int)bounds.maxX, (int)bounds.maxZ,
												rectangleColor
										);
										//Skip the rest of this group
										y = (int)bounds.minY-1;
									}
								}
								else if(markerTexture!=null)
									layer.withSprite(ResLoc.of(markerTexture), true, worldX, worldZ, markerSize, markerColor, markerRotation);
							}
						}
					}
			}
	}

	@Nullable
	private AxisAlignedBB findBlockGroupBounds(World world, BlockPos startPos, IBlockState targetState)
	{
		//Simple flood fill to find connected blocks of the same type
		List<BlockPos> visited = new ArrayList<>();
		List<BlockPos> toVisit = new ArrayList<>();
		toVisit.add(startPos);

		int minX = startPos.getX(), maxX = startPos.getX();
		int minY = startPos.getY(), maxY = startPos.getY();
		int minZ = startPos.getZ(), maxZ = startPos.getZ();

		while(!toVisit.isEmpty())
		{
			BlockPos pos = toVisit.remove(0);
			if(visited.contains(pos)) continue;

			visited.add(pos);

			//Update bounds
			minX = Math.min(minX, pos.getX());
			maxX = Math.max(maxX, pos.getX());
			minY = Math.min(minY, pos.getY());
			maxY = Math.max(maxY, pos.getY());
			minZ = Math.min(minZ, pos.getZ());
			maxZ = Math.max(maxZ, pos.getZ());

			//Check adjacent blocks (4 directions + up/down for 3D groups)
			for(int dx = -1; dx <= 1; dx++)
				for(int dy = -1; dy <= 1; dy++)
					for(int dz = -1; dz <= 1; dz++)
					{
						//Limit to cardinal directions for surface detection
						if(Math.abs(dx)+Math.abs(dy)+Math.abs(dz)!=1) continue;

						BlockPos neighbor = pos.add(dx, dy, dz);
						if(!visited.contains(neighbor)&&!toVisit.contains(neighbor))
							if(world.getBlockState(neighbor)==targetState)
								toVisit.add(neighbor);
					}
		}

		//Only return bounds for groups
		if(visited.size() > 1)
			return new AxisAlignedBB(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
		return null;
	}
}
