package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.utils.IRotationalEnergyBlock;

/**
 * Created by Pabilo8 on 2020-01-11.
 */
public class MessageRotaryPowerSync implements IMessage
{
	public float torque, rpm;
	public int id, x, y, z;

	public MessageRotaryPowerSync(IRotaryEnergy energy, int id, BlockPos pos)
	{
		this.rpm = energy.getRotationSpeed();
		this.torque = energy.getTorque();

		this.id = id;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public MessageRotaryPowerSync()
	{
		this.rpm = 0;
		this.torque = 0;

		this.id = -1;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(this.rpm);
		buf.writeFloat(this.torque);
		buf.writeInt(this.id);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.rpm = buf.readFloat();
		this.torque = buf.readFloat();
		this.id = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

	}

	public static class HandlerClient implements IMessageHandler<MessageRotaryPowerSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageRotaryPowerSync message, MessageContext ctx)
		{
			ClientUtils.mc().addScheduledTask(() ->
			{
				World world = ImmersiveEngineering.proxy.getClientWorld();

				if(world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					BlockPos bpos = new BlockPos(message.x, message.y, message.z);
					if(ClientUtils.mc().world.isBlockLoaded(bpos)&&ClientUtils.mc().world.getTileEntity(bpos) instanceof IRotationalEnergyBlock)
					{
						IRotationalEnergyBlock te = (IRotationalEnergyBlock)ClientUtils.mc().world.getTileEntity(bpos);
						te.updateRotationStorage(message.rpm, message.torque, message.id);
					}
				}
			});
			return null;
		}
	}

}