package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProgrammableSpeaker;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIConnectable;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.06.2019
 */
public class TileEntityProgrammableSpeaker extends TileEntityIIConnectable
		implements IRedstoneConnector, IDataConnector, ITickable, IHammerInteraction, IAdvancedTextOverlay, ISoundTile
{
	@SyncNBT(name = "redstoneChannel", events = SyncEvents.TILE_CUSTOM1)
	public int redstoneChannel = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM2)
	public boolean active = false;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM2)
	public String sound = ImmersiveIntelligence.MODID+":siren";
	@SyncNBT(events = SyncEvents.TILE_CUSTOM2)
	public float volume = 1f, tone = 1f;

	public boolean rsDirty = false;
	protected WireType wireData = null;
	protected RedstoneWireNetwork redstoneNetwork = new RedstoneWireNetwork().add(this);
	protected DataWireNetwork dataNetwork = new DataWireNetwork().add(this);
	@SideOnly(Side.CLIENT)
	SoundEvent playedSound;
	private boolean refreshWireNetwork = false;

	@Override
	public void update()
	{
		if(world.isRemote)
		{
			if(active)
				this.updateSound();
			if(!sound.isEmpty())
				if(playedSound!=null)
					ImmersiveEngineering.proxy.handleTileSound(playedSound, this, this.active, volume*((ProgrammableSpeaker.soundRange+4)/20f), tone);
		}
		else if(hasWorld())
		{
			boolean wasActive = active;
			active = this.getNetwork().getPowerOutput(redstoneChannel) > 0;
			if(active^wasActive)
				updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}

		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			redstoneNetwork.removeFromNetwork(null);
			dataNetwork.removeFromNetwork(null);
		}
		if(hasWorld()&&!world.isRemote&&rsDirty)
			redstoneNetwork.updateValues();
	}

	@Override
	public RedstoneWireNetwork getNetwork()
	{
		return redstoneNetwork;
	}

	@Override
	public void setNetwork(RedstoneWireNetwork net)
	{
		redstoneNetwork = net;
	}

	@Override
	public void onChange()
	{
		volume = getNetwork().channelValues[this.redstoneChannel]/15f;
	}

	@Override
	public DataWireNetwork getDataNetwork()
	{
		return dataNetwork;
	}

	@Override
	public void setDataNetwork(DataWireNetwork net)
	{
		this.dataNetwork = net;
	}

	@Override
	public void onDataChange()
	{

	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		IIDataHandlingUtils.optionalInt('t', packet).ifPresent(t ->
				tone = MathHelper.clamp(t/100f, -2, 2));
		IIDataHandlingUtils.optionalInt('v', packet).ifPresent(v ->
				volume = MathHelper.clamp(v/100f, 0, 1));

		//Update played sound
		IIDataHandlingUtils.expectingStringParam('s', packet, s -> {
			if(IIDataHandlingUtils.asBoolean('o', packet)) //play once
			{
				SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(s));
				if(soundEvent!=null)
					world.playSound(null, getPos(), soundEvent, SoundCategory.BLOCKS, ((ProgrammableSpeaker.soundRange+4)/20f), tone);
			}
			else
				sound = packet.get('s').toString();
		});
		if(!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	@SideOnly(Side.CLIENT)
	private void updateSound()
	{
		playedSound = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
	}

	@Override
	public void sendPacket(DataPacket packet)
	{

	}

	@Override
	public void updateInput(byte[] signals)
	{
		rsDirty = false;
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		// Sneaking iterates through colours, normal hammering toggles in and out
		if(player.isSneaking())
			redstoneChannel = (redstoneChannel+1)%16;

		redstoneNetwork.updateValues();
		onChange();
		updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		return true;
	}

	@Override
	public boolean acceptsWireType(WireType category)
	{
		return false;
	}

	@Override
	public boolean isRelay()
	{
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		String category = cableType.getCategory();
		return (Objects.equals(category, WireType.REDSTONE.getCategory())&&this.limitType==null)
				||(Objects.equals(category, IIDataWireType.DATA.getCategory())&&this.wireData==null);
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		if(Objects.equals(cableType.getCategory(), WireType.REDSTONE.getCategory())&&this.limitType==null)
		{
			RedstoneWireNetwork.updateConnectors(pos, world, redstoneNetwork);
			this.limitType = cableType;
		}
		else if(Objects.equals(cableType.getCategory(), IIDataWireType.DATA.getCategory())&&this.wireData==null)
		{
			DataWireNetwork.updateConnectors(pos, world, dataNetwork);
			this.wireData = cableType;
		}

		this.markContainingBlockForUpdate(null);
	}

	@Override
	public void removeCable(Connection connection)
	{
		WireType type = connection!=null?connection.cableType: null;
		if(type==null)
		{
			limitType = null;
			wireData = null;
		}
		if(type==limitType)
		{
			redstoneNetwork.removeFromNetwork(this);
			this.limitType = null;
		}
		if(type==wireData)
		{
			dataNetwork.removeFromNetwork(this);
			this.wireData = null;
		}
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con, TargetingInfo target, Vec3i offsetLink)
	{
		return getConnectionOffset(con, Objects.equals(con.cableType.getCategory(), IIDataWireType.DATA_CATEGORY));
	}

	private Vec3d getConnectionOffset(Connection con, boolean data)
	{
		if(data)
			return new Vec3d(0.5f, 0.325f, 0.5f);
		else
			return new Vec3d(0.5f, 0.2f, 0.5f);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5f, 0.2f, 0.5f);
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		super.onConnectivityUpdate(pos, dimension);
		refreshWireNetwork = false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND)))
			return null;
		return new String[]{I18n.format(Lib.DESC_INFO+"redstoneChannel",
				I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(redstoneChannel).getUnlocalizedName()))};
	}


	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return active;
	}
}
