package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.util.IWorldPosProvider;

/**
 * Defines a node that can send, receive, or relay radio packets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 23.06.2019
 */
public interface IRadioDevice extends IWorldPosProvider
{
	int MAX_USABILITY_COOLDOWN = 200;

	void onRadioSend(DataPacket packet);

	boolean onRadioReceive(DataPacket packet);

	/**
	 * Receives a packet with its original sender.
	 */
	default boolean onRadioReceive(DataPacket packet, IRadioDevice sender)
	{
		return onRadioReceive(packet);
	}

	/**
	 * Returns true when this device forwards received packets.
	 */
	default boolean canRelayRadio()
	{
		return true;
	}

	int getFrequency();

	void setFrequency(int value);

	boolean isBasicRadio();

	float getRange();

	DimensionBlockPos getRadioDevicePosition();

	@Override
	default BlockPos getIIPos()
	{
		return getRadioDevicePosition();
	}

	/**
	 * Gets the world of this radio. Delegate radios can override this method.
	 */
	@Override
	default World getIIWorld()
	{
		if(this instanceof TileEntity)
			return ((TileEntity)this).getWorld();
		return this instanceof Entity?((Entity)this).getEntityWorld(): null;
	}

	/**
	 * Binds a radio to an entity. The equipment stack is empty for an entity capability.
	 */
	default IRadioDevice bindTo(Entity wearer, ItemStack equipment)
	{
		return this;
	}

	int getRadioCooldown();

	void setRadioCooldown(int ticks);

	/**
	 * Returns true when this node can participate in radio communication.
	 */
	default boolean isRadioAvailable()
	{
		return getRadioCooldown() <= 0;
	}

	/**
	 * Adds radio downtime without exceeding the common safety limit.
	 */
	default void addRadioCooldown(int ticks)
	{
		if(ticks > 0)
			setRadioCooldown(Math.min(MAX_USABILITY_COOLDOWN, Math.max(0, getRadioCooldown())+ticks));
	}

	/**
	 * Decreases the radio cooldown by one server tick.
	 */
	default void tickRadioCooldown()
	{
		if(getRadioCooldown() > 0)
			setRadioCooldown(getRadioCooldown()-1);
	}
}
