package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

public class MessageExplosion extends IIMessage
{
	private boolean flaming, damagesTerrain;
	private float radius, strength;
	private Vec3d pos;

	public MessageExplosion(boolean flaming, boolean damagesTerrain, float radius, float strength, Vec3d pos)
	{
		this.flaming = flaming;
		this.damagesTerrain = damagesTerrain;
		this.radius = radius;
		this.strength = strength;
		this.pos = pos;
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
		ParticleUtils.spawnExplosionBoomFX(world, pos, radius, strength, flaming, damagesTerrain);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.flaming = buf.readBoolean();
		this.damagesTerrain = buf.readBoolean();

		this.radius = buf.readFloat();
		this.strength = buf.readFloat();

		this.pos = readVec3(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(flaming);
		buf.writeBoolean(damagesTerrain);

		buf.writeFloat(radius);
		buf.writeFloat(strength);

		writeVec3(buf, pos);
	}
}