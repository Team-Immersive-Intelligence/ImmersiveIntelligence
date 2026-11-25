package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack;

import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;

/**
 * Stores a parachute backpack while it is placed.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class TileEntityParachuteBackpack extends TileEntityBackpack
{
	public TileEntityParachuteBackpack()
	{
		super(BackpackType.PARACHUTE);
	}
}
