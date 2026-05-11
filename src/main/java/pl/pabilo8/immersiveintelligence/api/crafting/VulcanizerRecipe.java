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
 * @updated 10.05.2026
 * @ii-approved 0.3.1
 * @since 20.06.2021
 */
public class VulcanizerRecipe extends IIMultiblockRecipe
{
	public static final ResourceLocation TEXTURE_LATEX = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/blocks/multiblock/vulcanizer/latex_strip.png");
	public static final ResourceLocation TEXTURE_RUBBER = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/blocks/multiblock/vulcanizer/rubber_strip.png");

	public final IngredientStack input, compoundInput, sulfurInput;
	public final ResourceLocation resIn, resOut;
	public final ComparableItemStack mold;
	public final ItemStack output;

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

	public VulcanizerRecipe(ItemStack output, ComparableItemStack mold, IngredientStack mainInput, IngredientStack compoundInput, IngredientStack sulfurInput, int energy)
	{
		this(output, mold, mainInput, compoundInput, sulfurInput, energy, TEXTURE_LATEX, TEXTURE_RUBBER);
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
				.withSlot(156/2-9, 44, mold.stack.copy(), IOType.INPUT)
				.withSlot(138-4, 24, output, IOType.OUTPUT, "frame")
				.withMultiblockModel(32+8, -8, 80, 80, "")
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
