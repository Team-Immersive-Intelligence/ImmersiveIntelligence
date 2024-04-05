package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Easy handling for simple, no reply messages.
 *
 * @author Pabilo8
 * @since 30.08.2022
 */
public abstract class IIMessage implements IMessage
{
	public IIMessage()
	{

	}

	//--- Abstract Methods ---//

	protected abstract void onServerReceive(WorldServer world, NetHandlerPlayServer handler);

	@SideOnly(Side.CLIENT)
	protected abstract void onClientReceive(WorldClient world, NetHandlerPlayClient handler);

	//--- Utility Methods ---//

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeString(ByteBuf buf, String value)
	{
		ByteBufUtils.writeUTF8String(buf, value);
		return buf;
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected String readString(ByteBuf buf)
	{
		return ByteBufUtils.readUTF8String(buf);
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writePos(ByteBuf buf, BlockPos value)
	{
		return buf.writeInt(value.getX()).writeInt(value.getY()).writeInt(value.getZ());
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected BlockPos readPos(ByteBuf buf)
	{
		return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeDimPos(ByteBuf buf, DimensionBlockPos value)
	{
		return buf.writeInt(value.getX()).writeInt(value.getY()).writeInt(value.getZ()).writeInt(value.dimension);
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected DimensionBlockPos readDimPos(ByteBuf buf)
	{
		return new DimensionBlockPos(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeVec3(ByteBuf buf, Vec3d value)
	{
		return buf.writeDouble(value.x).writeDouble(value.y).writeDouble(value.z);
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected Vec3d readVec3(ByteBuf buf)
	{
		return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeTagCompound(ByteBuf buf, NBTTagCompound value)
	{
		ByteBufUtils.writeTag(buf, value);
		return buf;
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected NBTTagCompound readTagCompound(ByteBuf buf)
	{
		return ByteBufUtils.readTag(buf);
	}

	protected <T extends Enum<T>> ByteBuf writeEnum(ByteBuf buf, T value)
	{
		buf.writeInt(value.ordinal());
		return buf;
	}

	protected <T extends Enum<T>> T readEnum(ByteBuf buf, Class<T> enumClass)
	{
		return enumClass.getEnumConstants()[buf.readInt()];
	}

	//--- Message Handler ---//

	public static class IIMessageHandler<MSG extends IIMessage> implements IMessageHandler<MSG, IMessage>
	{
		@Override
		public IMessage onMessage(MSG message, MessageContext ctx)
		{
			if(ctx.side.isClient())
				receiveMessageClient(message, ctx);
			else
				receiveMessageServer(message, ctx);
			return null;
		}

		//--- Internal Methods ---//

		private void receiveMessageServer(MSG message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();

			if(world!=null) //This can happen if the task is scheduled right before leaving the world
				world.addScheduledTask(() -> message.onServerReceive(world, ctx.getServerHandler()));
		}

		@SideOnly(Side.CLIENT)
		private void receiveMessageClient(MSG message, MessageContext ctx)
		{
			if(ClientUtils.mc().world!=null)
				ClientUtils.mc().addScheduledTask(() -> message.onClientReceive(ClientUtils.mc().world, ctx.getClientHandler()));
		}
	}
}
