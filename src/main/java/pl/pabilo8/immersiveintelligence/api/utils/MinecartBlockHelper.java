package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Pabilo8 on 26-01-2020.
 */
public class MinecartBlockHelper
{
	public static Map<Predicate<ItemStack>, Function<World, EntityMinecart>> blocks = new HashMap<>();

	public static EntityMinecart getMinecartFromBlockStack(ItemStack stack, World world)
	{
		for(Entry<Predicate<ItemStack>, Function<World, EntityMinecart>> e : blocks.entrySet())
		{
			if(e.getKey().test(stack))
			{
				return e.getValue().apply(world);
			}
		}
		return new EntityMinecartEmpty(world);
	}
}
