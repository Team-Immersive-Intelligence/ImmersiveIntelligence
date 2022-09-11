package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;

public class TileEntityTransmissionBoxCreative extends TileEntityIEBase implements IComparatorOverride
{
	public int comparatorOutput = 0;
	RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public float getRotationSpeed()
		{
			return world.isBlockPowered(pos)?512: 80;
		}

		@Override
		public float getTorque()
		{
			return world.isBlockPowered(pos)?150: 25;
		}

		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return RotationSide.OUTPUT;
		}
	};

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			return (T)rotation;
		}
		return super.getCapability(capability, facing);
	}

	/**
	 * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
	 * clientside.
	 */
	@Override
	public boolean receiveClientEvent(int id, int arg)
	{
		if(id==0)
		{
			this.markContainingBlockForUpdate(null);
			return true;
		}
		return false;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{

	}

	@Override
	public int getComparatorInputOverride()
	{
		return this.comparatorOutput;
	}

}