package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
public class CoagulatorRecipe extends IIMultiblockRecipe
{
	private static HashMap<Predicate<ItemStack>, Integer> bucketTimeMap = new HashMap<>();
	public final FluidStack fluidInput, coagulantInput;
	public final ItemStack itemOutput;

	public CoagulatorRecipe(ItemStack itemOutput, FluidStack fluidInput, FluidStack coagulantInput, int energy, int mixingTime)
	{
		super(itemOutput, fluidInput, coagulantInput);
		this.itemOutput = itemOutput;
		this.fluidInput = fluidInput;
		this.coagulantInput = coagulantInput;
		setTimeAndEnergy(mixingTime, energy);
		setDryingTime(this.itemOutput, Coagulator.bucketTime);
	}

	public CoagulatorRecipe(ItemStack itemOutput, FluidStack fluidInput, FluidStack coagulantInput, int energy, int mixingTime, int dryingTime)
	{
		this(itemOutput, fluidInput, coagulantInput, energy, mixingTime);
		setDryingTime(this.itemOutput, dryingTime);
	}

	private static void setDryingTime(ItemStack output, int dryingTime)
	{
		Optional<Predicate<ItemStack>> found = bucketTimeMap.keySet().stream().filter(predicate -> predicate.test(output)).findFirst();
		found.ifPresent(itemStackPredicate -> bucketTimeMap.remove(itemStackPredicate));
		bucketTimeMap.put(output::isItemEqual, dryingTime);
	}

	public static int getDryingTimeFor(ItemStack stack)
	{
		for(Predicate<ItemStack> predicate : bucketTimeMap.keySet())
			if(predicate.test(stack))
				return bucketTimeMap.get(predicate);
		return Coagulator.bucketTime;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(144, 64)
				.withFluidTank(4, 2, fluidInput)
				.withFluidTank(4+22, 2, coagulantInput)
				.withOutputSlot(144-22, (64-16)/2-9, itemOutput)
				.withMultiblockModel(32+8, 0)
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
