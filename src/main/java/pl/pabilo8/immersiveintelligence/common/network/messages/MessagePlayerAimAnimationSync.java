package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

public class MessagePlayerAimAnimationSync extends IIMessage
{
	private int entityID;
	private boolean aiming;

	public MessagePlayerAimAnimationSync(Entity entity, boolean aiming)
	{
		this.entityID = entity.getEntityId();
		this.aiming = aiming;
	}

	public MessagePlayerAimAnimationSync()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		Entity entity = world.getEntityByID(entityID);
		if(entity instanceof EntityLivingBase)
			if(aiming)
			{
				if(!ClientEventHandler.aimingPlayers.contains(entity))
					ClientEventHandler.aimingPlayers.add((EntityLivingBase)entity);
			}
			else
				ClientEventHandler.aimingPlayers.removeIf(entityPlayer -> entityPlayer.equals(entity));
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.aiming = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.entityID);
		buf.writeBoolean(aiming);
	}
}