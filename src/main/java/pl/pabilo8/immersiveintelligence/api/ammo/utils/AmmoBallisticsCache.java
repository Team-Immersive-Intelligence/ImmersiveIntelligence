package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class storing computed ammo stats
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 04.06.2024
 */
public class AmmoBallisticsCache
{
	private static final Map<Integer, CachedBallisticStats> cache = new HashMap<>();
	private static final double BALLISTIC_PRECISION = 0.25;

	/**
	 * Retrieves the cached stats for the given ammo type, core, propellant and core type
	 *
	 * @param type  the ammo type
	 * @param stack the ammo stack
	 * @return cached stats
	 */
	public static CachedBallisticStats get(IAmmoType<?, ?> type, ItemStack stack)
	{
		Integer key = type.hashCode()*31+Double.hashCode(type.getMass(stack));
		return cache.computeIfAbsent(key, k -> new CachedBallisticStats(type, stack));
	}

	/**
	 * Caches the ballistic stats for the given ammo stack
	 *
	 * @author Pabilo8
	 * @ii-approved 0.3.1
	 * @since 04.06.2024
	 */
	public static class CachedBallisticStats
	{
		//--- Properties ---//
		private final double velocity, maxDirectRange, getMaxArtilleryRange, maxHeightReached;
		private final double[] distances = new double[(int)Math.ceil(90/BALLISTIC_PRECISION)];
		//TODO: 04.06.2024 direct fire angle distance corrections

		public CachedBallisticStats(IAmmoType<?, ?> type, ItemStack stack)
		{
			//get velocity
			this.velocity = type.getVelocity();

			//prepare calculation variables
			double gravity = EntityAmmoProjectile.GRAVITY*type.getMass(stack);
			double drag = 1d-EntityAmmoProjectile.DRAG;
			double maxHeightReached = 0;

			//simulate shell trajectory
			for(int i = 0; i < distances.length; i++)
			{
				double angle = Math.PI*0.5*(1d-i/(double)distances.length);
				double motionX = MathHelper.cos((float)angle)*this.velocity;// calculate the x component of the vector
				double motionY = MathHelper.sin((float)angle)*this.velocity;// calculate the y component of the vector
				double posX = 0, posY = 0;
				boolean motionPositive = true;

				//simulate movement until the shell reaches the ground
				while(posY > 0||motionY > 0)
				{
					motionX *= drag;
					motionY *= drag;
					motionY -= gravity;
					posX += motionX;
					posY += motionY;

					//get max height (fired at 0 degrees)
					if(i==0&&motionPositive&&motionY <= 0)
					{
						motionPositive = false;
						maxHeightReached = posY;
					}
				}
				distances[i] = posX;
			}

			//get max height (fired at 0 degrees)
			this.maxHeightReached = maxHeightReached;

			//get max direct range (fired at 90 degrees)
			maxDirectRange = distances[distances.length-1];
			//get max artillery distance (fired at 45 degrees)
			getMaxArtilleryRange = distances.length%2==0?distances[distances.length/2]: (distances[distances.length/2]+distances[distances.length/2+1])/2;

		}

		//--- Pre-Calculated Properties ---//

		/**
		 * @return the velocity of the projectile
		 */
		public double getVelocity()
		{
			return velocity;
		}

		/**
		 * @return the max distance a projectile can travel until descending one block
		 */
		public double getMaxDirectRange()
		{
			return maxDirectRange;
		}

		/**
		 * @return the max distance a projectile can travel in an arc
		 */
		public double getGetMaxArtilleryRange()
		{
			return getMaxArtilleryRange;
		}

		/**
		 * @return the max height reached by the projectile when fired vertically
		 */
		public double getMaxHeightReached()
		{
			return maxHeightReached;
		}

		/**
		 * @return the approximated distance the projectile can travel at a given angle
		 */
		public double getDistanceAtAngle(double angle)
		{
			return distances[(int)Math.floor(angle/BALLISTIC_PRECISION)];
		}
	}
}
