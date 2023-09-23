package pl.pabilo8.immersiveintelligence.common.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

	default <V> V getVariable(DataParameter<V> key)
	{
		return ((T)this).getDataManager().get(key);
	}

	default <V> void setVariable(DataParameter<V> key, V value)
	{
		((T)this).getDataManager().set(key, value);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@interface AutoSerialized
	{

	}
}
