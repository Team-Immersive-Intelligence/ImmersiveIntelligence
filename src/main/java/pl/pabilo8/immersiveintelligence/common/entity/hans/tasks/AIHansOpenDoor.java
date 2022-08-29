package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansPathNavigate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 02.02.2022
 */
public class AIHansOpenDoor extends AIHansBase
{
	public static final ArrayList<IDoorHandler> HANDLERS = new ArrayList<>();

	static
	{
		//Doors
		HANDLERS.add(new IDoorHandler()
		{
			@Override
			public boolean matchesType(IBlockState state, @Nullable TileEntity te)
			{
				return state.getBlock() instanceof BlockDoor&&state.getMaterial()==Material.WOOD;
			}

			@Override
			public void toggleDoor(EntityHans hans, BlockPos pos, boolean open, World world)
			{
				IBlockState state = world.getBlockState(pos);
				if(state.getBlock() instanceof BlockDoor)
					((BlockDoor)state.getBlock()).toggleDoor(world, pos, open);
			}

			@Override
			public boolean isDoorOpened(EntityHans hans, BlockPos pos, World world)
			{
				IBlockState state = world.getBlockState(pos);
				return state.getBlock() instanceof BlockDoor?state.getValue(BlockDoor.OPEN): Boolean.valueOf(false);
			}

			@Override
			public int getTimeForDoor()
			{
				return 20;
			}
		});

		//Fence Gates
		HANDLERS.add(new IDoorHandler()
		{
			@Override
			public boolean matchesType(IBlockState state, @Nullable TileEntity te)
			{
				return state.getBlock() instanceof BlockFenceGate;
			}

			@Override
			public void toggleDoor(EntityHans hans, BlockPos pos, boolean open, World world)
			{
				IBlockState state = world.getBlockState(pos);

				if(state.getValue(BlockFenceGate.OPEN)==open)
					return;
				boolean blockOpen = state.getValue(BlockFenceGate.OPEN);

				state = state.withProperty(BlockFenceGate.OPEN, open);

				if(!blockOpen)
				{
					EnumFacing enumfacing = EnumFacing.fromAngle(hans.rotationYaw);
					if(state.getValue(BlockFenceGate.FACING)==enumfacing.getOpposite())
						state = state.withProperty(BlockFenceGate.FACING, enumfacing);
				}

				world.setBlockState(pos, state, 10);
				world.playEvent(null, open?1008: 1014, pos, 0);
			}

			@Override
			public boolean isDoorOpened(EntityHans hans, BlockPos pos, World world)
			{
				IBlockState state = world.getBlockState(pos);
				return state.getBlock() instanceof BlockFenceGate?state.getValue(BlockFenceGate.OPEN): Boolean.valueOf(false);
			}

			@Override
			public int getTimeForDoor()
			{
				return 20;
			}
		});

		//Multiblock Gates
		HANDLERS.add(new IDoorHandler()
		{
			@Override
			public boolean matchesType(IBlockState state, @Nullable TileEntity te)
			{
				return te instanceof TileEntityGateBase;
			}

			@Override
			public void toggleDoor(EntityHans hans, BlockPos pos, boolean open, World world)
			{
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntityGateBase<?>)
				{
					TileEntityMultiblockMetal<?, ?> master = ((TileEntityGateBase<?>)te).master();

					if(master instanceof TileEntityGateBase<?>)
					{
						if(!world.isRemote)
							((TileEntityGateBase<?>)master).onAnimationChangeServer(open, 0);
						else
							((TileEntityGateBase<?>)master).onAnimationChangeClient(open, 0);
					}

				}
			}

			@Override
			public boolean isDoorOpened(EntityHans hans, BlockPos pos, World world)
			{
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntityGateBase<?>)
				{
					TileEntityMultiblockMetal<?, ?> master = ((TileEntityGateBase<?>)te).master();
					if(master instanceof TileEntityGateBase)
						return ((TileEntityGateBase<?>)master).open;
				}
				return false;
			}

			@Override
			public int getTimeForDoor()
			{
				return 50;
			}
		});

	}

	protected BlockPos doorPosition = BlockPos.ORIGIN;
	/**
	 * The wooden door block
	 */
	protected IDoorHandler doorHandler;
	/**
	 * If is true then the Entity has stopped Door Interaction and compoleted the task.
	 */
	boolean hasStoppedDoorInteraction;
	float entityPositionX;
	float entityPositionZ;
	/**
	 * If the entity close the door
	 */
	boolean closeDoor;
	/**
	 * The temporisation before the entity close the door (in ticks, always 20 = 1 second)
	 */
	int closeDoorTemporisation;

	public AIHansOpenDoor(EntityHans hans, boolean shouldClose)
	{
		super(hans);
		this.closeDoor = shouldClose;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(!this.hans.collidedHorizontally)
			return false;
		else
		{
			HansPathNavigate navigator = this.hans.getNavigator();
			Path path = navigator.getPath();

			if(path!=null&&!path.isFinished()&&navigator.getEnterDoors())
			{
				for(int i = 0; i < Math.min(path.getCurrentPathIndex()+2, path.getCurrentPathLength()); ++i)
				{
					PathPoint pathpoint = path.getPathPointFromIndex(i);
					this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y+1, pathpoint.z);

					if(this.hans.getDistanceSq(this.doorPosition.getX(), this.hans.posY, this.doorPosition.getZ()) <= 2.25D)
					{
						this.doorHandler = this.getDoorHandler(this.doorPosition);

						if(this.doorHandler!=null)
							return true;
					}
				}

				this.doorPosition = (new BlockPos(this.hans));
				this.doorHandler = this.getDoorHandler(this.doorPosition);

				Optional<AIHansOpenDoor> otherTask = hans.world.getEntitiesWithinAABB(EntityHans.class, new AxisAlignedBB(doorPosition).grow(4), input -> input!=hans&&input.isEntityAlive())
						.stream()
						.map(EntityHans::getDoorTask)
						.filter(Objects::nonNull)
						.filter(ai -> ai.doorPosition==doorPosition)
						.findFirst();

				return !otherTask.isPresent()&&this.doorHandler!=null;
			}
			else
			{
				doorPosition = BlockPos.ORIGIN;
				return false;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return this.closeDoor&&this.closeDoorTemporisation > 0&&!this.hasStoppedDoorInteraction;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.closeDoorTemporisation = this.doorHandler.getTimeForDoor();
		this.doorHandler.toggleDoor(this.hans, this.doorPosition, true, this.hans.world);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		--this.closeDoorTemporisation;
		float xx = (float)((double)((float)this.doorPosition.getX()+0.5F)-this.hans.posX);
		float zz = (float)((double)((float)this.doorPosition.getZ()+0.5F)-this.hans.posZ);
		float dist = this.entityPositionX*xx+this.entityPositionZ*zz;

		if(dist < 0.0F)
			this.hasStoppedDoorInteraction = true;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		if(this.closeDoor)
			this.doorHandler.toggleDoor(this.hans, this.doorPosition, false, this.hans.world);
	}

	private IDoorHandler getDoorHandler(BlockPos pos)
	{
		IBlockState state = this.hans.world.getBlockState(pos);
		TileEntity tile = this.hans.world.getTileEntity(pos);
		return HANDLERS.stream()
				.filter(iDoorHandler -> iDoorHandler.matchesType(state, tile))
				.findFirst()
				.orElse(null);
	}

	@Override
	public void setRequiredAnimation()
	{
		// TODO: 02.02.2022 door open animation 
	}

	public interface IDoorHandler
	{
		boolean matchesType(IBlockState state, @Nullable TileEntity te);

		void toggleDoor(EntityHans hans, BlockPos pos, boolean open, World world);

		boolean isDoorOpened(EntityHans hans, BlockPos pos, World world);

		int getTimeForDoor();
	}
}
