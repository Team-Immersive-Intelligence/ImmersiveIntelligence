package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;

public class TileEntityWheelIron extends TileEntityWheelBase
{
	@Override
	protected int getRenderRadiusIncrease()
	{
		return limitType!=null?limitType.getMaxLength(): 2;
	}

	@Override
	public float getRadius()
	{
		return 6;
	}

	@Override
	protected boolean canConnectBelt(MotorBeltType cableType)
	{
		return cableType.getBeltCategory().equals(IIRotaryUtils.BELT_CATEGORY);
	}
}
