package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;

/**
 * Created by Pabilo8 on 11-06-2019.
 */
public class TileEntityDataDebugger extends TileEntityIEBase implements ITickable, IRedstoneOutput, IDataDevice, IHammerInteraction
{
	public boolean toggle = false;
	public int mode = 0;

	@Override
	public void update()
	{
		if(mode < 2)
			if(!world.isRemote&&(world.isBlockIndirectlyGettingPowered(getPos()) > 0)&&!toggle)
			{
				toggle = !toggle;
				DataPacket pack = new DataPacket();
				pack.setVariable('a', new DataPacketTypeString("Hello World!"));
				if(Utils.findConnectorAround(pos, world)!=null)
				{
					IDataConnector conn = Utils.findConnectorAround(pos, world);
					conn.sendPacket(pack);
					ImmersiveIntelligence.logger.info("Actually Sending Packet!");
				}
				ImmersiveIntelligence.logger.info("Sending Packet!");
			}
			else if(!world.isRemote&&(world.isBlockIndirectlyGettingPowered(getPos()) > 0)&&toggle)
			{
				toggle = !toggle;
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
		return 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, EnumFacing side)
	{
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		mode = nbt.getInteger("mode");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("mode", mode);
	}

	@Override
	public void onReceive(DataPacket packet)
	{
		if(mode==0||mode==2)
		{
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageNoSpamChatComponents(new TextComponentString(packet.toNBT().toString())), Utils.targetPointFromTile(this, 256));
		}
	}

	@Override
	public void onSend()
	{

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		mode += 1;
		if(mode > 2)
			mode = 0;
		ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(ImmersiveIntelligence.proxy.info_key+"debugger_mode", new TextComponentTranslation(ImmersiveIntelligence.proxy.info_key+"debugger_mode."+mode))), ((EntityPlayerMP)player));
		return true;
	}
}
