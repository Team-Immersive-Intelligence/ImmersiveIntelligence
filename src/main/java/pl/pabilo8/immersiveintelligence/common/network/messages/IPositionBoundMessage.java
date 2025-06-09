package pl.pabilo8.immersiveintelligence.common.network.messages;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.04.2024
 */
public interface IPositionBoundMessage
{
	/**
	 * @return world where this message should be received
	 */
	World getWorld();

	/**
	 * @return position tracked by this message's recipients
	 */
	Vec3d getPosition();

	default int getPacketDistance()
	{
		return IIPacketHandler.DEFAULT_RANGE;
	}
}
