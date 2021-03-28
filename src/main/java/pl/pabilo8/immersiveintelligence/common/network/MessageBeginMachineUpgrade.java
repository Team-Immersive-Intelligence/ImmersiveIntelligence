package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;

public class MessageBeginMachineUpgrade implements IMessage
{
	BlockPos pos;
	String machineID;

	public MessageBeginMachineUpgrade(TileEntity tile, String machineID)
	{
		this.pos = tile.getPos();
		this.machineID = machineID;
	}

	public MessageBeginMachineUpgrade()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.machineID = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX()).writeInt(pos.getY()).writeInt(pos.getZ());
		ByteBufUtils.writeUTF8String(buf, machineID);
	}

	public static class HandlerServer implements IMessageHandler<MessageBeginMachineUpgrade, IMessage>
	{
		@Override
		public IMessage onMessage(MessageBeginMachineUpgrade message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				if(world.isBlockLoaded(message.pos))
				{
					TileEntity tile = world.getTileEntity(message.pos);
					MachineUpgrade upgrade = MachineUpgrade.getUpgradeByID(message.machineID);
					if(tile instanceof IUpgradableMachine&&((IUpgradableMachine)tile).getInstallProgress()==0&&upgrade!=null&&((IUpgradableMachine)tile).upgradeMatches(upgrade))
					{
						((IUpgradableMachine)tile).startUpgrade(upgrade);
						IIPacketHandler.INSTANCE.sendToAllTracking(message, Utils.targetPointFromTile(tile,32));
					}
				}
			});
			return null;
		}
	}

	public static class HandlerClient implements IMessageHandler<MessageBeginMachineUpgrade, IMessage>
	{
		@Override
		public IMessage onMessage(MessageBeginMachineUpgrade message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> {
				World world = ImmersiveEngineering.proxy.getClientWorld();
				if (world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					TileEntity tile = world.getTileEntity(message.pos);
					MachineUpgrade upgrade = MachineUpgrade.getUpgradeByID(message.machineID);
					if(tile instanceof IUpgradableMachine&&((IUpgradableMachine)tile).getInstallProgress()==0&&upgrade!=null)
					{
						((IUpgradableMachine)tile).startUpgrade(upgrade);
					}
				}
			});
			return null;
		}
	}
}