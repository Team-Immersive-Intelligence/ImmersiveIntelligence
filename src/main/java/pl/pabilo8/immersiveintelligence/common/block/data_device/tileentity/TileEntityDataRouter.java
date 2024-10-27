package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
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
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class TileEntityDataRouter extends TileEntityIEBase implements IPlayerInteraction, IHammerInteraction, ITickable, IBlockBounds, IDirectionalTile, IDataDevice
{
	public EnumFacing facing = EnumFacing.NORTH;
	char variable = '0';

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		variable = nbt.getString("variable").charAt(0);

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setString("variable", String.valueOf(variable));
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
		return new float[]{0f, 0f, 0f, 1f, 1f, 1f};
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
		if(packet.getPacketVariable(variable) instanceof DataTypeInteger)
		{
			int c = ((DataTypeInteger)packet.getPacketVariable(variable)).value;
			if(world.isBlockLoaded(this.pos.offset(EnumFacing.getFront(c)))&&world.getTileEntity(this.pos.offset(EnumFacing.getFront(c))) instanceof IDataConnector)
			{
				IDataConnector d = (IDataConnector)world.getTileEntity(this.pos.offset(EnumFacing.getFront(c)));
				d.sendPacket(packet);
			}
		}

	}

	@Override
	public boolean hammerUseSide(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		variable = IIUtils.cycleDataPacketChars(variable, hitY >= 0.5f, false);
		IIPacketHandler.sendChatString(player, String.valueOf(variable));
		return true;
	}
}
