package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SmallDataBuffer;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class TileEntitySmallDataBuffer extends TileEntityIEBase implements IPlayerInteraction, ITickable, IBlockBounds, IDirectionalTile, IDataDevice
{
	public boolean toggle = false;
	public EnumFacing facing = EnumFacing.NORTH;
	ArrayList<DataPacket> packets = new ArrayList<>();

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
		if(nbt.hasKey("packets"))
		{
			packets.clear();
			for(NBTBase base : nbt.getTagList("", 10).tagList)
			{
				DataPacket pack = new DataPacket();
				pack.fromNBT((NBTTagCompound)base);
				packets.add(pack);
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("facing", facing.ordinal());
		NBTTagList list = new NBTTagList();
		for(DataPacket pack : packets)
		{
			list.appendTag(pack.toNBT());
		}
		nbt.setTag("packet", list);
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return false;
	}


	@Override
	public void update()
	{
		if(!world.isRemote)
		{
			if(toggle^world.isBlockPowered(this.getPos()))
			{

				toggle = !toggle;

				if(toggle)
				{
					BlockPos newpos = pos.offset(facing);
					if(!packets.isEmpty()&&world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
					{
						IDataConnector con = ((IDataConnector)world.getTileEntity(newpos));
						con.sendPacket(packets.get(0));
						packets.remove(0);
					}
				}
			}
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, .875f, 1f};
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
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(packets.size() < SmallDataBuffer.packetCapacity)
		{
			DataPacket np = new DataPacket();
			np.fromNBT(packet.toNBT());
			this.packets.add(np);
		}
		//for (DataPacket pck : packets)
	}

}
