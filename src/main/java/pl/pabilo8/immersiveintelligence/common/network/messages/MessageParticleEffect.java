package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import static pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties.*;

/**
 * @author Pabilo8
 * @since 20.01.2021
 */
public class MessageParticleEffect extends IIMessage implements IPositionBoundMessage
{
	private String id;
	private World world;
	private Vec3d pos, motion, dir;
	private NBTTagCompound nbt;

	public MessageParticleEffect(String id, World world, Vec3d pos)
	{
		this(id, world, pos, Vec3d.ZERO);
	}

	public MessageParticleEffect(String id, World world, Vec3d pos, Vec3d dir)
	{
		this(id, world, pos, Vec3d.ZERO, new NBTTagCompound());
	}

	public MessageParticleEffect(String id, World world, Vec3d pos, Vec3d dir, NBTTagCompound mbt)
	{
		this.id = id;
		this.world = world;
		this.pos = pos;
		this.motion = Vec3d.ZERO;
		this.dir = dir;
		this.nbt = mbt;
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
		ParticleRegistry.spawnParticle(id, nbt);

		/*switch(id)
		{
			case "white_phosphorus":
				ParticleRegistry.spawnExplosionPhosphorusFX(pos);
				break;
			case "motorbike_explosion":
			{
				Entity e = world.getEntityByID(((int)pos.x));
				if(e instanceof EntityMotorbike)
				{
					EntityMotorbike motorbike = (EntityMotorbike)e;
					motorbike.selfDestruct();
				}
			}
			break;
			case "gunfire":
				ParticleRegistry.spawnGunfireFX(pos, Vec3d.ZERO, 8f);
				break;
		}*/
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.id = ByteBufUtils.readUTF8String(buf);
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		EasyNBT.wrapNBT(nbt)
				.withVec3d(POSITION, pos)
				.withVec3d(MOTION, motion)
				.withVec3d(ROTATION, dir);

		ByteBufUtils.writeUTF8String(buf, id);
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Vec3d getPosition()
	{
		return pos;
	}
}