package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IEProperties.PropertyBoolInverted;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.*;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
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
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 11.06.2019
 * @since 10.10.2024
 */
public class TileEntityDataDebugger extends TileEntityImmersiveConnectable implements ITickable, IDataConnector, IHammerInteraction, IDirectionalTile, IOBJModelCallback<IBlockState>, IAdvancedTextOverlay, IActiveState, IRedstoneOutput
{
	//Purely decorational, client only
	public int setupTime = 25;
	public int outputTime = 0;
	private DebuggerMode mode = DebuggerMode.TRANSCEIVER;
	private EnumFacing facing = EnumFacing.NORTH;
	private boolean toggle = false;
	private DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private boolean refreshWireNetwork = false;
	private DataPacket lastPacket = null;
	private String[] packetString = new String[0];

	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		if(world.isRemote&&setupTime > 0)
		{
			setupTime -= 1;
			if(setupTime==0)
				onDataChange();
		}
		else if(!world.isRemote)
		{
			if(mode.canReceive)
			{
				if(outputTime-1==0)
				{
					outputTime = 0;
					markDirty();
					IBlockState stateHere = world.getBlockState(pos);
					markContainingBlockForUpdate(stateHere);
					markBlockForUpdate(pos.offset(facing), stateHere);
				}
				else
					outputTime = Math.max(outputTime-1, 0);
			}
			if(mode.canTransmit)
			{
				if(world.isBlockIndirectlyGettingPowered(getPos()) > 0&&!toggle)
				{
					toggle = true;
					DataPacket pack = new DataPacket();
					pack.set('a', new DataTypeString("Hello World!"));
					this.getDataNetwork().sendPacket(pack, this);
					this.world.playSound(null, pos, IISounds.debuggerBeep, SoundCategory.BLOCKS, 1.0f, 0.0f);
				}
				else if(world.isBlockIndirectlyGettingPowered(getPos())==0&&toggle)
				{
					toggle = false;
				}
			}
		}
	}


	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = DebuggerMode.values()[nbt.getInteger("mode")];
		if(nbt.hasKey("noSetup"))
			setupTime = 0;
		setFacing(EnumFacing.getFront(nbt.getInteger("facing")));
		if(nbt.hasKey("packet"))
		{
			this.lastPacket = new DataPacket(nbt.getCompoundTag("packet"));
			if(world!=null&&world.isRemote)
				this.packetString = compilePacketString();
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("mode", mode.ordinal());
		if(setupTime < 25)
			nbt.setBoolean("noSetup", true);
		nbt.setInteger("facing", facing.ordinal());

		if(this.lastPacket!=null)
		{
			if(!world.isRemote)
				this.packetString = compilePacketString();
			nbt.setTag("packet", this.lastPacket.serializeNBT());
		}
	}

	private String[] compilePacketString()
	{
		//gets variables in format l:{Value:0}
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
			markDirty();
			markBlockForUpdate(pos, null);
		}
		return true;
	}

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		if(facing.getAxis().isHorizontal())
			this.facing = facing;
		else
			this.facing = EnumFacing.NORTH;
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
			IBlockState stateHere = world.getBlockState(pos);
			markContainingBlockForUpdate(stateHere);
			markBlockForUpdate(pos.offset(facing), stateHere);
		}
	}

	@Override
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		if(this.mode.canReceive)
		{
			this.lastPacket = packet;
			this.outputTime = 20;
			this.world.playSound(null, pos, IISounds.debuggerBeep, SoundCategory.BLOCKS, 1.0f, 1.0f);
			markDirty();
			markBlockForUpdate(this.pos, null);
		}
	}

	@Override
	public void sendPacket(DataPacket packet)
	{

	}

	@Override
	protected boolean isRelay()
	{
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(cableType!=IIDataWireType.DATA)
			return false;
		return limitType==null||limitType==cableType;
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
