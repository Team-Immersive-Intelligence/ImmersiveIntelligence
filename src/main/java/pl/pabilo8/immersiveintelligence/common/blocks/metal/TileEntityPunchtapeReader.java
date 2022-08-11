package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IISounds;

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
	public int mode = 0, rsTime = 0;
	EnumFacing facing = EnumFacing.NORTH;
	DataPacket received = null;

	@Override
	public void update()
	{
		if(mode==1)
		{
			if(hadRedstone^world.isBlockPowered(pos))
			{
				if(received!=null&&!hadRedstone)
				{
					world.playSound(null, this.pos, IISounds.punchtape_reader, SoundCategory.BLOCKS, 1f, 1f);
					IDataConnector conn = Utils.findConnectorFacing(pos, world, facing.getOpposite());
					if(conn!=null)
						conn.sendPacket(received.clone());
					else
					{
						final TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));
						if(te instanceof IDataDevice)
							((IDataDevice)te).onReceive(received.clone(), facing);
					}
				}
				hadRedstone = world.isBlockPowered(pos);
			}
		}
		else if(mode==2)
		{
			if(rsTime > 0)
				rsTime--;
		}
	}

	@Override
	public int getWeakRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}

	@Override
	public int getStrongRSOutput(IBlockState state, EnumFacing side)
	{
		return mode==2?(side!=facing&&rsTime > 0?15: 0): 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, EnumFacing side)
	{
		return mode > 0;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = nbt.getInteger("mode");
		setFacing(EnumFacing.getFront(nbt.getInteger("facing")));
		if(nbt.hasKey("received"))
			received = new DataPacket().fromNBT(nbt.getCompoundTag("received"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("mode", mode);
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
			mode += 1;
			if(mode > 2)
				mode = 0;
			ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.INFO_KEY+"punchtape_reader_mode", new TextComponentTranslation(CommonProxy.INFO_KEY+"punchtape_reader_mode."+mode))), ((EntityPlayerMP)player));
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
		if(heldItem.getItem() instanceof IDataStorageItem)
		{
			IDataStorageItem storage = (IDataStorageItem)heldItem.getItem();
			DataPacket packet = storage.getStoredData(heldItem);
			received = packet.clone();
			IDataConnector conn = Utils.findConnectorFacing(pos, world, facing.getOpposite());
			if(conn!=null)
				conn.sendPacket(packet);
			else
			{
				final TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));
				if(te instanceof IDataDevice)
					((IDataDevice)te).onReceive(packet, facing);
				if(mode==2)
					rsTime = 20;
			}
			world.playSound(null, this.pos, IISounds.punchtape_reader, SoundCategory.BLOCKS, 1f, 1f);
		}
		return false;
	}
}
