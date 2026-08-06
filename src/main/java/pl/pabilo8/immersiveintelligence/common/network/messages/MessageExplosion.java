package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sends an explosion visual effect to nearby clients.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.08.2026
 */
public class MessageExplosion extends IIMessage implements IPositionBoundMessage
{
	private static final int NUKE_MESSAGE_DISTANCE = 512;
	private static final int WHITE_PHOSPHORUS_MESSAGE_DISTANCE = 128;
	private static final int EMP_MESSAGE_DISTANCE = 192;
	private static final int MAX_EMP_TARGETS = 4096;

	private EffectType effectType = EffectType.EXPLOSION;
	private World world;
	private boolean flaming, damagesTerrain;
	private float radius, strength, size;
	private Vec3d pos = Vec3d.ZERO, direction = Vec3d.ZERO;
	private ComponentEffectShape shape = ComponentEffectShape.ORB;
	private List<BlockPos> particleBlocks = Collections.emptyList();
	private List<Vec3d> effectTargets = Collections.emptyList();

	private MessageExplosion(EffectType effectType, World world, Vec3d pos)
	{
		this.effectType = effectType;
		this.world = world;
		this.pos = pos;
	}

	/**
	 * Creates an empty message for Forge decoding.
	 */
	public MessageExplosion()
	{
	}

	/**
	 * Creates a regular explosion effect message without a surface sample.
	 */
	public static MessageExplosion createExplosionMessage(World world, boolean flaming, boolean damagesTerrain,
	                                                      float radius, float strength, Vec3d pos, Vec3d direction,
	                                                      ComponentEffectShape shape)
	{
		return createExplosionMessage(world, flaming, damagesTerrain, radius, strength, pos, direction, shape,
				Collections.emptyList());
	}

	/**
	 * Creates a regular explosion effect message with a bounded surface sample.
	 */
	public static MessageExplosion createExplosionMessage(World world, boolean flaming, boolean damagesTerrain,
	                                                      float radius, float strength, Vec3d pos, Vec3d direction,
	                                                      ComponentEffectShape shape, List<BlockPos> particleBlocks)
	{
		MessageExplosion message = new MessageExplosion(EffectType.EXPLOSION, world, pos);
		message.flaming = flaming;
		message.damagesTerrain = damagesTerrain;
		message.radius = radius;
		message.strength = strength;
		message.direction = direction;
		message.shape = shape;
		message.particleBlocks = particleBlocks==null||particleBlocks.isEmpty()?Collections.emptyList():
				new ArrayList<>(particleBlocks);
		return message;
	}

	/**
	 * Creates a white phosphorus effect message.
	 */
	public static MessageExplosion createWhitePhosphorusMessage(World world, Vec3d pos, Vec3d direction,
	                                                            ComponentEffectShape shape, float size)
	{
		MessageExplosion message = new MessageExplosion(EffectType.WHITE_PHOSPHORUS, world, pos);
		message.direction = direction;
		message.shape = shape;
		message.size = size;
		return message;
	}

	/**
	 * Creates a nuclear explosion effect message.
	 */
	public static MessageExplosion createNukeMessage(World world, Vec3d pos, float size)
	{
		MessageExplosion message = new MessageExplosion(EffectType.NUKE, world, pos);
		message.size = size;
		return message;
	}

