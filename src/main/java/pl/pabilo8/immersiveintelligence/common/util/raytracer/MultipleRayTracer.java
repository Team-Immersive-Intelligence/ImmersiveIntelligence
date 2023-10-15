package pl.pabilo8.immersiveintelligence.common.util.raytracer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 09.11.2020
 * <p>
 * A raytracing class used by II in bullets to get all the entities and block a bullet penetrates
 * Probably will be moved to IG
 * The name volumetric is inspired by War Thunder's system name, the term means that shells have a volume (like {@link AxisAlignedBB})
 * This raytracer simply iterates trough all the blocks using length of the aabb's lowest edge size
 * It can be configured in multiple ways, similar to MC's default {@link RayTraceResult}
 */
public class MultipleRayTracer implements Iterable<RayTraceResult>
{
	private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	private ArrayList<RayTraceResult> hits = new ArrayList<>();
	private BlockPos lastBLockHit;

	private MultipleRayTracer()
	{

	}

	public static MultipleRayTracer volumetricTrace(@Nonnull World world, @Nonnull Vec3d posStart, @Nonnull Vec3d posEnd, @Nonnull AxisAlignedBB aabb,
													List<BlockPos> blockFilter, boolean allowEntities, List<Entity> entityFilter, @Nullable Predicate<IBlockState> stopOn)
	{
		MultipleRayTracer rayTracer = new MultipleRayTracer();
		//the precision used in iteration
		final double precision = Math.abs(aabb.getAverageEdgeLength());
		//Expand the bounding box
		aabb = aabb.grow(0.25).offset(posStart);
		//Starting Position
		Vector3d p = new Vector3d(posStart.x, posStart.y, posStart.z);
		//Step
		Vector3d pDiff = new Vector3d(posEnd.x, posEnd.y, posEnd.z);
		pDiff.sub(p);
		pDiff.normalize();
		pDiff.scale(precision);

		double dist = posStart.distanceTo(posEnd);
		//Cache entities
		List<Entity> allEntities = allowEntities?listAllEntities(world, posStart, posEnd, entityFilter): null;

		for(double i = 0; i < dist; i += precision)
		{
			//Step position
			p.add(pDiff);
			aabb = aabb.offset(pDiff.x, pDiff.y, pDiff.z);

			//Collide with entities
			if(allowEntities)
				traceEntities(rayTracer, aabb, allEntities);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);

			if(pos.equals(rayTracer.lastBLockHit))
				continue;
			rayTracer.lastBLockHit = pos;

			//Ignore empty bounding boxes
			IBlockState state = world.getBlockState(pos);
			if(state.getCollisionBoundingBox(world, pos)==Block.NULL_AABB)
				continue;

			//Stop on this block
			if(stopOn!=null&&stopOn.test(world.getBlockState(pos)))
				break;

			//Skip excluded blocks
			if(traceExcludedBlocks(pos, blockFilter))
				continue;

			//Perform a precise raytrace on the block
			RayTraceResult traceResult = state.collisionRayTrace(world, pos,
					new Vec3d(aabb.minX, aabb.minY, aabb.minZ),
					new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)
			);

