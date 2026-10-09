package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Vulcanizer production recipe.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.10.2026
 * @ii-approved 0.3.1
 * @since 20.06.2021
 */
public class VulcanizerRecipe extends IIMultiblockRecipe
{
	public static final ResourceLocation MODEL_TIRE = new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/vulcanizer/output_tire.obj");
	public static final ResourceLocation MODEL_BELT = new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/vulcanizer/output_belt.obj");

	public final IngredientStack input, compoundInput, sulfurInput;
	public final ResourceLocation model;
	public final ComparableItemStack mold;
	public final ItemStack output;

	public VulcanizerRecipe(ItemStack output, ComparableItemStack mold, IngredientStack mainInput, IngredientStack compoundInput, IngredientStack sulfurInput, int energy, ResourceLocation model)
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

		this.model = model;
		completeRegistration(this.mold.stack, this.input, this.compoundInput, this.sulfurInput, this.output);
	}

	public VulcanizerRecipe(ItemStack output, ComparableItemStack mold, IngredientStack mainInput, IngredientStack compoundInput, IngredientStack sulfurInput, int energy)
	{
		this(output, mold, mainInput, compoundInput, sulfurInput, energy, MODEL_TIRE);
	}

	public static boolean isValidMold(ItemStack itemStack)
	{
		if(itemStack.isEmpty())
			return false;
		ComparableItemStack comparable = new ComparableItemStack(itemStack);
		return streamRecipes(VulcanizerRecipe.class)
				.anyMatch(recipe -> recipe.mold.equals(comparable));
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(156, 74+4)
				.withSlot(4, 4, input, IOType.INPUT, "frame")
				.withSlot(4, 24, compoundInput, IOType.INPUT, "frame_none")
				.withSlot(4, 44, sulfurInput, IOType.INPUT, "frame_none")
				.withToolSlot(156/2-9, 44, mold.stack.copy())
				.withSlot(138-4, 24, output, IOType.OUTPUT, "frame")
				.withMultiblockModel(32+8, -8, 80, 80, "")
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
