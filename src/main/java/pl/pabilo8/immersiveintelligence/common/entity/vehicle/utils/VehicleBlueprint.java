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
	 * Main developer-facing drive scalar. Higher values make the vehicle accelerate harder.
	 * Transmission and engine settings still describe the drivetrain, but this is the primary
	 * gameplay balance lever for wheel force.
	 */
	double driveForce() default 1.0;

	/**
	 * Expected wheel speed at full useful drive intent. Used to normalise engine/transmission output.
	 */
	double nominalWheelSpeed() default 360.0;

	/**
	 * Expected wheel torque at full useful drive intent. Used to normalise engine/transmission output.
	 */
	double nominalWheelTorque() default 80.0;

	/**
	 * Maximum block obstacle height that the wheel system may try to step/climb.
	 */
	double obstacleClimbHeight() default 0.75;

	/**
	 * How aggressively wheel contact converts obstacle height into upward force.
	 */
	double climbForceScale() default 0.0625;

	/**
	 * Per-wheel downward force when the wheel has no ground contact.
	 */
	double wheelGravityForce() default 0.04;

	/**
	 * Yaw torque scalar produced by horizontal wheel forces. Replaces most direct tuning of torqueFactor.
	 */
	double steeringTorque() default 0.025;

	/**
	 * Pitch/roll torque scalar produced by uneven wheel vertical forces.
	 */
	double orientationTorqueFactor() default 0.015;

	/**
	 * Lateral friction used by axle groups.
	 */
	double axleLateralFriction() default 0.7;

	/**
	 * Lateral friction used by track groups. Higher values make tracks resist side-slip.
	 */
	double trackLateralFriction() default 1.1;

	/**
	 * Maximum hull rotation step in degrees per tick, checked through OBB narrow-phase.
	 */
	double rotationStepLimit() default 2.0;

	/**
	 * Converts accumulated angular velocity into degrees-per-tick rotation before OBB validation.
	 */
	double angularVelocityToDegrees() default 1.0;

	/**
	 * @return the lateral friction coefficient for driven wheels (0.0-1.0)
	 */
	double lateralFrictionDrive() default 0.05;

	/**
	 * @return the lateral friction coefficient for idler wheels (0.0-1.0)
	 */
	double lateralFrictionIdler() default 0.7;

	/**
	 * @return the multiplier for lateral friction of  steering wheels
	 * @apiNote multiplies lateralFrictionIdler
	 */
	double steeringFrictionMultiplier() default 4;

	/**
	 * @return the force multiplication factor for engine power
	 */
	double forceFactor() default 0.015;

	/**
	 * @return the torque multiplication factor for turning
	 */
	double torqueFactor() default 0.025;

	/**
	 * @return the energy loss factor when the suspension bounces
	 */
	double suspensionDamping() default 0.7;

	/**
	 * Horizontal push strength applied to ordinary entities hit by a vehicle.
	 * Vehicles and vehicle parts are still handled by the rigid OBB collision path.
	 */
	double entityPushStrength() default 1.25;

	/**
	 * Horizontal vehicle speed squared above which ordinary entities take impact damage.
	 */
	double entityDamageSpeedSq() default 0.08;

	/**
	 * Damage scalar for high-speed vehicle impacts against ordinary entities.
	 */
	double entityDamageScale() default 18.0;

	//--- Physics ---//

	/**
	 * @return the linear drag factor of this vehicle (0.0-1.0)
	 */
	double linearDamping() default 0.98;

	/**
	 * @return the angular drag factor of this vehicle (0.0-1.0)
	 */
	double angularDamping() default 0.96;

	/**
	 * @return the air drag coefficient (typical 0.6-1.0 for vehicles)
	 */
	double airDragCoefficient() default 0.7;

	/**
	 * @return the rolling resistance coefficient (typical 0.01-0.03 for rubber on road)
	 */
	double rollingResistance() default 0.015;

	/**
	 * Maximum pitch angle in degrees for vehicle orientation
	 *
	 * @return maximum pitch angle
	 */
	double maxPitchAngle() default 30.0;

	/**
	 * Maximum roll angle in degrees for vehicle orientation
	 *
	 * @return maximum roll angle
	 */
	double maxRollAngle() default 25.0;

	/**
	 * Frontal area factor for air drag calculation
	 *
	 * @return frontal area factor (0.0-1.0)
	 */
	double frontalAreaFactor() default 0.8;
}
