package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class combining II's {@link IIMultiblockRecipe} recipes with IE's {@link MultiblockRecipe} recipes
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.10.2026
 */
public abstract class IIMultiblockRecipe extends MultiblockRecipe implements IIIMultiblockRecipe
{
	private static HashMap<Class<? extends IIMultiblockRecipe>, MultiblockRecipeRegistry<?>> registries = new HashMap<>();
	protected String name;
	private final String legacyName;
	private final boolean registered;
	private IIRecipeLayout recipeLayout;
	private int totalProcessTime;
	private int totalProcessEnergy;
	private int energyPerTick;

	public IIMultiblockRecipe(Object nameSource, Object... nameSources)
	{
		this(nameSource, nameSources, true);
	}

	protected IIMultiblockRecipe(Object nameSource, Object[] nameSources, boolean register)
	{
		name = generateLegacyRecipeName(nameSource, nameSources);
		legacyName = name;
		registered = register;
		if(register)
			registries.computeIfAbsent(this.getClass(), MultiblockRecipeRegistry::new).addRecipe(this, true);
	}

	/**
	 * Sets a complete identity and keeps the old save name as an alias.
	 */
	protected final void completeRegistration(Object source, Object... sources)
	{
		setName(generateRecipeName(source, sources));
		MultiblockRecipeRegistry<?> registry = registries.get(getClass());
		if(registry!=null&&registered)
			registry.addLegacyName(legacyName, this);
	}

	@Nonnull
	private static String generateLegacyRecipeName(Object nameSource, Object... nameSources)
	{
		//The recipe needs at least one object for its name source, that's the reason for the constructor
		Object[] joinedSources = new Object[nameSources.length+1];
		joinedSources[0] = nameSource;
		System.arraycopy(nameSources, 0, joinedSources, 1, nameSources.length);

		return Arrays.stream(joinedSources)
				//IngredientStacks don't have a custom toString method, but NBT does
				.map(o -> {
					if(o instanceof ItemStack)
						return IIItemUtils.getUniqueStackString(((ItemStack)o));
					if(o instanceof IngredientStack)
						return createIngredientStackName(((IngredientStack)o));
					if(o instanceof FluidStack)
						return ((FluidStack)o).getUnlocalizedName();
					return o;
				})
				.map(Object::toString)
				//Create this mess of a name and hope it is unique
				.collect(Collectors.joining("_"));
	}

	@Nonnull
	public static String generateRecipeName(Object source, Object... sources)
	{
		Object[] all = new Object[sources.length+1];
		all[0] = source;
		System.arraycopy(sources, 0, all, 1, sources.length);
		return Arrays.stream(all).map(IIMultiblockRecipe::getIdentityPart).collect(Collectors.joining("_"));
	}

	private static String getIdentityPart(Object value)
	{
		if(value instanceof FluidStack)
			return ((FluidStack)value).writeToNBT(new NBTTagCompound()).toString();
		if(value instanceof IngredientStack)
		{
			IngredientStack ingredient = (IngredientStack)value;
			NBTTagCompound nbt = ingredient.writeToNBT(new NBTTagCompound());
			if(ingredient.fluid!=null)
				nbt.setTag("fluidStack", ingredient.fluid.writeToNBT(new NBTTagCompound()));
			return nbt.toString();
		}
		if(value instanceof ItemStack)
			return IIItemUtils.getUniqueStackString((ItemStack)value);
		if(value instanceof Object[])
			return Arrays.stream((Object[])value).map(IIMultiblockRecipe::getIdentityPart).collect(Collectors.joining(",", "[", "]"));
		return String.valueOf(value);
	}

	private static String createIngredientStackName(IngredientStack stack)
	{
		StringBuilder sb = new StringBuilder();

		if(stack.fluid!=null)
			sb.append(stack.fluid.getUnlocalizedName())
					.append("_")
					.append(stack.fluid.amount);
		else if(stack.oreName!=null)
			sb.append(stack.oreName);
		else if(stack.stackList!=null)
		{
			boolean first = true;
			for(ItemStack contained : stack.stackList)
				if(!contained.isEmpty())
				{
					if(!first)
						sb.append("_");
					sb.append(IIItemUtils.getUniqueStackString(contained));
					first = false;
				}
		}
		else
			sb.append(IIItemUtils.getUniqueStackString(stack.stack));
		if(stack.inputSize > 1)
			sb.append("_").append(stack.inputSize);

		return sb.toString();
	}

	public void setName(String name)
	{
		if(!registered)
		{
			this.name = name;
			return;
		}
		MultiblockRecipeRegistry<?> registry = registries.get(getClass());
		if(registry==null)
			throw new IllegalStateException("Missing recipe registry: "+getClass().getName());
		registry.recipesMap.entrySet().removeIf(entry -> entry.getValue()==this);
		registry.recipesList.remove(this);
		this.name = name;
		registry.addRecipe(this, false);
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
		if(registry==null)
			return Collections.emptyList();
		List<T> recipes = registry.recipesList.stream()
				.filter(recipeFilter)
				.collect(Collectors.toList());
		registry.recipesList.removeAll(recipes);
		registry.recipesMap.entrySet().removeIf(entry -> recipes.contains(entry.getValue()));
		registry.legacyNames.entrySet().removeIf(entry -> recipes.contains(entry.getValue()));

		return recipes;
	}

