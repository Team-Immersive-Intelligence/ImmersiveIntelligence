package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.ListUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_SmallCrate;
import pl.pabilo8.immersiveintelligence.common.crafting.RecipeCrateConversion;
import pl.pabilo8.immersiveintelligence.common.crafting.RecipeMinecart;

import java.util.Objects;

/**
 * Created by Pabilo8 on 22-03-2020.
 */
public class IIRecipes
{
	public static void addCircuitRecipes()
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

//4x Vacuum tube + 1 x copper nugget = 2 x copper wire, 1 x iron plate, 1 x glass block
		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(IEContent.itemMaterial, 4, 26),
				new ItemStack(IEContent.itemMetal, 1, 20),

				new IngredientStack[]{new IngredientStack("plateIron"), new IngredientStack("wireCopper", 2), new IngredientStack("blockGlass")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "solderer work first", "inserter pick first", "inserter drop main", "solderer work main", "drill work second", "inserter pick second", "inserter drop main"},

				12000,
				1.0f
		);

		//1x Basic Electronic Component =  2x vacuum tube + nickel plate + 4 x redstone dust
		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(CommonProxy.item_material, 1, CommonProxy.item_material.getMetaBySubname("basic_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTube"), new IngredientStack("plateNickel"), new IngredientStack("dustRedstone", 4)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				18000,
				1.0f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(CommonProxy.item_material, 2, CommonProxy.item_material.getMetaBySubname("advanced_electron_tube")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSteel"), new IngredientStack("wireTungsten", 2), new IngredientStack("electronTube", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "inserter pick second", "inserter drop main", "inserter pick first", "inserter drop main", "solderer work main"},

				24000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(CommonProxy.item_material, 1, CommonProxy.item_material.getMetaBySubname("advanced_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTubeAdvanced"), new IngredientStack("chipBasic")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				32000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material, "transistor", 4),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSilicon", 2), new IngredientStack("plateAdvancedElectronicAlloy"), new IngredientStack("plateAluminum"), new IngredientStack("dyeBlack")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "solderer work first", "inserter pick first", "drill work main", "inserter drop main", "inserter pick second", "solderer work main", "inserter drop main", "inserter pick third", "inserter drop third", "solderer work main"},

				50000,
				0.9f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(CommonProxy.item_material, 1, CommonProxy.item_material.getMetaBySubname("processor_electronic_element")),
				Utils.getItemWithMetaName(CommonProxy.item_material_nugget, "silicon"),

				new IngredientStack[]{new IngredientStack("transistor", 32), new IngredientStack("electronTubeAdvanced", 2)},

				new String[]{"inserter", "solderer", "buzzsaw"},
				new String[]{"solderer work first", "inserter pick first", "buzzsaw work main", "solderer work main", "inserter drop main", "buzzsaw work main", "solderer work main"},

				500000,
				1.5f
		);
	}

	public static void addSiliconProcessingRecipes()
	{
		PrecissionAssemblerRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material_plate, "silicon"),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("bouleSilicon", 1)},

				new String[]{"buzzsaw"},
				new String[]{"buzzsaw work main"},

				24000,
				3f
		);

		PrecissionAssemblerRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material_ingot, "silicon"),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("bouleSilicon", 1)},

				new String[]{"hammer", "buzzsaw"},
				new String[]{"hammer work main", "hammer work main", "buzzsaw work main", "hammer work main"},

				24000,
				1f
		);


		CrusherRecipe.removeRecipesForInput(new ItemStack(Items.QUARTZ));
		CrusherRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material_dust, "quartz_dirty"),
				new IngredientStack("gemQuartz", 1),
				3200
		);

		BathingRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material_dust, "quartz"),
				new IngredientStack("dustQuartzDirty"),
				new FluidStack(CommonProxy.fluid_hydrofluoric_acid, 1000),
				4200, 240
		);

		ArcFurnaceRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material_boule, "silicon"),
				new IngredientStack("dustQuartz", 6),
				ItemStack.EMPTY,
				400, 512
		);


	}

	public static void addInkRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_cyan, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeCyan"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_yellow, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeMagenta"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_magenta, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeYellow"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ink_black, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeBlack"}, 3200);
	}

	public static void addBulletPressRecipes()
	{
		MetalPressRecipe.addRecipe(new ItemStack(CommonProxy.item_casing_artillery), new IngredientStack("ingotBrass", 4), Utils.getItemWithMetaName(CommonProxy.item_mold, "howitzer"), 2800);
		MetalPressRecipe.addRecipe(new ItemStack(CommonProxy.item_casing_grenade), new IngredientStack("nuggetBrass", 5), Utils.getItemWithMetaName(CommonProxy.item_mold, "grenade"), 1800);
		MetalPressRecipe.addRecipe(new ItemStack(CommonProxy.item_casing_machinegun, 3), new IngredientStack("ingotBrass"), Utils.getItemWithMetaName(CommonProxy.item_mold, "machinegun"), 1600);
		MetalPressRecipe.addRecipe(new ItemStack(CommonProxy.item_casing_submachinegun, 6), new IngredientStack("ingotBrass"), Utils.getItemWithMetaName(CommonProxy.item_mold, "submachinegun"), 1200);

		MetalPressRecipe.removeRecipes(new ItemStack(IEContent.itemBullet, 2, 0));
		MetalPressRecipe.addRecipe(new ItemStack(IEContent.itemBullet, 2, 0), "ingotBrass", new ItemStack(IEContent.itemMold, 1, 3), 2800);


	}

	public static void addFunctionalCircuits()
	{
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "arithmetic"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "logic"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "comparator"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "text"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);

		BlueprintCraftingRecipe.addRecipe("advanced_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "advanced_arithmetic"),
				new IngredientStack("circuitAdvanced", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("advanced_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "advanced_logic"),
				new IngredientStack("circuitAdvanced", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("advanced_functional_circuits",
				Utils.getItemWithMetaName(CommonProxy.item_circuit, "itemstack"),
				new IngredientStack("circuitAdvanced", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
	}

	public static void addSpringRecipes()
	{
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				Utils.getItemWithMetaName(CommonProxy.item_material_spring, "iron"),
				new IngredientStack("plateIron", 2), new ItemStack(IEContent.itemTool, 1)
		);
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				Utils.getItemWithMetaName(CommonProxy.item_material_spring, "steel"),
				new IngredientStack("plateSteel", 2), new ItemStack(IEContent.itemTool, 1)
		);
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				Utils.getItemWithMetaName(CommonProxy.item_material_spring, "brass"),
				new IngredientStack("plateBrass", 2), new ItemStack(IEContent.itemTool, 1)
		);

	}

	public static void addMiscIERecipes()
	{
		//Cheaper treated planks
		BathingRecipe.addRecipe(new ItemStack(IEContent.blockTreatedWood, 12),
				new IngredientStack("plankWood", 8),
				new FluidStack(IEContent.fluidCreosote, 1000),
				3200, 120
		);

		MetalPressRecipe.addRecipe(new ItemStack(CommonProxy.item_printed_page, 1, 0), new IngredientStack("paper"), new ItemStack(IEContent.itemMold, 1, 0), 600);

		ArcFurnaceRecipe.addRecipe(
				Utils.getItemWithMetaName(CommonProxy.item_material, "white_phosphorus", 4),
				new IngredientStack("dustPhosphorus", 4),
				ItemStack.EMPTY,
				400, 512,
				"dustCoke"
		);
	}

	//Laggy, weird and wrong, but kinda universal
	public static void addWoodTableSawRecipes(RegistryEvent.Register<IRecipe> event)
	{

		CraftingManager.REGISTRY.forEach(iRecipe ->
				{
					if(Objects.equals(iRecipe.getGroup(), "planks"))
					{
						ImmersiveIntelligence.logger.info("recipe for:"+iRecipe.getRecipeOutput());

						ItemStack out = iRecipe.getRecipeOutput().copy();
						out.setCount(Math.round(out.getCount()*1.5f));
						SawmillRecipe.addRecipe(out, new IngredientStack(ListUtils.fromItems(iRecipe.getIngredients().get(0).getMatchingStacks())).setUseNBT(false), Utils.getItemWithMetaName(CommonProxy.item_material, "dust_wood"), 16, 200, 1);
						//ImmersiveIntelligence.logger.info("Added recipe for "+stack.getDisplayName()+" x"+stack.getCount()+" -> "+out.getDisplayName()+" x"+out.getCount());
					}
				}
		);
		//Planks to sticks
		SawmillRecipe.addRecipe(new ItemStack(Items.STICK, 3), new IngredientStack("plankWood"), Utils.getItemWithMetaName(CommonProxy.item_material, "dust_wood"), 8, 200, 1);
		SawmillRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 1, 0), new IngredientStack("plankTreatedWood"), Utils.getItemWithMetaName(CommonProxy.item_material, "dust_wood"), 8, 200, 1);

		BottlingMachineRecipe.addRecipe(new ItemStack(Items.PAPER, 2), new IngredientStack("dustWood"), new FluidStack(FluidRegistry.WATER, 250));
	}

	public static void addRotaryPowerRecipes()
	{

	}

	public static void addMinecartRecipes(IForgeRegistry<IRecipe> registry)
	{
		registry.register(new RecipeMinecart(new ItemStack(CommonProxy.item_minecart, 1, 0), new ItemStack(IEContent.blockWoodenDevice0, 1, 0)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_wooden_crate"));
		registry.register(new RecipeMinecart(new ItemStack(CommonProxy.item_minecart, 1, 1), new ItemStack(IEContent.blockWoodenDevice0, 1, 5)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_reinforced_crate"));
		registry.register(new RecipeMinecart(new ItemStack(CommonProxy.item_minecart, 1, 2), new ItemStack(CommonProxy.block_metal_device, 1, 0)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_steel_crate"));

		registry.register(new RecipeMinecart(new ItemStack(CommonProxy.item_minecart, 1, 3), new ItemStack(IEContent.blockWoodenDevice0, 1, 1)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_wooden_barrel"));
		registry.register(new RecipeMinecart(new ItemStack(CommonProxy.item_minecart, 1, 4), new ItemStack(IEContent.blockMetalDevice0, 1, 4)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_metal_barrel"));
	}

	public static void addSmallCrateRecipes(IForgeRegistry<IRecipe> registry)
	{
		registry.register(new RecipeCrateConversion(new ItemStack(IEContent.blockWoodenDevice0, 1, 0),
				getSmallCrate(IIBlockTypes_SmallCrate.WOODEN_CRATE_BOX),
				getSmallCrate(IIBlockTypes_SmallCrate.WOODEN_CRATE_CUBE),
				getSmallCrate(IIBlockTypes_SmallCrate.WOODEN_CRATE_WIDE)
		).setRegistryName(ImmersiveIntelligence.MODID, "small_crate_wooden"));

		registry.register(new RecipeCrateConversion(new ItemStack(CommonProxy.block_metal_device, 1, 0),
				getSmallCrate(IIBlockTypes_SmallCrate.METAL_CRATE_BOX),
				getSmallCrate(IIBlockTypes_SmallCrate.METAL_CRATE_CUBE),
				getSmallCrate(IIBlockTypes_SmallCrate.METAL_CRATE_WIDE)
		).setRegistryName(ImmersiveIntelligence.MODID, "small_crate_metal"));

	}

	public static ItemStack getSmallCrate(IIBlockTypes_SmallCrate e)
	{
		return new ItemStack(CommonProxy.block_small_crate, 1, e.getMeta());
	}

	public static void addRDXProductionRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_methanol, 500), new FluidStack(CommonProxy.gas_hydrogen, 1000), new IngredientStack[]{new IngredientStack("dustNickel")}, 3200);
		MixerRecipe.addRecipe(new FluidStack(CommonProxy.fluid_methanol, 2000), new FluidStack(CommonProxy.gas_hydrogen, 3000), new IngredientStack[]{new IngredientStack("dustPlatinum")}, 4800);

		FermenterRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ammonia, 120), ItemStack.EMPTY, new IngredientStack("listAllMeatRaw"), 1600);
		FermenterRecipe.addRecipe(new FluidStack(CommonProxy.fluid_ammonia, 160), ItemStack.EMPTY, new ItemStack(Items.ROTTEN_FLESH), 1000);

		BottlingMachineRecipe.addRecipe(Utils.getItemWithMetaName(CommonProxy.item_material, "dust_formaldehyde", 2), new IngredientStack("nuggetSilver"), new FluidStack(CommonProxy.fluid_methanol, 1000));

		BottlingMachineRecipe.addRecipe(Utils.getItemWithMetaName(CommonProxy.item_material, "dust_hexamine", 1), new IngredientStack("dustFormaldehyde"), new FluidStack(CommonProxy.fluid_ammonia, 320));
	}

	public static void addHMXProductionRecipes()
	{
		BottlingMachineRecipe.addRecipe(Utils.getItemWithMetaName(CommonProxy.item_material, "dust_hmx", 1), new IngredientStack("materialHexogen"), new FluidStack(CommonProxy.fluid_nitric_acid, 500));
	}

}
