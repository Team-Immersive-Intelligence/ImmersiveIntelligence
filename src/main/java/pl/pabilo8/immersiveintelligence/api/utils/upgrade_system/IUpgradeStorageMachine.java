package pl.pabilo8.immersiveintelligence.api.utils.upgrade_system;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IUpgradeStorageMachine<T extends TileEntity & IUpgradableMachine> extends IUpgradableMachine
{
	UpgradeStorage<T> getUpgradeStorage();

	@Override
	default boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		return getUpgradeStorage().addUpgrade(upgrade, test);
	}

	@Override
	default boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return getUpgradeStorage().hasUpgrade(upgrade);
	}

	//upgradeMatches

	/**
	 * @deprecated Do not use, save upgradeStorage to NBT instead
	 */
	@Override
	@Deprecated
	default void saveUpgradesToNBT(NBTTagCompound tag)
	{

	}

	/**
	 * @deprecated Do not use, save upgradeStorage to NBT instead
	 */
	@Override
	@Deprecated
	default void getUpgradesFromNBT(NBTTagCompound tag)
	{

	}

	@Override
	default List<MachineUpgrade> getUpgrades()
	{
		return getUpgradeStorage().getUpgrades();
	}

	@Nullable
	@Override
	default MachineUpgrade getCurrentlyInstalled()
	{
		return getUpgradeStorage().getCurrentlyInstalled();
	}

	@Override
	default int getInstallProgress()
	{
		return getUpgradeStorage().getInstallProgress();
	}

	@SideOnly(Side.CLIENT)
	@Override
	default int getClientInstallProgress()
	{
		return getUpgradeStorage().getClientInstallProgress();
	}

	@Override
	default boolean addUpgradeInstallProgress(int toAdd)
	{
		return getUpgradeStorage().addUpgradeInstallProgress(toAdd);
	}

	@Override
	default boolean resetInstallProgress()
	{
		return getUpgradeStorage().resetInstallProgress();
	}

	@Override
	default void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		getUpgradeStorage().startUpgrade(upgrade);
	}

	@Override
	default void removeUpgrade(MachineUpgrade upgrade)
	{
		getUpgradeStorage().removeUpgrade(upgrade);
	}
}
