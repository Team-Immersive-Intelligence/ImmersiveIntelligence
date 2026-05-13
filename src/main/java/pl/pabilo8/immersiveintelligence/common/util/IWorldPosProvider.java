package pl.pabilo8.immersiveintelligence.common.util;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;

/**
 * Extended by interfaces that require world and position access, such as {@link IOwnableProperty}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.03.2026
 */
public interface IWorldPosProvider
{
	/**
	 * @return the position of the tile entity
	 */
	BlockPos getIIPos();

	/**
	 * @return the world of the tile entity
	 */
	World getIIWorld();
}
