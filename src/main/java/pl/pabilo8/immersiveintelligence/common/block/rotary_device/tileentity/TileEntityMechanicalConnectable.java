package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 20.07.2026
 * @ii-approved 0.3.1
 * @since 29.12.2019
 */
public abstract class TileEntityMechanicalConnectable extends TileEntityIIDirectionalConnectable implements IRotaryConnector, ITickable,
		IHammerInteraction, IBlockBounds, IOBJModelCallback<IBlockState>
{
	@SyncNBT(events = SyncEvents.TILE_ENERGY_CHANGED)
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
			return (float)getNetwork().getNetworkSpeed();
		}

		@Override
		public float getOutputTorque()
		{
			return (float)getNetwork().getNetworkTorque();
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = super.serializeNBT();
			nbt.setFloat("speed_network", (float)getNetwork().getNetworkSpeed());
			nbt.setFloat("torque_network", (float)getNetwork().getNetworkTorque());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			super.deserializeNBT(nbt);
			getNetwork().setValues(nbt.getFloat("speed_network"), nbt.getFloat("torque_network"));
		}
	};
	@Nonnull
	protected MotorBeltNetwork beltNetwork = new MotorBeltNetwork().add(this);
	protected boolean refreshBeltNetwork = false;

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
						getNetwork().updateValues();
				}
			}

			if(!refreshBeltNetwork&&world.getTotalWorldTime()%10==0)
			{
				refreshBeltNetwork = true;
				beltNetwork.removeFromNetwork(null);
			}
		}
	}

	@Nonnull
	@Override
	public MotorBeltNetwork getNetwork()
	{
		return beltNetwork;
	}

	@Override
	public void setNetwork(@Nonnull MotorBeltNetwork net)
	{
		beltNetwork = net;
	}

	@Override
	public void onChange()
	{
		updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
	}

	@Override
	public boolean isRelay()
	{
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
			if(facing==null||facing==getFacing())
				return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
			if(facing==null||facing==getFacing())
				return (T)energy;
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!IIRotaryUtils.isMotorBelt(cableType))
			return false;
		return super.canConnectCable(cableType, target, offset);
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
		return 10;
	}

	@Override
	public void removeCable(@Nullable ImmersiveNetHandler.Connection connection)
	{
		super.removeCable(connection);
		beltNetwork.removeFromNetwork(this);
		ImmersiveIntelligence.proxy.onMechanicalConnectorRemoved(connection);
	}

	@Override
	public float getDamageAmount(Entity e, Connection c)
	{
		if(c.cableType instanceof MotorBeltType)
			return (float)beltNetwork.getNetworkTorque()/4f;
		return super.getDamageAmount(e, c);
	}

	@Override
	public void invalidate()
	{
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
		if(connections!=null)
			connections.forEach(ImmersiveIntelligence.proxy::onMechanicalConnectorRemoved);
		super.invalidate();
	}

	@Override
	public RotaryStorage getRotaryStorage()
	{
		return energy;
	}
}
