package pl.pabilo8.immersiveintelligence.common.util.raytracer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

/**
 * A reusable raytracer class used by II in bullets to get all the entities and block a bullet penetrates.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 23.08.2026
 * @ii-approved 0.3.1
 * @since 08.06.2024
 */
public class FactoryTracer
{
	//--- Constants ---//
	private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	//--- Properties ---//
	@Nonnull
	private final AxisAlignedBB aabb;
	private final double precision;
	private boolean allowEntities = true;
	private boolean allowFluidBlocks = false;
	@Nonnull
	private Set<Entity> entityFilter = Collections.emptySet();
	@Nonnull
	private Set<BlockPos> blockFilter = Collections.emptySet();

	private FactoryTracer(@Nullable AxisAlignedBB aabb)
	{
		this.aabb = aabb==null?EMPTY_AABB: aabb;
		this.precision = Math.abs(this.aabb.getAverageEdgeLength());
	}

	public static FactoryTracer create(@Nullable AxisAlignedBB aabb)
	{
		return new FactoryTracer(aabb);
	}

	public FactoryTracer setEntityFilter(Set<Entity> entityFilter)
	{
		this.entityFilter = entityFilter;
		return this;
	}

	public FactoryTracer setBlockFilter(Set<BlockPos> blockFilter)
	{
		this.blockFilter = blockFilter;
		return this;
	}

	public FactoryTracer setAllowEntities(boolean allowEntities)
	{
		this.allowEntities = allowEntities;
		return this;
	}

	/**
	 * Sets whether fluid blocks can produce block trace results.
	 */
	public FactoryTracer setAllowFluidBlocks(boolean allowFluidBlocks)
	{
		this.allowFluidBlocks = allowFluidBlocks;
		return this;
	}

	public FactoryTracer setFilters(Set<Entity> entityFilter, Set<BlockPos> blockFilter)
	{
		this.entityFilter = entityFilter;
		this.blockFilter = blockFilter;
		return setAllowEntities(true);
	}

	/**
	 * Traces the path of the ray from the start to the end position.
	 *
	 * @param world    the world to trace in
	 * @param posStart the start position
	 * @param posEnd   the end position
	 * @return ordered list of all the entities and blocks the ray passes through
	 */
	public Collection<RayTraceResult> trace(World world, Vec3d posStart, Vec3d posEnd)
	{
		ArrayList<RayTraceResult> results = new ArrayList<>();
		stepTrace(world, posStart, posEnd, results::add);
		return results;
	}

	/**
	 * Traces the path of the ray from the start to the end position.
	 *
	 * @param world    the world to trace in
	 * @param posStart the start position
	 * @param posEnd   the end position
	 * @param onHit    the callback for when the ray hits something, tracing stops if it returns true
	 * @return last hit position, if onHit returned true
	 */
	@Nullable
	public RayTraceResult stepTrace(World world, Vec3d posStart, Vec3d posEnd, Predicate<RayTraceResult> onHit)
	{
		Vec3d difference = posEnd.subtract(posStart);
		double totalDist = difference.lengthVector();
		double stepLength = MathHelper.clamp(precision, 0.0625, 1);
		int totalSteps = (int)Math.max(1, Math.ceil(totalDist/stepLength));
		Vec3d step = difference.scale(1d/totalSteps);

		//Cache entities
		List<Entity> allEntities = allowEntities?listAllEntities(world, posStart, posEnd): null;
		Set<BlockPos> tracedBlocks = new HashSet<>();

		for(int i = 0; i < totalSteps; i++)
		{
			Vec3d stepStart = posStart.add(step.scale(i));
			Vec3d stepEnd = i==totalSteps-1?posEnd: posStart.add(step.scale(i+1));
			List<RayTraceResult> stepTraces = traceBlocks(world, stepStart, stepEnd, tracedBlocks);
			Map<RayTraceResult, Entity> entityOwners = new IdentityHashMap<>();

			//Collide with entities
			if(allEntities!=null)
				for(Entity entity : allEntities)
				{
					RayTraceResult trace = traceEntity(entity, stepStart, stepEnd);
					if(trace!=null)
					{
						stepTraces.add(trace);
						entityOwners.put(trace, entity);
					}
				}

			//Process all collisions in travel order.
			stepTraces.sort(Comparator.comparingDouble(trace -> stepStart.squareDistanceTo(trace.hitVec)));
			for(RayTraceResult trace : stepTraces)
			{
				if(trace.typeOfHit==Type.ENTITY)
				{
					Entity owner = entityOwners.get(trace);
					if(owner==null||allEntities==null||!allEntities.remove(owner))
						continue;
				}
				else
					tracedBlocks.add(trace.getBlockPos());
				if(onHit.test(trace))
					return trace;
			}
		}
		return null;
	}

