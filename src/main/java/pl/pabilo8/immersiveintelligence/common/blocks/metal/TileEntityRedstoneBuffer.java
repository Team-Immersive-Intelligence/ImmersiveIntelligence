package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class TileEntityRedstoneBuffer extends TileEntityIEBase implements IPlayerInteraction, ITickable, IBlockBounds, IDirectionalTile, IDataDevice, IHammerInteraction
{
	public boolean toggle = false;
	public boolean passtroughMode = false;
	public EnumFacing facing = EnumFacing.NORTH;
	DataPacket packet = new DataPacket();

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
		packet = new DataPacket();
		if(nbt.hasKey("packet"))
			packet.fromNBT(nbt.getCompoundTag("packet"));
		passtroughMode = nbt.getBoolean("passtroughMode");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("facing", facing.ordinal());
		nbt.setTag("packet", packet.toNBT());
		nbt.setBoolean("passtroughMode", passtroughMode);
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
			if(packet.hasAnyVariables())
			{
				if(passtroughMode&&world.isBlockPowered(this.getPos()))
				{
					IDataConnector conn = Utils.findConnectorFacing(pos, world, facing);
					if(conn!=null)
					{
						conn.sendPacket(packet.clone());
						packet=new DataPacket();
					}
				}
				else if(toggle^world.isBlockPowered(this.getPos()))
				{
					toggle = !toggle;
					if(toggle)
					{
						IDataConnector conn = Utils.findConnectorFacing(pos, world, facing);
						if(conn!=null)
							conn.sendPacket(packet.clone());
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
		this.packet = packet;
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
		{
			passtroughMode = !passtroughMode;
			ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.INFO_KEY+"redstone_buffer_mode", new TextComponentTranslation(CommonProxy.INFO_KEY+"redstone_buffer_mode."+(passtroughMode?"passtrough":"signal")))), ((EntityPlayerMP)player));
		}
		return true;
	}
}
