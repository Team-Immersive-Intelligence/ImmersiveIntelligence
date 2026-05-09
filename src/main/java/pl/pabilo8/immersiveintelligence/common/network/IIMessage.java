package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
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
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Easy handling for simple, no reply messages.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
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
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeUUID(ByteBuf buf, UUID value)
	{
		ByteBufUtils.writeUTF8String(buf, value.toString());
		return buf;
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
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeDimPos(ByteBuf buf, DimensionBlockPos value)
	{
		return buf.writeInt(value.getX()).writeInt(value.getY()).writeInt(value.getZ()).writeInt(value.dimension);
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
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected <T extends Enum<T>> ByteBuf writeEnum(ByteBuf buf, T value)
	{
		buf.writeInt(value.ordinal());
		return buf;
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeColor(ByteBuf buf, IIColor value)
	{
		buf.writeInt(value.getPackedRGB());
		return buf;
	}

	protected ByteBuf writeItemStack(ByteBuf buf, ItemStack stack)
	{
		ByteBufUtils.writeItemStack(buf, stack);
		return buf;
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeEasyNBT(ByteBuf buf, EasyNBT value)
	{
		return value.writeToByteBuf(buf);
	}

	/**
	 * @param buf   buffer to write to
	 * @param value value to save
	 * @return buf
	 */
	protected ByteBuf writeChunkedData(ByteBuf buf, MessageChunkedData value)
	{
		writeUUID(buf, value.sessionId);
		buf.writeInt(value.totalChunks);
		buf.writeInt(value.index);
		buf.writeInt(value.chunk.length);
		buf.writeBytes(value.chunk);
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
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected UUID readUUID(ByteBuf buf)
	{
		return UUID.fromString(ByteBufUtils.readUTF8String(buf));
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
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected DimensionBlockPos readDimPos(ByteBuf buf)
	{
		return new DimensionBlockPos(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
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
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected NBTTagCompound readTagCompound(ByteBuf buf)
	{
		return ByteBufUtils.readTag(buf);
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected EasyNBT readEasyNBT(ByteBuf buf)
	{
		return EasyNBT.wrapNBT(buf);
	}

	/**
	 * @param buf       buffer to read from
	 * @param enumClass class of the read enum
	 * @param <T>       enum type
	 * @return value read
	 */
	protected <T extends Enum<T>> T readEnum(ByteBuf buf, Class<T> enumClass)
	{
		return enumClass.getEnumConstants()[buf.readInt()];
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected IIColor readColor(ByteBuf buf)
	{
		return IIColor.fromPackedRGB(buf.readInt());
	}

	/**
	 * @param buf buffer to read from
	 * @return value read
	 */
	protected ItemStack readItemStack(ByteBuf buf)
	{
		return ByteBufUtils.readItemStack(buf);
	}

	/**
	 * Reads a chunk of NBT data from the buffer and adds it to the list of received chunks.
	 * If the complete message was received, it will be dechunked and the list will be cleared. Otherwise, null is returned.
	 *
	 * @param buf  buffer to read from
	 * @param list list of already received chunks for this message, to which the read chunk will be added
	 * @return null, if the complete message was not yet received, NBT data if it was.
	 */
	protected NBTTagCompound readChunkedNBT(ByteBuf buf, List<MessageChunkedData> list)
	{
		UUID uuid = readUUID(buf);
		int totalChunks = buf.readInt();
		int index = buf.readInt();
		int length = buf.readInt();
		byte[] data = new byte[length];
		buf.readBytes(data);

		MessageChunkedData chunk = new MessageChunkedData(uuid, totalChunks, index, data);
		list.add(chunk);
		List<MessageChunkedData> collect = list.stream()
				.filter(cd -> cd.sessionId.equals(chunk.sessionId))
				.collect(Collectors.toList());
		//Error upon receiving more chunks than expected
		if(collect.size() > chunk.getTotalChunks())
			list.removeAll(collect);

			//All chunks received, dechunk and return nbt
		else if(collect.size()==chunk.getTotalChunks())
		{
			list.removeAll(collect);
			return dechunkNBT(collect);
		}
		return null;
	}

	@Nonnull
	protected static List<MessageChunkedData> chunkNBT(@Nonnull NBTTagCompound nbt)
	{
		List<MessageChunkedData> list = new ArrayList<>();

		//Convert to compressed byte array
		byte[] data;
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			CompressedStreamTools.write(nbt, dos);
			data = bos.toByteArray();
		} catch(IOException e)
		{
			return list;
		}

		//Split into chunks of max 1MB
		final int MAX_CHUNK_SIZE = 1572864;
		int totalChunks = Math.max(1, (int)Math.ceil(data.length/(double)MAX_CHUNK_SIZE));
		//Create a session ID to group chunks on the client
		UUID sessionId = UUID.randomUUID();
		for(int i = 0; i < totalChunks; i++)
		{
			int start = i*MAX_CHUNK_SIZE;
			int end = Math.min(start+MAX_CHUNK_SIZE, data.length);
			byte[] chunk = Arrays.copyOfRange(data, start, end);
			list.add(new MessageChunkedData(sessionId, totalChunks, i, chunk));
		}
		return list;
	}

	@Nonnull
	protected static NBTTagCompound dechunkNBT(@Nonnull List<MessageChunkedData> list)
	{
		if(list.isEmpty())
			return new NBTTagCompound();

		//Group chunks by session ID
		Map<UUID, List<byte[]>> sessions = new HashMap<>();
		for(MessageChunkedData chunk : list)
			sessions.computeIfAbsent(chunk.getSessionId(), k -> new ArrayList<>()).add(chunk.getChunk());

		//Find the session with the most chunks (in case of multiple sessions, which shouldn't happen but just in case)
		List<byte[]> chunks = Collections.emptyList();
		for(List<byte[]> sessionChunks : sessions.values())
			if(sessionChunks.size() > chunks.size())
				chunks = sessionChunks;

		//Combine chunks into a single byte array
		int totalSize = chunks.stream().mapToInt(c -> c.length).sum();
		byte[] data = new byte[totalSize];
		int pos = 0;
		for(byte[] chunk : chunks)
		{
			System.arraycopy(chunk, 0, data, pos, chunk.length);
			pos += chunk.length;
		}

		try
		{
			return CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(data)));
		} catch(IOException e)
		{
			return new NBTTagCompound();
		}
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

			//noinspection ConstantValue
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

	/**
	 * Helper class to allow sending more complex NBT structures that may exceed packet size limits.
	 */
	protected static class MessageChunkedData
	{
		private final UUID sessionId;
		private final byte[] chunk;
		private final int index;
		private final int totalChunks;

		public MessageChunkedData(UUID sessionId, int totalChunks, int index, byte[] chunk)
		{
			this.sessionId = sessionId;
			this.chunk = chunk;
			this.index = index;
			this.totalChunks = totalChunks;
		}

		public UUID getSessionId()
		{
			return sessionId;
		}

		public byte[] getChunk()
		{
			return chunk;
		}

		public int getIndex()
		{
			return index;
		}

		public int getTotalChunks()
		{
			return totalChunks;
		}
	}
}
