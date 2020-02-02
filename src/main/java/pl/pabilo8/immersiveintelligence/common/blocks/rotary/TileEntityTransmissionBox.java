package pl.pabilo8.immersiveintelligence.common.blocks.rotary;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.IRotationalEnergyBlock;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageRotaryPowerSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices.rof_conversion_ratio;

/**
 * Created by Pabilo8 on 29-12-2019.
 */
public class TileEntityTransmissionBox extends TileEntityIEBase implements ITickable, IRotationAcceptor, IDirectionalTile, IHammerInteraction, IRotationalEnergyBlock
{
	EnumFacing facing = EnumFacing.NORTH;
	public int tick = 0;
	public RotaryStorage energy = new RotaryStorage()
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing().getOpposite()?RotationSide.OUTPUT: RotationSide.NONE;
		}
	};

	@Override
	public void updateRotationStorage(float rpm, float torque, int part)
	{
		if(world.isRemote)
		{
			if(part==0)
			{
				energy.setRotationSpeed(rpm);
				energy.setTorque(torque);
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&(facing==null||facing==getFacing().getOpposite()))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			if(facing==null||facing==getFacing().getOpposite())
				return (T)energy;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("energy"))
			energy.fromNBT(nbt.getCompoundTag("energy"));
		facing = EnumFacing.byIndex(nbt.getInteger("facing"));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setTag("energy", energy.toNBT());
		nbt.setInteger("facing", facing.ordinal());
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote)
		{
			tick -= 1;
			if(tick < 1)
			{
				energy.grow(0, 0, 0.98f);
			}
		}
	}

	@Override
	public void inputRotation(double rotation, @Nonnull EnumFacing side)
	{
		if(side==facing.getOpposite()&&!world.isRemote)
		{
			tick = 10;
			TileEntity t = world.getTileEntity(pos.offset(facing));
			float torque = RotaryUtils.getTorqueForIEDevice(t, 1);
			int output = (int)(20*IEConfig.Machines.dynamo_output*rotation*rof_conversion_ratio);
			float speed = output/torque;
			torque = output/speed;

			energy.grow(Math.round(speed), Math.round(torque), 0.98f);
			if(world.getTotalWorldTime()%20==0)
			{
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(energy, 0, pos), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 24));
			}

		}
	}

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 0;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return true;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return true;
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		return false;
	}
}
