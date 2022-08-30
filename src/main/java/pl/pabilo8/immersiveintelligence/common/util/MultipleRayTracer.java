package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 09.11.2020
 *
 * A raytracing class used by II in bullets to get all the entities and block a bullet penetrates
 * Probably will be moved to IG
 * The name volumetric is inspired by War Thunder's system name, the term means that shells have a volume (like {@link AxisAlignedBB})
 * This raytracer simply iterates trough all the blocks using length of the aabb's lowest edge size
 * It can be configured in multiple ways, similar to MC's default {@link RayTraceResult}
 */
public class MultipleRayTracer
{
	private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public ArrayList<RayTraceResult> hits = new ArrayList<>();

	private MultipleRayTracer()
	{

	}

	public static MultipleRayTracer volumetricTrace(@Nonnull World world, @Nonnull Vec3d posStart, @Nonnull Vec3d posEnd, @Nonnull AxisAlignedBB aabb, boolean ignoreBlockWithoutBoundingBox, boolean stopOnLiquid, boolean allowEntities, @Nonnull List<Entity> entityFilter, @Nonnull List<BlockPos> blockFilter)
	{
		MultipleRayTracer rayTracer = new MultipleRayTracer();
		//the precision used in iteration
		final double precision = Math.abs(aabb.getAverageEdgeLength());
		double px = posStart.x, py = posStart.y, pz = posStart.z;
		Vec3d pDiff = posEnd.subtract(posStart).normalize();
		double pxDiff = pDiff.x*precision, pyDiff = pDiff.y*precision, pzDiff = pDiff.z*precision;
		BlockPos lastPos = null;
		aabb = aabb.grow(0.25);
		double dist = posStart.distanceTo(posEnd);

		///kill @e[type=immersiveintelligence:bullet]
		for(double i = 0; i < dist; i += precision)
		{
			px += pxDiff;
			py += pyDiff;
			pz += pzDiff;

			if(allowEntities)
				traceEntities(world, rayTracer, aabb.offset(px, py, pz), entityFilter);
			final BlockPos pos = new BlockPos(px, py, pz);

			if(blockFilter.stream().noneMatch(blockPos -> blockPos.equals(pos)))
			{
				IBlockState state = world.getBlockState(pos);
				if(stopOnLiquid&&state.getBlock() instanceof IFluidBlock)
					break;

				if(state.getCollisionBoundingBox(world, pos)==Block.NULL_AABB)
				{
					if(!ignoreBlockWithoutBoundingBox)
					{
						EnumFacing ff = EnumFacing.getFacingFromVector((float)pxDiff, (float)pyDiff, (float)pzDiff);
						rayTracer.addResultToList(new RayTraceResult(new Vec3d(px, py, pz), ff));
					}
				}
				else
				{
					Vec3d bbStart = new Vec3d(aabb.minX, aabb.minY, aabb.minZ).addVector(px, py, pz);
					Vec3d bbEnd = new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ).addVector(px, py, pz);
					RayTraceResult traceResult = state.collisionRayTrace(world, pos, bbStart, bbEnd);
					if(traceResult!=null&&traceResult.typeOfHit!=Type.MISS)
						rayTracer.addResultToList(traceResult);
				}

			}
		}

		return rayTracer;
	}

	private static void traceEntities(World world, MultipleRayTracer t, AxisAlignedBB aabb, List<Entity> filter)
	{
		if(aabb!=null)
			for(Entity entity : world.getEntitiesInAABBexcluding(null, aabb, input -> !filter.contains(input)))
			{
				if(entity.canBeCollidedWith()&&!entity.noClip&&entity.getEntityBoundingBox().intersects(aabb))
					t.addResultToList(new RayTraceResult(entity));
			}
	}

	private void addResultToList(RayTraceResult traceResult)
	{
		if(traceResult!=null&&this.hits.stream().noneMatch(traceResult1 -> {
			if(traceResult.typeOfHit==traceResult1.typeOfHit)
				switch(traceResult1.typeOfHit)
				{
					case ENTITY:
						return traceResult.entityHit.equals(traceResult1.entityHit);
					case BLOCK:
						return traceResult.getBlockPos().equals(traceResult1.getBlockPos());
				}
			return false;
		}))
			this.hits.add(traceResult);
	}

	@Override
	public String toString()
	{
		return Arrays.toString(this.hits.toArray(new RayTraceResult[0]));
	}

	public static class MultipleTracerBuilder
	{
		private final World world;
		private final Vec3d posStart;
		private final Vec3d posEnd;
		private AxisAlignedBB aabb;
		private boolean ignoreBlockWithoutBoundingBox = true;
		private boolean stopOnLiquid = false;
		private boolean allowEntities = true;
		private List<Entity> entityFilter;
		private List<BlockPos> blockFilter;

		//internal
		private MultipleTracerBuilder(World world, Vec3d posStart, Vec3d posEnd)
		{
			this.world = world;
			this.posStart = posStart;
			this.posEnd = posEnd;
		}

		/**
		 *
		 * @param world the world raytracing is in
		 * @param posStart the start position vector
		 * @param posEnd the end position vector
		 * @return the builder
		 */
		public static MultipleTracerBuilder setPos(World world, Vec3d posStart, Vec3d posEnd)
		{
			return new MultipleTracerBuilder(world, posStart, posEnd);
		}

		/**
		 *
		 * @param aabb box for volumetric tracing
		 * @return the builder
		 */
		public MultipleTracerBuilder setAABB(AxisAlignedBB aabb)
		{
			this.aabb = aabb;
			return this;
		}

		/**
		 *
		 * @param ignoreBlockWithoutBoundingBox whether blocks with no bounding box (i.e. air) should be counted in
		 * @param stopOnLiquid whether should stop tracing on liquids
		 * @param allowEntities whether entities should be traced
		 * @return the builder
		 */
		public MultipleTracerBuilder setRules(boolean ignoreBlockWithoutBoundingBox, boolean stopOnLiquid, boolean allowEntities)
		{
			this.ignoreBlockWithoutBoundingBox = ignoreBlockWithoutBoundingBox;
			this.stopOnLiquid = stopOnLiquid;
			this.allowEntities = allowEntities;
			return this;
		}

		/**
		 *
		 * @param entityFilter list for filtering entities
		 * @return the builder
		 */
		public MultipleTracerBuilder setEntityFilter(List<Entity> entityFilter)
		{
			this.entityFilter = entityFilter;
			return this;
		}

		/**
		 *
		 * @param blockFilter list for filtering blocks
		 * @return the builder
		 */
		public MultipleTracerBuilder setBlockFilter(List<BlockPos> blockFilter)
		{
			this.blockFilter = blockFilter;
			return this;
		}

		/**
		 *
		 * @param entityFilter list for filtering entities
		 * @param blockFilter list for filtering blocks
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

			return MultipleRayTracer.volumetricTrace(this.world, this.posStart, posEnd, aabb, ignoreBlockWithoutBoundingBox, stopOnLiquid, allowEntities, entityFilter, blockFilter);
		}
	}
}
