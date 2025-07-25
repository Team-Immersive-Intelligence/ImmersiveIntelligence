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
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.IIGUI;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.06.2025
 * @since 17.05.2019
 */
public class TileEntityDataMerger extends TileEntityIEBase implements IPlayerInteraction, ITickable, IBlockBounds, IDirectionalTile, IDataDevice, IGuiTile, IIEInventory
{
	public EnumFacing facing = EnumFacing.NORTH;
	public DataPacket settingsPacket = new DataPacket();
	public DataMergerSendMode mode = DataMergerSendMode.SEND_ON_BOTH;
	DataPacket packetLeft = new DataPacket();
	DataPacket packetRight = new DataPacket();

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = DataMergerSendMode.values()[nbt.getByte("mode")];
		facing = EnumFacing.getFront(nbt.getInteger("facing"));

		settingsPacket = new DataPacket(nbt.getCompoundTag("packet"));
		packetLeft = new DataPacket(nbt.getCompoundTag("packetLeft"));
		packetRight = new DataPacket(nbt.getCompoundTag("packetRight"));

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setByte("mode", (byte)mode.ordinal());
		nbt.setInteger("facing", facing.ordinal());

		nbt.setTag("packet", settingsPacket.serializeNBT());
		nbt.setTag("packetLeft", packetLeft.serializeNBT());
		nbt.setTag("packetRight", packetRight.serializeNBT());
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
			mode = DataMergerSendMode.values()[message.getByte("mode")];
		if(message.hasKey("packet"))
			settingsPacket.deserializeNBT(message.getCompoundTag("packet"));
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
		boolean send = mode==DataMergerSendMode.SEND_ON_BOTH;

		//Incoming packet on the left
		if(side==facing.rotateYCCW())
		{
			packetLeft = packet.clone();
			send = send||mode==DataMergerSendMode.SEND_LEFT_ONLY;
		}

		//Incoming packet on the right
		if(side==facing.rotateY())
		{
			packetRight = packet.clone();
			send = send||mode==DataMergerSendMode.SEND_RIGHT_ONLY;
		}

		if(!send)
			return;

		for(char c : DataPacket.VARIABLE_NAMES)
			IIDataHandlingUtils.optionalInt(c, packet).ifPresent(integer -> {
				switch(VariableMergeMode.values()[integer+2])
				{
					//Original
					case RETAIN_ORIGINAL:
						break;
					//Left
					case FORCE_LEFT:
						packet.set(c, packetLeft.get(c));
						break;
					case PREFER_LEFT:
						if(packetLeft.has(c))
							packet.set(c, packetLeft.get(c));
						break;
					//Right
					case FORCE_RIGHT:
						packet.set(c, packetRight.get(c));
						break;
					case PREFER_RIGHT:
						if(packetRight.has(c))
							packet.set(c, packetRight.get(c));
						break;
				}
			});

		//Send packet
		IIDataHandlingUtils.sendPacketAdjacently(packet, world, this.pos, facing);
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGUI.DATA_MERGER.ordinal();
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

	public enum DataMergerSendMode
	{
		SEND_ON_BOTH,
		SEND_LEFT_ONLY,
		SEND_RIGHT_ONLY,
	}

	enum VariableMergeMode
	{
		PREFER_RIGHT,
		FORCE_RIGHT,
		RETAIN_ORIGINAL,
		PREFER_LEFT,
		FORCE_LEFT
	}
}
