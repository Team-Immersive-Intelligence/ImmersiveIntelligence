package pl.pabilo8.immersiveintelligence.common.util.raytracer;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Only traces through blocks, ignores blocks passed in a blacklist
 *
 * @author Pabilo8
 * @since 19.08.2023
 */
public class BlacklistedRayTracer
{
	public static RayTraceResult traceIgnoringBlocks(@Nonnull World world, @Nonnull Vec3d posStart, @Nonnull Vec3d posEnd, List<BlockPos> blockFilter, int tries)
	{
		//Starting Position
		double x = posStart.x, y = posStart.y, z = posStart.z;
		Vec3d step = posEnd.subtract(posStart).normalize();
		BlockPos lastBLockHit = new BlockPos(posStart);
		boolean noSkipping = !blockFilter.isEmpty();

		//Traverse positions
		int dist = (int)Math.ceil(posStart.distanceTo(posEnd));
		for(int i = 0; i < dist; i++)
		{
			x += step.x;
			y += step.y;
			z += step.z;

			//if it's the position as last time, skip it
			if(lastBLockHit.getX()==x&&lastBLockHit.getY()==y&&lastBLockHit.getZ()==z)
				continue;
			lastBLockHit = new BlockPos(x, y, z);

			//skip non-collidable blocks, like air
			if(world.getBlockState(lastBLockHit).getCollisionBoundingBox(world, BlockPos.ORIGIN)==Block.NULL_AABB)
				continue;

			//if the position is contained in
			if(noSkipping&&blockFilter.contains(lastBLockHit))
				continue;

			//decrement tries left
			if(tries-- > 0)
				continue;

			//return, if no tries are left
			return new RayTraceResult(Type.BLOCK, posEnd, EnumFacing.getFacingFromVector((float)step.x, (float)step.y, (float)step.z), lastBLockHit);
		}
		//Didn't collide with any block
		return new RayTraceResult(Type.MISS, Vec3d.ZERO, EnumFacing.getFacingFromVector((float)step.x, (float)step.y, (float)step.z), lastBLockHit);

//		RayTraceResult result;
//		do
//		{
//			result = world.rayTraceBlocks(posStart, posEnd);
//			if(result==null||result.typeOfHit!=Type.BLOCK)
//				return result;
//
//			posStart = new Vec3d(result.getBlockPos()).add(result.hitVec);
//		}
//		while(result.typeOfHit==Type.BLOCK&&(blockFilter.contains(result.getBlockPos())||(tries--) > 0));
//		return result;
	}
}
