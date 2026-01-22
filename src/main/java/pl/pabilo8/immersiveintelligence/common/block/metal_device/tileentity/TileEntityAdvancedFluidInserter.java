package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AdvancedFluidInserter;

/**
 * Fluid variant of the inserter
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.01.2026
 */
public class TileEntityAdvancedFluidInserter extends TileEntityFluidInserter
{
	@Override
	public int getEnergyUsage()
	{
		return AdvancedFluidInserter.energyUsage;
	}

	@Override
	public int getEnergyCapacity()
	{
		return AdvancedFluidInserter.energyCapacity;
	}

	@Override
	public int getMaxTakeAmount()
	{
		return AdvancedFluidInserter.maxTake;
	}

	@Override
	public int getPickupSpeed()
	{
		return AdvancedFluidInserter.taskTime;
	}
}
