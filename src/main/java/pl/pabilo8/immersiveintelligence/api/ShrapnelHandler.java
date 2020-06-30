package pl.pabilo8.immersiveintelligence.api;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
public class ShrapnelHandler
{
	public static HashMap<String, Shrapnel> registry = new HashMap<>();

	public static void addShrapnel(String name, int color, String texture, int damage, float mass, float brightness)
	{
		if(!registry.containsKey(name))
			registry.put(name, new Shrapnel(texture, color, damage, mass, brightness));
	}

	public static void removeShrapnel(String shrapnel)
	{
		registry.remove(shrapnel);
	}

	public static class Shrapnel
	{
		public String texture;
		public int damage, color;
		public float mass, brightness;
		public boolean flammable = false, good_vs_undead = false;

		private Shrapnel(String texture, int color, int damage, float mass, float brightness)
		{
			this.color = color;
			this.texture = texture;
			this.damage = damage;
			this.mass = mass;
			this.brightness = brightness;
			this.flammable = flammable;
		}
	}
}