	/**
	 * Creates an EMP explosion effect message.
	 */
	public static MessageExplosion createEMPMessage(World world, Vec3d pos, float radius, List<Vec3d> targets)
	{
		MessageExplosion message = new MessageExplosion(EffectType.EMP, world, pos);
		message.radius = radius;
		if(targets==null||targets.isEmpty())
			message.effectTargets = Collections.emptyList();
		else
			message.effectTargets = new ArrayList<>(targets.subList(0, Math.min(MAX_EMP_TARGETS, targets.size())));
		return message;
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		switch(effectType)
		{
			case WHITE_PHOSPHORUS:
				ParticleRegistry.spawnWhitePhosphorusFX(world, pos, direction, shape, size);
				break;
			case NUKE:
				ClientEventHandler.addScreenshakeSource(pos, MathHelper.clamp(size*2f, 1f, 4f), 20, 0);
				ParticleRegistry.spawnAtomicExplosionFX(world, pos, size);
				break;
			case EMP:
				ClientEventHandler.addScreenshakeSource(pos, MathHelper.clamp(radius/10f, 0.5f, 2f), 8, 0);
				ParticleRegistry.spawnEMPExplosionFX(world, pos, radius, effectTargets);
				break;
			case EXPLOSION:
			default:
				ClientEventHandler.addScreenshakeSource(pos, MathHelper.clamp(strength/4f, 0.25f, 3f), 4, 2);
				ParticleRegistry.spawnExplosionBoomFX(world, pos, direction, radius, strength, shape, particleBlocks);
				break;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.effectType = readEnum(buf, EffectType.class);
		this.pos = readVec3(buf);

		switch(effectType)
		{
			case WHITE_PHOSPHORUS:
				this.direction = readVec3(buf);
				this.shape = readEnum(buf, ComponentEffectShape.class);
				this.size = buf.readFloat();
				break;
			case NUKE:
				this.size = buf.readFloat();
				break;
			case EMP:
				this.radius = buf.readFloat();
				int effectTargetCount = Math.min(MAX_EMP_TARGETS,
						Math.min(buf.readUnsignedShort(), buf.readableBytes()/24));
				this.effectTargets = new ArrayList<>(effectTargetCount);
				for(int i = 0; i < effectTargetCount; i++)
					this.effectTargets.add(readVec3(buf));
				break;
			case EXPLOSION:
			default:
				this.flaming = buf.readBoolean();
				this.damagesTerrain = buf.readBoolean();
				this.radius = buf.readFloat();
				this.strength = buf.readFloat();
				this.direction = readVec3(buf);
				this.shape = readEnum(buf, ComponentEffectShape.class);

				int particleBlockCount = Math.min(buf.readUnsignedShort(), buf.readableBytes()/12);
				this.particleBlocks = new ArrayList<>(particleBlockCount);
				for(int i = 0; i < particleBlockCount; i++)
					this.particleBlocks.add(readPos(buf));
				break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeEnum(buf, effectType);
		writeVec3(buf, pos);

		switch(effectType)
		{
			case WHITE_PHOSPHORUS:
				writeVec3(buf, direction);
				writeEnum(buf, shape);
				buf.writeFloat(size);
				break;
			case NUKE:
				buf.writeFloat(size);
				break;
			case EMP:
				buf.writeFloat(radius);
				int effectTargetCount = Math.min(MAX_EMP_TARGETS, effectTargets.size());
				buf.writeShort(effectTargetCount);
				for(int i = 0; i < effectTargetCount; i++)
					writeVec3(buf, effectTargets.get(i));
				break;
			case EXPLOSION:
			default:
				buf.writeBoolean(flaming);
				buf.writeBoolean(damagesTerrain);
				buf.writeFloat(radius);
				buf.writeFloat(strength);
				writeVec3(buf, direction);
				writeEnum(buf, shape);

				int particleBlockCount = Math.min(0xFFFF, particleBlocks.size());
				buf.writeShort(particleBlockCount);
				for(int i = 0; i < particleBlockCount; i++)
					writePos(buf, particleBlocks.get(i));
				break;
		}
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

	@Override
	public int getPacketDistance()
	{
		switch(effectType)
		{
			case NUKE:
				return Math.max(Graphics.explosionMessageDistance, NUKE_MESSAGE_DISTANCE);
			case WHITE_PHOSPHORUS:
				return Math.max(Graphics.explosionMessageDistance, WHITE_PHOSPHORUS_MESSAGE_DISTANCE);
			case EMP:
				return Math.max(Graphics.explosionMessageDistance, EMP_MESSAGE_DISTANCE);
			case EXPLOSION:
			default:
				return Graphics.explosionMessageDistance;
		}
	}

	/**
	 * Selects the payload and client effect handled by this message.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 06.08.2026
	 */
	public enum EffectType
	{
		EXPLOSION,
		WHITE_PHOSPHORUS,
		NUKE,
		EMP
	}
}
