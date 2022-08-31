package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
public class MessageBooleanAnimatedPartsSync extends IIMessage
{
	//Allows to animate parts in a block (only two states - open and closed)
	private boolean open;
	private int id; //The number of the opened / closed component
	private BlockPos pos;

	public MessageBooleanAnimatedPartsSync(boolean open, int id, BlockPos pos)
	{
		this.open = open;
		this.id = id;
		this.pos = pos;
	}

	public MessageBooleanAnimatedPartsSync()
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(this.open);
		buf.writeInt(this.id);
		writePos(buf, pos);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.open = buf.readBoolean();
		this.id = buf.readInt();
		this.pos = readPos(buf);
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		if(world.isBlockLoaded(pos))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IBooleanAnimatedPartsBlock)
				((IBooleanAnimatedPartsBlock)te).onAnimationChangeServer(open, id);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(world.isBlockLoaded(pos))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IBooleanAnimatedPartsBlock)
				((IBooleanAnimatedPartsBlock)te).onAnimationChangeClient(open, id);
		}
	}
}