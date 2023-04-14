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
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * II's version of {@link MessageIITileSync}
 * @author Pabilo8
 * @author BluSunrize
 * @since 22.09.2022
 */
public class MessageEntityNBTSync extends IIMessage
{
	private int entityID;
	private NBTTagCompound tag;

	public MessageEntityNBTSync(Entity entity, NBTTagCompound tag)
	{
		this.entityID = entity.getEntityId();
		this.tag = tag;
	}

	public MessageEntityNBTSync()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		Entity entity = world.getEntityByID(entityID);
		if(entity instanceof EntityMachinegun)
			((EntityMachinegun)entity).readEntityFromNBT(tag);
		else if(entity instanceof EntityMotorbike)
			((EntityMotorbike)entity).syncKeyPress(tag);
		else if(entity instanceof EntityFieldHowitzer)
			((EntityFieldHowitzer)entity).syncKeyPress(tag);
		else if(entity instanceof EntityMortar)
			((EntityMortar)entity).syncKeyPress(tag);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		Entity entity = world.getEntityByID(entityID);
		if(entity instanceof EntityBullet)
			((EntityBullet)entity).readEntityFromNBT(tag);
		else if(entity instanceof EntityMachinegun)
			((EntityMachinegun)entity).readEntityFromNBT(tag);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.tag = readTagCompound(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.entityID);
		writeTagCompound(buf, this.tag);
	}
}