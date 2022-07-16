package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockOverlayText;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.AlarmSiren;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ProgrammableSpeaker;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageProgrammableSpeakerSync;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import java.util.Objects;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */
public class TileEntityProgrammableSpeaker extends TileEntityImmersiveConnectable
		implements IRedstoneConnector, IDataConnector, ITickable, IHammerInteraction, IBlockOverlayText, ISoundTile
{
	public int redstoneChannel = 0;
	public boolean rsDirty = false;
	public boolean active = false;
	public String soundID = ImmersiveIntelligence.MODID+":siren";
	public float soundVolume = 1f, tone = 1f;
	protected WireType wireData = null;
	protected RedstoneWireNetwork redstoneNetwork = new RedstoneWireNetwork().add(this);
	protected DataWireNetwork dataNetwork = new DataWireNetwork().add(this);
	EnumFacing facing = EnumFacing.NORTH;
	@SideOnly(Side.CLIENT)
	SoundEvent sound;
	private boolean refreshWireNetwork = false;

	@Override
	public void update()
	{

		if(world.isRemote)
		{
			if(active)
			this.updateSound();
			if (!soundID.equals(""))
			{

				if(sound!=null)
				{
					ImmersiveEngineering.proxy.handleTileSound(sound, this, this.active, soundVolume*((ProgrammableSpeaker.soundRange+4)/20f), tone);
				}

			}
		}
		else if(hasWorld())
		{
			boolean wasActive = active;
			active = this.getNetwork().getPowerOutput(redstoneChannel) > 0;
			if(active^wasActive)
				IIPacketHandler.INSTANCE.sendToDimension(new MessageProgrammableSpeakerSync(active, this.getPos(),tone, soundVolume,soundID), this.world.provider.getDimension());
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
		soundVolume=getNetwork().channelValues[this.redstoneChannel]/15f;
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
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		//once
		boolean once = packet.getPacketVariable('o') instanceof DataPacketTypeBoolean&&((DataPacketTypeBoolean)packet.getPacketVariable('o')).value;

		if(packet.getPacketVariable('t') instanceof DataPacketTypeInteger)
			tone = MathHelper.clamp(((DataPacketTypeInteger)packet.getPacketVariable('t')).value/100f, -2, 2);

		if(packet.getPacketVariable('v') instanceof DataPacketTypeInteger)
			soundVolume = MathHelper.clamp(((DataPacketTypeInteger)packet.getPacketVariable('v')).value/100f, 0, 1);

		if(packet.variables.containsKey('s'))
		{
			if(once)
			{
				SoundEvent s = SoundEvent.REGISTRY.getObject(new ResourceLocation(packet.getPacketVariable('s').valueToString()));
				if(s!=null)
					world.playSound(null, getPos(), s, SoundCategory.BLOCKS, 1f*((ProgrammableSpeaker.soundRange+4)/20f), tone);
			}
			else
			{
				soundID = packet.getPacketVariable('s').valueToString();
				IIPacketHandler.INSTANCE.sendToDimension(new MessageProgrammableSpeakerSync(active, this.getPos(), tone, soundVolume, soundID), this.world.provider.getDimension());
			}

		}

	}

	@SideOnly(Side.CLIENT)
	private void updateSound()
	{
		sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundID));
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

		markDirty();
		redstoneNetwork.updateValues();
		onChange();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 254, 0);
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		String category = cableType.getCategory();
		return (Objects.equals(category, WireType.REDSTONE.getCategory())&& this.limitType==null) || (Objects.equals(category, IIDataWireType.DATA.getCategory())&& this.wireData==null);
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		if(Objects.equals(cableType.getCategory(), WireType.REDSTONE.getCategory())&& this.limitType==null)
		{
			RedstoneWireNetwork.updateConnectors(pos, world, redstoneNetwork);
			this.limitType = cableType;
		}
		else if(Objects.equals(cableType.getCategory(), IIDataWireType.DATA.getCategory())&& this.wireData==null)
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
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("active", active);

		nbt.setString("sound", soundID);
		nbt.setFloat("volume", soundVolume);

		nbt.setInteger("facing", facing.ordinal());

		nbt.setInteger("redstoneChannel", redstoneChannel);

		nbt.setFloat("tone", tone);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		active = nbt.getBoolean("active");

		soundID = nbt.getString("sound");
		soundVolume = nbt.getFloat("volume");

		facing = EnumFacing.getFront(nbt.getInteger("facing"));

		redstoneChannel = nbt.getInteger("redstoneChannel");

		tone=nbt.getFloat("tone");
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5f, 0.2f, 0.5f);
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		refreshWireNetwork = false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		int inc = getRenderRadiusIncrease();
		return new AxisAlignedBB(this.pos.getX()-inc, this.pos.getY()-inc, this.pos.getZ()-inc,
				this.pos.getX()+inc+1, this.pos.getY()+inc+1, this.pos.getZ()+inc+1);
	}

	int getRenderRadiusIncrease()
	{
		return Math.max(WireType.REDSTONE.getMaxLength(), IIDataWireType.DATA.getMaxLength());
	}

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop, boolean hammer)
	{
		if(!hammer)
			return null;
		return new String[]{I18n.format(Lib.DESC_INFO+"redstoneChannel",
				I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(redstoneChannel).getUnlocalizedName()))};
	}

	@Override
	public boolean useNixieFont(EntityPlayer player, RayTraceResult mop)
	{
		return false;
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return active;
	}
}
