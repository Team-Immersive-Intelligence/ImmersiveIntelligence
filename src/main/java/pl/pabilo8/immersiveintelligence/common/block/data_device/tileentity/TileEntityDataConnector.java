package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.annotations.VisibleForTesting;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.compat.ComputerCraftHelper;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.07.2026
 * @ii-approved 0.3.1
 * @since 31.05.2019
 */
public class TileEntityDataConnector extends TileEntityIIDirectionalConnectable implements
		ITickable, IHammerInteraction, IBlockBounds, IDataConnector, IOBJModelCallback<IBlockState>, IAdvancedTextOverlay,
		IPeripheralTile
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.SIDE_CLICKED)
			.withMirroringOnPlacement(true);

	//--- OpenComputers / ComputerCraft compat ---//
	public DataPacket lastReceived = null;
	public boolean compatReceived = true; //whether a computer received the signal
	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	@SyncNBT
	public int color = 0;
	private boolean refreshWireNetwork = false;

	@Nonnull
	@Override
	public FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public boolean acceptsWireType(WireType category)
	{
		return IIDataWireType.DATA_CATEGORY.equals(category.getCategory());
	}

	@Override
	public boolean isRelay()
	{
		return false;
	}

	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}
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
	public void onPacketReceive(DataPacket packet)
	{
		//computercraft/opencomputers
		this.lastReceived = packet.clone();
		compatReceived = false;

		if(packet.matchesConnector(EnumDyeColor.byMetadata(this.color), -1))
		{
			BlockPos devicePos = this.pos.offset(facing);
			TileEntity device = world.getTileEntity(devicePos);

			if(world.isBlockLoaded(devicePos)&&device instanceof IDataDevice)
			{
				IDataDevice d = (IDataDevice)device;
				d.onReceive(packet, facing.getOpposite());
			}
		}

	}

	@Override
	public void sendPacket(DataPacket packet)
	{
		this.getDataNetwork().sendPacket(packet.withPacketColor(EnumDyeColor.byMetadata(this.color)), this);
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		color = IIUtils.cycleInt(player.isSneaking(), color, 0, 15);
		onDataChange();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 254, 0);
		return true;
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
		EnumFacing side = facing.getOpposite();
		double conRadius = con.cableType.getRenderDiameter()/2;
		return new Vec3d(.5+side.getFrontOffsetX()*(.25-conRadius), 0.5+side.getFrontOffsetY()*(.25-conRadius), .5+side.getFrontOffsetZ()*(.25-conRadius));
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		super.onConnectivityUpdate(pos, dimension);
		refreshWireNetwork = false;
	}

	@Override
	public float[] getBlockBounds()
	{
		float length = .75f;
		float wMin = .25f;
		float wMax = .75f;
		switch(facing.getOpposite())
		{
			case UP:
				return new float[]{wMin, 0, wMin, wMax, length, wMax};
			case DOWN:
				return new float[]{wMin, 1-length, wMin, wMax, 1, wMax};
			case SOUTH:
				return new float[]{wMin, wMin, 0, wMax, wMax, length};
			case NORTH:
				return new float[]{wMin, wMin, 1-length, wMax, wMax, 1};
			case EAST:
				return new float[]{0, wMin, wMin, length, wMax, wMax};
			case WEST:
				return new float[]{1-length, wMin, wMin, 1, wMax, wMax};
		}
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderColour(IBlockState object, String group)
	{
		if("Color".equals(group))
			return 0xff000000|EnumDyeColor.byMetadata(this.color).getColorValue();
		return 0xffffffff;
	}

	@Override
	public String getCacheKey(IBlockState object)
	{
		return String.valueOf(this.color);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND)))
			return new String[]{I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(color).getUnlocalizedName())};
		else
			return null;
	}

	@VisibleForTesting
	public void setColor(EnumDyeColor color)
	{
		this.color = color.getMetadata();
	}
}
