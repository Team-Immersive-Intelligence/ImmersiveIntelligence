package pl.pabilo8.immersiveintelligence.common.util.lambda;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.stream.Collector;

/**
 * Collects ItemStacks into an IngredientStack using a list.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.12.2025
 */
public class IngredientStackCollector implements Collector<ItemStack, ArrayList<ItemStack>, IngredientStack>
{
	private IngredientStackCollector()
	{
	}

	public static IngredientStackCollector collect()
	{
		return new IngredientStackCollector();
	}

	@Override
	public java.util.function.Supplier<ArrayList<ItemStack>> supplier()
	{
		return ArrayList::new;
	}

	@Override
	public java.util.function.BiConsumer<ArrayList<ItemStack>, ItemStack> accumulator()
	{
		return ArrayList::add;
	}

	@Override
	public java.util.function.BinaryOperator<ArrayList<ItemStack>> combiner()
	{
		return (list1, list2) -> {
			list1.addAll(list2);
			return list1;
		};
	}

	@Override
	public java.util.function.Function<ArrayList<ItemStack>, IngredientStack> finisher()
	{
		return IngredientStack::new;
	}

	@Override
	public java.util.Set<Characteristics> characteristics()
	{
		return java.util.Collections.emptySet();
	}
}
