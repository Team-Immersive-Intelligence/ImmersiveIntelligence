package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
public class CoagulatorRecipe extends IIMultiblockRecipe
{
	private static List<DryingInformation> dryingInformation = new ArrayList<>();
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

	private static void setDryingTime(ItemStack outputStack, int dryingTime)
	{
		for(DryingInformation information : dryingInformation)
			if(information.outputPredicate.matchesItemStackIgnoringSize(outputStack))
			{
				information.time = dryingTime;
				return;
			}

		IngredientStack outputPredicate = new IngredientStack(outputStack);
		Optional<CoagulatorRecipe> first = streamRecipes(CoagulatorRecipe.class)
				.filter(recipe -> outputPredicate.matchesItemStack(recipe.itemOutput))
				.findFirst();

		assert first.isPresent();

		IILogger.error("Could not find drying information for stack "+outputStack);
		dryingInformation.add(new DryingInformation(outputPredicate, first.get().fluidInput, dryingTime));
	}

	@Nonnull
	public static DryingInformation getDryingInformationFor(ItemStack outputStack)
	{
		for(DryingInformation information : dryingInformation)
			if(information.outputPredicate.matchesItemStackIgnoringSize(outputStack))
				return information;

		IILogger.error("Could not find drying information for stack "+outputStack);
		return new DryingInformation(new IngredientStack(ItemStack.EMPTY),
				new FluidStack(FluidRegistry.WATER, 1000), Coagulator.bucketTime);
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

	public static class DryingInformation
	{
		private final IngredientStack outputPredicate;
		private final FluidStack fluid;
		private int time;

		public DryingInformation(IngredientStack outputPredicate, FluidStack fluid, int time)
		{
			this.outputPredicate = outputPredicate;
			this.fluid = fluid;
			this.time = time;
		}

		public IngredientStack getOutputPredicate()
		{
			return outputPredicate;
		}

		public FluidStack getFluid()
		{
			return fluid;
		}

		public int getTime()
		{
			return time;
		}
	}
}
