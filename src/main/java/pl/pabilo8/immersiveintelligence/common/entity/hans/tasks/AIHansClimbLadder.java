package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

/**
 * @author Flemmli97
 * @since 28.12.2021
 * <p>
 * Stolen (rightfully, kinda) from Improved Mobs. Huge thanks to Flemmli97 for this piece of code.<br>
 * <a href="https://github.com/Flemmli97/ImprovedMobs/blob/1.12.2/src/main/java/com/flemmli97/improvedmobs/entity/ai/EntityAIClimbLadder.java">https://github.com/Flemmli97/ImprovedMobs/blob/1.12.2/src/main/java/com/flemmli97/improvedmobs/entity/ai/EntityAIClimbLadder.java</a><br>
 * Code was adapted to II's Hanses.
 * </p>
 */
public class AIHansClimbLadder extends AIHansBase
{
	private Path path;

	public AIHansClimbLadder(EntityHans hans)
	{
		super(hans);
		this.setMutexBits(4);
	}

	@Override
	public void setRequiredAnimation()
	{
		// TODO: 28.12.2021 ladder climb animation
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.hans.getNavigator().noPath())
		{
			this.path = this.hans.getNavigator().getPath();
			return this.path!=null&&this.hans.isOnLadder();
		}
		return false;
	}

	@Override
	public void updateTask()
	{
		int i = this.path.getCurrentPathIndex();
		if(i+1 < this.path.getCurrentPathLength())
		{
			int y = this.path.getPathPointFromIndex(i).y;//this.living.getPosition().getY();
			PathPoint pointNext = this.path.getPathPointFromIndex(i+1);
			IBlockState down = this.hans.world.getBlockState(this.hans.getPosition().down());
			if(pointNext.y < y||(pointNext.y==y&&!down.getBlock().isLadder(down, this.hans.world, this.hans.getPosition().down(), this.hans)))
				this.hans.motionY = -0.15;
			else
				this.hans.motionY = 0.15;
		}
	}
}