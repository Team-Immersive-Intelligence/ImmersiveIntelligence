package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.vecmath.Vector2f;


/**
 * Tells the client to spawn an II particle effect.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.12.2024
 * @ii-approved 0.3.1
 * @since 20.01.2021
 */
public class MessageParticleEffect extends IIMessage implements IPositionBoundMessage
{
	private String id;
	private World world;
	private Vec3d position, motion;
	private Vector2f rotation;
	private EasyNBT nbt;

	public MessageParticleEffect(String id, World world, Vec3d position, Vec3d motion, Vector2f rotation, EasyNBT nbt)
	{
		this.id = id;
		this.world = world;
		this.position = position;
		this.motion = motion;
		this.rotation = rotation;
		this.nbt = nbt;
	}

	public MessageParticleEffect(String id, World world, Vec3d position, Vec3d motion, float rotationYaw, float rotationPitch, EasyNBT nbt)
	{
		this(id, world, position, motion, new Vector2f(rotationYaw, rotationPitch), nbt);
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
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.id = ByteBufUtils.readUTF8String(buf);
		this.nbt = readEasyNBT(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, id);
		writeEasyNBT(buf, (nbt = (nbt==null?EasyNBT.newNBT(): nbt))
				.withVec3d(ParticleProperties.POSITION.getName(), position)
				.withVec3d(ParticleProperties.MOTION.getName(), motion)
				.withVec2d(ParticleProperties.ROTATION.getName(), rotation)
		);
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Vec3d getPosition()
	{
		return position;
	}

	@Override
	public int getPacketDistance()
	{
		return 96;
	}
}
