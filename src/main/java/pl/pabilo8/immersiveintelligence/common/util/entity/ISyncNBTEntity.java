package pl.pabilo8.immersiveintelligence.common.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public interface ISyncNBTEntity<T extends Entity & ISyncNBTEntity<T>>
{
	@SuppressWarnings({"unchecked"})
	default void receiveNBTMessageServer(NBTTagCompound nbt)
	{
		T tis = ((T)this);
		NBTSerialisation.synchroniseFor(tis, (tag, tile) -> tag.deserializeAll(tis, nbt, true));
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked"})
	default void receiveNBTMessageClient(NBTTagCompound nbt)
	{
		T tis = ((T)this);
		NBTSerialisation.synchroniseFor(tis, (tag, tile) -> tag.deserializeAll(tis, nbt, true));
	}

	@SuppressWarnings({"unchecked"})
	default void updateEntityForTime()
	{
		@SuppressWarnings({"unchecked"})
		T tis = ((T)this);
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(tis, (tag, tile) -> tag.serializeForTime(tile, nbt, tis.ticksExisted));
		IIPacketHandler.sendToClient(new MessageEntityNBTSync(tis, nbt));
	}

	@SuppressWarnings({"unchecked"})
	default void updateEntityForEvent(SyncNBT.SyncEvents event)
	{
		T tis = ((T)this);
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(tis, (tag, tile) -> tag.serializeForEvent(tile, nbt, event));
		IIPacketHandler.sendToClient(new MessageEntityNBTSync(tis, nbt));
	}
}
