package pl.pabilo8.immersiveintelligence.common.entity.hans;

import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

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

	@Override
	protected PathFinder getPathFinder()
	{
		this.nodeProcessor = new HansWalkNodeProcessor();
		this.nodeProcessor.setCanEnterDoors(true);
		this.nodeProcessor.setCanOpenDoors(true);
		this.nodeProcessor.setCanSwim(true);
		return new PathFinder(this.nodeProcessor);
	}
}
