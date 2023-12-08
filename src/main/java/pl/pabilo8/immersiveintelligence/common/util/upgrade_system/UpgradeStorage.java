package pl.pabilo8.immersiveintelligence.common.util.upgrade_system;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 03.12.2023
 */
public class UpgradeStorage<T extends TileEntity>
{
	private T tile;
	private ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
	private MachineUpgrade currentlyInstalled = null;
	private int upgradeProgress = 0, clientUpgradeProgress = 0;

	public UpgradeStorage(T tile)
	{
		this.tile = tile;
	}

	public void update()
	{
		if(tile.getWorld().isRemote&&clientUpgradeProgress < getMaxClientProgress())
			clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrenchUpgradeProgress/2f), getMaxClientProgress());
	}

	public void saveUpgradesToNBT(NBTTagCompound tag)
	{
		for(MachineUpgrade upgrade : upgrades)
			tag.setBoolean(upgrade.getName(), true);
	}

	public void getUpgradesFromNBT(NBTTagCompound tag)
	{
		upgrades.clear();
		upgrades.addAll(MachineUpgrade.getUpgradesFromNBT(tag));
	}

	public ArrayList<MachineUpgrade> getUpgrades()
	{
		return upgrades;
	}

	@Nullable
	public MachineUpgrade getCurrentlyInstalled()
	{
		return currentlyInstalled;
	}

	public int getInstallProgress()
	{
		return upgradeProgress;
	}

	public int getClientInstallProgress()
	{
		return clientUpgradeProgress;
	}

	float getMaxClientProgress()
	{
		if(getCurrentlyInstalled()!=null)
			return IIUtils.getMaxClientProgress(getInstallProgress(), getCurrentlyInstalled().getProgressRequired(), getCurrentlyInstalled().getSteps());
		return getInstallProgress();
	}

	public boolean addUpgradeInstallProgress(int toAdd)
	{
		upgradeProgress += toAdd;
		return true;
	}

	public boolean resetInstallProgress()
	{
		currentlyInstalled = null;
		if(upgradeProgress > 0)
		{
			upgradeProgress = 0;
			clientUpgradeProgress = 0;
			return true;
		}
		return false;
	}

	public void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		currentlyInstalled = upgrade;
		upgradeProgress = 0;
		clientUpgradeProgress = 0;
	}

	public void removeUpgrade(MachineUpgrade upgrade)
	{
		upgrades.remove(upgrade);
	}
}
