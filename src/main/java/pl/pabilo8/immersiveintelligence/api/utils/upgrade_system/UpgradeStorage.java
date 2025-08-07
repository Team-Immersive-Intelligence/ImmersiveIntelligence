package pl.pabilo8.immersiveintelligence.api.utils.upgrade_system;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.12.2023
 */
public class UpgradeStorage<T extends TileEntity & IUpgradableMachine> implements INBTSerializable<NBTTagCompound>
{
	//REFACTOR: 12.12.2023 move to capabilities
	private final T tile;
	private final ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
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

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		for(MachineUpgrade upgrade : upgrades)
			tag.setBoolean(upgrade.getName(), true);
		return tag;
	}

	public void deserializeNBT(NBTTagCompound tag)
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

	private void sendTileUpdate()
	{
		//TODO: 07.08.2025 change to IIBase once functionality is moved
		if(tile instanceof TileEntityMultiblockIIGeneric<?>)
			((TileEntityMultiblockIIGeneric<?>)tile).updateTileForEvent(SyncEvents.TILE_UPGRADES_MODIFIED);
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
			sendTileUpdate();
			return true;
		}
		return false;
	}

	public void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		currentlyInstalled = upgrade;
		upgradeProgress = 0;
		clientUpgradeProgress = 0;
		sendTileUpdate();
	}

	public void removeUpgrade(MachineUpgrade upgrade)
	{
		upgrades.remove(upgrade);
		sendTileUpdate();
	}

	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return upgrades.contains(upgrade);
	}

	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		if(!test&&!hasUpgrade(upgrade))
		{
			upgrades.add(upgrade);
			sendTileUpdate();
			return true;
		}
		return false;
	}
}
