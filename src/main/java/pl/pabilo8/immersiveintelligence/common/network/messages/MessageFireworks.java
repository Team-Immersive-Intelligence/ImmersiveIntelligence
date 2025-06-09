package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

public class MessageFireworks extends IIMessage
{
	private NBTTagCompound tag;
	private Vec3d pos;

	public MessageFireworks(NBTTagCompound tag, Vec3d pos)
	{
		this.tag = tag;
		this.pos = pos;
	}

	public MessageFireworks()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		NBTTagList list = new NBTTagList();
		list.appendTag(tag.getTag("Explosion"));
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Explosions", list);
		world.makeFireworks(pos.x, pos.y, pos.z, 0, 0, 0, nbt);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.tag = readTagCompound(buf);
		this.pos = readVec3(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeTagCompound(buf, this.tag);
		writeVec3(buf, pos);
	}
}