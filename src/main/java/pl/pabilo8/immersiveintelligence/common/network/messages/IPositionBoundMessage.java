package pl.pabilo8.immersiveintelligence.common.network.messages;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Pabilo8
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
}
