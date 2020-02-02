package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;

/**
 * Created by Pabilo8 on 2019-05-26.
 */
public class MessageBooleanAnimatedPartsSync implements IMessage
{
	//Allows to animate parts in a block (only two states - open and closed)
	public boolean open;
	public int id, x, y, z;

	public MessageBooleanAnimatedPartsSync(boolean open, int id, BlockPos pos)
	{
		this.open = open;
		//The number of the opened / closed component
		this.id = id;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public MessageBooleanAnimatedPartsSync()
	{
		this.open = false;
		this.id = -1;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.open);
		buf.writeInt(this.id);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.open = buf.readBoolean();
		this.id = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

	}

	public static class HandlerServer implements IMessageHandler<MessageBooleanAnimatedPartsSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageBooleanAnimatedPartsSync message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() ->
			{
				//ImmersiveIntelligence.logger.info("server");
				if(world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof IBooleanAnimatedPartsBlock)
				{
					IBooleanAnimatedPartsBlock te = (IBooleanAnimatedPartsBlock)ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
					te.onAnimationChangeServer(message.open, message.id);
				}
			});
			return null;
		}
	}

	public static class HandlerClient implements IMessageHandler<MessageBooleanAnimatedPartsSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageBooleanAnimatedPartsSync message, MessageContext ctx)
		{
			ClientUtils.mc().addScheduledTask(() ->
			{
				World world = ImmersiveEngineering.proxy.getClientWorld();
				//ImmersiveIntelligence.logger.info("client");
				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					if(ClientUtils.mc().world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof IBooleanAnimatedPartsBlock)
					{
						IBooleanAnimatedPartsBlock te = (IBooleanAnimatedPartsBlock)ClientUtils.mc().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
						te.onAnimationChangeClient(message.open, message.id);
					}
				}
			});
			return null;
		}
	}

}