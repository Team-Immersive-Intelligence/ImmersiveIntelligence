package pl.pabilo8.immersiveintelligence.api.upgrade;

import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.common.util.IWorldPosProvider;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.07.2020
 */
public interface IUpgradableDevice extends IWorldPosProvider
{
	/**
	 * Returns the master tile entity of this multiblock, or itself if not a part of a multiblock.
	 *
	 * @return the master tile entity
	 */
	IUpgradableDevice master();

	/**
	 * The tier of the device, purely cosmetic, changes the style of the upgrade GUI.
	 *
	 * @return the device tier
	 */
	default MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.WOODEN;
	}

	/**
	 * Adds an upgrade, begins installation, or checks if an upgrade can be added to the machine.
	 * If the machine is currently installing an upgrade, it will not accept new ones.
	 *
	 * @param upgrade   the upgrade to add
	 * @param operation determines what happens to the machine by doing this action
	 * @return true if the upgrade can be added, false otherwise
	 */
	boolean addUpgrade(Upgrade upgrade, UpgradeOperation operation);

	/**
	 * Checks if the machine has an upgrade installed
	 *
	 * @param upgrade upgrade to check for
	 * @return true if the upgrade is installed, false otherwise
	 */
	boolean isUpgradeInstalled(Upgrade upgrade);

	/**
	 * Removes an installed upgrade. Does nothing if the upgrade is not installed.
	 *
	 * @param upgrade the upgrade to remove
	 * @return true if the upgrade was removed, false otherwise
	 */
	boolean removeUpgrade(Upgrade upgrade);

	/**
	 * @return list of all upgrades installed on the machine
	 */
	List<Upgrade> getAllInstalledUpgrades();

	/**
	 * @return the upgrade currently being installed, null if none
	 */
	@Nullable
	Upgrade getCurrentUpgrade();

	/**
	 * @return the current progress of the upgrade being installed, or 0 if none
	 */
	int getUpgradeInstallProgress(boolean clientProgress);

	/**
	 * Adds to the install progress of the currently installing upgrade. Does nothing if no upgrade is being installed.
	 * If progress exceeds that of the required amount, the upgrade is installed and progress is reset.
	 *
	 * @param toAdd amount to add
	 * @return true if progress was added, false otherwise
	 */
	boolean addUpgradeInstallProgress(int toAdd);

	/**
	 * Resets the install progress of the currently installing upgrade, cancelling the installation.
	 *
	 * @return true if progress was reset, false otherwise
	 */
	boolean resetInstallProgress();
}
