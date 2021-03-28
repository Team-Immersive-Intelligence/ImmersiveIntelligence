package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistry;
import pl.pabilo8.immersiveintelligence.Config.IIConfig;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_SmallCrate;
import pl.pabilo8.immersiveintelligence.common.crafting.RecipeCrateConversion;
import pl.pabilo8.immersiveintelligence.common.crafting.RecipeMinecart;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22-03-2020
 */
public class IIRecipes
{
	public static void addCircuitRecipes()
	{
		//Allow me to introduce you to Immersive Gregineering™

		if(IIConfig.changeCircuitProduction)
		{
			//Remove old circuit recipe
			ItemStack circuitboard = new ItemStack(IEContent.itemMaterial, 1, 27);
			BlueprintCraftingRecipe.recipeList.get("components").removeIf(blueprintCraftingRecipe -> blueprintCraftingRecipe.output.isItemEqual(circuitboard));

			BlueprintCraftingRecipe.addRecipe("basic_circuits",
					new ItemStack(IIContent.itemMaterial, 1,
							IIContent.itemMaterial.getMetaBySubname("basic_circuit_board_raw")), new ItemStack(IEContent.blockStoneDecoration, 2, BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta()), "plateCopper");
			BlueprintCraftingRecipe.addRecipe("basic_circuits", new ItemStack(IEContent.itemMaterial, 1, 27), "circuitBasicEtched", new IngredientStack("chipBasic", 2));

			BlueprintCraftingRecipe.addRecipe("advanced_circuits",
					new ItemStack(IIContent.itemMaterial, 1,
							IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board_raw")), new IngredientStack("circuitBasicRaw", 2), "plateAdvancedElectronicAlloy");
			BlueprintCraftingRecipe.addRecipe("advanced_circuits",
					new ItemStack(IIContent.itemMaterial, 1,
							IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board")), "circuitAdvancedEtched", new IngredientStack("chipAdvanced", 2));

			BlueprintCraftingRecipe.addRecipe("processors",
					new ItemStack(IIContent.itemMaterial, 1,
							IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_raw")), new IngredientStack("circuitAdvancedRaw", 4), new IngredientStack("plateAdvancedElectronicAlloy", 2));
			BlueprintCraftingRecipe.addRecipe("processors",
					new ItemStack(IIContent.itemMaterial, 1,
							IIContent.itemMaterial.getMetaBySubname("processor_circuit_board")), "circuitProcessorEtched", new IngredientStack("chipProcessor", 2));

		}

		//Circuits
		BathingRecipe.addRecipe(new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_circuit_board_etched")), new IngredientStack("circuitBasicRaw"), FluidRegistry.getFluidStack("etching_acid", 1000), 15000, 360);
		BathingRecipe.addRecipe(new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board_etched")), new IngredientStack("circuitAdvancedRaw"), FluidRegistry.getFluidStack("etching_acid", 2000), 150000, 560);
		BathingRecipe.addRecipe(new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_etched")), new IngredientStack("circuitProcessorRaw"), FluidRegistry.getFluidStack("etching_acid", 4000), 1500000, 720);

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
				new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTube"), new IngredientStack("plateNickel"), new IngredientStack("dustRedstone", 4)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				18000,
				1.0f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(IIContent.itemMaterial, 2, IIContent.itemMaterial.getMetaBySubname("advanced_electron_tube")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSteel"), new IngredientStack("wireTungsten", 2), new IngredientStack("electronTube", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "inserter pick second", "inserter drop main", "inserter pick first", "inserter drop main", "solderer work main"},

				24000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTubeAdvanced"), new IngredientStack("chipBasic")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				32000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				Utils.getStackWithMetaName(IIContent.itemMaterial, "transistor", 4),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSilicon", 2), new IngredientStack("plateAdvancedElectronicAlloy"), new IngredientStack("plateAluminum"), new IngredientStack("dyeBlack")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "solderer work first", "inserter pick first", "drill work main", "inserter drop main", "inserter pick second", "solderer work main", "inserter drop main", "inserter pick third", "inserter drop third", "solderer work main"},

				50000,
				0.9f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_electronic_element")),
				Utils.getStackWithMetaName(IIContent.itemMaterialNugget, "silicon"),

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
				Utils.getStackWithMetaName(IIContent.itemMaterialPlate, "silicon"),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("bouleSilicon", 1)},

				new String[]{"buzzsaw"},
				new String[]{"buzzsaw work main"},

				24000,
				3f
		);

		PrecissionAssemblerRecipe.addRecipe(
				Utils.getStackWithMetaName(IIContent.itemMaterialIngot, "silicon"),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("bouleSilicon", 1)},

				new String[]{"hammer", "buzzsaw"},
				new String[]{"hammer work main", "hammer work main", "buzzsaw work main", "hammer work main"},

				24000,
				1f
		);


		CrusherRecipe.removeRecipesForInput(new ItemStack(Items.QUARTZ));
		CrusherRecipe.addRecipe(
				Utils.getStackWithMetaName(IIContent.itemMaterialDust, "quartz_dirty"),
				new IngredientStack("gemQuartz", 1),
				3200
		);

		BathingRecipe.addRecipe(
				Utils.getStackWithMetaName(IIContent.itemMaterialDust, "quartz"),
				new IngredientStack("dustQuartzDirty"),
				new FluidStack(IIContent.fluidHydrofluoricAcid, 1000),
				4200, 240
		);

		ArcFurnaceRecipe.addRecipe(
				Utils.getStackWithMetaName(IIContent.itemMaterialBoule, "silicon"),
				new IngredientStack("dustQuartz", 6),
				ItemStack.EMPTY,
				400, 512
		);


	}

	public static void addInkRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkCyan, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeCyan"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkYellow, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeYellow"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkMagenta, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeMagenta"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkBlack, 1000), new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeBlack"}, 3200);
	}

	public static void addBulletPressRecipes()
	{
		MetalPressRecipe.addRecipe(new ItemStack(IIContent.itemAmmoArtillery, 1, ItemIIBulletBase.CASING), new IngredientStack("ingotBrass", 4), Utils.getStackWithMetaName(IIContent.itemPressMold, "howitzer"), 2800);
		MetalPressRecipe.addRecipe(new ItemStack(IIContent.itemGrenade, 1, ItemIIBulletBase.CASING), new IngredientStack("nuggetBrass", 5), Utils.getStackWithMetaName(IIContent.itemPressMold, "grenade"), 1800);
		MetalPressRecipe.addRecipe(new ItemStack(IIContent.itemAmmoMachinegun, 3, ItemIIBulletBase.CASING), new IngredientStack("ingotBrass"), Utils.getStackWithMetaName(IIContent.itemPressMold, "machinegun"), 1600);
		MetalPressRecipe.addRecipe(new ItemStack(IIContent.itemAmmoSubmachinegun, 6, ItemIIBulletBase.CASING), new IngredientStack("ingotBrass"), Utils.getStackWithMetaName(IIContent.itemPressMold, "submachinegun"), 1200);

		MetalPressRecipe.removeRecipes(new ItemStack(IEContent.itemBullet, 2, 0));
		MetalPressRecipe.addRecipe(new ItemStack(IEContent.itemBullet, 2, 0), "ingotBrass", new ItemStack(IEContent.itemMold, 1, 3), 2800);


	}

	public static void addFunctionalCircuits()
	{
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "arithmetic"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "logic"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "comparator"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("basic_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "text"),
				new IngredientStack("circuitBasic", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);

		BlueprintCraftingRecipe.addRecipe("advanced_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "advanced_arithmetic"),
				new IngredientStack("circuitAdvanced", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("advanced_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "advanced_logic"),
				new IngredientStack("circuitAdvanced", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
		BlueprintCraftingRecipe.addRecipe("advanced_functional_circuits",
				Utils.getStackWithMetaName(IIContent.itemCircuit, "itemstack"),
				new IngredientStack("circuitAdvanced", 2), new ItemStack(IEContent.itemTool, 1, 1)
		);
	}

	public static void addSpringRecipes()
	{
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				Utils.getStackWithMetaName(IIContent.itemMaterialSpring, "iron", 2),
				new IngredientStack("plateIron", 1), new ItemStack(IEContent.itemTool, 1)
		);
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				Utils.getStackWithMetaName(IIContent.itemMaterialSpring, "steel", 2),
				new IngredientStack("plateSteel", 1), new ItemStack(IEContent.itemTool, 1)
		);
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				Utils.getStackWithMetaName(IIContent.itemMaterialSpring, "brass", 2),
				new IngredientStack("plateBrass", 1), new ItemStack(IEContent.itemTool, 1)
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

		MetalPressRecipe.addRecipe(new ItemStack(IIContent.itemPrintedPage, 1, 0), new IngredientStack("paper"), new ItemStack(IEContent.itemMold, 1, 0), 600);

		ArcFurnaceRecipe.addRecipe(
				Utils.getStackWithMetaName(IIContent.itemMaterial, "white_phosphorus", 4),
				new IngredientStack("dustPhosphorus", 4),
				ItemStack.EMPTY,
				400, 512,
				"dustCoke"
		);
	}

	//Laggy, weird and wrong, but kinda universal
	public static void addWoodTableSawRecipes()
	{

		CraftingManager.REGISTRY.forEach(iRecipe ->
				{
					if(blusunrize.immersiveengineering.common.util.Utils.compareToOreName(iRecipe.getRecipeOutput(), "plankWood"))
					{
						ItemStack out = iRecipe.getRecipeOutput().copy();
						out.setCount(Math.round(out.getCount()*1.5f));
						List<ItemStack> stacks = new ArrayList<>();
						for(Ingredient ingredient : iRecipe.getIngredients())
							Arrays.stream(ingredient.getMatchingStacks()).filter(stack -> blusunrize.immersiveengineering.common.util.Utils.compareToOreName(stack, "logWood")).forEachOrdered(stacks::add);

						if(!stacks.isEmpty())
						{
							SawmillRecipe.addRecipe(out, new IngredientStack(stacks).setUseNBT(false), Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_wood"), 16, 200, 1);

						}

						//ImmersiveIntelligence.logger.info("Added recipe for "+stack.getDisplayName()+" x"+stack.getCount()+" -> "+out.getDisplayName()+" x"+out.getCount());
					}
				}
		);
		//Crusher, logs to sawdust
		CrusherRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_wood", 2), new IngredientStack("logWood"), 4096);
		CrusherRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_wood", 1), new IngredientStack("plankWood", 2), 3192);

		//Planks to sticks
		SawmillRecipe.addRecipe(new ItemStack(Items.STICK, 3), new IngredientStack("plankWood"), Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_wood"), 8, 100, 1);
		SawmillRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 3, 0), new IngredientStack("plankTreatedWood"), Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_wood"), 8, 100, 1);

		BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "pulp_wood"), new IngredientStack("dustWood"), new FluidStack(FluidRegistry.WATER, 250));
		BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "pulp_wood_treated", 1), new IngredientStack("pulpWood"), new FluidStack(IEContent.fluidCreosote, 1000));

		MetalPressRecipe.addRecipe(new ItemStack(Items.PAPER, 2, 0), new IngredientStack("pulpWood"), new ItemStack(IEContent.itemMold, 1, 0), 100);
		MetalPressRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "artificial_leather"), new IngredientStack("pulpWoodTreated"), new ItemStack(IEContent.itemMold, 1, 0), 600);

	}

