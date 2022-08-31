package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 20.07.2021
 */
public class MessageParticleGunfire extends IIMessage
{
	private boolean usesEntity;
	private int entityID;

	public Vec3d pos;
	public Vec3d dir;
	public float size;

	/**
	 * @param pos  of the shooter
	 * @param dir  of the shot
	 * @param size of the bullet
	 */
	public MessageParticleGunfire(Vec3d pos, Vec3d dir, float size)
	{
		this.usesEntity = false;
		this.pos = pos;
		this.dir = dir;
		this.size = size;
	}

	/**
	 * Use for biped model entities firing hand weapons, like submachineguns
	 *
	 * @param entity which is shooting
	 */
	public MessageParticleGunfire(Entity entity, float size)
	{
		this.usesEntity = true;
		this.entityID = entity.getEntityId();
		this.size = size;
	}

	public MessageParticleGunfire()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(usesEntity)
		{
			Entity entity = world.getEntityByID(entityID);
			if(entity instanceof EntityLivingBase)
				ClientEventHandler.gunshotEntities.put(((EntityLivingBase)entity), size);
		}
		else
			ParticleUtils.spawnGunfireFX(pos,dir, size);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.usesEntity = buf.readBoolean();
		if(this.usesEntity)
			this.entityID = buf.readInt();
		else
		{
			this.pos = readVec3(buf);
			this.dir = readVec3(buf);
		}
		this.size = buf.readFloat();

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(usesEntity);

		if(usesEntity)
			buf.writeInt(this.entityID);
		else
		{
			writeVec3(buf, pos);
			writeVec3(buf, dir);
		}
		buf.writeFloat(size);
	}
}
