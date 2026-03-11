package pl.pabilo8.immersiveintelligence.api.upgrade;

import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * An extension of {@link IUpgradableDevice} that uses an internal {@link UpgradeManager} to handle upgrade installation, removal and storage.
 *
 * @param <T> the upgradable device class implementing this interface
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.12.2023
 */
public interface IManagedUpgradableDevice<T extends IUpgradableDevice> extends IUpgradableDevice
{
	@Nonnull
	UpgradeManager<T> getUpgradeManager();

	@Override
	default boolean addUpgrade(Upgrade upgrade, UpgradeOperation operation)
	{
		return getUpgradeManager().add(upgrade, operation);
	}

	@Override
	default boolean isUpgradeInstalled(Upgrade upgrade)
	{
		return getUpgradeManager().has(upgrade);
	}

	@Override
	default List<Upgrade> getAllInstalledUpgrades()
	{
		return getUpgradeManager().getAllInstalled();
	}

	@Nullable
	@Override
	default Upgrade getCurrentUpgrade()
	{
		return getUpgradeManager().getCurrentUpgrade();
	}

	@Override
	default int getUpgradeInstallProgress(boolean clientProgress)
	{
		return getUpgradeManager().getInstallProgress(clientProgress);
	}

	@Override
	default boolean addUpgradeInstallProgress(int toAdd)
	{
		return getUpgradeManager().addInstallProgress(toAdd);
	}

	@Override
	default boolean resetInstallProgress()
	{
		return getUpgradeManager().resetInstallProgress();
	}

	@Override
	default boolean removeUpgrade(Upgrade upgrade)
	{
		return getUpgradeManager().remove(upgrade);
	}
}