	public static void addRotaryPowerRecipes()
	{

	}

	public static void addMinecartRecipes(IForgeRegistry<IRecipe> registry)
	{
		registry.register(new RecipeMinecart(new ItemStack(IIContent.itemMinecart, 1, 0), new ItemStack(IEContent.blockWoodenDevice0, 1, 0)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_wooden_crate"));
		registry.register(new RecipeMinecart(new ItemStack(IIContent.itemMinecart, 1, 1), new ItemStack(IEContent.blockWoodenDevice0, 1, 5)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_reinforced_crate"));
		registry.register(new RecipeMinecart(new ItemStack(IIContent.itemMinecart, 1, 2), new ItemStack(IIContent.blockMetalDevice, 1, 0)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_steel_crate"));

		registry.register(new RecipeMinecart(new ItemStack(IIContent.itemMinecart, 1, 3), new ItemStack(IEContent.blockWoodenDevice0, 1, 1)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_wooden_barrel"));
		registry.register(new RecipeMinecart(new ItemStack(IIContent.itemMinecart, 1, 4), new ItemStack(IEContent.blockMetalDevice0, 1, 4)).setRegistryName(ImmersiveIntelligence.MODID, "minecart_metal_barrel"));
	}

	public static void addSmallCrateRecipes(IForgeRegistry<IRecipe> registry)
	{
		registry.register(new RecipeCrateConversion(new ItemStack(IEContent.blockWoodenDevice0, 1, 0),
				getSmallCrate(IIBlockTypes_SmallCrate.WOODEN_CRATE_BOX),
				getSmallCrate(IIBlockTypes_SmallCrate.WOODEN_CRATE_CUBE),
				getSmallCrate(IIBlockTypes_SmallCrate.WOODEN_CRATE_WIDE)
		).setRegistryName(ImmersiveIntelligence.MODID, "small_crate_wooden"));

		registry.register(new RecipeCrateConversion(new ItemStack(IIContent.blockMetalDevice, 1, 0),
				getSmallCrate(IIBlockTypes_SmallCrate.METAL_CRATE_BOX),
				getSmallCrate(IIBlockTypes_SmallCrate.METAL_CRATE_CUBE),
				getSmallCrate(IIBlockTypes_SmallCrate.METAL_CRATE_WIDE)
		).setRegistryName(ImmersiveIntelligence.MODID, "small_crate_metal"));

	}

	public static ItemStack getSmallCrate(IIBlockTypes_SmallCrate e)
	{
		return new ItemStack(IIContent.blockSmallCrate, 1, e.getMeta());
	}

	public static void addRDXProductionRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidMethanol, 500), new FluidStack(IIContent.gasHydrogen, 1000), new IngredientStack[]{new IngredientStack("dustNickel")}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidMethanol, 2000), new FluidStack(IIContent.gasHydrogen, 3000), new IngredientStack[]{new IngredientStack("dustPlatinum")}, 4800);

		FermenterRecipe.addRecipe(new FluidStack(IIContent.fluidAmmonia, 120), ItemStack.EMPTY, new IngredientStack("listAllMeatRaw"), 1600);
		FermenterRecipe.addRecipe(new FluidStack(IIContent.fluidAmmonia, 160), ItemStack.EMPTY, new ItemStack(Items.ROTTEN_FLESH), 1000);

		BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_formaldehyde", 2), new IngredientStack("nuggetSilver"), new FluidStack(IIContent.fluidMethanol, 1000));

		BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_hexamine", 1), new IngredientStack("dustFormaldehyde"), new FluidStack(IIContent.fluidAmmonia, 320));
	}

	public static void addHMXProductionRecipes()
	{
		BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "dust_hmx", 1), new IngredientStack("materialHexogen"), new FluidStack(IIContent.fluidNitricAcid, 500));
	}

	public static void addConcreteRecipes()
	{
		//Concrete Bricks / Volksbeton
		BathingRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.blockConcreteDecoration, "concrete_bricks"), new IngredientStack(new ItemStack(Blocks.BRICK_BLOCK)), new FluidStack(FluidRegistry.getFluid("concrete"), 500), 1600, 120);
		//Panzerconcrete / Panzerbeton
		ArcFurnaceRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.blockConcreteDecoration, "sturdy_concrete_bricks"), Utils.getStackWithMetaName(IIContent.blockConcreteDecoration, "concrete_bricks"), ItemStack.EMPTY, 200, 2400, new IngredientStack("stickSteel", 2));
		//Uberconcrete / Uberbeton
		ArcFurnaceRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.blockConcreteDecoration, "uberconcrete", 2), Utils.getStackWithMetaName(IIContent.blockConcreteDecoration, "sturdy_concrete_bricks"), ItemStack.EMPTY, 200, 3600, new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta()));

	}

	public static void addChemicalBathCleaningRecipes()
	{
		String[] dyes =
				{
						"Black",
						"Red",
						"Green",
						"Brown",
						"Blue",
						"Purple",
						"Cyan",
						"LightGray",
						"Gray",
						"Pink",
						"Lime",
						"Yellow",
						"LightBlue",
						"Magenta",
						"Orange",
						"White"
				};

		for(String dye : dyes)
		{
			addBathingCleaningRecipe(
					new ItemStack(Blocks.WOOL, 1),
					new IngredientStack("wool"+dye),
					1000, 1024, 200, true, false, false
			);

			addBathingCleaningRecipe(
					new ItemStack(Blocks.GLASS, 1),
					new IngredientStack("blockGlass"+dye),
					1000, 1024, 200, true
			);

			addBathingCleaningRecipe(
					new ItemStack(Blocks.GLASS_PANE, 1),
					new IngredientStack("paneGlass"+dye),
					1000, 1024, 200, true
			);
		}

		for(int i = 0; i < 16; i += 1)
		{
			if(i!=0)
			{
				addBathingCleaningRecipe(
						new ItemStack(Blocks.BED, 1),
						new IngredientStack(new ItemStack(Blocks.BED, 1, i)),
						2000,
						3096, 240, true, false, false
				);

				addBathingCleaningRecipe(
						new ItemStack(Blocks.CARPET, 1),
						new IngredientStack(new ItemStack(Blocks.CARPET, 1, i)),
						2000,
						3096, 240, true, false, false
				);
			}
			addBathingCleaningRecipe(
					new ItemStack(Blocks.HARDENED_CLAY, 1),
					new IngredientStack(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i)),
					2000,
					1440, 280, false, true, true
			);
		}

		Block[] shulker_boxes = new Block[]{
				Blocks.ORANGE_SHULKER_BOX,
				Blocks.MAGENTA_SHULKER_BOX,
				Blocks.LIGHT_BLUE_SHULKER_BOX,
				Blocks.YELLOW_SHULKER_BOX,
				Blocks.LIME_SHULKER_BOX,
				Blocks.PINK_SHULKER_BOX,
				Blocks.GRAY_SHULKER_BOX,
				Blocks.SILVER_SHULKER_BOX,
				Blocks.CYAN_SHULKER_BOX,
				Blocks.BLUE_SHULKER_BOX,
				Blocks.BROWN_SHULKER_BOX,
				Blocks.GREEN_SHULKER_BOX,
				Blocks.RED_SHULKER_BOX
		};

		for(Block shulker_box : shulker_boxes)
		{
			addBathingCleaningRecipe(
					new ItemStack(Blocks.WHITE_SHULKER_BOX, 1),
					new IngredientStack(new ItemStack(shulker_box, 1)),
					2000,
					4096, 120, false, true, true
			);
		}

		addBathingCleaningRecipe(
				new ItemStack(Items.BANNER, 1),
				new IngredientStack(new ItemStack(Items.BANNER, 1)),
				2000,
				1024, 160, true, false, false
		);


	}


	private static void addBathingCleaningRecipe(ItemStack out, IngredientStack in, int amount, int energy, int time, boolean allowHFl)
	{
		addBathingCleaningRecipe(out, in, amount, energy, time, true, true, allowHFl);
	}

	private static void addBathingCleaningRecipe(ItemStack out, IngredientStack in, int amount, int energy, int time, boolean allowWater, boolean allowSulfuric, boolean allowHFl)
	{
		if(allowWater)
			BathingRecipe.addRecipe(out, in, new FluidStack(FluidRegistry.WATER, amount), energy, time);
		if(allowSulfuric)
			BathingRecipe.addRecipe(out, in, new FluidStack(IIContent.fluidSulfuricAcid, allowWater?amount/2: amount), allowWater?energy/2: energy, allowWater?time/2: time);
		if(allowHFl)
			BathingRecipe.addRecipe(out, in, new FluidStack(IIContent.fluidHydrofluoricAcid, amount/2), allowWater?energy/4: energy/2, allowWater?time/4: time/2);
	}

	public static void addUpgradeRecipes()
	{
		IIContent.UPGRADE_INSERTER
				.addStack(new IngredientStack(Utils.getStackWithMetaName(IIContent.itemPrecissionTool, "precission_inserter")))
				.addStack(new IngredientStack("scaffoldingSteel"))
				.setRequiredProgress(10000);

		IIContent.UPGRADE_SAW_UNREGULATOR
				.addStack(new IngredientStack(Utils.getStackWithMetaName(IIContent.itemMotorGear, "steel")))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 1, 8)))
				.addStack(new IngredientStack("stickSteel", 2))
				.setRequiredProgress(20000);

		IIContent.UPGRADE_IMPROVED_GEARBOX
				.addStack(new IngredientStack(Utils.getStackWithMetaName(IIContent.itemMotorGear, "tungsten", 2)))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 1, 9)))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())))
				.setRequiredProgress(20000);

		//Weapons - Basic Tier

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER
				.addStack(new IngredientStack("blockGlassRed",1))
				.addStack(new IngredientStack("blockGlass",1))
				.addStack(new IngredientStack("blockSteel",2))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())))
				.addStack(new IngredientStack("circuitBasic",4))
				.setRequiredProgress(40000);

		//Weapons - Basic Tier

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 4, 14)))
				.addStack(new IngredientStack("blockSteel",2))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 3, 10)))
				.addStack(new IngredientStack("circuitAdvanced",4))
				.setRequiredProgress(80000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.FLUID_PLACER.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice1, 4, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 2, 14)))
				.addStack(new IngredientStack("blockSteel",1))
				.addStack(new IngredientStack("circuitAdvanced",4))
				.setRequiredProgress(80000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.CAPACITOR_HV.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta())))
				.addStack(new IngredientStack("blockSteel",1))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 2, 10)))
				.addStack(new IngredientStack("circuitAdvanced",4))
				.setRequiredProgress(80000);

		//Weapons - Processor Tier

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_CPDS
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 8, 14)))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 2, 10)))
				.addStack(new IngredientStack("blockSteel",3))
				.addStack(new IngredientStack("circuitProcessor",4))
				.setRequiredProgress(300000);
	}

}
