package pl.pabilo8.immersiveintelligence.common.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public interface IIIEntity<T extends Entity & IIIEntity<T>>
{
	default void entityInit()
	{
		EntityDataManager dataManager = ((T)this).getDataManager();

	}
}
