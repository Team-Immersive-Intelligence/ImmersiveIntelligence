package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * Resends the complete client-relevant state of a Tactile entity after its model changes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.09.2026
 */
public class MessageTactileSync extends IIMessage implements IEntityBoundMessage
{
	private EntityAMTTactile tactile;
	private int entityID;
	private String name, customName;
	private AxisAlignedBB aabb;
	private double posX, posY, posZ;
	private float rotationYaw, rotationPitch;
	private boolean visibility;

	public MessageTactileSync(EntityAMTTactile tactile)
	{
		this.tactile = tactile;
		this.entityID = tactile.getEntityId();
		this.name = tactile.name;
		this.customName = tactile.customName;
		this.aabb = tactile.aabb;
		this.posX = tactile.posX;
		this.posY = tactile.posY;
		this.posZ = tactile.posZ;
		this.rotationYaw = tactile.rotationYaw;
		this.rotationPitch = tactile.rotationPitch;
		this.visibility = tactile.visibility;
	}

	public MessageTactileSync()
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
		Entity entity = world.getEntityByID(entityID);
		if(entity instanceof EntityAMTTactile)
			((EntityAMTTactile)entity).applySynchronizedState(name, customName, aabb,
					posX, posY, posZ, rotationYaw, rotationPitch, visibility);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.name = ByteBufUtils.readUTF8String(buf);
		this.customName = ByteBufUtils.readUTF8String(buf);
		this.aabb = new AxisAlignedBB(
				buf.readDouble(), buf.readDouble(), buf.readDouble(),
				buf.readDouble(), buf.readDouble(), buf.readDouble()
		);
		this.posX = buf.readDouble();
		this.posY = buf.readDouble();
		this.posZ = buf.readDouble();
		this.rotationYaw = buf.readFloat();
		this.rotationPitch = buf.readFloat();
		this.visibility = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, name==null?"": name);
		ByteBufUtils.writeUTF8String(buf, customName==null?"": customName);
		buf.writeDouble(aabb.minX);
		buf.writeDouble(aabb.minY);
		buf.writeDouble(aabb.minZ);
		buf.writeDouble(aabb.maxX);
		buf.writeDouble(aabb.maxY);
		buf.writeDouble(aabb.maxZ);
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeFloat(rotationYaw);
		buf.writeFloat(rotationPitch);
		buf.writeBoolean(visibility);
	}

	@Override
	public Entity getEntity()
	{
		return tactile;
	}
}
