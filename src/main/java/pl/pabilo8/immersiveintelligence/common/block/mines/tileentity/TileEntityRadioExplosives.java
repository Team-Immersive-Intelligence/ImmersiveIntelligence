package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;

/**
 * @author Pabilo8
 * @since 06.02.2021
 */
public class TileEntityRadioExplosives extends TileEntityMineBase implements ITickable, IBlockBounds, IDirectionalTile, IRadioDevice, IRedstoneConnector
{
	public int frequency = 0;
	public DataPacket programmedPacket = new DataPacket();

	private static final Vec3d CONN_OFFSET = new Vec3d(0.5, 0.25, 0.5);
	public EnumFacing facing = EnumFacing.NORTH;

	protected RedstoneWireNetwork wireNetwork = new RedstoneWireNetwork().add(this);
	private boolean refreshWireNetwork = false;

	@Override
	public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		armed = nbtTagCompound.getBoolean("armed");
		facing = EnumFacing.getFront(nbtTagCompound.getInteger("facing"));
		super.readCustomNBT(nbtTagCompound, b);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		nbtTagCompound.setBoolean("armed", armed);
		nbtTagCompound.setInteger("facing", facing.getIndex());
		super.writeCustomNBT(nbtTagCompound, b);
		RadioNetwork.INSTANCE.addDevice(this);
	}

	public void explode()
	{
		if(!armed)
			return;
		RadioNetwork.INSTANCE.removeDevice(this);
		super.explode();
	}

	@Override
	public float[] getBlockBounds()
	{
		switch(facing)
		{
			case NORTH:
				return new float[]{1/16f, 2/16f, 0, 15/16f, 14/16f, 9/16f};
			case SOUTH:
				return new float[]{1/16f, 2/16f, 1-9/16f, 15/16f, 14/16f, 1};
			case EAST:
				return new float[]{1-9/16f, 2/16f, 1/16f, 1, 14/16f, 15/16f};
			case WEST:
				return new float[]{0, 2/16f, 1/16f, 9/16f, 14/16f, 15/16f};
			case UP:
				return new float[]{1/16f, 1-9/16f, 2/16f, 15/16f, 1, 14/16f};
			case DOWN:
				return new float[]{1/16f, 0, 2/16f, 15/16f, 9/16f, 14/16f};
		}
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		super.readOnPlacement(placer, stack);
		if(!stack.isEmpty())
			this.programmedPacket = new DataPacket().fromNBT(ItemNBTHelper.getTagCompound(stack, "programmed_data"));
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
	public void onRadioSend(DataPacket packet)
	{

	}

	@Override
	public boolean onRadioReceive(DataPacket packet)
	{
		if(packet.equals(this.programmedPacket))
		{
			explode();
		}
		return false;
	}

	@Override
	public int getFrequency()
	{
		return frequency;
	}

	@Override
	public void setFrequency(int value)
	{
		this.frequency = value;
	}

	@Override
	public boolean isBasicRadio()
	{
		return true;
	}

	@Override
	public float getRange()
	{
		float factor = world.isRainingAt(pos)?(float)Mines.weatherHarshness: 1f;
		return Mines.radioRange*factor;
	}

	@Override
	public DimensionBlockPos getDevicePosition()
	{
		return new DimensionBlockPos(this);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return CONN_OFFSET;
	}

	@Override
	protected boolean isRelay()
	{
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		return WireType.REDSTONE_CATEGORY.equals(cableType.getCategory());
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
		if(wireNetwork.getPowerOutput(0) > 0)
			explode();
	}

	@Override
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public void updateInput(byte[] signals)
	{

	}

	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}
		if(hasWorld()&&!world.isRemote)
			wireNetwork.updateValues();
	}
}
