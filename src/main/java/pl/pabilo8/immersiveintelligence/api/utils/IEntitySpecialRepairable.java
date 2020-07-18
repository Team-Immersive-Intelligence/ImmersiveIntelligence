package pl.pabilo8.immersiveintelligence.api.utils;

/**
 * @author Pabilo8
 * @since 02-07-2020
 */
public interface IEntitySpecialRepairable
{
	boolean canRepair();

	boolean repair(int repairPoints);

	int getRepairCost();
}
