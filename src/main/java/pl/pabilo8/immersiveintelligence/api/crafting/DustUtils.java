package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashMultimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @updated 29.08.2024
 * @ii-approved 0.3.1
 * @since 29.07.2021
 */
public class DustUtils
{
	/**
	 * {@link IngredientStack} is amount sensitive, default amount of dust from an item is 100mB
	 */
	private static final HashMultimap<String, IngredientStack> dustIngredients = HashMultimap.create();
	private static final HashMap<String, IIColor> dustColors = new HashMap<>();

	@VisibleForTesting
	public static void cleanRegistry()
	{
		dustIngredients.clear();
		dustColors.clear();
	}

	public static void registerDust(IngredientStack stack, String name)
	{
		registerDust(stack, name, IIColor.BLACK);
	}

	public static void registerDust(IngredientStack stack, String name, IIColor color)
	{
		dustIngredients.put(name, stack);
		if(!dustColors.containsKey(name))
			dustColors.put(name, color);
	}

	/**
	 * Checks if a stack is registered as dust
	 *
	 * @param stack to be checked
	 * @return whether a dust is registered for this stack
	 */
	public static boolean isDustStack(ItemStack stack)
	{
		return dustIngredients.entries().stream()
				.anyMatch(s -> s.getValue().matchesItemStackIgnoringSize(stack));
	}

	/**
	 * Same as {@link #dustIngredients}
	 *
	 * @param stack to be turned to dust
	 * @return a DustStack created from the ingredient
	 */
	public static DustStack fromItemStack(ItemStack stack)
	{
		Optional<Entry<String, IngredientStack>> first = dustIngredients.entries().stream()
				.filter(s -> s.getValue().matchesItemStackIgnoringSize(stack))
				.findFirst();

		return first.map(stringIngredientStackEntry -> new DustStack(stringIngredientStackEntry.getKey(),
				stack.getCount()*stringIngredientStackEntry.getValue().inputSize)).orElse(DustStack.getEmptyStack());
	}

	public static IIColor getColor(DustStack stack)
	{
		return dustColors.get(stack.name);
	}

	public static List<ItemStack> getDustStacks(String dustName)
	{
		return dustIngredients.entries().stream()
				.filter(e -> e.getKey().equals(dustName))
				.map(e -> e.getValue().getExampleStack())
				.collect(Collectors.toList());
	}

	/**
	 * Use Case: Creates a list of itemstacks to be dropped if a dust storage is broken
	 *
	 * <p>Iterates over all possible stacks for the dust, sorts them in amount descending order,
	 * then takes the max possible amount for each and adds this many itemstacks to the list</p>
	 *
	 * @param dust to be compared
	 * @return itemstacks contained in duststack
	 */
	public static ItemStack[] fromDustStack(DustStack dust)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		Entry<String, IngredientStack>[] matching = dustIngredients.entries().stream()
				.filter(e -> e.getKey().equals(dust.name))
				.sorted(Comparator.comparingInt(o -> o.getValue().inputSize))
				.toArray((IntFunction<Entry<String, IngredientStack>[]>)Entry[]::new);

		int i = dust.amount;
		for(Entry<String, IngredientStack> entry : matching)
		{
			int l = (int)Math.floor(i/(float)entry.getValue().inputSize);
			ItemStack copy = entry.getValue().getExampleStack().copy();
			copy.setCount(l);
			list.add(copy);
			i -= l*entry.getValue().inputSize;

			if(i==0)
				break;
		}

		return list.toArray(new ItemStack[0]);
	}

	@SideOnly(Side.CLIENT)
	public static String getDustName(DustStack dustStorage)
	{
		return dustStorage.isEmpty()?"": I18n.format("dust."+dustStorage.name);
	}
}
