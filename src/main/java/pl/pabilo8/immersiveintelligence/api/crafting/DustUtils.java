package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.HashMultimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * @author Pabilo8
 * @since 29.07.2021
 */
public class DustUtils
{
	/**
	 * {@link IngredientStack} is amount sensitive, default amount of dust from an item is 100mB
	 */
	private static final HashMultimap<String, IngredientStack> dustIngredients = HashMultimap.create();
	private static final HashMap<String, Integer> dustColors = new HashMap<>();

	public static void registerDust(IngredientStack stack, String name)
	{
		registerDust(stack, name, 0);
	}

	public static void registerDust(IngredientStack stack, String name, int color)
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

	/**
	 * @param stack of which amount will be counted as ITEM amount (not in mB)
	 * @return a DustStack created from the ingredient, it's amount is ITEM amount multiplied by dust amount per item taken from {@link #dustIngredients}
	 * <p>TL;DR: converts item amount to mB</p>
	 */
	public static DustStack fromIngredient(IngredientStack stack)
	{
		Optional<Entry<String, IngredientStack>> first = dustIngredients.entries().stream()
				.filter(s -> ApiUtils.stackMatchesObject(stack.getExampleStack(), s.getValue()))
				.findFirst();

		return first.map(stringIngredientStackEntry -> new DustStack(stringIngredientStackEntry.getKey(),
				stack.inputSize*stringIngredientStackEntry.getValue().inputSize)).orElse(DustStack.getEmptyStack());

	}

	public static int getColor(DustStack stack)
	{
		return dustColors.get(stack.name);
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
