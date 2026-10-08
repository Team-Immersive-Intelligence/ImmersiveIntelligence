package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;

import javax.annotation.Nonnull;

/**
 * Stores energy and checks the full cost before an action.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.10.2026
 */
public class IIEnergyStorage extends FluxStorageAdvanced
{
	public IIEnergyStorage(int capacity)
	{
		super(capacity);
	}

	public IIEnergyStorage(int capacity, int limitTransfer)
	{
		super(capacity, limitTransfer);
	}

	public IIEnergyStorage(int capacity, int limitReceive, int limitExtract)
	{
		super(capacity, limitReceive, limitExtract);
	}

	/**
	 * Checks stored energy and the extraction limit without draining energy.
	 */
	public boolean hasEnergy(int amount)
	{
		return amount >= 0&&(amount==0||extractEnergy(amount, true)==amount);
	}

	/**
	 * Drains the full cost, or leaves the storage unchanged.
	 */
	public boolean tryConsumeEnergy(int amount, boolean simulate)
	{
		if(!hasEnergy(amount))
			return false;
		if(!simulate&&amount > 0)
			extractEnergy(amount, false);
		return true;
	}

	public boolean tryConsumeEnergy(int amount)
	{
		return tryConsumeEnergy(amount, false);
	}

	/**
	 * Runs an action after its full energy cost is drained.
	 */
	public boolean runIfPowered(int amount, @Nonnull Runnable action)
	{
		if(!tryConsumeEnergy(amount))
			return false;
		action.run();
		return true;
	}

	@Override
	public IIEnergyStorage setDecayFactor(double factor)
	{
		super.setDecayFactor(factor);
		return this;
	}
}
