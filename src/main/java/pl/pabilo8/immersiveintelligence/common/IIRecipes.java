package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;

/**
 * Created by Pabilo8 on 22-03-2020.
 */
public class IIRecipes
{
	public static final void addCircuitRecipes()
	{
		//Allow me to introduce you to Immersive Gregineering™

		//Remove old circuit recipe
		ItemStack circuitboard = new ItemStack(IEContent.itemMaterial, 1, 27);
		BlueprintCraftingRecipe.recipeList.get("components").removeIf(blueprintCraftingRecipe -> blueprintCraftingRecipe.output.isItemEqual(circuitboard));

		BlueprintCraftingRecipe.addRecipe("basic_circuits",
				new ItemStack(CommonProxy.item_material, 1,
						CommonProxy.item_material.getMetaBySubname("basic_circuit_board_raw")), new ItemStack(IEContent.blockStoneDecoration, 2, BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta()), "plateCopper");
		BlueprintCraftingRecipe.addRecipe("basic_circuits", new ItemStack(IEContent.itemMaterial, 1, 27), "circuitBasicEtched", new IngredientStack("chipBasic", 2));

		BlueprintCraftingRecipe.addRecipe("advanced_circuits",
				new ItemStack(CommonProxy.item_material, 1,
						CommonProxy.item_material.getMetaBySubname("advanced_circuit_board_raw")), new IngredientStack("circuitBasicRaw", 2), "plateAdvancedElectronicAlloy");
		BlueprintCraftingRecipe.addRecipe("advanced_circuits",
				new ItemStack(CommonProxy.item_material, 1,
						CommonProxy.item_material.getMetaBySubname("advanced_circuit_board")), "circuitAdvancedEtched", new IngredientStack("chipAdvanced", 2));

		BlueprintCraftingRecipe.addRecipe("processors",
				new ItemStack(CommonProxy.item_material, 1,
						CommonProxy.item_material.getMetaBySubname("processor_circuit_board_raw")), new IngredientStack("circuitAdvancedRaw", 4), new IngredientStack("plateAdvancedElectronicAlloy", 2));
		BlueprintCraftingRecipe.addRecipe("processors",
				new ItemStack(CommonProxy.item_material, 1,
						CommonProxy.item_material.getMetaBySubname("processor_circuit_board")), "circuitProcessorEtched", new IngredientStack("chipProcessor", 2));

		//Circuits
		BathingRecipe.addRecipe(new ItemStack(CommonProxy.item_material, 1, CommonProxy.item_material.getMetaBySubname("basic_circuit_board_etched")), new IngredientStack("circuitBasicRaw"), FluidRegistry.getFluidStack("etching_acid", 1000), 15000, 360);
		BathingRecipe.addRecipe(new ItemStack(CommonProxy.item_material, 1, CommonProxy.item_material.getMetaBySubname("advanced_circuit_board_etched")), new IngredientStack("circuitAdvancedRaw"), FluidRegistry.getFluidStack("etching_acid", 2000), 150000, 560);
		BathingRecipe.addRecipe(new ItemStack(CommonProxy.item_material, 1, CommonProxy.item_material.getMetaBySubname("processor_circuit_board_etched")), new IngredientStack("circuitProcessorRaw"), FluidRegistry.getFluidStack("etching_acid", 4000), 1500000, 720);


	}

	public static final void addInkRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_cyan, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeCyan"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_yellow, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeMagenta"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_magenta, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeYellow"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_black, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeBlack"}, 3200);
	}

	public static final void addRotaryPowerRecipes()
	{

	}

}
