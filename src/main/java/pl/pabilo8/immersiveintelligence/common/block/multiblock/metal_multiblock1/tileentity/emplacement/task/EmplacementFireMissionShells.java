package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.02.2024
 */
public class EmplacementFireMissionShells extends EmplacementFireMissionEntities
{
	public EmplacementFireMissionShells()
	{
		super();
	}

	@Override
	public boolean shouldContinue()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "target_shells";
	}

	@Override
	public void updateTargets(TileEntityEmplacement emplacement)
	{

	}
}
