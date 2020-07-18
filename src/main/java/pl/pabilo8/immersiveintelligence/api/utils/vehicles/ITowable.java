package pl.pabilo8.immersiveintelligence.api.utils.vehicles;

import net.minecraft.entity.Entity;

/**
 * @author Pabilo8
 * @since 18.07.2020
 */
public interface ITowable
{
	Entity getTowingEntity();

	boolean startTowing(Entity tower);

	boolean stopTowing();

	boolean canMoveTowed();
}