	/**
	 * @return a list of all registered multiblock recipes
	 * @apiNote do not use it in multiblock classes, this method returns ALL the registered recipes, not only for a specific machine.
	 */
	public static List<IIMultiblockRecipe> listAllMultiblockRecipes()
	{
		List<IIMultiblockRecipe> allRecipes = new ArrayList<>();
		registries.values().forEach(registry -> allRecipes.addAll(registry.recipesList));
		return allRecipes;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends IIMultiblockRecipe> List<T> getRecipes(Class<T> recipeClass)
	{
		MultiblockRecipeRegistry<T> registry = (MultiblockRecipeRegistry<T>)registries.get(recipeClass);
		return registry==null?Collections.emptyList(): registry.getRecipes();
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends IIMultiblockRecipe> Stream<T> streamRecipes(Class<T> recipeClass)
	{
		if(!registries.containsKey(recipeClass))
			return Stream.empty();
		return (Stream<T>)registries.get(recipeClass).recipesList.stream();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T extends IIMultiblockRecipe> T getRecipe(Class<T> recipeClass, String name)
	{
		MultiblockRecipeRegistry<T> registry = (MultiblockRecipeRegistry<T>)registries.get(recipeClass);
		return registry==null?null: registry.getRecipe(name);
	}

	public static Class<IIMultiblockRecipe> getRecipeClassFromName(String type)
	{
		for(Class<? extends IIMultiblockRecipe> klass : registries.keySet())
			if(getRecipeClassName(klass).equals(type))
				//noinspection unchecked
				return (Class<IIMultiblockRecipe>)klass;
		return null;
	}

	public static String getRecipeClassName(Class<? extends IIMultiblockRecipe> klass)
	{
		return IIStringUtil.toSnakeCase(klass.getSimpleName()).replace("_recipe", "");
	}

	@SideOnly(Side.CLIENT)
	protected void loadClientSideContent()
	{

	}

	/**
	 *
	 *
	 * @return a universal recipe layout used by JEI Compat and the Manual, created using an {@link IIRecipeLayoutBuilder}.
	 * @apiNote Initialize the recipe layout here, not in {@link #getRecipeLayout()}
	 */
	@Nullable
	protected abstract IIRecipeLayout initRecipeLayout();

	/**
	 *
	 * @return a universal recipe layout used by JEI Compat and the Manual, created using an {@link IIRecipeLayoutBuilder}.
	 */
	@Nullable
	public final IIRecipeLayout getRecipeLayout()
	{
		return recipeLayout==null?(this.recipeLayout = this.initRecipeLayout()): this.recipeLayout;
	}

	/**
	 * Reloads all registered multiblock recipe layouts by calling {@link #initRecipeLayout()} on each of them.
	 */
	public static void reloadAllRecipeLayouts()
	{
		for(MultiblockRecipeRegistry<?> registry : registries.values())
			for(IIMultiblockRecipe recipe : registry.recipesList)
				recipe.recipeLayout = recipe.initRecipeLayout();
	}

	/**
	 * Called on post-init and after reload to load client-side recipe content, like animations or sound references.
	 */
	@SideOnly(Side.CLIENT)
	public static void loadAllClientSideContent()
	{
		registries.values().forEach(registry ->
				registry.recipesList.forEach(IIMultiblockRecipe::loadClientSideContent)
		);
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
	@Deprecated
	public final int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @param subCategory a name of the subcategory
	 * @return true if the recipe matches the given subcategory, false otherwise
	 * @implNote when the recipe does not implement a split into categories, the category name is passed as subCategory instead
	 */
	public boolean matchesSubCategory(String subCategory)
	{
		return true;
	}

	private static class MultiblockRecipeRegistry<T extends IIMultiblockRecipe>
	{
		private final HashMap<String, T> recipesMap = new HashMap<>();
		private final HashMap<String, T> legacyNames = new HashMap<>();
		private final ArrayList<T> recipesList = new ArrayList<>();

		protected MultiblockRecipeRegistry(Class<T> recipeClass)
		{

		}

		@SuppressWarnings("unchecked")
		public void addRecipe(@Nonnull IIMultiblockRecipe recipe, boolean provisional)
		{
			try
			{
				T castedRecipe = (T)recipe;
				if(provisional)
				{
					String base = recipe.name;
					int suffix = 1;
					while(recipesMap.containsKey(recipe.name))
						recipe.name = base+"~"+suffix++;
				}
				else
				{
					T previous = recipesMap.get(recipe.getName());
					if(previous!=null&&previous!=recipe)
					{
						IILogger.warn("Replacing duplicate recipe identity: "+recipe.getName());
						recipesList.remove(previous);
						legacyNames.replaceAll((key, value) -> value==previous?castedRecipe: value);
					}
				}
				recipesMap.put(recipe.getName(), castedRecipe);
				recipesList.add(castedRecipe);
			} catch(ClassCastException e)
			{
				throw new IllegalArgumentException("Invalid recipe type registered: "+recipe.getName());
			}

		}

		@SuppressWarnings("unchecked")
		private void addLegacyName(String name, IIMultiblockRecipe recipe)
		{
			legacyNames.put(name, (T)recipe);
		}

		@Nullable
		public T getRecipe(String name)
		{
			T recipe = recipesMap.containsKey(name)?recipesMap.get(name): legacyNames.get(name);
			if(recipe!=null)
				return recipe;
			int arrayName = name.indexOf("_[Ljava.lang.Object;@");
			if(arrayName < 0)
				return null;
			String prefix = name.substring(0, arrayName);
			T matched = null;
			for(T candidate : recipesList)
			{
				String legacy = ((IIMultiblockRecipe)candidate).legacyName;
				if(!legacy.startsWith(prefix+"_[Ljava.lang.Object;@"))
					continue;
				if(matched!=null)
					return null;
				matched = candidate;
			}
			return matched;
		}

		@Nonnull
		public List<T> getRecipes()
		{
			return recipesList;
		}
	}
}
