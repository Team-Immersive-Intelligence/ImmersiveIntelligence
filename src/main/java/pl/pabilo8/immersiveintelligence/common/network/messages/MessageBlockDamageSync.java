package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * @author Pabilo8
 * @since 2020-01-11
 */
public class MessageBlockDamageSync extends IIMessage
{
	private float damage;
	private DimensionBlockPos dPos;

	public MessageBlockDamageSync(DamageBlockPos pos)
	{
		this.damage = pos.damage;
		this.dPos = pos;
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
			PenetrationRegistry.blockDamageClient
					.stream()
					.filter(d -> d.equals(dmgPos))
					.findFirst()
					.orElseGet(
							() -> {
								PenetrationRegistry.blockDamageClient.add(dmgPos);
								return dmgPos;
							}
					).damage = dmgPos.damage;
		}
		else
			PenetrationRegistry.blockDamageClient.removeIf(damageBlockPos -> damageBlockPos.equals(dmgPos));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(this.damage);
		writeDimPos(buf, dPos);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.damage = buf.readFloat();
		this.dPos = readDimPos(buf);

	}

}