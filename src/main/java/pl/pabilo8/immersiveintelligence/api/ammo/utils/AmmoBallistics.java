package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

/**
 * Immutable description of a projectile flight model used by {@link AmmoBallisticsCache}.
 * The model owns one tick of motion, which allows ammo entities, missiles and non-ammo
 * projectiles to share the same trajectory cache without sharing their physics implementation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2026
 */
@Value
public class AmmoBallistics
{
	Object cacheIdentity;
	double velocity;
	int maxFlightTime;
	BallisticFlightModel flightModel;

	/**
	 * Creates the standard II ammo projectile model.
	 */
	public static AmmoBallistics forProjectile(IAmmoType<?, ?> type, ItemStack stack, double velocityModifier)
	{
		double mass = type.getMass(stack);
		double velocity = type.getVelocity()*velocityModifier;
		return forProjectile(type, mass, velocity);
	}

	/**
	 * Creates the standard model from explicit ammo parameters.
	 */
	public static AmmoBallistics forProjectile(IAmmoType<?, ?> type, double mass, double velocity)
	{
		velocity *= EntityAmmoProjectile.SLOWMO;
		double gravity = EntityAmmoProjectile.GRAVITY*mass*EntityAmmoProjectile.SLOWMO;
		return projectile(new AmmoModelIdentity(type, mass), velocity, gravity,
				1D-EntityAmmoProjectile.DRAG, 0, EntityAmmoProjectile.MAX_TICKS);
	}

	/**
	 * Creates the standard II missile model. Its booster suppresses drag and gravity.
	 */
	public static AmmoBallistics forMissile(IAmmoType<?, ?> type, ItemStack stack,
											double velocityModifier, int boosterTime)
	{
		double mass = type.getMass(stack);
		double velocity = type.getVelocity()*velocityModifier;
		return forMissile(type, mass, velocity, boosterTime);
	}

	/**
	 * Creates the standard missile model from explicit ammo parameters.
	 */
	public static AmmoBallistics forMissile(IAmmoType<?, ?> type, double mass, double velocity, int boosterTime)
	{
		velocity *= EntityAmmoProjectile.SLOWMO;
		double gravity = EntityAmmoProjectile.GRAVITY*mass*EntityAmmoProjectile.SLOWMO;
		return projectile(new AmmoModelIdentity(type, mass), velocity, gravity,
				1D-EntityAmmoProjectile.DRAG, boosterTime, EntityAmmoProjectile.MAX_TICKS);
	}

	/**
	 * Creates a projectile model which applies gravity and drag before moving each tick.
	 */
	public static AmmoBallistics projectile(Object cacheIdentity, double velocity, double gravity,
											double drag, int maxFlightTime)
	{
		return projectile(cacheIdentity, velocity, gravity, drag, 0, maxFlightTime);
	}

	/**
	 * Creates a projectile model with a period during which its booster suppresses physics.
	 */
	public static AmmoBallistics projectile(Object cacheIdentity, double velocity, double gravity,
											double drag, int boosterTime, int maxFlightTime)
	{
		validateGravityAndDrag(gravity, drag);
		ProjectileModelIdentity identity = new ProjectileModelIdentity(
				cacheIdentity, gravity, drag, Math.max(0, boosterTime)
		);
		return custom(identity, velocity, maxFlightTime, state -> {
			if(state.getTick() >= identity.getBoosterTime())
			{
				state.multiplyMotion(identity.getDrag());
				state.addMotion(0, -identity.getGravity()*identity.getDrag());
			}
			state.move();
		});
	}

	/**
	 * Creates a model which moves first and applies drag and gravity afterwards.
	 * This matches particles such as the II chemthrower shot.
	 */
	public static AmmoBallistics dragAfterMove(Object cacheIdentity, double velocity, double gravity,
											   double drag, int maxFlightTime)
	{
		validateGravityAndDrag(gravity, drag);
		PostMoveModelIdentity identity = new PostMoveModelIdentity(cacheIdentity, gravity, drag);
		return custom(identity, velocity, maxFlightTime, state -> {
			state.move();
			state.multiplyMotion(identity.getDrag());
			state.addMotion(0, -identity.getGravity());
		});
	}

	/**
	 * Creates a custom flight model. The identity must be immutable and its equality must cover
	 * every value which changes the trajectory. The callback should advance position and motion
	 * by exactly one tick.
	 */
	public static AmmoBallistics custom(Object cacheIdentity, double velocity, int maxFlightTime,
										BallisticFlightModel flightModel)
	{
		if(cacheIdentity==null||flightModel==null||!Double.isFinite(velocity)
				||velocity <= 0||maxFlightTime <= 0)
			throw new IllegalArgumentException("Invalid ballistic flight model");
		return new AmmoBallistics(cacheIdentity, velocity, maxFlightTime, flightModel);
	}

	private static void validateGravityAndDrag(double gravity, double drag)
	{
		if(!Double.isFinite(gravity)||!Double.isFinite(drag)||drag <= 0||drag > 1)
			throw new IllegalArgumentException("Invalid gravity or drag");
	}

	FlightState createState(double elevation)
	{
		double radians = Math.toRadians(elevation);
		return new FlightState(0, 0, Math.cos(radians)*velocity, Math.sin(radians)*velocity, 0);
	}

	void update(FlightState state)
	{
		flightModel.update(state);
		state.tick++;
	}

	/**
	 * Mutable state passed to a custom flight model. Its mutation methods intentionally mirror
	 * the small set of operations used by projectile entities.
	 */
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class FlightState
	{
		private double positionX;
		private double positionY;
		private double motionX;
		private double motionY;
		private int tick;

		public void move()
		{
			positionX += motionX;
			positionY += motionY;
		}

		public void multiplyMotion(double multiplier)
		{
			motionX *= multiplier;
			motionY *= multiplier;
		}

		public void addMotion(double x, double y)
		{
			motionX += x;
			motionY += y;
		}

		public void setMotion(double x, double y)
		{
			motionX = x;
			motionY = y;
		}

		public void setPosition(double x, double y)
		{
			positionX = x;
			positionY = y;
		}
	}

	@FunctionalInterface
	public interface BallisticFlightModel
	{
		void update(FlightState state);
	}

	@Value
	private static class AmmoModelIdentity
	{
		IAmmoType<?, ?> type;
		double mass;
	}

	@Value
	private static class ProjectileModelIdentity
	{
		Object identity;
		double gravity;
		double drag;
		int boosterTime;
	}

	@Value
	private static class PostMoveModelIdentity
	{
		Object identity;
		double gravity;
		double drag;
	}
}
