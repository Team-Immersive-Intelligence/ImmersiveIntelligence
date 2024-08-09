package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import java.lang.annotation.*;

/**
 * Annotation for fields that should be synced to NBT
 *
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 30.12.2023
 */
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
		TILE_GUI_OPENED,
		TILE_GUI_CLOSED,
		TILE_RECIPE_CHANGED,
		TILE_DAMAGED,
		TILE_CUSTOM1,
		TILE_CUSTOM2,

		ENTITY_VEHICLE_CONTROLS,
		ENTITY_PASSENGER,
		ENTITY_INTERACT,
		ENTITY_COLLISION,
		ENTITY_DAMAGED,
		ENTITY_DEATH,
		ENTITY_CUSTOM1,
		ENTITY_CUSTOM2
	}
}
