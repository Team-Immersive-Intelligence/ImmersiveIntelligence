package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Caches terrain line-of-sight results for voxels inside a bounded three-dimensional region.
 * <p>
 * The matrix resolves cells lazily rather than flood-filling them. Flood filling measures terrain
 * reachability and can travel around corners, while visibility requires a straight line from the
 * observer. Lazy resolution also avoids tracing millions of empty cells when only a small number
 * of them contain possible targets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.09.2026
 * @since 12.09.2026
 */
public class TerrainVisibilityMatrix
{
	private final BitSet resolved = new BitSet();
	private final BitSet visible = new BitSet();
	private Set<BlockPos> ignoredPositions = Collections.emptySet();
	@Nullable
	private World world;
	private Vec3d origin = Vec3d.ZERO;
	private int minX, minY, minZ;
	private int sizeX, sizeY, sizeZ;
	private boolean valid;

	/**
	 * Starts a new visibility generation. Previously resolved cells are discarded.
	 *
	 * @param world  world containing the terrain
	 * @param origin observer position
	 * @param bounds region represented by the matrix
	 */
	public void update(@Nullable World world, @Nullable Vec3d origin, @Nullable AxisAlignedBB bounds)
	{
		update(world, origin, bounds, Collections.emptySet());
	}

	/**
	 * Starts a new visibility generation while treating selected world positions as transparent.
	 * Supplied positions are copied, so mutable position objects can safely be reused by the caller.
	 *
	 * @param world            world containing the terrain
	 * @param origin           observer position
	 * @param bounds           region represented by the matrix
	 * @param ignoredPositions positions which cannot obstruct visibility
	 */
	public void update(@Nullable World world, @Nullable Vec3d origin, @Nullable AxisAlignedBB bounds,
					   @Nullable Collection<? extends BlockPos> ignoredPositions)
	{
		invalidate();
		if(world==null||origin==null||bounds==null)
			return;

		int maxX = MathHelper.floor(bounds.maxX);
		int maxY = MathHelper.floor(bounds.maxY);
		int maxZ = MathHelper.floor(bounds.maxZ);
		this.minX = MathHelper.floor(bounds.minX);
		this.minY = MathHelper.floor(bounds.minY);
		this.minZ = MathHelper.floor(bounds.minZ);
		long width = (long)maxX-minX+1L;
		long height = (long)maxY-minY+1L;
		long depth = (long)maxZ-minZ+1L;
		if(width <= 0||height <= 0||depth <= 0||width > Integer.MAX_VALUE||height > Integer.MAX_VALUE
				||depth > Integer.MAX_VALUE)
			return;
		long area = width*height;
		if(area > Integer.MAX_VALUE||depth > Integer.MAX_VALUE/area)
			return;

		this.world = world;
		this.origin = origin;
		this.sizeX = (int)width;
		this.sizeY = (int)height;
		this.sizeZ = (int)depth;
		this.ignoredPositions = copyPositions(ignoredPositions);
		this.valid = true;
	}

	/**
	 * @return whether this generation represents the supplied observer and bounds
	 */
	public boolean isConfiguredFor(@Nullable World world, @Nullable Vec3d origin, @Nullable AxisAlignedBB bounds)
	{
		if(!valid||this.world!=world||origin==null||bounds==null
				||Double.compare(this.origin.x, origin.x)!=0||Double.compare(this.origin.y, origin.y)!=0
				||Double.compare(this.origin.z, origin.z)!=0)
			return false;
		return minX==MathHelper.floor(bounds.minX)&&minY==MathHelper.floor(bounds.minY)
				&&minZ==MathHelper.floor(bounds.minZ)
				&&minX+sizeX-1==MathHelper.floor(bounds.maxX)
				&&minY+sizeY-1==MathHelper.floor(bounds.maxY)
				&&minZ+sizeZ-1==MathHelper.floor(bounds.maxZ);
	}

	/**
	 * @return whether this generation also uses exactly the supplied transparent positions
	 */
	public boolean isConfiguredFor(@Nullable World world, @Nullable Vec3d origin, @Nullable AxisAlignedBB bounds,
								   @Nullable Collection<? extends BlockPos> ignoredPositions)
	{
		return isConfiguredFor(world, origin, bounds)
				&&this.ignoredPositions.equals(copyPositions(ignoredPositions));
	}

