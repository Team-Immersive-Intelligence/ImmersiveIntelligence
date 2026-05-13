package pl.pabilo8.immersiveintelligence.api.upgrade;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class responsible for installation, removal and storing installed upgrades of a machine (a {@link net.minecraft.tileentity.TileEntity} or {@link net.minecraft.entity.Entity}( based on a designated {@link UpgradeTechTree}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.12.2023
 */
public class UpgradeManager<T extends IUpgradableDevice> implements INBTSerializable<NBTTagCompound>
{
	private final T parent;

	private final ArrayList<Upgrade> upgrades = new ArrayList<>();
	private final UpgradeTechTree techTree;
	private Upgrade currentlyInstalled = null;
	private int upgradeProgress = 0, clientUpgradeProgress = 0;
	private float maxClientUpgradeProgress = 0;

	public UpgradeManager(T parent)
	{
		this.parent = parent;
		this.techTree = UpgradeTechTree.getTreeFor(parent);
	}

	public UpgradeManager(T parent, UpgradeTechTree techTree)
	{
		this.parent = parent;
		this.techTree = techTree;
	}

	public void update()
	{
		if(parent.getIIWorld().isRemote&&clientUpgradeProgress < maxClientUpgradeProgress)
			clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrenchUpgradeProgress/2f), maxClientUpgradeProgress);
	}

	//--- NBT ---//

	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.conditionally(currentlyInstalled!=null,
						nbt -> nbt.withString("currently_installed", currentlyInstalled.getId().toString()))
				.withInt("upgrade_progress", upgradeProgress)
				.withInt("client_upgrade_progress", clientUpgradeProgress)
				.withList("upgrades", upgrade -> new NBTTagString(upgrade.getId().toString()), upgrades)
				.unwrap();
	}

	public void deserializeNBT(NBTTagCompound tag)
	{
		this.upgrades.clear();
		this.currentlyInstalled = null;
		EasyNBT enbt = EasyNBT.wrapNBT(tag);

		enbt.streamList(NBTTagString.class, "upgrades")
				.map(NBTTagString::getString)
				.map(ResLoc::of)
				.map(Upgrade::getUpgradeByID)
				.filter(Objects::nonNull)
				.forEach(upgrades::add);
		enbt.checkSetString("currently_installed", s ->
				this.currentlyInstalled = Upgrade.getUpgradeByID(ResLoc.of(s)));
		this.upgradeProgress = enbt.getInt("upgrade_progress");
		this.clientUpgradeProgress = enbt.getInt("client_upgrade_progress");
		this.maxClientUpgradeProgress = UpgradeUtils.getMaxClientProgress(upgradeProgress, currentlyInstalled);
	}

	//--- Upgrade Handling ---//

	@Nonnull
	public List<Upgrade> getAllInstalled()
	{
		return upgrades;
	}

	@Nullable
	public Upgrade getCurrentUpgrade()
	{
		return currentlyInstalled;
	}

	public int getInstallProgress(boolean client)
	{
		if(currentlyInstalled==null)
			return 0;
		return client?clientUpgradeProgress: upgradeProgress;
	}

	public boolean addInstallProgress(int toAdd)
	{
		if(currentlyInstalled==null)
			return false;

		upgradeProgress += toAdd;
		maxClientUpgradeProgress = UpgradeUtils.getMaxClientProgress(upgradeProgress, currentlyInstalled);
		if(upgradeProgress >= currentlyInstalled.getProgressRequired())
		{
			parent.addUpgrade(currentlyInstalled, UpgradeOperation.FORCE_ADD);
			sendTileUpdate();
		}
		return true;
	}

	public boolean remove(Upgrade upgrade)
	{
		upgrades.remove(upgrade);
		sendTileUpdate();
		return true;
	}

	public boolean resetInstallProgress()
	{
		currentlyInstalled = null;
		if(upgradeProgress > 0)
		{
			upgradeProgress = 0;
			clientUpgradeProgress = 0;
			maxClientUpgradeProgress = 0;
			sendTileUpdate();
			return true;
		}
		return false;
	}

	public boolean has(Upgrade upgrade)
	{
		return upgrades.contains(upgrade);
	}

	public boolean add(Upgrade upgrade, UpgradeOperation operation)
	{
		//Check if the upgrade exists within the tech tree and is not already installed
		if(!techTree.isUpgradeAvailable(upgrades, upgrade))
			return false;

		//Perform operation
		switch(operation)
		{
			//Just a check
			case PROBE:
				return true;
			//Begin upgrade installation
			case INSTALL:
			{
				if(currentlyInstalled!=null)
					return false;
				currentlyInstalled = upgrade;
				maxClientUpgradeProgress = UpgradeUtils.getMaxClientProgress(upgradeProgress, currentlyInstalled);
				upgradeProgress = 0;
				clientUpgradeProgress = 0;
				sendTileUpdate();
				return true;
			}
			//Add the upgrade without an installation
			case FORCE_ADD:
			{
				//Clear currently installed upgrade if forced to add it
				if(currentlyInstalled==upgrade)
				{
					currentlyInstalled = null;
					upgradeProgress = 0;
					clientUpgradeProgress = 0;
					maxClientUpgradeProgress = 0;
				}
				//Add the upgrade
				upgrades.add(upgrade);
				sendTileUpdate();
				return true;
			}
			//Whatever you want to do, do not do it, or Carver will visit you tomorrow at 4AM wielding an II Autocannon
			default:
				return false;
		}
	}

	//--- HashCode ---//

	@Override
	public int hashCode()
	{
		return upgrades.hashCode();
	}

	//--- Utils ---//

	private void sendTileUpdate()
	{
		if(parent instanceof TileEntityMultiblockIIBase<?>)
			((TileEntityMultiblockIIBase<?>)parent).updateTileForEvent(SyncEvents.TILE_UPGRADES_MODIFIED);
	}
}
