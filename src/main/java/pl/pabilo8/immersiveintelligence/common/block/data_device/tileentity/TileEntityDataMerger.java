package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
// TODO: 22.09.2022 rework this shite of a device
public class TileEntityDataMerger extends TileEntityIEBase implements IPlayerInteraction, ITickable, IBlockBounds, IDirectionalTile, IDataDevice, IGuiTile, IIEInventory
{
	public EnumFacing facing = EnumFacing.NORTH;
	public DataPacket packet = new DataPacket();
	//0 - both, 1 - left only, 2 - right only
	public byte mode = 0;
	DataPacket packetLeft = new DataPacket();
	DataPacket packetRight = new DataPacket();

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = nbt.getByte("mode");
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
		packet = new DataPacket();
		for(char c : DataPacket.varCharacters)
			packet.setVariable(c, new DataTypeInteger(0));
		packetLeft = new DataPacket();
		packetRight = new DataPacket();
		if(nbt.hasKey("packet"))
			packet.fromNBT(nbt.getCompoundTag("packet"));
		if(nbt.hasKey("packetLeft"))
			packetLeft.fromNBT(nbt.getCompoundTag("packetLeft"));
		if(nbt.hasKey("packetRight"))
			packetRight.fromNBT(nbt.getCompoundTag("packetRight"));

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setByte("mode", mode);
		nbt.setInteger("facing", facing.ordinal());
		nbt.setTag("packet", packet.toNBT());
		nbt.setTag("packetLeft", packetLeft.toNBT());
		nbt.setTag("packetRight", packetRight.toNBT());
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("mode"))
			mode = message.getByte("mode");
		if(message.hasKey("packet"))
			packet.fromNBT(message.getCompoundTag("packet"));
	}

	@Override
	public void update()
	{

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
		DataPacket newpacket = packet.clone();
		boolean send = mode==0;

		//Left -2 -1 (1)
		if(side==facing.rotateYCCW())
		{
			packetLeft = packet.clone();
			send = send||mode==1;
			if(send&&packetRight.hasAnyVariables())
			{
				for(char c : packetRight.variables.keySet())
				{
					if(!packetLeft.hasVariable(c))
					{
						newpacket.setVariable(c, packetRight.getPacketVariable(c));
					}
				}
			}
		}

		//Right 1 2 (2)
		if(side==facing.rotateY())
		{
			packetRight = packet.clone();
			send = send||mode==2;
			if(send&&packetLeft.hasAnyVariables())
			{
				for(char c : packetLeft.variables.keySet())
				{
					if(!packetRight.hasVariable(c))
					{
						newpacket.setVariable(c, packetLeft.getPacketVariable(c));
					}
				}
			}
		}

		for(char c : DataPacket.varCharacters)
			if(this.packet.getPacketVariable(c) instanceof DataTypeInteger)
				switch(((DataTypeInteger)this.packet.getPacketVariable(c)).value)
				{
					case 0:
					{
					}
					break;
					case 2:
					{
						if(packetLeft.variables.containsKey(c))
							newpacket.setVariable(c, packetLeft.getPacketVariable(c));
						else
							newpacket.removeVariable(c);
					}
					break;
					case 1:
					{
						if(packetLeft.variables.containsKey(c))
							newpacket.setVariable(c, packetLeft.getPacketVariable(c));
					}
					break;
					case -2:
					{
						if(packetRight.variables.containsKey(c))
							newpacket.setVariable(c, packetRight.getPacketVariable(c));
						else
							newpacket.removeVariable(c);
					}
					break;
					case -1:
					{
						if(packetRight.variables.containsKey(c))
							newpacket.setVariable(c, packetRight.getPacketVariable(c));
					}
					break;
				}
		if(send&&world.isBlockLoaded(this.pos.offset(facing))&&world.getTileEntity(this.pos.offset(facing)) instanceof IDataConnector)
		{
			IDataConnector d = (IDataConnector)world.getTileEntity(this.pos.offset(facing));
			d.sendPacket(newpacket);
		}

	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_DATA_MERGER.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return this;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}
}
