package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static blusunrize.immersiveengineering.api.energy.wires.WireApi.canMix;

/**
 * @author Pabilo8
 * @since 29-12-2019
 */
public abstract class TileEntityMechanicalConnectable extends TileEntityImmersiveConnectable implements IMotorBeltConnector, ITickable, IDirectionalTile, IHammerInteraction, IBlockBounds, IOBJModelCallback<IBlockState>, IRotationalEnergyBlock
{
	protected MotorBeltNetwork beltNetwork = new MotorBeltNetwork().add(this);
	public RotaryStorage energy = new RotaryStorage()
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing()||facing==null?RotationSide.BOTH: RotationSide.NONE;
		}

		@Override
		public float getOutputRotationSpeed()
		{
			return getNetwork()!=null?(float)getNetwork().getNetworkRPM(): this.getRotationSpeed();
		}

		@Override
		public float getOutputTorque()
		{
			return getNetwork()!=null?(float)getNetwork().getNetworkTorque(): this.getTorque();
		}
	};
	protected boolean refreshBeltNetwork = false;

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
			else if(part==1)
			{
				getNetwork().setClient(rpm, torque);
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			if(facing==null||facing==getFacing())
				return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			if(facing==null||facing==getFacing())
				return (T)energy;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!RotaryUtils.isMechanicalBelt(cableType))
			return false;
		return limitType==null||(this.isRelay()&&canMix(limitType, cableType));
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("energy"))
			energy.fromNBT(nbt.getCompoundTag("energy"));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("energy", energy.toNBT());
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote)
		{
			if(world.getTotalWorldTime()%20==0)
				getNetwork().updateValues();

			if(world.getTileEntity(getPos().offset(getFacing()))!=null)
			{
				TileEntity te = world.getTileEntity(getPos().offset(getFacing()));
				if(te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, getFacing().getOpposite()))
				{
					IRotaryEnergy other = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, getFacing().getOpposite());
					if(energy.handleRotation(other, getFacing().getOpposite()))
					{
						getNetwork().updateValues();
					}
				}
			}

			if(!refreshBeltNetwork)
			{
				refreshBeltNetwork = true;
				beltNetwork.removeFromNetwork(null);
			}
		}
	}

	@Override
	public MotorBeltNetwork getNetwork()
	{
		return beltNetwork;
	}

	@Override
	public void setNetwork(MotorBeltNetwork net)
	{
		beltNetwork = net;
	}

	@Override
	public void onChange()
	{
		markDirty();
		IBlockState stateHere = world.getBlockState(pos);
		markContainingBlockForUpdate(stateHere);
		markBlockForUpdate(getConnectionPos(), stateHere);

		//IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(energy, 0, getPos()), Utils.targetPointFromTile(this, 32));
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(getNetwork().getEnergyStorage(), 1, getPos()), IIPacketHandler.targetPointFromTile(this, 32));

	}

	public abstract BlockPos getConnectionPos();

	@Override
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		onChange();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 254, 0);
		return true;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(cableType, target, other);
		MotorBeltNetwork.updateConnectors(pos, world, beltNetwork);
	}

	@Override
	public void processDamage(Entity e, float amount, Connection c)
	{

	}

	@Override
	protected float getBaseDamage(Connection c)
	{
		return 1;
	}

	@Override
	protected float getMaxDamage(Connection c)
	{
		return 20;
	}

	@Override
	public void removeCable(@Nullable ImmersiveNetHandler.Connection connection)
	{
		super.removeCable(connection);
		beltNetwork.removeFromNetwork(this);
	}


	@Override
	public RotaryStorage getRotaryStorage()
	{
		return energy;
	}

	@Override
	public float getDamageAmount(Entity e, Connection c)
	{
		if(c.cableType instanceof MotorBeltType)
		{
			return (float)beltNetwork.getNetworkTorque()/4f;
		}
		return super.getDamageAmount(e, c);
	}
}
