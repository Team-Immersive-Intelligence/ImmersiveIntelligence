package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotationalEnergyBlock;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.01.2020
 */
public class MessageRotaryPowerSync extends IIMessage implements IPositionBoundMessage
{
	private World world;
	private BlockPos pos;
	private float torque, rpm;
	private int id;

	public MessageRotaryPowerSync(World world, BlockPos pos, int id, IRotaryEnergy energy)
	{
		this.rpm = energy.getRotationSpeed();
		this.torque = energy.getTorque();
		this.id = id;
		this.world = world;
		this.pos = pos;
	}

	public MessageRotaryPowerSync()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(world.isBlockLoaded(pos))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IRotationalEnergyBlock)
				((IRotationalEnergyBlock)te).updateRotationStorage(rpm, torque, id);
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(this.rpm);
		buf.writeFloat(this.torque);
		buf.writeInt(this.id);
		writePos(buf, pos);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.rpm = buf.readFloat();
		this.torque = buf.readFloat();
		this.id = buf.readInt();
		this.pos = readPos(buf);

	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Vec3d getPosition()
	{
		return new Vec3d(pos);
	}
}
