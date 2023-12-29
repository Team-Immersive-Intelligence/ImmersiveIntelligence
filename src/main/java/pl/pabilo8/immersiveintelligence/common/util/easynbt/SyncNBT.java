package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface SyncNBT
{
	/**
	 * @return The name of the NBT tag
	 */
	String name() default "";

	/**
	 * @return Delay in ticks between syncs in OnUpdate
	 */
	int time() default 20;

	/**
	 * @return The events that will trigger a sync
	 */
	SyncEvents[] events() default {};

	enum SyncEvents
	{
		GUI_OPENED,
		RECIPE_CHANGED
	}
}
