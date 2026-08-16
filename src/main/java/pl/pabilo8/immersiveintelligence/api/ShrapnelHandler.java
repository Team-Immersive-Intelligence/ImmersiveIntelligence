package pl.pabilo8.immersiveintelligence.api;

import net.minecraft.potion.PotionEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.common.ammo.components.factory.AmmoComponentShrapnel;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Registers shrapnel materials and their ammunition components.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 24.05.2019
 */
public class ShrapnelHandler
{
	public static HashMap<String, Shrapnel> registry = new HashMap<>();

	public static Shrapnel addShrapnel(String name, IIColor color, int damage, float mass, float brightness)
	{
		Shrapnel shrapnel = registry.computeIfAbsent(name, s -> new Shrapnel(name, color, damage, mass, brightness));
		AmmoRegistry.registerComponent(new AmmoComponentShrapnel(name));
		return shrapnel;
	}

	public static void removeShrapnel(String shrapnel)
	{
		registry.remove(shrapnel);
		//TODO: 28.04.2024 remove ammo component
	}

	/**
	 * Defines the gameplay and visual properties of one shrapnel material.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.05.2019
	 */
	public static class Shrapnel
	{
		public final String name;
		public final int damage;
		public final IIColor color;
		public final float mass, brightness;
		public boolean flammable = false;
		public boolean goodVsUndead = false;
		private boolean disruptsRadio = false;
		public boolean fallsSlowly = false;
		@Nullable
		public PotionEffect potion = null;

		private Shrapnel(String name, IIColor color, int damage, float mass, float brightness)
		{
			this.name = name;
			this.color = color;
			this.damage = damage;
			this.mass = mass;
			this.brightness = brightness;
		}

		/**
		 * Sets whether this shrapnel can ignite.
		 */
		public Shrapnel setFlammable(boolean flammable)
		{
			this.flammable = flammable;
			return this;
		}

		/**
		 * Sets whether this shrapnel deals double damage to undead targets.
		 */
		public Shrapnel setGoodVsUndead(boolean goodVsUndead)
		{
			this.goodVsUndead = goodVsUndead;
			return this;
		}

		/**
		 * Returns whether this shrapnel disrupts nearby radio devices.
		 */
		public boolean isDisruptsRadio()
		{
			return disruptsRadio;
		}

		/**
		 * Sets whether this shrapnel disrupts nearby radio devices.
		 */
		public Shrapnel setDisruptsRadio(boolean disruptsRadio)
		{
			this.disruptsRadio = disruptsRadio;
			return this;
		}

		/**
		 * Sets whether this shrapnel uses a long, slow-falling trajectory.
		 */
		public Shrapnel setFallsSlowly(boolean fallsSlowly)
		{
			this.fallsSlowly = fallsSlowly;
			return this;
		}

		/**
		 * Sets the potion effect applied to living targets on hit.
		 */
		public Shrapnel setPotion(@Nullable PotionEffect potion)
		{
			this.potion = potion==null?null: new PotionEffect(potion);
			return this;
		}
	}
}
