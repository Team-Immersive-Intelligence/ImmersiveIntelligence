package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */
public class EmplacementTaskShells extends EmplacementTaskEntities
{
	public EmplacementTaskShells()
	{
		super(input -> input instanceof EntityAmmoArtilleryProjectile&&!input.isDead);
	}

	@Override
	public String getName()
	{
		return "target_shells";
	}
}
