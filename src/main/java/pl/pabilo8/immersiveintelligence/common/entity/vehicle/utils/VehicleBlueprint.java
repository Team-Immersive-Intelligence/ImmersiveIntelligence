package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 04.10.2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VehicleBlueprint
{
	/**
	 * @return the resource location of this vehicle
	 */
	String id();

	/**
	 * @return the mass of this vehicle
	 */
	double mass();

	/**
	 * @return the type of this vehicle
	 */
	VehicleType type();

	/**
	 * @return the base durability of this vehicle's parts
	 */
	int baseDurability() default 100;

	/**
	 * @return the base armor of this vehicle's parts
	 */
	int baseArmor() default 4;

	//--- Wheels ---//

	/**
	 * @return the linear drag factor of this vehicle (0.0-1.0), lower means more drag (slower)
	 */
	double linearDamping() default 0.98;

	/**
	 * @return the angular drag factor of this vehicle (0.0-1.0), lower means more drag (slower rotation)
	 */
	double angularDamping() default 0.94;

	/**
	 * @return the yaw response factor of this vehicle (0.0-1.0), higher means more responsive (faster turning)
	 */
	double torqueFactor() default 0.025;

	double lateralFrictionDrive() default 0.05;

	double lateralFrictionIdler() default 0.25;

	double maxLateralForceFactor() default 1.5;

	double forceFactor() default 0.01;
}