	/**
	 * Returns cached line of sight for the voxel containing {@code point}. The first query for a
	 * voxel performs one opaque-block trace to its centre; subsequent queries are constant-time.
	 */
	public boolean isVisible(@Nullable Vec3d point)
	{
		if(!valid||world==null||point==null)
			return false;
		int x = MathHelper.floor(point.x);
		int y = MathHelper.floor(point.y);
		int z = MathHelper.floor(point.z);
		int index = getIndex(x, y, z);
		if(index < 0)
			return false;
		if(!resolved.get(index))
		{
			resolved.set(index);
			if(hasLineOfSight(new Vec3d(x+0.5d, y+0.5d, z+0.5d)))
				visible.set(index);
		}
		return visible.get(index);
	}

	public void invalidate()
	{
		resolved.clear();
		visible.clear();
		world = null;
		origin = Vec3d.ZERO;
		ignoredPositions = Collections.emptySet();
		sizeX = sizeY = sizeZ = 0;
		valid = false;
	}

	private int getIndex(int x, int y, int z)
	{
		int relativeX = x-minX;
		int relativeY = y-minY;
		int relativeZ = z-minZ;
		if(relativeX < 0||relativeX >= sizeX||relativeY < 0||relativeY >= sizeY
				||relativeZ < 0||relativeZ >= sizeZ)
			return -1;
		return relativeX+sizeX*(relativeY+sizeY*relativeZ);
	}

	/**
	 * Amanatides-Woo voxel traversal. Origin and destination cells are ignored: only intervening
	 * terrain can block sight to an entity occupying the destination voxel.
	 */
	private boolean hasLineOfSight(Vec3d destination)
	{
		if(world==null)
			return false;
		int x = MathHelper.floor(origin.x);
		int y = MathHelper.floor(origin.y);
		int z = MathHelper.floor(origin.z);
		int endX = MathHelper.floor(destination.x);
		int endY = MathHelper.floor(destination.y);
		int endZ = MathHelper.floor(destination.z);
		if(x==endX&&y==endY&&z==endZ)
			return true;

		double dx = destination.x-origin.x;
		double dy = destination.y-origin.y;
		double dz = destination.z-origin.z;
		int stepX = Integer.compare(endX, x);
		int stepY = Integer.compare(endY, y);
		int stepZ = Integer.compare(endZ, z);
		double deltaX = stepX==0?Double.POSITIVE_INFINITY: Math.abs(1d/dx);
		double deltaY = stepY==0?Double.POSITIVE_INFINITY: Math.abs(1d/dy);
		double deltaZ = stepZ==0?Double.POSITIVE_INFINITY: Math.abs(1d/dz);
		double nextX = stepX==0?Double.POSITIVE_INFINITY:
				((stepX > 0?x+1d: x)-origin.x)/dx;
		double nextY = stepY==0?Double.POSITIVE_INFINITY:
				((stepY > 0?y+1d: y)-origin.y)/dy;
		double nextZ = stepZ==0?Double.POSITIVE_INFINITY:
				((stepZ > 0?z+1d: z)-origin.z)/dz;

		while(x!=endX||y!=endY||z!=endZ)
		{
			double next = Math.min(nextX, Math.min(nextY, nextZ));
			if(nextX==next)
			{
				x += stepX;
				nextX += deltaX;
			}
			if(nextY==next)
			{
				y += stepY;
				nextY += deltaY;
			}
			if(nextZ==next)
			{
				z += stepZ;
				nextZ += deltaZ;
			}
			if(x==endX&&y==endY&&z==endZ)
				return true;

			BlockPos pos = new BlockPos(x, y, z);
			if(ignoredPositions.contains(pos))
				continue;
			if(!world.isBlockLoaded(pos))
				return false;
			IBlockState state = world.getBlockState(pos);
			if(state!=null&&state.isOpaqueCube())
				return false;
		}
		return true;
	}

	private static Set<BlockPos> copyPositions(@Nullable Collection<? extends BlockPos> positions)
	{
		if(positions==null||positions.isEmpty())
			return Collections.emptySet();
		Set<BlockPos> copy = new HashSet<>(positions.size());
		for(BlockPos position : positions)
			if(position!=null)
				copy.add(new BlockPos(position));
		return copy;
	}
}
