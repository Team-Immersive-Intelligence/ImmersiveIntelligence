package pl.pabilo8.immersiveintelligence.api.utils.minecart;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
public abstract class EntityMinecartEnergyContainer extends EntityMinecartII implements IFluxReceiver
{
	private static final DataParameter<Integer> dataMarkerEnergy = EntityDataManager.createKey(EntityMinecartEnergyContainer.class, DataSerializers.VARINT);
	MinecartEnergyStorage energyStorage = new MinecartEnergyStorage(getEnergyCapacity(), getMaxReceive(), getMaxExtract(), 0);

	public EntityMinecartEnergyContainer(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartEnergyContainer(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(dataMarkerEnergy, 0);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote)
			updateStorage(true);
		else if(ticksExisted%200==0)
			updateStorage(false);
	}

	protected void updateStorage(boolean b)
	{
		if(b)
			energyStorage.setEnergyStored(dataManager.get(dataMarkerEnergy));
		else
			dataManager.set(dataMarkerEnergy, energyStorage.getEnergyStored());
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		readEnergyFromNBT(compound);
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		writeEnergyToNBT(compound);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityEnergy.ENERGY)
			return (T)energyStorage;
		return super.getCapability(capability, facing);
	}

	/**
	 * @return whether the minecart can be charged by tesla coils, disabled by default
	 */
	public boolean isTeslaCharged()
	{
		return false;
	}

	/**
	 * @return whether the stored energy is infinite, disabled by default
	 */
	public boolean isInfinite()
	{
		return false;
	}

	/**
	 * @return how much energy the minecart can contain (in IF)
	 */
	public abstract int getEnergyCapacity();

	/**
	 * @return how much energy the minecart can receive (in IF)
	 */
	public abstract int getMaxReceive();

	/**
	 * @return how much energy the minecart can extract (in IF)
	 */
	public abstract int getMaxExtract();

	@Override
	void writeNBTToStack(NBTTagCompound nbt)
	{
		writeEnergyToNBT(nbt);
	}

	@Override
	void readFromStack(ItemStack stack)
	{
		readEnergyFromNBT(ItemNBTHelper.getTag(stack));
	}

	public void writeEnergyToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("energyStorage", energyStorage.getEnergyStored());
	}

	public void readEnergyFromNBT(NBTTagCompound nbt)
	{
		energyStorage.setEnergyStored(nbt.getInteger("energyStorage"));
	}

	@Override
	public int receiveEnergy(@Nullable EnumFacing from, int energy, boolean simulate)
	{
		return energyStorage.receiveEnergy(energy, simulate);
	}

	@Override
	public int getEnergyStored(@Nullable EnumFacing from)
	{
		return isInfinite()?Integer.MAX_VALUE: energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(@Nullable EnumFacing from)
	{
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(@Nullable EnumFacing from)
	{
		return true;
	}

	public class MinecartEnergyStorage extends EnergyStorage
	{
		public MinecartEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
		{
			super(capacity, maxReceive, maxExtract, energy);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
			int i = isInfinite()?Integer.MAX_VALUE: super.extractEnergy(maxExtract, simulate);
			if(!simulate&&i!=0)
				updateStorage(false);
			return i;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			int i = isInfinite()?0: super.receiveEnergy(maxReceive, simulate);
			if(!simulate&&i!=0)
				updateStorage(false);
			return i;
		}

		public void setEnergyStored(int energy)
		{
			this.energy = energy;
		}

	}
}
