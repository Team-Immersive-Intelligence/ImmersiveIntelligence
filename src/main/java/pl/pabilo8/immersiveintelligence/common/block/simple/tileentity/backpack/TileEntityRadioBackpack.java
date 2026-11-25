package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.Backpacks;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Receives radio packets and sends them to a data wire network.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class TileEntityRadioBackpack extends TileEntityBackpack implements IRadioDevice, IDataConnector, ITickable
{
	@SyncNBT
	public int frequency;
	@SyncNBT
	public int radioCooldown;
	private DataWireNetwork dataNetwork = new DataWireNetwork().add(this);
	private boolean refreshNetwork = true;

	public TileEntityRadioBackpack()
	{
		super(BackpackType.RADIO);
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{
		super.readOnPlacement(placer, stack);
		IRadioDevice radio = backpack.getCapability(RadioNetwork.RADIO_CAPABILITY, null);
		if(radio!=null)
		{
			setFrequency(radio.getFrequency());
			setRadioCooldown(radio.getRadioCooldown());
		}
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, @Nonnull IBlockState state)
	{
		ItemStack stack = backpack.copy();
		IRadioDevice radio = stack.getCapability(RadioNetwork.RADIO_CAPABILITY, null);
		if(radio!=null)
		{
			radio.setFrequency(frequency);
			radio.setRadioCooldown(radioCooldown);
		}
		return stack;
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		if(!world.isRemote)
			RadioNetwork.INSTANCE.addDevice(this);
	}

	@Override
	public void onChunkUnload()
	{
		RadioNetwork.INSTANCE.removeDevice(this);
		super.onChunkUnload();
	}

	@Override
	public void invalidate()
	{
		RadioNetwork.INSTANCE.removeDevice(this);
		super.invalidate();
	}

	@Override
	public void update()
	{
		if(world.isRemote)
			return;
		tickRadioCooldown();
		refreshDataNetwork();
	}

	@Override
	public boolean acceptsWireType(WireType wire)
	{
		return IIDataWireType.DATA_CATEGORY.equals(wire.getCategory());
	}

	@Override
	public void connectCable(WireType wire, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(wire, target, other);
		refreshNetwork = true;
	}

	@Override
	public void removeCable(@Nullable Connection connection)
	{
		super.removeCable(connection);
		if(!world.isRemote)
		{
			dataNetwork.removeFromNetwork(this);
			refreshNetwork = true;
		}
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		super.onConnectivityUpdate(pos, dimension);
		refreshNetwork = true;
	}

	private void refreshDataNetwork()
	{
		if(refreshNetwork)
		{
			refreshNetwork = false;
			DataWireNetwork.updateConnectors(pos, world, new DataWireNetwork());
		}
	}

	@Override
	public DataWireNetwork getDataNetwork()
	{
		return dataNetwork;
	}

	@Override
	public void setDataNetwork(DataWireNetwork network)
	{
		dataNetwork = network;
	}

	@Override
	public void onDataChange()
	{
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		RadioNetwork.INSTANCE.sendPacket(packet, this, new ArrayList<>());
	}

	@Override
	public void sendPacket(DataPacket packet)
	{
		refreshDataNetwork();
		dataNetwork.sendPacket(packet, this);
	}

	@Override
	public void onRadioSend(DataPacket packet)
	{
	}

	@Override
	public boolean onRadioReceive(DataPacket packet)
	{
		if(world.isRemote||isInvalid()||!isRadioAvailable()||limitType==null)
			return false;
		sendPacket(packet);
		return true;
	}

	@Override
	public boolean canRelayRadio()
	{
		return true;
	}

	@Override
	public int getFrequency()
	{
		return frequency;
	}

	@Override
	public void setFrequency(int frequency)
	{
		this.frequency = MathHelper.clamp(frequency, 0, IIConfig.radioBasicMaxFrequency);
		markDirty();
	}

	@Override
	public boolean isBasicRadio()
	{
		return true;
	}

	@Override
	public float getRange()
	{
		return Backpacks.radiopackRange;
	}

	@Override
	public DimensionBlockPos getRadioDevicePosition()
	{
		return new DimensionBlockPos(pos, world);
	}

	@Override
	public int getRadioCooldown()
	{
		return radioCooldown;
	}

	@Override
	public void setRadioCooldown(int ticks)
	{
		radioCooldown = MathHelper.clamp(ticks, 0, MAX_USABILITY_COOLDOWN);
		markDirty();
	}
}
