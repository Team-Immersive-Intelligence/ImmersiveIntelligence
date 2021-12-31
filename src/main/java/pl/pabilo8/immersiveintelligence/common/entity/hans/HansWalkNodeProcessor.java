package pl.pabilo8.immersiveintelligence.common.entity.hans;

import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Flemmli97
 * @since 28.12.2021
 * <p>
 *     Stolen (rightfully, kinda) from Improved Mobs. Huge thanks to Flemmli97 for this piece of code.<br>
 *     <a href="https://github.com/Flemmli97/ImprovedMobs/blob/1.12.2/src/main/java/com/flemmli97/improvedmobs/entity/ai/NewWalkNodeProcessor.java">https://github.com/Flemmli97/ImprovedMobs/blob/1.12.2/src/main/java/com/flemmli97/improvedmobs/entity/ai/NewWalkNodeProcessor.java</a><br>
 *     Code was adapted to II's Hanses.
 * </p>
 */
// TODO: 28.12.2021 Pioneer Hanses
public class HansWalkNodeProcessor extends WalkNodeProcessor
{
	private boolean canBreakBlocks = false;
	private boolean breakableFlag = false;

	private PathNodeType getPathNodeType(EntityLiving entitylivingIn, int x, int y, int z)
	{
		this.breakableFlag = false;
		return this.getPathNodeType(this.blockaccess, x, y, z, entitylivingIn, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
	}

	@Override
	public int findPathOptions(@Nonnull PathPoint[] pathOptions, PathPoint currentPoint, @Nonnull PathPoint targetPoint, float maxDistance)
	{
		int i = 0;
		int j = 0;
		PathNodeType pathnodetype = this.getPathNodeType(this.entity, currentPoint.x, currentPoint.y+1, currentPoint.z);

		if(this.entity.getPathPriority(pathnodetype) >= 0.0F)
		{
			j = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
		}

		BlockPos blockpos = (new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z)).down();
		double d0 = currentPoint.y-(1.0D-this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos).maxY);
		PathPoint pathpoint = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z+1, j, d0, EnumFacing.SOUTH);
		PathPoint pathpoint1 = this.getSafePoint(currentPoint.x-1, currentPoint.y, currentPoint.z, j, d0, EnumFacing.WEST);
		PathPoint pathpoint2 = this.getSafePoint(currentPoint.x+1, currentPoint.y, currentPoint.z, j, d0, EnumFacing.EAST);
		PathPoint pathpoint3 = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z-1, j, d0, EnumFacing.NORTH);
		PathPoint ladderUp = this.openPoint(currentPoint.x, currentPoint.y+1, currentPoint.z);
		PathPoint ladderDown = this.openPoint(currentPoint.x, currentPoint.y-1, currentPoint.z);

		if(pathpoint!=null&&!pathpoint.visited&&pathpoint.distanceTo(targetPoint) < maxDistance)
		{
			pathOptions[i++] = pathpoint;
		}

		if(pathpoint1!=null&&!pathpoint1.visited&&pathpoint1.distanceTo(targetPoint) < maxDistance)
		{
			pathOptions[i++] = pathpoint1;
		}

		if(pathpoint2!=null&&!pathpoint2.visited&&pathpoint2.distanceTo(targetPoint) < maxDistance)
		{
			pathOptions[i++] = pathpoint2;
		}

		if(pathpoint3!=null&&!pathpoint3.visited&&pathpoint3.distanceTo(targetPoint) < maxDistance)
		{
			pathOptions[i++] = pathpoint3;
		}
		IBlockState ladderCheck = this.blockaccess.getBlockState(new BlockPos(currentPoint.x, currentPoint.y+1, currentPoint.z));
		if(!ladderUp.visited&&ladderUp.distanceTo(targetPoint) < maxDistance&&ladderCheck.getBlock().isLadder(ladderCheck, this.blockaccess, new BlockPos(currentPoint.x, currentPoint.y+1, currentPoint.z), this.entity))
		{
			pathOptions[i++] = ladderUp;
		}

		IBlockState ladderCheckDown = this.blockaccess.getBlockState(new BlockPos(currentPoint.x, currentPoint.y-1, currentPoint.z));
		if(!ladderDown.visited&&ladderDown.distanceTo(targetPoint) < maxDistance&&ladderCheckDown.getBlock().isLadder(ladderCheckDown, this.blockaccess, new BlockPos(currentPoint.x, currentPoint.y-1, currentPoint.z), this.entity))
		{
			pathOptions[i++] = ladderDown;
		}

		boolean flag = pathpoint3==null||pathpoint3.nodeType==PathNodeType.OPEN||pathpoint3.costMalus!=0.0F;
		boolean flag1 = pathpoint==null||pathpoint.nodeType==PathNodeType.OPEN||pathpoint.costMalus!=0.0F;
		boolean flag2 = pathpoint2==null||pathpoint2.nodeType==PathNodeType.OPEN||pathpoint2.costMalus!=0.0F;
		boolean flag3 = pathpoint1==null||pathpoint1.nodeType==PathNodeType.OPEN||pathpoint1.costMalus!=0.0F;

		if(flag&&flag3)
		{
			PathPoint pathpoint4 = this.getSafePoint(currentPoint.x-1, currentPoint.y, currentPoint.z-1, j, d0, EnumFacing.NORTH);
			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x-0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z-0.5-d1+0.5D, currentPoint.x-0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z-0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&pathpoint4!=null&&!pathpoint4.visited&&pathpoint4.distanceTo(targetPoint) < maxDistance)
			{
				pathOptions[i++] = pathpoint4;
			}
		}

		if(flag&&flag2)
		{
			PathPoint pathpoint5 = this.getSafePoint(currentPoint.x+1, currentPoint.y, currentPoint.z-1, j, d0, EnumFacing.NORTH);
			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x+0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z-0.5-d1+0.5D, currentPoint.x+0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z-0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&pathpoint5!=null&&!pathpoint5.visited&&pathpoint5.distanceTo(targetPoint) < maxDistance)
			{
				pathOptions[i++] = pathpoint5;
			}
		}

		if(flag1&&flag3)
		{
			PathPoint pathpoint6 = this.getSafePoint(currentPoint.x-1, currentPoint.y, currentPoint.z+1, j, d0, EnumFacing.SOUTH);

			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x-0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z+0.5-d1+0.5D, currentPoint.x-0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z+0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&pathpoint6!=null&&!pathpoint6.visited&&pathpoint6.distanceTo(targetPoint) < maxDistance)
			{
				pathOptions[i++] = pathpoint6;
			}
		}

		if(flag1&&flag2)
		{
			PathPoint pathpoint7 = this.getSafePoint(currentPoint.x+1, currentPoint.y, currentPoint.z+1, j, d0, EnumFacing.SOUTH);
			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x+0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z+0.5-d1+0.5D, currentPoint.x+0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z+0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&pathpoint7!=null&&!pathpoint7.visited&&pathpoint7.distanceTo(targetPoint) < maxDistance)
			{
				pathOptions[i++] = pathpoint7;
			}
		}
		return i;
	}

	@Nullable
	private PathPoint getSafePoint(int x, int y, int z, int p_186332_4_, double p_186332_5_, EnumFacing facing)
	{
		PathPoint pathpoint = null;
		BlockPos blockpos = new BlockPos(x, y, z);
		BlockPos blockpos1 = blockpos.down();
		double d0 = y-(1.0D-this.blockaccess.getBlockState(blockpos1).getBoundingBox(this.blockaccess, blockpos1).maxY);
		if(d0-p_186332_5_ > 1.125D)
			return null;
		else
		{
			this.breakableFlag = false;
			PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
			float f = this.entity.getPathPriority(pathnodetype);
			double d1 = this.entity.width/2.0D;
			if(f >= 0.0F)
			{
				pathpoint = this.openPoint(x, y, z);
				pathpoint.nodeType = pathnodetype;
				pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
				if(this.breakableFlag)
					pathpoint.costMalus++;
			}
			if(pathnodetype!=PathNodeType.WALKABLE)
			{
				if(pathpoint==null&&p_186332_4_ > 0&&pathnodetype!=PathNodeType.FENCE&&pathnodetype!=PathNodeType.TRAPDOOR)
				{
					pathpoint = this.getSafePoint(x, y+1, z, p_186332_4_-1, p_186332_5_, facing);
					if(pathpoint!=null&&(pathpoint.nodeType==PathNodeType.OPEN||pathpoint.nodeType==PathNodeType.WALKABLE)&&this.entity.width < 1.0F)
					{
						double d2 = (x-facing.getFrontOffsetX())+0.5D;
						double d3 = (z-facing.getFrontOffsetZ())+0.5D;
						AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2-d1, y+0.001D, d3-d1, d2+d1, ((float)y+this.entity.height), d3+d1);
						AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos);
						AxisAlignedBB axisalignedbb2 = axisalignedbb.expand(0.0D, axisalignedbb1.maxY-0.002D, 0.0D);
						if(this.entity.world.collidesWithAnyBlock(axisalignedbb2))
							pathpoint = null;
					}
				}
				if(pathnodetype==PathNodeType.OPEN)
				{
					AxisAlignedBB axisalignedbb3 = new AxisAlignedBB(x-d1+0.5D, y+0.001D, z-d1+0.5D, x+d1+0.5D, ((float)y+this.entity.height), z+d1+0.5D);
					if(this.entity.world.collidesWithAnyBlock(axisalignedbb3))
						return null;
					if(this.entity.width >= 1.0F)
					{
						PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, x, y-1, z);
						if(pathnodetype1==PathNodeType.BLOCKED)
						{
							pathpoint = this.openPoint(x, y, z);
							pathpoint.nodeType = PathNodeType.WALKABLE;
							pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
							if(this.breakableFlag)
								pathpoint.costMalus++;
							return pathpoint;
						}
					}
					int i = 0;
					while(y > 0&&pathnodetype==PathNodeType.OPEN)
					{
						--y;
						if(i++ >= this.entity.getMaxFallHeight())
							return null;
						pathnodetype = this.getPathNodeType(this.entity, x, y, z);
						f = this.entity.getPathPriority(pathnodetype);
						if(pathnodetype!=PathNodeType.OPEN&&f >= 0.0F)
						{
							pathpoint = this.openPoint(x, y, z);
							pathpoint.nodeType = pathnodetype;
							pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
							if(this.breakableFlag)
								pathpoint.costMalus++;
							break;
						}
						if(f < 0.0F)
							return null;
					}
				}
			}
			return pathpoint;
		}
	}

	@Nonnull
	@Override
	protected PathNodeType getPathNodeTypeRaw(IBlockAccess acc, int x, int y, int z)
	{
		BlockPos blockpos = new BlockPos(x, y, z);
		IBlockState iblockstate = acc.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		// TODO: 28.12.2021 revisit block breaking
		/*if(this.entity!=null&&this.canBreakBlocks()&&ConfigHandler.breakableBlocks.canBreak(iblockstate))
		{
			double d1 = (double)this.entity.width/2.0D;
			AxisAlignedBB aabb = new AxisAlignedBB((double)x-d1+0.5D, (double)y+1.001D, (double)z-d1+0.5D, (double)x+d1+0.5D, (float)y+1+this.entity.height, (double)z+d1+0.5D);
			if(this.entity.posY > blockpos.getY()+0.8)
				return this.defaultNode(acc, iblockstate, blockpos, block);
			else if(this.entity.posY <= blockpos.getY()+1&&this.entity.posY >= blockpos.getY())
			{
				if(this.entity.world.collidesWithAnyBlock(aabb)||(block instanceof BlockDoor&&iblockstate.getValue(BlockDoor.HALF)==BlockDoor.EnumDoorHalf.LOWER))
				{
					this.breakableFlag = true;
					return PathNodeType.WALKABLE;
				}
				else
					return this.defaultNode(acc, iblockstate, blockpos, block);
			}
			return PathNodeType.OPEN;
		}
		else
		{*/
		if(this.entity!=null)
			if(block.isLadder(iblockstate, acc, blockpos, this.entity))
				return PathNodeType.WALKABLE;
		return this.defaultNode(acc, iblockstate, blockpos, block);
		/*}*/
	}

	private PathNodeType defaultNode(IBlockAccess acc, IBlockState iblockstate, BlockPos blockpos, Block block)
	{
		Material material = iblockstate.getMaterial();
		PathNodeType type = block.getAiPathNodeType(iblockstate, acc, blockpos, this.entity);
		if(type!=null)
			return type;

		if(material==Material.AIR||iblockstate.getCollisionBoundingBox(acc, blockpos)==Block.NULL_AABB)
			return PathNodeType.OPEN;
		else if(block instanceof BlockTrapDoor||block instanceof BlockLilyPad)
			return PathNodeType.TRAPDOOR;
		else if(block instanceof BlockFire)
			return PathNodeType.DAMAGE_FIRE;
		else if(block instanceof BlockCactus)
			return PathNodeType.DAMAGE_CACTUS;
		else if(block instanceof BlockDoor)
			return this.getDoorType(iblockstate);
		else if(block instanceof BlockRailBase)
			return PathNodeType.RAIL;
		else if(this.isFence(acc, iblockstate, blockpos, block))
			return PathNodeType.FENCE;
		else if(material==Material.WATER)
			return PathNodeType.WATER;
		else if(material==Material.LAVA)
			return PathNodeType.LAVA;
		else
			return block.isPassable(acc, blockpos)?PathNodeType.OPEN: PathNodeType.BLOCKED;
	}

	public boolean canBreakBlocks()
	{
		return this.canBreakBlocks;
	}

	public void setBreakBlocks(boolean flag)
	{
		this.canBreakBlocks = flag;
	}

	//For some block not extending fences but having a bigger collision box
	private boolean isFence(IBlockAccess acc, IBlockState iblockstate, BlockPos blockpos, Block block)
	{
		if(block instanceof BlockFence||block instanceof BlockWall||(block instanceof BlockFenceGate&&!iblockstate.getValue(BlockFenceGate.OPEN)))
		{
			return true;
		}
		else if(this.entity!=null)
		{
			AxisAlignedBB collision2 = new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(blockpos.up());
			List<AxisAlignedBB> list = Lists.newArrayList();
			iblockstate.addCollisionBoxToList(this.entity.world, blockpos, collision2, list, null, false);
			return !list.isEmpty();
		}
		return false;
	}

	private PathNodeType getDoorType(IBlockState state)
	{
		boolean open = state.getValue(BlockDoor.OPEN);
		if(!open)
		{
			if(state.getMaterial()==Material.WOOD)
				return PathNodeType.DOOR_WOOD_CLOSED;
			else
				return PathNodeType.DOOR_IRON_CLOSED;
		}
		return PathNodeType.DOOR_OPEN;
	}
}
