package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 20.01.2021
 */
public class MessageParticleEffect extends IIMessage
{
	private Vec3d pos;
	private String id;

	public MessageParticleEffect(Vec3d pos, String id)
	{
		this.pos = pos;
		this.id = id;
	}

	public MessageParticleEffect()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		switch(id)
		{
			case "white_phosphorus":
				ParticleUtils.spawnExplosionPhosphorusFX(pos);
				break;
			case "motorbike_explosion":
			{
				// TODO: 30.08.2022 improve 
				Entity e = world.getEntityByID(((int)pos.x));
				if(e instanceof EntityMotorbike)
				{
					EntityMotorbike motorbike = (EntityMotorbike)e;
					motorbike.selfDestruct();
				}
			}
			break;
			case "gunfire":
				ParticleUtils.spawnGunfireFX(pos, Vec3d.ZERO, 8f);
				break;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = readVec3(buf);
		this.id = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeVec3(buf, pos);
		ByteBufUtils.writeUTF8String(buf, id);
	}
}