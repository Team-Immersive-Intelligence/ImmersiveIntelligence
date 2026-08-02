package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IEProperties.PropertyBoolInverted;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IActiveState;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IUsesBooleanProperty;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 10.10.2024
 * @updated 20.07.2026
 * @ii-approved 0.3.1
 * @since 11.06.2019
 */
public class TileEntityDataDebugger extends TileEntityIIDirectionalConnectable implements ITickable, IDataConnector, IHammerInteraction,
		IOBJModelCallback<IBlockState>, IAdvancedTextOverlay, IActiveState, IRedstoneOutput
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL);
	@SyncNBT
	public int setupTime = 25;
	@SyncNBT(name = "packet", events = SyncEvents.TILE_CUSTOM1)
	public DataPacket lastPacket = new DataPacket();
	@SyncNBT(events = SyncEvents.TILE_CUSTOM2)
	public DebuggerMode mode = DebuggerMode.TRANSCEIVER;

	public int outputTime = 0;
	private boolean toggle = false;
	private DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private boolean refreshWireNetwork = false;
	private String[] packetString = new String[0];

	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		if(setupTime > 0)
		{
			setupTime -= 1;
			if(setupTime==0)
				onDataChange();
		}
		if(!world.isRemote)
		{
			if(mode.canReceive)
			{
				if(outputTime==1)
				{
					outputTime = 0;
					markDirty();
					markBlockForUpdate(pos, null);
				}
				else
					outputTime = Math.max(outputTime-1, 0);
			}
			if(mode.canTransmit&&outputTime==0)
			{
				//Do not interpret the debugger's own redstone signal as a trigger to send a packet
				if(world.isBlockIndirectlyGettingPowered(getPos()) > 0&&!toggle)
				{
					toggle = true;
					DataPacket packet = new DataPacket();
					packet.set('a', new DataTypeString("Hello World!"));
					this.getDataNetwork().sendPacket(packet, this);
					this.world.playSound(null, pos, IISounds.debuggerBeep, SoundCategory.BLOCKS, 1.0f, 0.0f);
				}
				else if(world.isBlockIndirectlyGettingPowered(getPos())==0&&toggle)
				{
					toggle = false;
				}
			}
		}
	}

	public FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(world.isRemote)
			this.packetString = compilePacketString();
	}

	private String[] compilePacketString()
	{
		if(lastPacket==null||lastPacket.isEmpty())
			return new String[0];
		return minimizeArrays(
				lastPacket.stream()
						.map(entry -> String.format("%s %s = %s",
								entry.getValue().getTypeColor().getHexCol(entry.getValue().getName()),
								entry.getName(),
								entry.getValue().toString().replace(
												"\n", "\n"+StringUtils.repeat(' ', (entry.getValue().getName().length()+7)))
										.trim()
						))
						.map(s -> s.split("\n"))
						.toArray(String[][]::new)
		);
	}

	/**
	 * Joins 2d string arrays into a single dimension one
	 */
	private String[] minimizeArrays(String[][] array)
	{
		ArrayList<String> joined = new ArrayList<>();
		for(String[] strings : array)
			joined.addAll(Arrays.asList(strings));
		return joined.toArray(new String[0]);
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
		{
			mode = IIUtils.cycleEnum(true, DebuggerMode.class, mode);
			IIPacketHandler.sendChatTranslation(player, IIReference.INFO_KEY+"debugger_mode",
					new TextComponentTranslation(IIReference.INFO_KEY+"debugger_mode."+mode.getName())
			);
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
		return true;
	}

	@Override
	public DataWireNetwork getDataNetwork()
	{
		return wireNetwork;
	}

	@Override
	public void setDataNetwork(DataWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public void onDataChange()
	{
		if(!isInvalid())
		{
			markDirty();
			markBlockForUpdate(this.pos, null);
		}
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		if(this.mode.canReceive)
		{
			this.lastPacket = packet;
			this.outputTime = 20;
			this.world.playSound(null, pos, IISounds.debuggerBeep, SoundCategory.BLOCKS, 1.0f, 1.0f);
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	@Override
	public void sendPacket(DataPacket packet)
	{

	}

	@Override
	public boolean isRelay()
	{
		return true;
	}

	@Override
	public boolean acceptsWireType(WireType category)
	{
		return IIDataWireType.DATA_CATEGORY.equals(category.getCategory());
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(cableType, target, other);
		DataWireNetwork.updateConnectors(pos, world, wireNetwork);
	}

	@Override
	public void removeCable(@Nullable ImmersiveNetHandler.Connection connection)
	{
		super.removeCable(connection);
		wireNetwork.removeFromNetwork(this);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		BlockPos p = con.start==pos?con.start.subtract(con.end): con.end.subtract(con.start);
		Vec3d pp = (facing.rotateY().getAxis()==Axis.X)?new Vec3d(p.getX(), 0, 0): new Vec3d(0, 0, p.getZ());
		if(pp.distanceTo(Vec3d.ZERO)==0)
			pp = new Vec3d(facing.rotateY().getDirectionVec());

		return new Vec3d(.5, 0.95, .5)
				.add(new Vec3d(facing.getDirectionVec()).scale(0.25))
				.add(pp.normalize().scale(0.325));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		String s_out = I18n.format(IIReference.INFO_KEY+"debugger_mode",
				I18n.format(IIReference.INFO_KEY+"debugger_mode."+mode.getName()));
		if(lastPacket!=null)
		{
			ArrayList<String> s = new ArrayList<>(Arrays.asList(this.packetString));
			s.add(0, s_out);
			return s.toArray(new String[0]);
		}
		return new String[]{s_out};
	}


	@Override
	public PropertyBoolInverted getBoolProperty(Class<? extends IUsesBooleanProperty> inf)
	{
		return IEProperties.BOOLEANS[0];
	}

	@Override
	public boolean getIsActive()
	{
		return setupTime > 0;
	}

	@Override
	public boolean shouldRenderGroup(IBlockState object, String group)
	{
		return true;
	}

	@Override
	public Optional<TRSRTransformation> applyTransformations(IBlockState object, String group, Optional<TRSRTransformation> transform)
	{
		Matrix4 mat = transform.map(trsrTransformation -> new Matrix4(trsrTransformation.getMatrix())).orElseGet(Matrix4::new);
		mat = mat.translate(.5, 0, .5).rotate(Math.toRadians(25), 0, 1, 0).translate(-.5, 0, -.5);
		transform = Optional.of(new TRSRTransformation(mat.toMatrix4f()));
		return transform;
	}

	@Override
	public int getStrongRSOutput(IBlockState state, EnumFacing side)
	{
		return mode.canReceive&&outputTime > 0?15: 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, EnumFacing side)
	{
		return false;
	}

	public enum DebuggerMode implements ISerializableEnum
	{
		TRANSCEIVER(true, true),
		TRANSMITTER(false, true),
		RECEIVER(true, false);

		final boolean canReceive, canTransmit;

		DebuggerMode(boolean canReceive, boolean canTransmit)
		{
			this.canReceive = canReceive;
			this.canTransmit = canTransmit;
		}
	}
}
