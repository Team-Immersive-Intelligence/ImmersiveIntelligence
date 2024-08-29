package pl.pabilo8.immersiveintelligence.api;

import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.common.ammo.components.factory.AmmoComponentShrapnel;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
public class ShrapnelHandler
{
	public static HashMap<String, Shrapnel> registry = new HashMap<>();

	public static Shrapnel addShrapnel(String name, IIColor color, ResLoc texture, int damage, float mass, float brightness)
	{
		//Register the shrapnel
		Shrapnel shrapnel = registry.computeIfAbsent(name, s -> new Shrapnel(name, texture, color, damage, mass, brightness));
		//Register an ammo component of the shrapnel
		AmmoComponentShrapnel component = new AmmoComponentShrapnel(name);
		AmmoRegistry.registerComponent(component);
		return shrapnel;
	}

	public static void removeShrapnel(String shrapnel)
	{
		registry.remove(shrapnel);
		//TODO: 28.04.2024 remove ammo component
	}

	public static class Shrapnel
	{
		//--- Properties ---//
		public final String name;
		public final ResLoc texture;
		public final int damage;
		public final IIColor color;
		public final float mass, brightness;
		public boolean flammable = false;
		public boolean goodVsUndead = false;
		private boolean disruptsRadio;

		private Shrapnel(String name, ResLoc texture, IIColor color, int damage, float mass, float brightness)
		{
			this.name = name;
			this.color = color;
			this.texture = texture;
			this.damage = damage;
			this.mass = mass;
			this.brightness = brightness;
		}

		//--- Setters ---//

		/**
		 * @param flammable whether the shrapnel can be ignited and deal fire damage
		 * @return this
		 */
		public Shrapnel setFlammable(boolean flammable)
		{
			this.flammable = flammable;
			return this;
		}

		/**
		 * @param goodVsUndead whether the shrapnel deals extra damage to the undead
		 * @return this
		 */
		public Shrapnel setGoodVsUndead(boolean goodVsUndead)
		{
			this.goodVsUndead = goodVsUndead;
			return this;
		}

		/**
		 * @param disruptsRadio whether the shrapnel disrupts radio signals
		 * @return this
		 */
		public void setDisruptsRadio(boolean disruptsRadio)
		{
			this.disruptsRadio = disruptsRadio;
		}

		public boolean isDisruptsRadio()
		{
			return disruptsRadio;
		}
	}
}
