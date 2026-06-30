package pl.pabilo8.immersiveintelligence.common.util.tile;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.06.2026
 */
@SuppressWarnings({"unchecked", "unused"})
public class TileEntityIIBase extends TileEntityIEBase
{
	//--- NBT ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.deserializeAll(this, nbt, false));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.serializeAll(this, nbt));
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.deserializeAll(this, message, true));
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.deserializeAll(this, message, true));
	}


	//--- Additional SyncNBT methods ---//

	public void updateEntityForTime(int time)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.serializeForTime(entity, nbt, time));
		IIPacketHandler.sendToClient(new MessageIITileSync(this, nbt));
	}

	@SuppressWarnings({"unchecked"})
	public void updateEntityForEvent(SyncNBT.SyncEvents event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.serializeForEvent(entity, nbt, event));
		IIPacketHandler.sendToClient(new MessageIITileSync(this, nbt));
	}

	public void sendServerUpdateForEvent(SyncNBT.SyncEvents event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.serializeForEvent(entity, nbt, event));
		IIPacketHandler.sendToServer(new MessageIITileSync(this, nbt));
	}

	//--- Built-In ---//

	@Override
	public void onEntityCollision(@Nonnull World world, @Nonnull Entity entity)
	{
		super.onEntityCollision(world, entity);
	}
}
