package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Vulcanizer production recipe.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.06.2021
 */
//REFACTOR: 06.12.2025 move to new system fully
public class VulcanizerRecipe extends IIMultiblockRecipe
{
	public static final ResourceLocation TEXTURE_LATEX = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/blocks/multiblock/vulcanizer/latex_strip.png");
	public static final ResourceLocation TEXTURE_RUBBER = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/blocks/multiblock/vulcanizer/rubber_strip.png");
	public static ArrayListMultimap<ComparableItemStack, VulcanizerRecipe> recipeList = ArrayListMultimap.create();
	public static HashMap<String, Function<NBTTagCompound, VulcanizerRecipe>> deserializers = new HashMap<>();
	public final IngredientStack input;
	public final IngredientStack compoundInput;
	public final IngredientStack sulfurInput;
	public final ComparableItemStack mold;
	public final ItemStack output;
	public final ResourceLocation resIn, resOut;

	public VulcanizerRecipe(ItemStack output, ComparableItemStack mold, IngredientStack mainInput, IngredientStack compoundInput, IngredientStack sulfurInput, int energy, ResourceLocation resIn, ResourceLocation resOut)
	{
		super(mold.stack, mainInput);
		this.output = output;
		this.mold = mold;
		this.input = ApiUtils.createIngredientStack(mainInput);
		this.compoundInput = ApiUtils.createIngredientStack(compoundInput);
		this.sulfurInput = ApiUtils.createIngredientStack(sulfurInput);
		setTimeAndEnergy(1000, energy);

		this.inputList = Lists.newArrayList(this.input, this.compoundInput, this.sulfurInput, new IngredientStack(this.mold.stack));
		this.outputList = ListUtils.fromItem(this.output);

		this.resIn = resIn;
		this.resOut = resOut;
	}

	public static VulcanizerRecipe addRecipe(ItemStack output, ComparableItemStack mold, IngredientStack input, IngredientStack compound, IngredientStack sulfur, int energy)
	{
		return addRecipe(output, mold, input, compound, sulfur, energy, TEXTURE_LATEX, TEXTURE_RUBBER);
	}

	public static VulcanizerRecipe addRecipe(ItemStack output, ComparableItemStack mold, IngredientStack input, IngredientStack compound, IngredientStack sulfur, int energy, ResourceLocation resIn, ResourceLocation resOut)
	{
		VulcanizerRecipe r = new VulcanizerRecipe(output, mold, input, compound, sulfur, energy, resIn, resOut);
		recipeList.put(mold, r);
		return r;
	}

	public static VulcanizerRecipe findRecipe(ItemStack mold, ItemStack input)
	{
		if(mold.isEmpty()||input.isEmpty())
			return null;
		ComparableItemStack comp = ApiUtils.createComparableItemStack(mold, false);
		List<VulcanizerRecipe> list = recipeList.get(comp);
		for(VulcanizerRecipe recipe : list)
			if(recipe.matches(mold, input))
				return recipe.getActualRecipe(mold, input);
		return null;
	}

	public static List<VulcanizerRecipe> removeRecipes(ItemStack output)
	{
		List<VulcanizerRecipe> list = new ArrayList<>();
		Set<ComparableItemStack> keySet = new HashSet<>(recipeList.keySet());
		for(ComparableItemStack mold : keySet)
		{
			Iterator<VulcanizerRecipe> it = recipeList.get(mold).iterator();
			while(it.hasNext())
			{
				VulcanizerRecipe ir = it.next();
				if(OreDictionary.itemMatches(ir.output, output, true))
				{
					list.add(ir);
					it.remove();
				}
			}
		}
		return list;
	}

	public static boolean isValidMold(ItemStack itemStack)
	{
		if(itemStack.isEmpty())
			return false;
		return recipeList.containsKey(ApiUtils.createComparableItemStack(itemStack, false));
	}

	public static VulcanizerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("type")&&deserializers.containsKey(nbt.getString("type")))
			return deserializers.get(nbt.getString("type")).apply(nbt);
		IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));
		ComparableItemStack mold = ComparableItemStack.readFromNBT(nbt.getCompoundTag("mold"));
		List<VulcanizerRecipe> list = recipeList.get(mold);
		for(VulcanizerRecipe recipe : list)
			if(recipe.input.equals(input))
				return recipe;
		return null;
	}

	public VulcanizerRecipe setInputSize(int size)
	{
		this.input.inputSize = size;
		return this;
	}

	public boolean matches(ItemStack mold, ItemStack input)
	{
		return this.input.matches(input);
	}

	public VulcanizerRecipe getActualRecipe(ItemStack mold, ItemStack input)
	{
		return this;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(156, 74+4)
				.withSlot(4, 4, input, IOType.INPUT, "frame")
				.withSlot(4, 24, compoundInput, IOType.INPUT, "frame_none")
				.withSlot(4, 44, sulfurInput, IOType.INPUT, "frame_none")
				.withSlot(156/2-9, 44, mold.stack.copy(), IOType.INPUT)
				.withSlot(138-4, 24, output, IOType.OUTPUT, "frame")
				.withMultiblockModel(32+8, -8, 80, 80, "")
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("mold", mold.writeToNBT(new NBTTagCompound()));
		nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
}
