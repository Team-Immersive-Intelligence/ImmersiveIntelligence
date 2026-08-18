package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AlarmSiren;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.REDSTONE_CATEGORY;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.06.2019
 */
public class TileEntityAlarmSiren extends TileEntityIIDirectionalConnectable
		implements IRedstoneConnector, ITickable, IHammerInteraction, IAdvancedTextOverlay
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL);

	@SyncNBT
	public int redstoneChannel = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public boolean active = false;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public float soundVolume = 1f;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityAlarmSiren> loopSound;
	private RedstoneWireNetwork wireNetwork = new RedstoneWireNetwork().add(this);
	private boolean refreshWireNetwork = false;

	@Override
	public void update()
	{
		if(world.isRemote)
		{
			if(active)
			{
				if(loopSound==null||loopSound.isDonePlaying())
				{
					loopSound = new ConditionCompoundSound<>(IISounds.siren, new Vec3d(pos).addVector(0.5, 0.5, 0.5),
							this, t -> !t.isInvalid()&&t.active);
					loopSound.setMaxRange(AlarmSiren.soundRange);
				}
				if(loopSound!=null)
					loopSound.setVolume(soundVolume);
			}

		}

		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}
	}

	//--- Wiring ---//

	@Override
	public RedstoneWireNetwork getNetwork()
	{
		return wireNetwork;
	}

	@Override
	public void setNetwork(RedstoneWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public void onChange()
	{
		active = this.getNetwork().getPowerOutput(redstoneChannel) > 0;
		soundVolume = wireNetwork.channelValues[this.redstoneChannel]/15f;
		if(!world.isRemote)
			updateTileForAll();
	}

	@Override
	public void updateInput(byte[] signals)
	{

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		//Sneaking iterates through colours
		if(player.isSneaking())
		{
			redstoneChannel = (redstoneChannel+1)%16;
			wireNetwork.updateValues();
			onChange();
			return true;
		}
		return false;
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
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		super.onConnectivityUpdate(pos, dimension);
		refreshWireNetwork = false;
	}

	@Override
	public boolean acceptsWireType(WireType category)
	{
		return REDSTONE_CATEGORY.equals(category.getCategory());
	}

	@Override
	public boolean isRelay()
	{
		return true;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5f, 0.2f, 0.5f);
	}

	//--- Facing ---//

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND)))
			return null;
		return new String[]{I18n.format(Lib.DESC_INFO+"redstoneChannel",
				I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(redstoneChannel).getUnlocalizedName()))};
	}
}
