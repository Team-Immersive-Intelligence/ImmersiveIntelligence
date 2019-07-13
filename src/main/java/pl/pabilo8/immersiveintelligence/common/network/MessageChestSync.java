package pl.pabilo8.immersiveintelligence.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAmmunitionCrate;

/**
 * Created by Pabilo8 on 2019-05-26.
 */
public class MessageChestSync implements IMessage
{
	public boolean open;
	public int x, y, z;

	public MessageChestSync(boolean open, BlockPos pos)
	{
		this.open = open;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public MessageChestSync()
	{
		this.open = false;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.open);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.open = buf.readBoolean();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

	}

	public static class Handler implements IMessageHandler<MessageChestSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageChestSync message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> onMessageMain(message));
			return null;
		}

		private void onMessageMain(MessageChestSync message)
		{
			if(Minecraft.getMinecraft().world.isBlockLoaded(new BlockPos(message.x, message.y, message.z))&&Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof TileEntityAmmunitionCrate)
			{
				TileEntityAmmunitionCrate te = (TileEntityAmmunitionCrate)Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
				te.open = message.open;
			}
		}
	}

}