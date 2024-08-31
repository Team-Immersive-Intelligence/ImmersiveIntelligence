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
import java.util.*;
import java.util.function.Predicate;

/**
 * A reusable raytracer class used by II in bullets to get all the entities and block a bullet penetrates.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
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
	@Nonnull
	private Set<Entity> entityFilter = Collections.emptySet();
	@Nonnull
	private Set<BlockPos> blockFilter = Collections.emptySet();

	private FactoryTracer(@Nullable AxisAlignedBB aabb)
	{
		this.aabb = aabb==null?EMPTY_AABB: aabb;
		this.precision = Math.abs(this.aabb.getAverageEdgeLength());
	}

	public static FactoryTracer create(@Nonnull AxisAlignedBB aabb)
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
		//Starting Position
		double pX = posStart.x, pY = posStart.y, pZ = posStart.z;
		//Last block position
		BlockPos pos = new BlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		//Avoid scanning the same position twice
		boolean skipPos = false;
		//Step
		Vector3d pDiff = new Vector3d(posEnd.x-posStart.x, posEnd.y-posStart.y, posEnd.z-posStart.z);
		pDiff.normalize();
		pDiff.scale(precision);

		//Expand the bounding box
		AxisAlignedBB aabb;
		int totalSteps = (int)Math.max(1, posStart.distanceTo(posEnd)/precision);

		//Cache entities
		List<Entity> allEntities = allowEntities?listAllEntities(world, posStart, posEnd): null;

		for(int i = 0; i < totalSteps; i++)
		{
			//Step position
			pX += pDiff.x;
			pY += pDiff.y;
			pZ += pDiff.z;
			aabb = this.aabb.offset(pX, pY, pZ);

			//Collide with entities
			if(allEntities!=null)
			{
				ListIterator<Entity> iterator = allEntities.listIterator();
				while(iterator.hasNext())
				{
					Entity entity = iterator.next();
					if(!entity.isEntityAlive())
						iterator.remove();

					if(entity.getEntityBoundingBox().intersects(aabb))
					{
						Vec3d hitVec = entity.getPositionVector().subtract(getAABBCenter(this.aabb));
						RayTraceResult trace = new RayTraceResult(entity, hitVec);
						if(onHit.test(trace))
							return trace;
						iterator.remove();
					}
				}
			}

			//Recalculate block position
			if(pX!=pos.getX()||pY!=pos.getY()||pZ!=pos.getZ())
			{
				pos = new BlockPos(pX, pY, pZ);
				skipPos = false;
			}

			if(skipPos)
				continue;

			//Ignore empty bounding boxes
			IBlockState state = world.getBlockState(pos);
			if(state.getCollisionBoundingBox(world, pos)==Block.NULL_AABB)
				continue;

			//Skip excluded blocks
			if(blockFilter.contains(pos))
				continue;

			//Perform a precise raytrace on the block
			RayTraceResult trace = state.collisionRayTrace(world, pos,
					new Vec3d(aabb.minX, aabb.minY, aabb.minZ),
					new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)
			);
			//Avoid scanning the same position twice
			if(trace!=null&&trace.typeOfHit!=Type.MISS)
				skipPos = true;
			if(onHit.test(trace))
				return trace;
		}
		return null;
	}

	private List<Entity> listAllEntities(@Nonnull World world, @Nonnull Vec3d posStart, @Nonnull Vec3d posEnd)
	{
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(posStart.x, posStart.y, posStart.z, posEnd.x, posEnd.y, posEnd.z),
				entity -> entity.canBeCollidedWith()&&!entity.noClip
		);
		entities.removeAll(entityFilter);
		return entities;
	}

	private Vec3d getAABBCenter(AxisAlignedBB aabb)
	{
		return new Vec3d(aabb.minX+(aabb.maxX-aabb.minX)*0.5D, aabb.minY+(aabb.maxY-aabb.minY)*0.5D, aabb.minZ+(aabb.maxZ-aabb.minZ)*0.5D);
	}

}
