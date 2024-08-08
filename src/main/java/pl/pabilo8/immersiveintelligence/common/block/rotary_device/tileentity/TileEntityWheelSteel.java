package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;

public class TileEntityWheelSteel extends TileEntityWheelBase
{
	@Override
	protected int getRenderRadiusIncrease()
	{
		return limitType!=null?limitType.getMaxLength(): 2;
	}

	@Override
	public float getRadius()
	{
		return 8;
	}

	@Override
	protected boolean canConnectBelt(MotorBeltType cableType)
	{
		return false;
	}
}
