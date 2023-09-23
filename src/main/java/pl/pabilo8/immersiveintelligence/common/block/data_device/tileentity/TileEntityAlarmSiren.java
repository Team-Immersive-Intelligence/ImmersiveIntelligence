package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AlarmSiren;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.REDSTONE_CATEGORY;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */
public class TileEntityAlarmSiren extends TileEntityImmersiveConnectable
		implements IRedstoneConnector, ITickable, IDirectionalTile, IHammerInteraction, IAdvancedTextOverlay, ISoundTile
{
	public int redstoneChannel = 0;
	public boolean rsDirty = false;

	EnumFacing facing = EnumFacing.NORTH;

	public boolean active = false;
	public float soundVolume = 1f;

	protected RedstoneWireNetwork wireNetwork = new RedstoneWireNetwork().add(this);
	private boolean refreshWireNetwork = false;

	@Override
	public void update()
	{

		if(world.isRemote)
		{
			ImmersiveEngineering.proxy.handleTileSound(IISounds.siren, this, this.active, soundVolume*((AlarmSiren.soundRange+8)/16f), 1);
		}
		else if(hasWorld())
		{
			boolean wasActive = active;
			active = this.getNetwork().getPowerOutput(redstoneChannel) > 0;
			if(active^wasActive)
				sendSoundUpdate();
		}

		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}
		if(hasWorld()&&!world.isRemote&&rsDirty)
			wireNetwork.updateValues();
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
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return !entity.isSneaking();
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return !axis.getAxis().isVertical();
	}

	@Override
	public void setNetwork(RedstoneWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public RedstoneWireNetwork getNetwork()
	{
		return wireNetwork;
	}

	@Override
	public void onChange()
	{
		soundVolume = wireNetwork.channelValues[this.redstoneChannel]/15f;

	}

	@Override
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public void updateInput(byte[] signals)
	{
		rsDirty = false;

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		// Sneaking iterates through colours, normal hammerign toggles in and out
		if(player.isSneaking())
			redstoneChannel = (redstoneChannel+1)%16;

		markDirty();
		wireNetwork.updateValues();
		onChange();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 254, 0);
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!REDSTONE_CATEGORY.equals(cableType.getCategory()))
			return false;
		return limitType==null||limitType==cableType;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(cableType, target, other);
		RedstoneWireNetwork.updateConnectors(pos, world, wireNetwork);
	}

	@Override
	public void removeCable(@Nullable ImmersiveNetHandler.Connection connection)
	{
		super.removeCable(connection);
		wireNetwork.removeFromNetwork(this);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("active", active);
		nbt.setFloat("volume", soundVolume);
		nbt.setInteger("facing", facing.ordinal());
		nbt.setInteger("redstoneChannel", redstoneChannel);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		active = nbt.getBoolean("active");
		soundVolume = nbt.getFloat("volume");
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
		redstoneChannel = nbt.getInteger("redstoneChannel");
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		if(message.hasKey("volume"))
			soundVolume = message.getFloat("volume");
		if(message.hasKey("active"))
			active = message.getBoolean("active");
	}

	private void sendSoundUpdate()
	{
		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
				.withBoolean("active", active)
				.withFloat("volume", soundVolume)
		));
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
	private AxisAlignedBB renderAABB;

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
		return WireType.REDSTONE.getMaxLength();
	}

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return active;
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND)))
			return null;
		return new String[]{I18n.format(Lib.DESC_INFO+"redstoneChannel",
				I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(redstoneChannel).getUnlocalizedName()))};
	}
}
