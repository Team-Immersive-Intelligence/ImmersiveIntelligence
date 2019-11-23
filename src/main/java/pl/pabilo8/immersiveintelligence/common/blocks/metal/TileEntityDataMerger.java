package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;

import javax.annotation.Nonnull;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class TileEntityDataMerger extends TileEntityIEBase implements IPlayerInteraction, ITickable, IRotationAcceptor, IBlockBounds, IDirectionalTile, IDataDevice
{
	public EnumFacing facing = EnumFacing.NORTH;
	DataPacket packet = new DataPacket();
	DataPacket packetLeft = new DataPacket();
	DataPacket packetRight = new DataPacket();
	//0 - both, 1 - left only, 2 - right only
	byte mode = 0;

	@Override
	public void inputRotation(double rotation, @Nonnull EnumFacing side)
	{
		if(side!=this.facing.getOpposite())
			return;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = nbt.getByte("mode");
		facing = EnumFacing.byIndex(nbt.getInteger("facing"));
		packet = new DataPacket();
		for(char c : DataPacket.varCharacters)
			packet.setVariable(c, new DataPacketTypeInteger(0));
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
		DataPacket newpacket = new DataPacket();
		boolean send = false;

		//Right
		if(side==facing.rotateY())
		{
			packetRight = packet;
			if(mode==0||mode==2)
			{
				for(char c : packet.variables.keySet())
				{
					if(this.packet.getPacketVariable(c) instanceof DataPacketTypeInteger)
					{
						int m = ((DataPacketTypeInteger)this.packet.getPacketVariable(c)).value;
						switch(m)
						{
							case 0:
							{
								newpacket.setVariable(c, packet.getPacketVariable(c));
							}
							break;
							case 1:
							{
								newpacket.setVariable(c, packetLeft.getPacketVariable(c));
							}
							break;
							case 2:
							{
								newpacket.setVariable(c, packetRight.getPacketVariable(c));
							}
							break;
						}
					}
				}
				send = true;
			}
		}
		//Left
		if(side==facing.rotateYCCW())
		{
			packetLeft = packet;
			if(mode==0||mode==1)
			{
				for(char c : packet.variables.keySet())
				{
					if(this.packet.getPacketVariable(c) instanceof DataPacketTypeInteger)
					{
						int m = ((DataPacketTypeInteger)this.packet.getPacketVariable(c)).value;
						switch(m)
						{
							case 0:
							{
								newpacket.setVariable(c, packet.getPacketVariable(c));
							}
							break;
							case 1:
							{
								newpacket.setVariable(c, packetLeft.getPacketVariable(c));
							}
							break;
							case 2:
							{
								newpacket.setVariable(c, packetRight.getPacketVariable(c));
							}
							break;
						}
					}
				}
			}
			send = true;
		}

		if(send)
		{
			if(world.isBlockLoaded(this.pos.offset(facing))&&world.getTileEntity(this.pos.offset(facing)) instanceof IDataDevice)
			{
				IDataDevice d = (IDataDevice)world.getTileEntity(this.pos.offset(facing));
				d.onReceive(packet, facing);
			}
		}

	}

	@Override
	public void onSend()
	{

	}
}