			//Doesn't accept null result / miss
			if(traceResult!=null&&traceResult.typeOfHit!=Type.MISS)
				rayTracer.addResultToList(traceResult);
		}

		return rayTracer;
	}

	private static List<Entity> listAllEntities(@Nonnull World world, @Nonnull Vec3d posStart, @Nonnull Vec3d posEnd, List<Entity> entityFilter)
	{
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posStart.x, posStart.y, posStart.z, posEnd.x, posEnd.y, posEnd.z), entity -> entity.canBeCollidedWith()&&!entity.noClip);
		entities.removeAll(entityFilter);
		return entities;
	}

	private static void traceEntities(MultipleRayTracer t, AxisAlignedBB aabb, List<Entity> allEntities)
	{
		Iterator<Entity> it = allEntities.iterator();
		while(it.hasNext())
		{
			Entity next = it.next();
			if(next.getEntityBoundingBox().intersects(aabb))
			{
				t.addResultToList(new RayTraceResult(next));
				it.remove();
			}
		}
	}

	static boolean traceExcludedBlocks(BlockPos current, List<BlockPos> blockFilter)
	{
		Iterator<BlockPos> it = blockFilter.iterator();
		while(it.hasNext())
			if(it.next()==current)
			{
				it.remove();
				return true;
			}
		return false;
	}

	private void addResultToList(@Nonnull RayTraceResult traceResult)
	{
		this.hits.add(traceResult);
	}

	@Override
	public String toString()
	{
		return Arrays.toString(this.hits.toArray(new RayTraceResult[0]));
	}

	@Override
	public Iterator<RayTraceResult> iterator()
	{
		return hits.listIterator();
	}

	public List<RayTraceResult> getHits()
	{
		return hits;
	}

	public static class MultipleTracerBuilder
	{
		private final World world;
		private final Vec3d posStart;
		private final Vec3d posEnd;
		private AxisAlignedBB aabb;
		private boolean allowEntities = true;
		private List<Entity> entityFilter;
		private List<BlockPos> blockFilter;
		private Predicate<IBlockState> stopOn = null;

		//internal
		private MultipleTracerBuilder(World world, Vec3d posStart, Vec3d posEnd)
		{
			this.world = world;
			this.posStart = posStart;
			this.posEnd = posEnd;
		}

		/**
		 * @param world    the world raytracing is in
		 * @param posStart the start position vector
		 * @param posEnd   the end position vector
		 * @return the builder
		 */
		public static MultipleTracerBuilder setPos(World world, Vec3d posStart, Vec3d posEnd)
		{
			return new MultipleTracerBuilder(world, posStart, posEnd);
		}

		/**
		 * @param aabb box for volumetric tracing
		 * @return the builder
		 */
		public MultipleTracerBuilder setAABB(AxisAlignedBB aabb)
		{
			this.aabb = aabb;
			return this;
		}

		/**
		 * @param ignoreBlockWithoutBoundingBox whether blocks with no bounding box (i.e. air) should be counted in
		 * @param stopOnLiquid                  whether should stop tracing on liquids
		 * @param allowEntities                 whether entities should be traced
		 * @return the builder
		 */
		public MultipleTracerBuilder setRules(boolean allowEntities, Predicate<IBlockState> stopOn)
		{
			this.allowEntities = allowEntities;
			this.stopOn = stopOn;
			return this;
		}

		/**
		 * @param entityFilter list for filtering entities
		 * @return the builder
		 */
		public MultipleTracerBuilder setEntityFilter(List<Entity> entityFilter)
		{
			this.entityFilter = entityFilter;
			return this;
		}

		/**
		 * @param blockFilter list for filtering blocks
		 * @return the builder
		 */
		public MultipleTracerBuilder setBlockFilter(List<BlockPos> blockFilter)
		{
			this.blockFilter = blockFilter;
			return this;
		}

		/**
		 * @param entityFilter list for filtering entities
		 * @param blockFilter  list for filtering blocks
		 * @return the builder
		 */
		public MultipleTracerBuilder setFilters(List<Entity> entityFilter, List<BlockPos> blockFilter)
		{
			this.entityFilter = entityFilter;
			this.blockFilter = blockFilter;
			return this;
		}

		public MultipleRayTracer volumetricTrace()
		{
			if(entityFilter==null)
				entityFilter = new ArrayList<>();
			if(blockFilter==null)
				blockFilter = new ArrayList<>();

			if(aabb==null)
				aabb = MultipleRayTracer.EMPTY_AABB;

			return MultipleRayTracer.volumetricTrace(this.world, this.posStart, posEnd, aabb,
					blockFilter, allowEntities, entityFilter, stopOn
			);
		}
	}
}
