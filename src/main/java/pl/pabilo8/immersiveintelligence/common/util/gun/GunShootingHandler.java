package pl.pabilo8.immersiveintelligence.common.util.gun;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Used to store and calculate gun recoil and overheating.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.05.2026
 */
public class GunShootingHandler implements INBTSerializable<NBTTagCompound>
{
	private float shotDelay, maxShotDelay;
	private float loadingProgress, maxLoadingProgress;
	private boolean startLoading = false;

	public GunShootingHandler()
	{

	}

	public void update()
	{
		shotDelay = Math.max(0, shotDelay-1);
	}

	//--- With ---//

	public GunShootingHandler withMaxShotDelay(float maxShotDelay)
	{
		this.maxShotDelay = maxShotDelay;
		return this;
	}

	//--- Getters ---//

	public float getShotDelay(float partialTicks)
	{
		return Math.max(0, shotDelay-partialTicks);
	}

	public float getLoadingProgress(float partialTicks)
	{
		return startLoading?Math.min(maxLoadingProgress, loadingProgress+partialTicks): 0;
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("shot_delay", shotDelay);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		shotDelay = nbt.getFloat("shot_delay");
	}
}
