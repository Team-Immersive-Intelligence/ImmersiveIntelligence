package pl.pabilo8.immersiveintelligence.api.utils.vehicles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public interface IUpgradableMachine
{
	boolean canFitUpgrade(MachineUpgrade upgrade);

	boolean hasUpgrade(MachineUpgrade upgrade);

	<T extends TileEntity & IUpgradableMachine> T getUpgradeMaster();

	void saveUpgradesToNBT(NBTTagCompound tag);

	void getUpgradesFromNBT(NBTTagCompound tag);
}
