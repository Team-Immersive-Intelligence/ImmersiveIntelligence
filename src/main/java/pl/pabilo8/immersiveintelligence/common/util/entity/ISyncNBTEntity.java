package pl.pabilo8.immersiveintelligence.common.util.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.12.2022
 */
public interface ISyncNBTEntity<T extends Entity & ISyncNBTEntity<T>> extends IEntityAdditionalSpawnData
{
	@SuppressWarnings({"unchecked"})
	default void receiveNBTMessageServer(NBTTagCompound nbt)
	{
		T tis = ((T)this);
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.deserializeAll(tis, nbt, true));
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked"})
	default void receiveNBTMessageClient(NBTTagCompound nbt)
	{
		T tis = ((T)this);
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.deserializeAll(tis, nbt, true));
	}

	default void readEntityFromNBT(NBTTagCompound compound)
	{
		T tis = ((T)this);
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.deserializeAll(tis, compound, false));
	}

	default void writeEntityToNBT(NBTTagCompound compound)
	{
		T tis = ((T)this);
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.serializeAll(tis, compound));
	}

	@SuppressWarnings({"unchecked"})
	default void updateEntityForTime()
	{
		@SuppressWarnings({"unchecked"})
		T tis = ((T)this);
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.serializeForTime(entity, nbt, tis.ticksExisted));
		IIPacketHandler.sendToClient(new MessageEntityNBTSync(tis, nbt));
	}

	@SuppressWarnings({"unchecked"})
	default void updateEntityForEvent(SyncNBT.SyncEvents event)
	{
		T tis = ((T)this);
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.serializeForEvent(entity, nbt, event));
		IIPacketHandler.sendToClient(new MessageEntityNBTSync(tis, nbt));
	}

	default void sendServerUpdateForEvent(SyncNBT.SyncEvents event)
	{
		T tis = ((T)this);
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(tis, (tag, entity) -> tag.serializeForEvent(entity, nbt, event));
		IIPacketHandler.sendToServer(new MessageEntityNBTSync(tis, nbt));
	}

	default boolean reloadEntity()
	{
		T tis = ((T)this);
		if(!tis.world.isRemote)
			sendServerReInitUpdate();
		return true;
	}

	default void sendServerReInitUpdate()
	{
		T tis = ((T)this);
		IIPacketHandler.sendToClient(new MessageEntityNBTSync(tis, EasyNBT.newNBT()
				.withBoolean("re_init", true)
		));
	}

	default void sendServerPositionMotionUpdate()
	{
		T tis = ((T)this);
		IIPacketHandler.sendToClient(new MessageEntityNBTSync(tis, EasyNBT.newNBT()
				.withVec3d("pos", tis.posX, tis.posY, tis.posZ)
				.withVec3d("motion", tis.motionX, tis.motionY, tis.motionZ)
				.withFloat("rotationYaw", tis.rotationYaw)
				.withFloat("rotationPitch", tis.rotationPitch)
		));
	}

	default void receivePositionMotionUpdate(NBTTagCompound nbt)
	{
		T tis = ((T)this);
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);
		//Pos
		Vec3d pos = enbt.getVec3d("pos");
		tis.setPosition(pos.x, pos.y, pos.z);
		//Motion
		Vec3d motion = enbt.getVec3d("motion");
		tis.motionX = motion.x;
		tis.motionY = motion.y;
		tis.motionZ = motion.z;
		//Rotation
		tis.rotationYaw = tis.prevRotationYaw = enbt.getFloat("rotationYaw");
		tis.rotationPitch = tis.prevRotationPitch = enbt.getFloat("rotationPitch");
	}

	@Override
	default void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	default void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound tag = ByteBufUtils.readTag(additionalData);
		if(tag!=null)
		{
			readEntityFromNBT(tag);
			doPostWorldLoadSetup(tag);
		}
	}

	default void doPostWorldLoadSetup(NBTTagCompound tag)
	{

	}
}
