package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.WireType;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 20.07.2026
 * @ii-approved 0.3.1
 * @since 08.08.2024
 */
public class TileEntityWheelSteel extends TileEntityWheelBase
{
	@Override
	public boolean acceptsWireType(WireType category)
	{
		return (IIRotaryUtils.BELT_CATEGORY.equals(category.getCategory())||
				IIRotaryUtils.TRACK_CATEGORY.equals(category.getCategory()));
	}

	@Override
	public float getRadius()
	{
		return 8;
	}
}
