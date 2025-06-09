package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class combining II's {@link IIMultiblockRecipe} recipes with IE's {@link MultiblockRecipe} recipes
 */
public abstract class IIMultiblockRecipe extends MultiblockRecipe implements IIIMultiblockRecipe
{
	private static HashMap<Class<? extends IIMultiblockRecipe>, MultiblockRecipeRegistry<?>> registries = new HashMap<>();
	protected String name;
	private int totalProcessTime;
	private int totalProcessEnergy;
	private int energyPerTick;

	public IIMultiblockRecipe(Object nameSource, Object... nameSources)
	{
		//The recipe needs at least one object for its name source, that's the reason for the constructor
		Object[] joinedSources = new Object[nameSources.length+1];
		joinedSources[0] = nameSource;
		System.arraycopy(nameSources, 0, joinedSources, 1, nameSources.length);

		//Create a name for the recipe based on the sources
		name = Arrays.stream(joinedSources)
				//IngredientStacks don't have a custom toString method, but NBT does
				.map(o -> {
					if(o instanceof IngredientStack)
						return ((IngredientStack)o).writeToNBT(new NBTTagCompound());
					if(o instanceof FluidStack)
						return ((FluidStack)o).getUnlocalizedName();
					return o;
				})
				.map(Object::toString)
				//Create this mess of a name and hope it is unique
				.collect(Collectors.joining("_"));

		registries.computeIfAbsent(this.getClass(), MultiblockRecipeRegistry::new).addRecipe(this);
	}

	protected final void setTimeAndEnergy(int totalProcessTime, int totalProcessEnergy)
	{
		this.totalProcessTime = totalProcessTime;
		this.totalProcessEnergy = totalProcessEnergy;
		this.energyPerTick = totalProcessTime==0?0: this.totalProcessEnergy/this.totalProcessTime;
	}

	@Override
	@Nonnull(when = When.NEVER)
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		//Why serialize something that is the same on every client and server
		return null;
	}

	@Override
	public final int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	@Override
	public final int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}

	public final int getEnergyPerTick()
	{
		return energyPerTick;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Collects recipes matching the given filter and removes them from the registry
	 *
	 * @param recipeClass  recipe type class
	 * @param recipeFilter filter to apply to the recipes
	 * @param <T>          the type of the recipe
	 * @return a list of recipes matching the filter
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends IIMultiblockRecipe> List<T> removeRecipesByFilter(Class<T> recipeClass, Predicate<T> recipeFilter)
	{
		MultiblockRecipeRegistry<T> registry = (MultiblockRecipeRegistry<T>)registries.get(recipeClass);
		List<T> recipes = registry.recipesList.stream()
				.filter(recipeFilter)
				.collect(Collectors.toList());
		registry.recipesList.removeAll(recipes);

		return recipes;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends IIMultiblockRecipe> List<T> getRecipes(Class<T> recipeClass)
	{
		MultiblockRecipeRegistry<T> registry = (MultiblockRecipeRegistry<T>)registries.get(recipeClass);
		return registry.getRecipes();
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends IIMultiblockRecipe> Stream<T> streamRecipes(Class<T> recipeClass)
	{
		return (Stream<T>)registries.get(recipeClass).recipesList.stream();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T extends IIMultiblockRecipe> T getRecipe(Class<T> recipeClass, String name)
	{
		MultiblockRecipeRegistry<T> registry = (MultiblockRecipeRegistry<T>)registries.get(recipeClass);
		return registry.getRecipe(name);
	}

	private static class MultiblockRecipeRegistry<T extends IIMultiblockRecipe>
	{
		private final HashMap<String, T> recipesMap = new HashMap<>();
		private final ArrayList<T> recipesList = new ArrayList<>();

		protected MultiblockRecipeRegistry(Class<T> recipeClass)
		{

		}

		@SuppressWarnings("unchecked")
		public void addRecipe(@Nonnull IIMultiblockRecipe recipe)
		{
			try
			{
				T castedRecipe = (T)recipe;
				recipesMap.put(recipe.getName(), castedRecipe);
				recipesList.add(castedRecipe);
			} catch(ClassCastException e)
			{
				throw new IllegalArgumentException("Invalid recipe type registered: "+recipe.getName());
			}

		}

		@Nullable
		public T getRecipe(String name)
		{
			return recipesMap.get(name);
		}

		@Nonnull
		public List<T> getRecipes()
		{
			return recipesList;
		}
	}
}
