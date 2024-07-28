package pl.pabilo8.immersiveintelligence.common.util.upgrade_system;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;

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

	@Override
	default void saveUpgradesToNBT(NBTTagCompound tag)
	{
		tag.setTag("upgrades", getUpgradeStorage().saveUpgradesToNBT());
	}

	@Override
	default void getUpgradesFromNBT(NBTTagCompound tag)
	{
		getUpgradeStorage().getUpgradesFromNBT(tag.getCompoundTag("upgrades"));
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
