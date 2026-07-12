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

public class MessageExplosion extends IIMessage implements IPositionBoundMessage
{
	private World world;
	private boolean flaming, damagesTerrain;
	private float radius, strength;
	private Vec3d pos, direction;
	private ComponentEffectShape shape;
	private List<BlockPos> particleBlocks = Collections.emptyList();

	public MessageExplosion(World world, boolean flaming, boolean damagesTerrain, float radius, float strength, Vec3d pos, Vec3d direction, ComponentEffectShape shape)
	{
		this(world, flaming, damagesTerrain, radius, strength, pos, direction, shape, Collections.emptyList());
	}

	public MessageExplosion(World world, boolean flaming, boolean damagesTerrain, float radius, float strength,
	                        Vec3d pos, Vec3d direction, ComponentEffectShape shape, List<BlockPos> particleBlocks)
	{
		this.world = world;
		this.flaming = flaming;
		this.damagesTerrain = damagesTerrain;
		this.radius = radius;
		this.strength = strength;
		this.pos = pos;
		this.direction = direction;
		this.shape = shape;
		this.particleBlocks = particleBlocks==null?Collections.emptyList(): particleBlocks;
	}

	public MessageExplosion()
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
		ClientEventHandler.addScreenshakeSource(pos, MathHelper.clamp(strength/4f, 0.25f, 3f), 4, 2);
		ParticleRegistry.spawnExplosionBoomFX(world, pos, direction, radius, strength, shape, particleBlocks);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.flaming = buf.readBoolean();
		this.damagesTerrain = buf.readBoolean();

		this.radius = buf.readFloat();
		this.strength = buf.readFloat();

		this.pos = readVec3(buf);
		this.direction = readVec3(buf);

		this.shape = readEnum(buf, ComponentEffectShape.class);

		if(buf.readableBytes() >= 2)
		{
			int particleBlockCount = buf.readUnsignedShort();
			this.particleBlocks = new ArrayList<>(particleBlockCount);
			for(int i = 0; i < particleBlockCount&&buf.readableBytes() >= 8; i++)
				this.particleBlocks.add(BlockPos.fromLong(buf.readLong()));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(flaming);
		buf.writeBoolean(damagesTerrain);

		buf.writeFloat(radius);
		buf.writeFloat(strength);

		writeVec3(buf, pos);
		writeVec3(buf, direction);

		writeEnum(buf, shape);

		int particleBlockCount = Math.min(0xFFFF, particleBlocks.size());
		buf.writeShort(particleBlockCount);
		for(int i = 0; i < particleBlockCount; i++)
			buf.writeLong(particleBlocks.get(i).toLong());
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
		return Graphics.explosionMessageDistance;
	}
}