	@Nullable
	private RayTraceResult traceEntity(Entity entity, Vec3d posStart, Vec3d posEnd)
	{
		Entity[] parts = entity.getParts();
		if(parts==null)
			return traceEntityPart(entity, posStart, posEnd);

		RayTraceResult closest = null;
		for(Entity part : parts)
		{
			RayTraceResult trace = traceEntityPart(part, posStart, posEnd);
			if(trace!=null&&(closest==null||posStart.squareDistanceTo(trace.hitVec) < posStart.squareDistanceTo(closest.hitVec)))
				closest = trace;
		}
		return closest;
	}

	@Nullable
	private RayTraceResult traceEntityPart(Entity entity, Vec3d posStart, Vec3d posEnd)
	{
		AxisAlignedBB target = entity.getEntityBoundingBox();
		AxisAlignedBB expanded = new AxisAlignedBB(
				target.minX-aabb.maxX, target.minY-aabb.maxY, target.minZ-aabb.maxZ,
				target.maxX-aabb.minX, target.maxY-aabb.minY, target.maxZ-aabb.minZ
		);
		RayTraceResult intercept = expanded.calculateIntercept(posStart, posEnd);
		Vec3d hitVec = intercept==null?(expanded.contains(posStart)?posStart: null): intercept.hitVec;
		return hitVec==null?null: new RayTraceResult(entity, hitVec);
	}

	private List<RayTraceResult> traceBlocks(World world, Vec3d posStart, Vec3d posEnd, Set<BlockPos> tracedBlocks)
	{
		BlockPos min = new BlockPos(
				Math.min(posStart.x, posEnd.x), Math.min(posStart.y, posEnd.y), Math.min(posStart.z, posEnd.z)
		);
		BlockPos max = new BlockPos(
				Math.max(posStart.x, posEnd.x), Math.max(posStart.y, posEnd.y), Math.max(posStart.z, posEnd.z)
		);
		List<RayTraceResult> traces = new ArrayList<>();

		for(BlockPos pos : BlockPos.getAllInBox(min, max))
		{
			if(tracedBlocks.contains(pos)||blockFilter.contains(pos))
				continue;

			IBlockState state = world.getBlockState(pos);
			boolean fluidBlock = allowFluidBlocks&&(state.getBlock() instanceof IFluidBlock||state.getMaterial().isLiquid());
			if(!fluidBlock&&state.getCollisionBoundingBox(world, pos)==Block.NULL_AABB)
				continue;

			RayTraceResult trace = state.collisionRayTrace(world, pos, posStart, posEnd);
			if(fluidBlock&&(trace==null||trace.typeOfHit==Type.MISS))
			{
				AxisAlignedBB bounds = new AxisAlignedBB(pos);
				RayTraceResult intercept = bounds.calculateIntercept(posStart, posEnd);
				if(intercept!=null)
					trace = new RayTraceResult(intercept.hitVec, intercept.sideHit, pos);
				else if(bounds.contains(posStart))
					trace = new RayTraceResult(posStart,
							EnumFacing.getFacingFromVector((float)(posEnd.x-posStart.x), (float)(posEnd.y-posStart.y), (float)(posEnd.z-posStart.z)).getOpposite(), pos);
			}
			if(trace!=null&&trace.typeOfHit!=Type.MISS)
				traces.add(trace);
		}

		return traces;
	}

	private List<Entity> listAllEntities(@Nonnull World world, @Nonnull Vec3d posStart, @Nonnull Vec3d posEnd)
	{
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(posStart.x, posStart.y, posStart.z, posEnd.x, posEnd.y, posEnd.z)
						.expand(aabb.minX, aabb.minY, aabb.minZ)
						.expand(aabb.maxX, aabb.maxY, aabb.maxZ),
				entity -> entity.canBeCollidedWith()&&!entity.noClip
		);
		entities.removeAll(entityFilter);
		return entities;
	}

}
