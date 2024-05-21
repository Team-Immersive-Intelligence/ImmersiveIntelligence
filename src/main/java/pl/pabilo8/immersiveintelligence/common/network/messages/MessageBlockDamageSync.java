package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.common.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 2020-01-11
 */
public class MessageBlockDamageSync extends IIMessage
{
	private float damage;
	private DimensionBlockPos dPos;
	//used for particle effects
	private Vec3d direction;

	public MessageBlockDamageSync(DamageBlockPos pos)
	{
		this.damage = pos.damage;
		this.dPos = pos;
	}

	public MessageBlockDamageSync(DamageBlockPos pos, Vec3d direction)
	{
		this.damage = pos.damage;
		this.dPos = pos;
		this.direction = direction;
	}

	public MessageBlockDamageSync()
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
		DamageBlockPos dmgPos = new DamageBlockPos(dPos, damage);

		if(damage > 0)
		{
			if(direction!=null)
			{
				//Centre of the block - apporach distance = hit point
				//TODO: 08.04.2024 readd damage fx

				/*Vec3d cPos = new Vec3d(dPos)
						.addVector(0.5, 0.5, 0.5)
						.add(direction.scale(-0.6));
				direction = direction.scale(-1);

				ParticleShockwave particle = new ParticleShockwave(world, cPos, Vec3d.ZERO, 2.5f);
				particle.setMaxAge(5);
				ParticleRegistry.IIParticleSystem.addEffect(particle);

				for(int p = 0; p < 4; p++)
					ParticleRegistry.spawnBlockFragmentFX(cPos,
							new Vec3d(direction.x, 0, direction.z)
									.rotateYaw(Utils.RAND.nextFloat()%1.57f-0.785f)
									.rotatePitch(Utils.RAND.nextFloat()%1.57f-0.785f)
									.addVector(0, direction.y, 0)
									.scale(0.28),
							0.5f, world.getBlockState(dPos));*/
			}

			PenetrationCache.blockDamageClient
					.stream()
					.filter(d -> d.equals(dmgPos))
					.findFirst()
					.orElseGet(
							() -> {
								PenetrationCache.blockDamageClient.add(dmgPos);
								return dmgPos;
							}
					).damage = dmgPos.damage;
		}
		else
			PenetrationCache.blockDamageClient.removeIf(damageBlockPos -> damageBlockPos.equals(dmgPos));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(this.damage);
		writeDimPos(buf, dPos);
		if(direction!=null)
		{
			buf.writeBoolean(true);
			writeVec3(buf, direction);
		}
		else
			buf.writeBoolean(false);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.damage = buf.readFloat();
		this.dPos = readDimPos(buf);
		if(buf.readBoolean())
			this.direction = readVec3(buf);

	}

}