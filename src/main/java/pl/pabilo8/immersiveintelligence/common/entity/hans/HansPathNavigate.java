package pl.pabilo8.immersiveintelligence.common.entity.hans;

import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 28.12.2021
 */
public class HansPathNavigate extends PathNavigateGround
{
	public HansPathNavigate(EntityHans hans, World worldIn)
	{
		super(hans, worldIn);
	}

	@Nonnull
	@Override
	protected PathFinder getPathFinder()
	{
		this.nodeProcessor = new HansWalkNodeProcessor();
		this.nodeProcessor.setCanEnterDoors(true);
		this.nodeProcessor.setCanOpenDoors(true);
		this.nodeProcessor.setCanSwim(true);
		return new PathFinder(this.nodeProcessor);
	}

	@Nonnull
	@Override
	public HansWalkNodeProcessor getNodeProcessor()
	{
		return ((HansWalkNodeProcessor)nodeProcessor);
	}

	@Override
	public boolean canEntityStandOnPos(BlockPos pos)
	{
		IBlockState state = this.world.getBlockState(pos);
		IBlockState dState = this.world.getBlockState(pos.down());

		//do not walk into blocks marked as "damage" or "blocked"
		return getNodeProcessor().defaultNode(world, state, pos, state.getBlock()).getPriority() >= 0&&dState.isFullBlock();
	}
}
