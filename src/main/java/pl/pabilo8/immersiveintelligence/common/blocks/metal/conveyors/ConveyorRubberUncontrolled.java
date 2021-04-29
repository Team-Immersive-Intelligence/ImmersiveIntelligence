package pl.pabilo8.immersiveintelligence.common.blocks.metal.conveyors;

import net.minecraft.tileentity.TileEntity;

/**
 * @author Pabilo8
 * @since 29.04.2021
 */
public class ConveyorRubberUncontrolled extends ConveyorRubber
{
	@Override
	public boolean isActive(TileEntity tile)
	{
		return true;
	}
}
