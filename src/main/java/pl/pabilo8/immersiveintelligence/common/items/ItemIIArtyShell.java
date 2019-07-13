package pl.pabilo8.immersiveintelligence.common.items;

import net.minecraft.entity.Entity;

/**
 * Created by Pabilo8 on 2019-05-11.
 */
public class ItemIIArtyShell extends ItemIIBase
{
	private String[] name;
	private int[] explosionRadius, weight, color;
	private boolean[] tracer;
	private Entity[] explosionEffect;

	public ItemIIArtyShell(String[] name, int[] explosionRadius, int[] weight, int[] color, boolean[] tracer, Entity[] explosionEffect)
	{
		super("artillery_shell", 64, name);
		this.name = name.clone();
		this.explosionRadius = explosionRadius.clone();
		this.weight = weight.clone();
		this.color = color.clone();
		this.tracer = tracer.clone();
		this.explosionEffect = explosionEffect.clone();
	}
}
