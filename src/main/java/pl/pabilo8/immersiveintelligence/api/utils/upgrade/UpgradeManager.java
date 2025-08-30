package pl.pabilo8.immersiveintelligence.api.utils.upgrade;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class responsible for installation, removal and storing installed upgrades of a machine based on a designated {@link UpgradeTechTree}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.12.2023
 */
public class UpgradeManager<T extends TileEntity & IUpgradableDevice> implements INBTSerializable<NBTTagCompound>
{
	private final T tile;
	private final ArrayList<Upgrade> upgrades = new ArrayList<>();
	private final UpgradeTechTree techTree;
	private Upgrade currentlyInstalled = null;
	private int upgradeProgress = 0, clientUpgradeProgress = 0;
	private float maxClientUpgradeProgress = 0;

	public UpgradeManager(T tile)
	{
		this.tile = tile;
		this.techTree = UpgradeTechTree.getTreeFor(tile);
	}

	public UpgradeManager(T tile, UpgradeTechTree techTree)
	{
		this.tile = tile;
		this.techTree = techTree;
	}

	public void update()
	{
		if(tile.getWorld().isRemote&&clientUpgradeProgress < maxClientUpgradeProgress)
			clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrenchUpgradeProgress/2f), maxClientUpgradeProgress);
	}

	//--- NBT ---//

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		//New
		tag.setString("currentlyInstalled", currentlyInstalled==null?"": currentlyInstalled.toString());
		tag.setInteger("upgradeProgress", upgradeProgress);
		tag.setInteger("clientUpgradeProgress", clientUpgradeProgress);

		return tag;
	}

	public void deserializeNBT(NBTTagCompound tag)
	{
		upgrades.clear();

		//New
		tag.getTagList("upgrades", 8).tagList
				.stream()
				.map(NBTBase::toString)
				.map(ResLoc::of)
				.map(Upgrade::getUpgradeByID)
				.filter(Objects::nonNull)
				.forEach(upgrades::add);

		String currentId = tag.getString("currentlyInstalled");
		this.currentlyInstalled = currentId.isEmpty()?null: Upgrade.getUpgradeByID(ResLoc.of(currentId));
		this.upgradeProgress = tag.getInteger("upgradeProgress");
		this.clientUpgradeProgress = tag.getInteger("clientUpgradeProgress");
		this.maxClientUpgradeProgress = UpgradeUtils.getMaxClientProgress(upgradeProgress, currentlyInstalled);

		//Legacy
		tag.getKeySet().stream()
				.map(Upgrade::getUpgradeByID)
				.filter(Objects::nonNull)
				.forEach(upgrades::add);
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
		if(upgradeProgress >= currentlyInstalled.getProgressRequired())
			add(currentlyInstalled, UpgradeOperation.FORCE_ADD);
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

	//--- Utils ---//

	private void sendTileUpdate()
	{
		if(tile instanceof TileEntityMultiblockIIBase<?>)
			((TileEntityMultiblockIIBase<?>)tile).updateTileForEvent(SyncEvents.TILE_UPGRADES_MODIFIED);
	}
}
