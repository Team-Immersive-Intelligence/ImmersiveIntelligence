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

	//--- Wheels and Physics ---//

	/**
	 * @return the linear drag factor of this vehicle (0.0-1.0)
	 */
	double linearDamping() default 0.98;

	/**
	 * @return the angular drag factor of this vehicle (0.0-1.0)
	 */
	double angularDamping() default 0.96;

	/**
	 * @return the lateral friction coefficient for driven wheels (0.0-1.0)
	 */
	double lateralFrictionDrive() default 0.05;

	/**
	 * @return the lateral friction coefficient for idler wheels (0.0-1.0)
	 */
	double lateralFrictionIdler() default 0.25;

	/**
	 * @return the force multiplication factor for engine power
	 */
	double forceFactor() default 0.015;

	/**
	 * @return the torque multiplication factor for turning
	 */
	double torqueFactor() default 0.025;

	/**
	 * @return the air drag coefficient (typical 0.6-1.0 for vehicles)
	 */
	double airDragCoefficient() default 0.7;

	/**
	 * @return the rolling resistance coefficient (typical 0.01-0.03 for rubber on road)
	 */
	double rollingResistance() default 0.015;

	/**
	 * @return the static friction coefficient when stationary
	 */
	double staticFriction() default 0.9;

	/**
	 * @return the kinetic friction coefficient when moving
	 */
	double kineticFriction() default 0.7;

	/**
	 * @return the frontal area factor for air drag calculation (0.0-1.0)
	 */
	double frontalAreaFactor() default 0.85;
}
