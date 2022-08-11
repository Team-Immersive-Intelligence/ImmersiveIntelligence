package pl.pabilo8.immersiveintelligence.common.entity.hans;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityRazorWire;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Flemmli97
 * @since 28.12.2021
 * <p>
 * Stolen (rightfully, kinda) from Improved Mobs. Huge thanks to Flemmli97 for this piece of code.<br>
 * <a href="https://github.com/Flemmli97/ImprovedMobs/blob/1.12.2/src/main/java/com/flemmli97/improvedmobs/entity/ai/NewWalkNodeProcessor.java">https://github.com/Flemmli97/ImprovedMobs/blob/1.12.2/src/main/java/com/flemmli97/improvedmobs/entity/ai/NewWalkNodeProcessor.java</a><br>
 * Code was adapted to II's Hanses.
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
	public PathPoint getStart()
	{
		int y;
		AxisAlignedBB bounds = this.entity.getEntityBoundingBox();

		if(this.getCanSwim()&&this.entity.isInWater())
		{
			y = (int)bounds.minY;
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), y, MathHelper.floor(this.entity.posZ));
			for(IBlockState state = this.blockaccess.getBlockState(pos); state.getMaterial().isLiquid(); state = this.blockaccess.getBlockState(pos))
				pos.setY(++y);
		}
		else if(this.entity.onGround)
		{
			y = MathHelper.floor(bounds.minY+0.5D);
		}
		else
		{
			y = MathHelper.floor(this.entity.posY);
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), y, MathHelper.floor(this.entity.posZ));
			while(y > 0&&(this.blockaccess.getBlockState(pos).getMaterial()==Material.AIR||this.blockaccess.getBlockState(pos).getBlock().isPassable(this.blockaccess, pos)))
				pos.setY(y--);
			y++;
		}

		//account for node size
		float r = this.entity.width*0.5F;
		int x = MathHelper.floor(this.entity.posX-r);
		int z = MathHelper.floor(this.entity.posZ-r);
		if(this.entity.getPathPriority(this.getPathNodeType(this.entity, x, y, z)) < 0.0F)
		{
			Set<BlockPos> diagonals = new HashSet<>();
			diagonals.add(new BlockPos(bounds.minX-r, y, bounds.minZ-r));
			diagonals.add(new BlockPos(bounds.minX-r, y, bounds.maxZ-r));
			diagonals.add(new BlockPos(bounds.maxX-r, y, bounds.minZ-r));
			diagonals.add(new BlockPos(bounds.maxX-r, y, bounds.maxZ-r));
			for(BlockPos p : diagonals)
			{
				PathNodeType pathnodetype = this.getPathNodeType(this.entity, p.getX(), p.getY(), p.getZ());
				if(this.entity.getPathPriority(pathnodetype) >= 0.0F)
				{
					return this.openPoint(p.getX(), p.getY(), p.getZ());
				}
			}
		}
		return this.openPoint(x, y, z);
	}

	@Override
	public int findPathOptions(@Nonnull PathPoint[] pathOptions, PathPoint currentPoint, @Nonnull PathPoint targetPoint, float maxDistance)
	{
		int optionCount = 0;
		int step = 0;
		PathNodeType pathnodetype = this.getPathNodeType(this.entity, currentPoint.x, currentPoint.y+1, currentPoint.z);

		if(this.entity.getPathPriority(pathnodetype) >= 0.0F)
			step = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));

		BlockPos pos = (new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z)).down();
		double floor = currentPoint.y-(1.0D-this.blockaccess.getBlockState(pos).getBoundingBox(this.blockaccess, pos).maxY);
		PathPoint south = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z+1, step, floor, EnumFacing.SOUTH);
		PathPoint west = this.getSafePoint(currentPoint.x-1, currentPoint.y, currentPoint.z, step, floor, EnumFacing.WEST);
		PathPoint east = this.getSafePoint(currentPoint.x+1, currentPoint.y, currentPoint.z, step, floor, EnumFacing.EAST);
		PathPoint north = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z-1, step, floor, EnumFacing.NORTH);
		PathPoint ladderUp = this.openPoint(currentPoint.x, currentPoint.y+1, currentPoint.z);
		PathPoint ladderDown = this.openPoint(currentPoint.x, currentPoint.y-1, currentPoint.z);

		if(south!=null&&!south.visited&&south.distanceTo(targetPoint) < maxDistance)
			pathOptions[optionCount++] = south;

		if(west!=null&&!west.visited&&west.distanceTo(targetPoint) < maxDistance)
			pathOptions[optionCount++] = west;

		if(east!=null&&!east.visited&&east.distanceTo(targetPoint) < maxDistance)
			pathOptions[optionCount++] = east;

		if(north!=null&&!north.visited&&north.distanceTo(targetPoint) < maxDistance)
			pathOptions[optionCount++] = north;
		IBlockState ladderCheck = this.blockaccess.getBlockState(new BlockPos(currentPoint.x, currentPoint.y+1, currentPoint.z));
		if(!ladderUp.visited&&ladderUp.distanceTo(targetPoint) < maxDistance&&ladderCheck.getBlock().isLadder(ladderCheck, this.blockaccess, new BlockPos(currentPoint.x, currentPoint.y+1, currentPoint.z), this.entity))
			pathOptions[optionCount++] = ladderUp;

		IBlockState ladderCheckDown = this.blockaccess.getBlockState(new BlockPos(currentPoint.x, currentPoint.y-1, currentPoint.z));
		if(!ladderDown.visited&&ladderDown.distanceTo(targetPoint) < maxDistance&&ladderCheckDown.getBlock().isLadder(ladderCheckDown, this.blockaccess, new BlockPos(currentPoint.x, currentPoint.y-1, currentPoint.z), this.entity))
			pathOptions[optionCount++] = ladderDown;

		boolean northPassable = north==null||north.nodeType==PathNodeType.OPEN||north.costMalus!=0.0F;
		boolean southPassable = south==null||south.nodeType==PathNodeType.OPEN||south.costMalus!=0.0F;
		boolean eastPassable = east==null||east.nodeType==PathNodeType.OPEN||east.costMalus!=0.0F;
		boolean westPassable = west==null||west.nodeType==PathNodeType.OPEN||west.costMalus!=0.0F;

		if(northPassable&&westPassable)
		{
			PathPoint northWestPassable = this.getSafePoint(currentPoint.x-1, currentPoint.y, currentPoint.z-1, step, floor, EnumFacing.NORTH);
			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x-0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z-0.5-d1+0.5D, currentPoint.x-0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z-0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&northWestPassable!=null&&!northWestPassable.visited&&northWestPassable.distanceTo(targetPoint) < maxDistance)
				pathOptions[optionCount++] = northWestPassable;
		}

		if(northPassable&&eastPassable)
		{
			PathPoint northEastPassable = this.getSafePoint(currentPoint.x+1, currentPoint.y, currentPoint.z-1, step, floor, EnumFacing.NORTH);
			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x+0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z-0.5-d1+0.5D, currentPoint.x+0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z-0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&northEastPassable!=null&&!northEastPassable.visited&&northEastPassable.distanceTo(targetPoint) < maxDistance)
				pathOptions[optionCount++] = northEastPassable;
		}

		if(southPassable&&westPassable)
		{
			PathPoint southWestPassable = this.getSafePoint(currentPoint.x-1, currentPoint.y, currentPoint.z+1, step, floor, EnumFacing.SOUTH);

			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x-0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z+0.5-d1+0.5D, currentPoint.x-0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z+0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&southWestPassable!=null&&!southWestPassable.visited&&southWestPassable.distanceTo(targetPoint) < maxDistance)
				pathOptions[optionCount++] = southWestPassable;
		}

		if(southPassable&&eastPassable)
		{
			PathPoint southEastPassable = this.getSafePoint(currentPoint.x+1, currentPoint.y, currentPoint.z+1, step, floor, EnumFacing.SOUTH);
			double d1 = this.entity.width/2.0D;
			AxisAlignedBB aabb1 = new AxisAlignedBB(currentPoint.x+0.5-d1+0.5D, currentPoint.y+1.001D, currentPoint.z+0.5-d1+0.5D, currentPoint.x+0.5+d1+0.5D, ((float)currentPoint.y+1+this.entity.height), currentPoint.z+0.5+d1+0.5D);
			if(!this.entity.world.collidesWithAnyBlock(aabb1)&&southEastPassable!=null&&!southEastPassable.visited&&southEastPassable.distanceTo(targetPoint) < maxDistance)
				pathOptions[optionCount++] = southEastPassable;
		}
		return optionCount;
	}

	@Nullable
	private PathPoint getSafePoint(int x, int y, int z, int p_186332_4_, double p_186332_5_, EnumFacing facing)
	{
		PathPoint point = null;
		BlockPos pos = new BlockPos(x, y, z);
		BlockPos below = pos.down();
		double d0 = y-(1.0D-this.blockaccess.getBlockState(below).getBoundingBox(this.blockaccess, below).maxY);
		if(d0-p_186332_5_ > 1.125D)
			return null;
		else
		{
			this.breakableFlag = false;
			PathNodeType type = this.getPathNodeType(this.entity, x, y, z);
			float priority = this.entity.getPathPriority(type);
			double width = this.entity.width/2.0D;
			if(priority >= 0.0F)
			{
				point = this.openPoint(x, y, z);
				point.nodeType = type;
				point.costMalus = Math.max(point.costMalus, priority);
				if(this.breakableFlag)
					point.costMalus++;
			}
			if(type!=PathNodeType.WALKABLE)
			{
				if(point==null&&p_186332_4_ > 0&&type!=PathNodeType.FENCE&&type!=PathNodeType.TRAPDOOR)
				{
					point = this.getSafePoint(x, y+1, z, p_186332_4_-1, p_186332_5_, facing);
					if(point!=null&&(point.nodeType==PathNodeType.OPEN||point.nodeType==PathNodeType.WALKABLE)&&this.entity.width < 1.0F)
					{
						double d2 = (x-facing.getFrontOffsetX())+0.5D;
						double d3 = (z-facing.getFrontOffsetZ())+0.5D;
						AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2-width, y+0.001D, d3-width, d2+width, ((float)y+this.entity.height), d3+width);
						AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(pos).getBoundingBox(this.blockaccess, pos);
						AxisAlignedBB axisalignedbb2 = axisalignedbb.expand(0.0D, axisalignedbb1.maxY-0.002D, 0.0D);
						if(this.entity.world.collidesWithAnyBlock(axisalignedbb2))
							point = null;
					}
				}
				if(type==PathNodeType.OPEN)
				{
					AxisAlignedBB axisalignedbb3 = new AxisAlignedBB(x-width+0.5D, y+0.001D, z-width+0.5D, x+width+0.5D, ((float)y+this.entity.height), z+width+0.5D);
					if(this.entity.world.collidesWithAnyBlock(axisalignedbb3))
						return null;
					if(this.entity.width >= 1.0F)
					{
						PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, x, y-1, z);
						if(pathnodetype1==PathNodeType.BLOCKED)
						{
							point = this.openPoint(x, y, z);
							point.nodeType = PathNodeType.WALKABLE;
							point.costMalus = Math.max(point.costMalus, priority);
							if(this.breakableFlag)
								point.costMalus++;
							return point;
						}
					}
					int i = 0;
					while(y > 0&&type==PathNodeType.OPEN)
					{
						--y;
						if(i++ >= this.entity.getMaxFallHeight())
							return null;
						type = this.getPathNodeType(this.entity, x, y, z);
						priority = this.entity.getPathPriority(type);
						if(type!=PathNodeType.OPEN&&priority >= 0.0F)
						{
							point = this.openPoint(x, y, z);
							point.nodeType = type;
							point.costMalus = Math.max(point.costMalus, priority);
							if(this.breakableFlag)
								point.costMalus++;
							break;
						}
						if(priority < 0.0F)
							return null;
					}
				}
			}
			return point;
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

	protected PathNodeType defaultNode(IBlockAccess world, IBlockState state, BlockPos pos, Block block)
	{
		Material material = state.getMaterial();
		PathNodeType type = block.getAiPathNodeType(state, world, pos, this.entity);

		if(type!=null)
			return type;

		if(material==Material.AIR||state.getCollisionBoundingBox(world, pos)==Block.NULL_AABB)
			return PathNodeType.OPEN;

		TileEntity te = world.getTileEntity(pos);

		if(block instanceof BlockDoor)
			return HansUtils.getDoorNode(state);
		else if(te instanceof TileEntityGateBase)
			return HansUtils.getGateNode(((TileEntityGateBase<?>)te));
		else if(block instanceof BlockFenceGate)
			return HansUtils.getFenceGateNode(state);

		else if(block instanceof BlockTrapDoor||block instanceof BlockLilyPad)
			return PathNodeType.TRAPDOOR;

		else if(te instanceof TileEntityRazorWire)
			return PathNodeType.DANGER_OTHER;
		else if(block instanceof BlockFire)
			return PathNodeType.DAMAGE_FIRE;
		else if(block instanceof BlockCactus)
			return PathNodeType.DAMAGE_CACTUS;

		else if(block instanceof BlockRailBase)
			return PathNodeType.RAIL;

		else if(this.isFence(world, state, pos, block))
			return PathNodeType.FENCE;

		else if(material==Material.WATER)
			return PathNodeType.WATER;
		else if(material==Material.LAVA)
			return PathNodeType.LAVA;
		else
			return block.isPassable(world, pos)?PathNodeType.OPEN: PathNodeType.BLOCKED;
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
		if(block instanceof BlockFence||block instanceof BlockWall)
			return true;
		else if(this.entity!=null)
		{
			AxisAlignedBB collision2 = new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(blockpos.up());
			List<AxisAlignedBB> list = Lists.newArrayList();
			iblockstate.addCollisionBoxToList(this.entity.world, blockpos, collision2, list, null, false);
			return !list.isEmpty();
		}
		return false;
	}
}
