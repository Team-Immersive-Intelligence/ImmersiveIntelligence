package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

public class MessageExplosion extends IIMessage
{
	private boolean flaming, damagesTerrain;
	private float radius, strength;
	private Vec3d pos, direction;
	private ComponentEffectShape shape;

	public MessageExplosion(boolean flaming, boolean damagesTerrain, float radius, float strength, Vec3d pos, Vec3d direction, ComponentEffectShape shape)
	{
		this.flaming = flaming;
		this.damagesTerrain = damagesTerrain;
		this.radius = radius;
		this.strength = strength;
		this.pos = pos;
		this.direction = direction;
		this.shape = shape;
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
		ParticleRegistry.spawnExplosionBoomFX(world, pos, direction,
				new IIExplosion(world, null, pos, direction, radius, strength, shape, flaming, damagesTerrain, false));
		ClientEventHandler.addScreenshakeSource(pos, MathHelper.clamp(strength/4f, 0.25f, 3f), 4);
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
	}
}