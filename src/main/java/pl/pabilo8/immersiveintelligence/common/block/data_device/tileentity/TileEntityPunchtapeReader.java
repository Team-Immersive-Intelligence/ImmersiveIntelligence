package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 11-06-2019
 */
public class TileEntityPunchtapeReader extends TileEntityIEBase implements ITickable, IRedstoneOutput, IDataDevice, IPlayerInteraction, IHammerInteraction, IDirectionalTile
{
	public boolean hadRedstone = false;
	//no redstone
	//send signal on redstone
	//redstone when signal is sent
	public PunchtapeReaderMode mode;
	public int rsTime = 0;
	EnumFacing facing = EnumFacing.NORTH;
	DataPacket received = null;

	@Override
	public void update()
	{
		if(mode==PunchtapeReaderMode.PACKET_ON_REDSTONE)
		{
			if(hadRedstone^world.isBlockPowered(pos))
			{
				if(received!=null&&!hadRedstone)
				{
					world.playSound(null, this.pos, IISounds.punchtapeReader, SoundCategory.BLOCKS, 1f, 1f);
					IIDataHandlingUtils.sendPacketAdjacently(received.clone(), world, pos, facing.getOpposite());
				}
				hadRedstone = world.isBlockPowered(pos);
			}
		}
		else if(mode==PunchtapeReaderMode.REDSTONE_ON_PACKET)
			if(rsTime > 0)
				rsTime--;
	}

	@Override
	public int getWeakRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}

	@Override
	public int getStrongRSOutput(IBlockState state, EnumFacing side)
	{
		return mode==PunchtapeReaderMode.REDSTONE_ON_PACKET?(side!=facing&&rsTime > 0?15: 0): 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, EnumFacing side)
	{
		return mode!=PunchtapeReaderMode.REDSTONE_INDIFFERENT;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = PunchtapeReaderMode.values()[nbt.getInteger("mode")];
		setFacing(EnumFacing.getFront(nbt.getInteger("facing")));
		if(nbt.hasKey("received"))
			received = new DataPacket().fromNBT(nbt.getCompoundTag("received"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("mode", mode.ordinal());
		nbt.setInteger("facing", facing.ordinal());
		if(received!=null)
			nbt.setTag("received", received.toNBT());
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
		{
			IIUtils.cycleEnum(true, PunchtapeReaderMode.class, mode);
			IIPacketHandler.sendChatTranslation(player, IIReference.INFO_KEY+"punchtape_reader_mode",
					new TextComponentTranslation(IIReference.INFO_KEY+"punchtape_reader_mode."+mode));
			markDirty();
			markBlockForUpdate(getPos(), null);
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
		return true;
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
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(!(heldItem.getItem() instanceof IDataStorageItem))
			return false;

		received = ((IDataStorageItem)heldItem.getItem()).getStoredData(heldItem).clone();
		IIDataHandlingUtils.sendPacketAdjacently(received, world, pos, facing.getOpposite());
		if(mode==PunchtapeReaderMode.REDSTONE_ON_PACKET)
			rsTime = 20;

		world.playSound(null, this.pos, IISounds.punchtapeReader, SoundCategory.BLOCKS, 1f, 1f);
		return true;
	}

	private enum PunchtapeReaderMode
	{
		REDSTONE_INDIFFERENT,
		PACKET_ON_REDSTONE,
		REDSTONE_ON_PACKET,
	}
}
