package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

/**
 * II's version of {@link MessageIITileSync}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author BluSunrize
 * @since 22.09.2022
 */
public class MessageEntityNBTSync extends IIMessage implements IEntityBoundMessage
{
	private Entity entity;
	private int entityID;
	private NBTTagCompound nbt;

	public MessageEntityNBTSync(Entity entity, NBTTagCompound nbt)
	{
		this.entity = entity;
		this.entityID = entity.getEntityId();
		this.nbt = nbt;
	}

	public MessageEntityNBTSync(Entity entity, EasyNBT nbt)
	{
		this(entity, nbt.unwrap());
	}

	public MessageEntityNBTSync()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		Entity entity = world.getEntityByID(entityID);

		if(entity instanceof ISyncNBTEntity)
			((ISyncNBTEntity<?>)entity).receiveNBTMessageServer(nbt);
			//TODO: 09.07.2024 get rid of ones below
		else if(entity instanceof EntityMachinegun)
			((EntityMachinegun)entity).readEntityFromNBT(nbt);
		else if(entity instanceof EntityMortar)
			((EntityMortar)entity).syncKeyPress(nbt);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		Entity entity = world.getEntityByID(entityID);
		if(!(entity instanceof ISyncNBTEntity))
			return;
		ISyncNBTEntity<?> synced = (ISyncNBTEntity<?>)entity;

		if(nbt.hasKey("pos"))
			synced.receivePositionMotionUpdate(nbt);
		else
			synced.receiveNBTMessageClient(nbt);
		if(nbt.hasKey("re_init"))
			synced.reloadEntity();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.nbt = readTagCompound(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entity.getEntityId());
		writeTagCompound(buf, this.nbt);
	}

	@Override
	public Entity getEntity()
	{
		return entity;
	}
}
