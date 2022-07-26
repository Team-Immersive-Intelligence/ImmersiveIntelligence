package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockOverlayText;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class TileEntityDataCallbackConnector extends TileEntityImmersiveConnectable implements ITickable, IDirectionalTile, IHammerInteraction, IBlockBounds, IDataConnector, IOBJModelCallback<IBlockState>, IBlockOverlayText
{
	public EnumFacing facing = EnumFacing.DOWN;

	public int colorIn = 0;
	public int colorOut = 1;

	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private boolean refreshWireNetwork = false;

	/**
	 * Like the old updateEntity(), except more generic.
	 */
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
	public void setDataNetwork(DataWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public DataWireNetwork getDataNetwork()
	{
		return wireNetwork;
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
		if(packet.matchesConnector(EnumDyeColor.byMetadata(this.colorIn), -1))
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
		this.getDataNetwork().sendPacket(packet.setPacketColor(EnumDyeColor.byMetadata(this.colorOut)), this);
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
			colorIn = Utils.cycleIntAvoid(true, colorIn, 0, 15, colorOut);
		else
			colorOut = Utils.cycleIntAvoid(true, colorOut, 0, 15, colorIn);
		onDataChange();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 254, 0);
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!cableType.getCategory().equals(IIDataWireType.DATA_CATEGORY))
			return false;
		return limitType==null;
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
	public EnumFacing getFacing()
	{
		return this.facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 0;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return true;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return false;
	}


	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setInteger("facing", facing.ordinal());
		nbt.setInteger("colorIn", colorIn);
		nbt.setInteger("colorOut", colorOut);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
		colorIn = nbt.getInteger("colorIn");
		colorOut = nbt.getInteger("colorOut");
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
		refreshWireNetwork = false;
	}

	@SideOnly(Side.CLIENT)
	private AxisAlignedBB renderAABB;

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		int inc = getRenderRadiusIncrease();
		return new AxisAlignedBB(this.pos.getX()-inc, this.pos.getY()-inc, this.pos.getZ()-inc, this.pos.getX()+inc+1, this.pos.getY()+inc+1, this.pos.getZ()+inc+1);
	}

	int getRenderRadiusIncrease()
	{
		return IIDataWireType.DATA.getMaxLength();
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

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderColour(IBlockState object, String group)
	{
		if("Color_In".equals(group))
			return 0xff000000|EnumDyeColor.byMetadata(this.colorIn).getColorValue();
		if("Color_Out".equals(group))
			return 0xff000000|EnumDyeColor.byMetadata(this.colorOut).getColorValue();
		return 0xffffffff;
	}

	@Override
	public String getCacheKey(IBlockState object)
	{
		return this.colorIn+";"+this.colorOut;
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop, boolean hammer)
	{
		if(hammer)
			return new String[]{
					I18n.format(Lib.DESC_INFO+"blockSide.io.0")+": "+I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(colorIn).getUnlocalizedName()),
					I18n.format(Lib.DESC_INFO+"blockSide.io.1")+": "+I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(colorOut).getUnlocalizedName())
			};
		else
			return new String[0];
	}

	@Override
	public boolean useNixieFont(EntityPlayer player, RayTraceResult mop)
	{
		return false;
	}
}