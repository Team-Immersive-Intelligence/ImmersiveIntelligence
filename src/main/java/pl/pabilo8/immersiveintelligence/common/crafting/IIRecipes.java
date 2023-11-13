package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.crafting.RecipeRGBColouration;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.PackerHandler;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.crafting.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.IIBlockTypes_Mine;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIConcreteDecoration.ConcreteDecorations;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIOre.Ores;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIISmallCrate.IIBlockTypes_SmallCrate;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIMinecart.Minecarts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMetalPressMold.PressMolds;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIPrecisionTool.PrecisionTools;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIVulcanizerMold.VulcanizerMolds;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialBoule.MaterialsBoule;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialDust.MaterialsDust;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialIngot.MaterialsIngot;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialNugget.MaterialsNugget;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialPlate.MaterialsPlate;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialSpring.MaterialsSpring;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorGear.MotorGear;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIAdvancedPowerPack;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 22-03-2020
 */
public class IIRecipes
{
	public static ItemStack BASIC_CIRCUIT, TOOL_HAMMER, TOOL_CUTTERS;
	public static IngredientStack AMMO_CASINGS;

	public static void doRecipes(IForgeRegistry<IRecipe> recipeRegistry)
	{
		//--- Setup Items ---//
		BASIC_CIRCUIT = new ItemStack(IEContent.itemMaterial, 1, 27);
		TOOL_HAMMER = new ItemStack(IEContent.itemTool, 1, 0);
		TOOL_CUTTERS = new ItemStack(IEContent.itemTool, 1, 1);

		//Used by ammo pouch
		AMMO_CASINGS = new IngredientStack(AmmoRegistry.INSTANCE.registeredBulletItems.values()
				.stream()
				.map(iAmmo -> iAmmo.getCasingStack(1))
				.collect(Collectors.toList())
		);

		//--- Add Recipes ---//
		addMinecartRecipes(recipeRegistry);
		addSmallCrateRecipes(recipeRegistry);

		addMetalPressRecipes();
		addBulletPressRecipes();
		addFillerRecipes();

		addSiliconProcessingRecipes();
		addCircuitRecipes();

		addFunctionalCircuits();
		addSpringRecipes();
		addHandWeaponRecipes(recipeRegistry);
		addMiscIERecipes();

		addWoodTableSawRecipes();
		addRotaryPowerRecipes();
		addUpgradeRecipes();

		addRDXProductionRecipes();
		addHMXProductionRecipes();

		addConcreteRecipes();
		addChemicalBathCleaningRecipes();

		addColouringRecipes(recipeRegistry);
		addChemicalPainterRecipes();

		addPackerHandling();

		addElectrolyzerRecipes();
		addInkRecipes();

		addSmeltingRecipes();
		addArcFurnaceRecyclingRecipes();

		addAmmunitionCasingRecipes();
		addRubberRecipes();
		addDuraluminiumRecipes();

		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidEtchingAcid, 1000),
				new FluidStack(IIContent.gasChlorine, 500), new Object[]{"dustIron"}, 4800);

		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidSulfuricAcid, 500),
				new FluidStack(FluidRegistry.WATER, 1000), new Object[]{"dustSulfur"}, 4800);

		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidHydrofluoricAcid, 500),
				new FluidStack(IIContent.fluidSulfuricAcid, 1000), new Object[]{"dustFluorite"}, 5600);

		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidNitricAcid, 250),
				new FluidStack(IIContent.fluidSulfuricAcid, 1000), new Object[]{"dustSaltpeter"}, 5600);

		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidBrine, 750),
				new FluidStack(FluidRegistry.WATER, 750), new Object[]{"dustSalt"}, 3200);


	}

	private static void addElectrolyzerRecipes()
	{
		//Immersive Engineering can into space???
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("water", 750),
				FluidRegistry.getFluidStack("oxygen", 250),
				FluidRegistry.getFluidStack("hydrogen", 500),
				160, 80);
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("brine", 750),
				FluidRegistry.getFluidStack("chlorine", 375),
				FluidRegistry.getFluidStack("hydrogen", 375),
				160, 80);

		//Why Realism when you have Immersiveness ^^
		ElectrolyzerRecipe.addRecipe(new FluidStack(IIContent.gasCO2, 750), new FluidStack(IIContent.gasCO, 500),
				new FluidStack(IIContent.gasOxygen, 250), 160, 160);
		RefineryRecipe.addRecipe(new FluidStack(IIContent.fluidFormicAcid, 16), new FluidStack(IIContent.fluidMethanol, 8),
				new FluidStack(IIContent.gasCO, 8), 65);

	}

	private static void addColouringRecipes(IForgeRegistry<IRecipe> registry)
	{
		addColoringRecipe(registry, IIContent.itemTracerPowder, 0, "colour", "tracer_powder_colour");
		addColoringRecipe(registry, IIContent.itemTracerPowder, 1, "colour", "flare_powder_colour");

		addColoringRecipe(registry, IIContent.itemAdvancedPowerPack, -1,
				ItemIIAdvancedPowerPack.NBT_Colour, "advanced_powerpack_coloring");

		addColoringRecipe(registry, IIContent.itemLightEngineerHelmet, -1,
				ItemIIUpgradeableArmor.NBT_Colour, "light_engineer_armor_helmet_coloring");
		addColoringRecipe(registry, IIContent.itemLightEngineerChestplate, -1,
				ItemIIUpgradeableArmor.NBT_Colour, "light_engineer_armor_chestplate_coloring");
		addColoringRecipe(registry, IIContent.itemLightEngineerLeggings, -1,
				ItemIIUpgradeableArmor.NBT_Colour, "light_engineer_armor_leggings_coloring");
		addColoringRecipe(registry, IIContent.itemLightEngineerBoots, -1,
				ItemIIUpgradeableArmor.NBT_Colour, "light_engineer_armor_boots_coloring");
	}

	private static void addColoringRecipe(IForgeRegistry<IRecipe> registry, Item item, int meta, String colorTag, String recipeName)
	{
		registry.register(new RecipeRGBColouration((s) ->
				(OreDictionary.itemMatches(new ItemStack(item, 1, meta==-1?0: meta), s, meta!=-1)),
				(s) -> (ItemNBTHelper.hasKey(s, colorTag)?ItemNBTHelper.getInt(s, colorTag): 0xffffff),
				(s, i) -> ItemNBTHelper.setInt(s, colorTag, i)).setRegistryName(ImmersiveIntelligence.MODID, recipeName));
	}

	public static void addCircuitRecipes()
	{
		//Allow me to introduce you to Immersive Gregineering™

		if(IIConfig.changeCircuitProduction)
		{
			BlueprintCraftingRecipe.recipeList.get("components").removeIf(blueprintCraftingRecipe -> blueprintCraftingRecipe.output.isItemEqual(BASIC_CIRCUIT));

			BlueprintCraftingRecipe.addRecipe("basic_circuits",
					IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_RAW, 3),
					new ItemStack(IEContent.blockStoneDecoration, 2, BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta()),
					"plateCopper"
			);

			/*BlueprintCraftingRecipe.addRecipe("basic_circuits",
					IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_RAW, 3),
					new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta()),
					"plateCopper",
					"templateCircuit"
			);*/

			BlueprintCraftingRecipe.addRecipe("basic_circuits", BASIC_CIRCUIT,
					"circuitBasicEtched",
					new IngredientStack("chipBasic", 2)
			);

			BlueprintCraftingRecipe.addRecipe("advanced_circuits",
					IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_RAW),
					new IngredientStack("circuitBasicRaw", 2),
					"plateAdvancedElectronicAlloy"
			);
			BlueprintCraftingRecipe.addRecipe("advanced_circuits",
					IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD),
					"circuitAdvancedEtched",
					new IngredientStack("chipAdvanced", 2)
			);

			BlueprintCraftingRecipe.addRecipe("advanced_circuits",
					IIContent.itemMaterial.getStack(Materials.CRYPTOGRAPHIC_CIRCUIT_BOARD),
					"circuitAdvanced",
					new ItemStack(IEContent.itemMaterial, 2, 9));

			BlueprintCraftingRecipe.addRecipe("processors",
					IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_RAW),
					new IngredientStack("circuitAdvancedRaw", 2),
					new IngredientStack("plateAdvancedElectronicAlloy", 1)
			);
			BlueprintCraftingRecipe.addRecipe("processors",
					IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD),
					"circuitProcessorEtched",
					new IngredientStack("chipProcessor", 2)
			);

		}

		//Circuits
		BathingRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_ETCHED), new IngredientStack("circuitBasicRaw"), FluidRegistry.getFluidStack("etching_acid", 500), 15000, 360);
		BathingRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_ETCHED), new IngredientStack("circuitAdvancedRaw"), FluidRegistry.getFluidStack("etching_acid", 1000), 150000, 560);
		BathingRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_ETCHED), new IngredientStack("circuitProcessorRaw"), FluidRegistry.getFluidStack("etching_acid", 2000), 1500000, 720);

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
				IIContent.itemMaterial.getStack(Materials.BASIC_ELECTRONIC_ELEMENT),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTube"), new IngredientStack("plateNickel"), new IngredientStack("dustRedstone", 4)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				18000,
				1.0f
		);

		PrecissionAssemblerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRON_TUBE),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSteel"), new IngredientStack("wireTungsten", 2), new IngredientStack("electronTube", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "inserter pick second", "inserter drop main", "inserter pick first", "inserter drop main", "solderer work main"},

				24000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRONIC_ELEMENT),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTubeAdvanced"), new IngredientStack("chipBasic")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				32000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.TRANSISTOR, 4),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSilicon", 2), new IngredientStack("plateAdvancedElectronicAlloy"), new IngredientStack("plateAluminum"), new IngredientStack("dyeBlack")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "solderer work first", "inserter pick first", "drill work main", "inserter drop main", "inserter pick second", "solderer work main", "inserter drop main", "inserter pick third", "inserter drop third", "solderer work main"},

				50000,
				0.9f
		);

		PrecissionAssemblerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.PROCESSOR_ELECTRONIC_ELEMENT),
				IIContent.itemMaterialNugget.getStack(MaterialsNugget.SILICON),

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
				IIContent.itemMaterialPlate.getStack(MaterialsPlate.SILICON),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("bouleSilicon")},

				new String[]{"buzzsaw"},
				new String[]{"buzzsaw work main"},

				24000,
				3f
		);

		PrecissionAssemblerRecipe.addRecipe(
				IIContent.itemMaterialIngot.getStack(MaterialsIngot.SILICON),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("bouleSilicon")},

				new String[]{"hammer", "buzzsaw"},
				new String[]{"hammer work main", "hammer work main", "buzzsaw work main", "hammer work main"},

				24000,
				1f
		);


		CrusherRecipe.removeRecipesForInput(new ItemStack(Items.QUARTZ));
		CrusherRecipe.addRecipe(
				IIContent.itemMaterialDust.getStack(MaterialsDust.QUARTZ_DIRTY),
				new IngredientStack("gemQuartz"),
				3200
		);

		BathingRecipe.addRecipe(
				IIContent.itemMaterialDust.getStack(MaterialsDust.QUARTZ),
				new IngredientStack("dustQuartzDirty"),
				new FluidStack(IIContent.fluidHydrofluoricAcid, 1000),
				4200, 240
		);

		ArcFurnaceRecipe.addRecipe(
				IIContent.itemMaterialBoule.getStack(MaterialsBoule.SILICON),
				new IngredientStack("dustQuartz", 6),
				ItemStack.EMPTY,
				400, 512
		);
	}

	public static void addInkRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkCyan, 1000),
				new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeCyan"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkYellow, 1000),
				new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeYellow"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkMagenta, 1000),
				new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeMagenta"}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidInkBlack, 1000),
				new FluidStack(FluidRegistry.WATER, 500), new Object[]{"dyeBlack"}, 3200);
	}

	public static void addBulletPressRecipes()
	{

		addMetalPressBullet(Casings.ARTILLERY_8BCAL,
				new IngredientStack("plateBrass", 4),
				PressMolds.HOWITZER, 2800);
		addMetalPressBullet(Casings.MORTAR_6BCAL,
				new IngredientStack("plateAluminum", 3),
				PressMolds.MORTAR, 2500);
		addMetalPressBullet(Casings.LIGHT_ARTILLERY_6BCAL,
				new IngredientStack("plateBrass", 3),
				PressMolds.LIGHT_HOWITZER, 2200);

		addMetalPressBullet(Casings.AUTOCANNON_3BCAL,
				new IngredientStack("ingotBrass", 2),
				PressMolds.AUTOCANNON, 1600);

		addMetalPressBullet(Casings.MG_2BCAL,
				new IngredientStack("ingotBrass"),
				PressMolds.MACHINEGUN, 1600);
		addMetalPressBullet(Casings.STG_1BCAL,
				new IngredientStack("ingotBrass"),
				PressMolds.ASSAULT_RIFLE, 1400);
		addMetalPressBullet(Casings.SMG_1BCAL,
				new IngredientStack("ingotBrass"),
				PressMolds.SUBMACHINEGUN, 1200);

		addMetalPressBullet(Casings.NAVAL_MINE,
				new IngredientStack("plateSteel", 4),
				PressMolds.NAVAL_MINE, 4800);
		addMetalPressBullet(Casings.TRIPMINE,
				new IngredientStack("plateBrass", 2),
				PressMolds.TRIPMINE, 3600);
		addMetalPressBullet(Casings.TELLERMINE,
				new IngredientStack("plateBrass", 2),
				PressMolds.TELLERMINE, 3600);

		MetalPressRecipe.removeRecipes(new ItemStack(IEContent.itemBullet, 2, 0));
		MetalPressRecipe.addRecipe(new ItemStack(IEContent.itemBullet, 3, 0),
				"ingotBrass",
				new ItemStack(IEContent.itemMold, 1, 3), 2800);

		//Magazines

		BlueprintCraftingRecipe.addRecipe("bullet_magazines",
				IIContent.itemBulletMagazine.getStack(Magazines.MACHINEGUN),
				new IngredientStack("plateSteel", 2),
				new IngredientStack("springSteel"));

		BlueprintCraftingRecipe.addRecipe("bullet_magazines",
				IIContent.itemBulletMagazine.getStack(Magazines.SUBMACHINEGUN),
				new IngredientStack("plateSteel", 2),
				new IngredientStack("springSteel"));

		BlueprintCraftingRecipe.addRecipe("bullet_magazines",
				IIContent.itemBulletMagazine.getStack(Magazines.ASSAULT_RIFLE),
				new IngredientStack("plateSteel", 2),
				new IngredientStack("springSteel"));

		BlueprintCraftingRecipe.addRecipe("bullet_magazines",
				IIContent.itemBulletMagazine.getStack(Magazines.SUBMACHINEGUN_DRUM),
				new IngredientStack("plateSteel", 5),
				new IngredientStack("springSteel"));

		BlueprintCraftingRecipe.addRecipe("bullet_magazines",
				IIContent.itemBulletMagazine.getStack(Magazines.AUTOCANNON),
				new IngredientStack("plateSteel", 3),
				new IngredientStack("springSteel", 2));

		BlueprintCraftingRecipe.addRecipe("bullet_magazines",
				IIContent.itemBulletMagazine.getStack(Magazines.CPDS_DRUM),
				new IngredientStack("plateSteel", 8),
				new IngredientStack(new ItemStack(IEContent.itemMaterial, 1, 8)),
				new IngredientStack("springSteel", 2),
				new IngredientStack("dyeGreen", 2)
		);
	}

	private static void addMetalPressBullet(Casings casing, IngredientStack stack, PressMolds mold, int energy)
	{
		MetalPressRecipe.addRecipe(IIContent.itemAmmoCasing.getStack(casing),
				stack, IIContent.itemPressMold.getStack(mold), energy);
	}

	public static void addFunctionalCircuits()
	{
		for(Circuits value : Circuits.values())
			BlueprintCraftingRecipe.addRecipe(value.tier.getName()+"_functional_circuits",
					IIContent.itemCircuit.getStack(value),
					new IngredientStack(value.tier.material), TOOL_CUTTERS
			);
	}

	public static void addSpringRecipes()
	{
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				IIContent.itemMaterialSpring.getStack(MaterialsSpring.IRON, 2),
				new IngredientStack("plateIron", 1), TOOL_HAMMER
		);
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				IIContent.itemMaterialSpring.getStack(MaterialsSpring.STEEL, 2),
				new IngredientStack("plateSteel", 1), TOOL_HAMMER
		);
		BlueprintCraftingRecipe.addRecipe("metal_springs",
				IIContent.itemMaterialSpring.getStack(MaterialsSpring.BRASS, 2),
				new IngredientStack("plateBrass", 1), TOOL_HAMMER
		);

	}

	public static void addHandWeaponRecipes(IForgeRegistry<IRecipe> recipeRegistry)
	{
		//IE Revolver Tweaks
		if(IIConfig.changeRevolverProduction)
		{
			//TODO: 15.10.2023 change revolver to use iron instead of steel
		}

		//Crafting Components
		PrecissionAssemblerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.TUNGSTEN_GUN_BARREL),
				ItemStack.EMPTY,
				new IngredientStack[]{
						new IngredientStack("gunbarrelSteel"),
						new IngredientStack("dustTungsten"),
				},
				new String[]{"drill", "inserter", "welder"},
				new String[]{
						"drill work main",
						"inserter pick first", "inserter drop main",
						"welder work main", "drill work main"},
				32000,
				0.9f
		);

		//Industrial Rifle
		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(IIContent.itemRifle),
				ItemStack.EMPTY,
				new IngredientStack[]{
						new IngredientStack("gunstockWood"),
						new IngredientStack("gunbarrelIron", 2),
						new IngredientStack(new ItemStack(IEContent.itemMaterial, 1, 8)),
						new IngredientStack("gunpartBasic"),
				},
				new String[]{"welder", "inserter"},
				new String[]{
						"welder work first", "inserter pick second", "inserter drop third",
						"inserter pick second", "inserter drop third", "welder work third",
						"inserter pick third", "inserter drop main",
				},
				32000,
				1.2f
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
				IIContent.itemMaterial.getStack(Materials.WHITE_PHOSPHORUS, 4),
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
							Arrays.stream(ingredient.getMatchingStacks())
									.filter(stack -> blusunrize.immersiveengineering.common.util.Utils.compareToOreName(stack, "logWood"))
									.forEachOrdered(stacks::add);

						if(!stacks.isEmpty())
							SawmillRecipe.addRecipe(out, new IngredientStack(stacks).setUseNBT(false),
									IIContent.itemMaterial.getStack(Materials.DUST_WOOD),
									Sawmill.torqueMin+2, 200, 1);

						//IILogger.info("Added recipe for "+stack.getDisplayName()+" x"+stack.getCount()+" -> "+out.getDisplayName()+" x"+out.getCount());
					}
				}
		);
		//Crusher, logs to sawdust
		CrusherRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.DUST_WOOD, 2),
				new IngredientStack("logWood"), 4096);
		CrusherRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.DUST_WOOD),
				new IngredientStack("plankWood", 2), 3192);

		//Planks to sticks
		SawmillRecipe.addRecipe(new ItemStack(Items.STICK, 3),
				new IngredientStack("plankWood"),
				IIContent.itemMaterial.getStack(Materials.DUST_WOOD),
				Sawmill.torqueMin, 100, 1);
		SawmillRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 3, 0),
				new IngredientStack("plankTreatedWood"),
				IIContent.itemMaterial.getStack(Materials.DUST_WOOD),
				Sawmill.torqueMin, 100, 1);

		BottlingMachineRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.PULP_WOOD),
				new IngredientStack("dustWood"),
				new FluidStack(FluidRegistry.WATER, 250)
		);
		BottlingMachineRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.PULP_WOOD_TREATED),
				new IngredientStack("pulpWood"),
				new FluidStack(IEContent.fluidCreosote, 1000)
		);

		MetalPressRecipe.addRecipe(new ItemStack(Items.PAPER, 2, 0),
				new IngredientStack("pulpWood"),
				new ItemStack(IEContent.itemMold, 1, 0),
				100);
		MetalPressRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.ARTIFICIAL_LEATHER),
				new IngredientStack("pulpWoodTreated"),
				new ItemStack(IEContent.itemMold, 1, 0),
				600);

		//Charred log to coke dust and sawdust
		CrusherRecipe.addRecipe(new ItemStack(IIContent.itemMaterial, 1, 17), new IngredientStack(new ItemStack(IIContent.blockCharredLog)), 2024)
				.addToSecondaryOutput(IIContent.itemMaterial.getStack(Materials.DUST_WOOD), 1f);

	}

	public static void addRotaryPowerRecipes()
	{

	}

	public static void addMinecartRecipes(IForgeRegistry<IRecipe> registry)
	{
		for(Minecarts value : Minecarts.values())
			registry.register(new RecipeMinecart(IIContent.itemMinecart.getStack(value), value.stack.get())
					.setRegistryName(ImmersiveIntelligence.MODID, "minecart_"+value.getName()));
	}

	public static void addSmallCrateRecipes(IForgeRegistry<IRecipe> registry)
	{
		registry.register(new RecipeCrateConversion(
				new ItemStack(IEContent.blockWoodenDevice0, 1, 0),
				IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.WOODEN_CRATE_BOX),
				IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.WOODEN_CRATE_CUBE),
				IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.WOODEN_CRATE_WIDE)
		).setRegistryName(ImmersiveIntelligence.MODID, "small_crate_wooden"));

		registry.register(new RecipeCrateConversion(
				IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.METAL_CRATE),
				IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.METAL_CRATE_BOX),
				IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.METAL_CRATE_CUBE),
				IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.METAL_CRATE_WIDE)
		).setRegistryName(ImmersiveIntelligence.MODID, "small_crate_metal"));

	}

	public static void addRDXProductionRecipes()
	{
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidMethanol, 500),
				new FluidStack(IIContent.gasHydrogen, 1000),
				new IngredientStack[]{new IngredientStack("dustNickel")}, 3200);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidMethanol, 2000),
				new FluidStack(IIContent.gasHydrogen, 3000),
				new IngredientStack[]{new IngredientStack("dustPlatinum")}, 4800);

		FermenterRecipe.addRecipe(new FluidStack(IIContent.fluidAmmonia, 120), ItemStack.EMPTY,
				new IngredientStack("listAllMeatRaw"), 1600);
		FermenterRecipe.addRecipe(new FluidStack(IIContent.fluidAmmonia, 160), ItemStack.EMPTY,
				new ItemStack(Items.ROTTEN_FLESH), 1000);

		BottlingMachineRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.DUST_FORMALDEHYDE, 2),
				new IngredientStack("nuggetSilver"),
				new FluidStack(IIContent.fluidMethanol, 1000)
		);

		BottlingMachineRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.DUST_HEXAMINE),
				new IngredientStack("dustFormaldehyde"),
				new FluidStack(IIContent.fluidAmmonia, 320)
		);
	}

	public static void addHMXProductionRecipes()
	{
		BottlingMachineRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.DUST_HMX),
				new IngredientStack("materialHexogen"),
				new FluidStack(IIContent.fluidNitricAcid, 500));
	}

	public static void addConcreteRecipes()
	{
		//Concrete Bricks / Volksbeton
		BathingRecipe.addRecipe(IIContent.blockConcreteDecoration.getStack(ConcreteDecorations.CONCRETE_BRICKS),
				new IngredientStack(new ItemStack(Blocks.BRICK_BLOCK)),
				new FluidStack(FluidRegistry.getFluid("concrete"), 500), 1600, 120);
		//Panzerconcrete / Panzerbeton
		ArcFurnaceRecipe.addRecipe(IIContent.blockConcreteDecoration.getStack(ConcreteDecorations.STURDY_CONCRETE_BRICKS),
				IIContent.blockConcreteDecoration.getStack(ConcreteDecorations.CONCRETE_BRICKS), ItemStack.EMPTY, 200, 2400,
				new IngredientStack("stickSteel", 2));
		//Uberconcrete / Uberbeton
		ArcFurnaceRecipe.addRecipe(IIContent.blockConcreteDecoration.getStack(ConcreteDecorations.UBERCONCRETE, 2),
				IIContent.blockConcreteDecoration.getStack(ConcreteDecorations.STURDY_CONCRETE_BRICKS),
				ItemStack.EMPTY, 200, 3600,
				new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta()));

	}

	public static void addFillerRecipes()
	{
		//Sandbag
		FillerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.SANDBAG),
				new IngredientStack("fabricHemp"),
				new DustStack("sand", 50),
				100,
				2000
		);
		FillerRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.SANDBAG),
				new IngredientStack("fabricHemp"),
				new DustStack("gravel", 40),
				80,
				2500
		);
	}

	public static void addChemicalBathCleaningRecipes()
	{
		addBathingCleaningRecipe(
				new ItemStack(Blocks.WOOL, 1),
				new IngredientStack("wool"),
				1000, 1024, 200, true, false, false
		);
		addBathingCleaningRecipe(
				new ItemStack(Blocks.GLASS, 1),
				new IngredientStack("blockGlass"),
				1000, 1024, 200, true
		);
		addBathingCleaningRecipe(
				new ItemStack(Blocks.GLASS_PANE, 1),
				new IngredientStack("paneGlass"),
				1000, 1024, 200, true
		);

		addBathingCleaningRecipe(
				new ItemStack(Blocks.BED, 1),
				new IngredientStack(new ItemStack(Blocks.BED, 1, OreDictionary.WILDCARD_VALUE)),
				2000,
				3096, 240, true, false, false
		);

		addBathingCleaningRecipe(
				new ItemStack(Blocks.CARPET, 1),
				new IngredientStack(new ItemStack(Blocks.CARPET, 1, OreDictionary.WILDCARD_VALUE)),
				2000,
				3096, 240, true, false, false
		);

		addBathingCleaningRecipe(
				new ItemStack(Blocks.HARDENED_CLAY, 1),
				new IngredientStack(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE)),
				2000,
				1440, 280, false, true, true
		);

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

		addBathingCleaningRecipe(
				new ItemStack(Blocks.PURPLE_SHULKER_BOX, 1),
				new IngredientStack(Arrays.stream(shulker_boxes).map(ItemStack::new).collect(Collectors.toList())),
				2000,
				4096, 120, false, true, true
		);

		addBathingCleaningRecipe(
				new ItemStack(Items.BANNER, 1, OreDictionary.WILDCARD_VALUE),
				new IngredientStack(new ItemStack(Items.BANNER, 1, 15)),
				2000,
				1024, 160, true, false, false
		);

	}

	public static void addChemicalPainterRecipes()
	{
		// TODO: 14.10.2021 colored crates 
		// TODO: 14.10.2021 shulker boxes, beds
		// TODO: 14.10.2021 banners

		//Vanilla Blocks

		PaintingRecipe.addRecipe((rgb, stack) -> {
			//get closest approximated dye
			EnumDyeColor dye = IIUtils.getRGBTextFormatting(rgb);
			return new ItemStack(Blocks.WOOL, 1, dye.getMetadata());
		}, new IngredientStack(new ItemStack(Blocks.WOOL)), 512, 240, 125);

		PaintingRecipe.addRecipe((rgb, stack) -> {
			//get closest approximated dye
			EnumDyeColor dye = IIUtils.getRGBTextFormatting(rgb);
			return new ItemStack(Blocks.CARPET, 1, dye.getMetadata());
		}, new IngredientStack(new ItemStack(Blocks.CARPET)), 512, 240, 50);

		PaintingRecipe.addRecipe((rgb, stack) -> {
			//get closest approximated dye
			EnumDyeColor dye = IIUtils.getRGBTextFormatting(rgb);
			return new ItemStack(Blocks.STAINED_GLASS, 1, dye.getMetadata());
		}, new IngredientStack(new ItemStack(Blocks.GLASS)), 512, 240, 125);

		PaintingRecipe.addRecipe((rgb, stack) -> {
			//get closest approximated dye
			EnumDyeColor dye = IIUtils.getRGBTextFormatting(rgb);
			return new ItemStack(Blocks.STAINED_GLASS_PANE, 1, dye.getMetadata());
		}, new IngredientStack(new ItemStack(Blocks.GLASS_PANE)), 512, 240, 125);

		PaintingRecipe.addRecipe((rgb, stack) -> {
			//get closest approximated dye
			EnumDyeColor dye = IIUtils.getRGBTextFormatting(rgb);
			return new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata());
		}, new IngredientStack(new ItemStack(Blocks.HARDENED_CLAY)), 512, 240, 125);

		PaintingRecipe.addRecipe((rgb, stack) -> {
			//get closest approximated dye
			EnumDyeColor dye = IIUtils.getRGBTextFormatting(rgb);
			return new ItemStack(Items.BED, 1, dye.getMetadata());
		}, new IngredientStack(new ItemStack(Items.BED)), 512, 240, 200);

		//II / IE items
		PaintingRecipe.addRecipe((rgb, stack) -> {
			IIContent.itemAdvancedPowerPack.setColor(stack, rgb);
			return stack;
		}, new IngredientStack(new ItemStack(IIContent.itemAdvancedPowerPack)), 8192, 340, 2000);

		PaintingRecipe.addRecipe((rgb, stack) -> {
			Items.LEATHER_HELMET.setColor(stack, rgb);
			return stack;
		}, new IngredientStack(NonNullList.from(ItemStack.EMPTY,
				new ItemStack(Items.LEATHER_HELMET),
				new ItemStack(Items.LEATHER_CHESTPLATE),
				new ItemStack(Items.LEATHER_LEGGINGS),
				new ItemStack(Items.LEATHER_BOOTS)
		)), 8192, 340, 2000);

		// TODO: 15.10.2021 dyable LE armor
		PaintingRecipe.addRecipe((rgb, stack) -> {
			//IIContent.itemLightEngineerChestplate.setColor(stack,rgb);
			return stack;
		}, new IngredientStack(NonNullList.from(ItemStack.EMPTY,
				new ItemStack(IIContent.itemLightEngineerHelmet),
				new ItemStack(IIContent.itemLightEngineerChestplate),
				new ItemStack(IIContent.itemLightEngineerLeggings),
				new ItemStack(IIContent.itemLightEngineerBoots)
		)), 8192, 340, 2000);

		for(IAmmo bullet : AmmoRegistry.INSTANCE.registeredBulletItems.values())
		{
			ItemStack bulletStack = bullet.getBulletWithParams("", "", "");
			//clear nbt
			bulletStack.setTagCompound(new NBTTagCompound());
			PaintingRecipe.addRecipe((rgb, stack) -> {
				ItemStack ret = bullet.setPaintColour(stack, rgb);
				ret.setCount(1);
				return ret;
			}, new IngredientStack(bulletStack).setUseNBT(false), (int)(bullet.getCaliber()*1024), 100+(int)(bullet.getCaliber()*40), 50+(int)(bullet.getCaliber()*25));
		}
	}

	private static void addBathingCleaningRecipe(ItemStack out, IngredientStack in, int amount, int energy, int time, boolean allowHFl)
	{
		addBathingCleaningRecipe(out, in, amount, energy, time, true, true, allowHFl);
	}

	private static void addBathingCleaningRecipe(ItemStack out, IngredientStack in, int amount, int energy, int time, boolean allowWater, boolean allowSulfuric, boolean allowHFl)
	{
		if(allowWater)
			BathingRecipe.addWashingRecipe(out, in, new FluidStack(FluidRegistry.WATER, amount), energy, time);
		if(allowSulfuric)
			BathingRecipe.addWashingRecipe(out, in, new FluidStack(IIContent.fluidSulfuricAcid, allowWater?amount/2: amount), allowWater?energy/2: energy, allowWater?time/2: time);
		if(allowHFl)
			BathingRecipe.addWashingRecipe(out, in, new FluidStack(IIContent.fluidHydrofluoricAcid, amount/2), allowWater?energy/4: energy/2, allowWater?time/4: time/2);
	}

	public static void addUpgradeRecipes()
	{
		//Effect Crates

		IIContent.UPGRADE_INSERTER
				.addStack(new IngredientStack(IIContent.itemPrecissionTool.getStack(PrecisionTools.INSERTER)))
				.addStack(new IngredientStack("scaffoldingSteel"))
				.setRequiredProgress(20000)
				.setRequiredSteps(12);

		IIContent.UPGRADE_MG_LOADER
				.addStack(new IngredientStack("plateSteel", 8))
				.setRequiredProgress(20000)
				.setRequiredSteps(10);

		//Sawmill

		IIContent.UPGRADE_SAW_UNREGULATOR
				.addStack(new IngredientStack(IIContent.itemMotorGear.getStack(MotorGear.STEEL)))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 1, 8)))
				.addStack(new IngredientStack("stickSteel", 2))
				.setRequiredProgress(32000);

		IIContent.UPGRADE_IMPROVED_GEARBOX
				.addStack(new IngredientStack(IIContent.itemMotorGear.getStack(MotorGear.TUNGSTEN, 2)))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 1, 9)))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())))
				.setRequiredProgress(32000);

		//Packer

		IIContent.UPGRADE_PACKER_FLUID
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.FLUID_PUMP.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.BARREL.getMeta())))
				.setRequiredProgress(20000);

		IIContent.UPGRADE_PACKER_ENERGY
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.TESLA_COIL.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.CAPACITOR_MV.getMeta())))
				.setRequiredProgress(20000);

		IIContent.UPGRADE_PACKER_RAILWAY
				.addStack(new IngredientStack("gravel", 3))
				.addStack(new IngredientStack("rail", 3))
				.setRequiredProgress(40000);

		IIContent.UPGRADE_PACKER_NAMING
				.addStack(new IngredientStack("circuitBasic", 2))
				.setRequiredProgress(20000);

		//Radar

		IIContent.UPGRADE_RADIO_LOCATORS
				.addStack(new IngredientStack("plateSteel", 4))
				.addStack(new IngredientStack(new ItemStack(IIContent.itemRadioTuner, 2, 1)))
				.addStack(new IngredientStack(new ItemStack(IIContent.itemDataWireCoil, 10, 0)))
				.setRequiredProgress(250000)
				.setRequiredSteps(1);

		//Weapons - Basic Tier

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN
				.addStack(new IngredientStack(new ItemStack(IIContent.blockSandbags, 4))) //Sandbags
				.addStack(new IngredientStack(new ItemStack(IIContent.itemMachinegun, 2)))
				.addStack(new IngredientStack(new ItemStack(IIContent.blockMetalDevice, 2, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())))
				.addStack(new IngredientStack("circuitBasic", 4))
				.setRequiredProgress(40000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER
				.addStack(new IngredientStack("blockGlassRed", 1))
				.addStack(new IngredientStack("blockGlass", 1))
				.addStack(new IngredientStack("blockSteel", 2))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())))
				.addStack(new IngredientStack("circuitBasic", 6))
				.setRequiredProgress(40000);

		//Weapons - Advanced Tier

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 4, 14)))
				.addStack(new IngredientStack("blockSteel", 2))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 3, 9)))
				.addStack(new IngredientStack(new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())))
				.addStack(new IngredientStack("circuitAdvanced", 6))
				.setRequiredProgress(80000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.FLUID_PLACER.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice1, 4, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 2, 14)))
				.addStack(new IngredientStack("blockSteel", 1))
				.addStack(new IngredientStack("circuitAdvanced", 6))
				.setRequiredProgress(80000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.CAPACITOR_HV.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta())))
				.addStack(new IngredientStack("blockSteel", 1))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 2, 9)))
				.addStack(new IngredientStack(new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())))
				.addStack(new IngredientStack("circuitAdvanced", 6))
				.setRequiredProgress(80000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_TESLA
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 8, BlockTypes_MetalDevice0.CAPACITOR_HV.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 8, BlockTypes_MetalDecoration0.COIL_HV.getMeta())))
				.addStack(new IngredientStack("circuitAdvanced", 6))
				.setRequiredProgress(120000);

		/*
		IIContent.UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.CAPACITOR_MV.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDevice1, 4, BlockTypes_MetalDevice1.FLOODLIGHT.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 3, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta())))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 4, 9)))
				.addStack(new IngredientStack("circuitAdvanced",6))
				.setRequiredProgress(80000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_MORTAR
				.addStack(new IngredientStack((new ItemStack(IEContent.blockMetalDevice1, 2, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta()))))
				.addStack(new IngredientStack("blockSteel",2))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 3, 9)))
				.addStack(new IngredientStack("circuitBasic",4))
				.addStack(new IngredientStack(new ItemStack(IIContent.blockMetalDevice, 2, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())))
				.addStack(new IngredientStack("circuitAdvanced",4))
				.setRequiredProgress(160000);

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_LIGHT_HOWITZER
				.addStack(new IngredientStack((new ItemStack(IEContent.blockMetalDevice1, 2, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta()))))
				.addStack(new IngredientStack("blockSteel",2))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 3, 9)))
				.addStack(new IngredientStack("circuitBasic",4))
				.addStack(new IngredientStack(new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())))
				.addStack(new IngredientStack("circuitAdvanced",4))
				.setRequiredProgress(160000);
		 */

		//Weapons - Processor Tier

		IIContent.UPGRADE_EMPLACEMENT_WEAPON_CPDS
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 8, 14)))
				.addStack(new IngredientStack(new ItemStack(IEContent.itemMaterial, 2, 9)))
				.addStack(new IngredientStack("blockSteel", 3))
				.addStack(new IngredientStack("circuitProcessor", 6))
				.setRequiredProgress(300000);
	}

	public static void addSmeltingRecipes()
	{

		FurnaceRecipes.instance().addSmeltingRecipe(
				IIContent.blockOre.getStack(Ores.ZINC),
				IIContent.itemMaterialIngot.getStack(MaterialsIngot.ZINC),
				0.5F);

		FurnaceRecipes.instance().addSmeltingRecipe(
				IIContent.itemMaterialDust.getStack(MaterialsDust.ZINC),
				IIContent.itemMaterialIngot.getStack(MaterialsIngot.ZINC),
				0.5F);

		FurnaceRecipes.instance().addSmeltingRecipe(
				IIContent.itemMaterialDust.getStack(MaterialsDust.BRASS),
				IIContent.itemMaterialIngot.getStack(MaterialsIngot.BRASS),
				0.5F);

		FurnaceRecipes.instance().addSmeltingRecipe(
				IIContent.blockOre.getStack(Ores.PLATINUM),
				IIContent.itemMaterialIngot.getStack(MaterialsIngot.PLATINUM),
				0.5F);

		FurnaceRecipes.instance().addSmeltingRecipe(
				IIContent.itemMaterialDust.getStack(MaterialsDust.PLATINUM),
				IIContent.itemMaterialIngot.getStack(MaterialsIngot.PLATINUM),
				0.5F);

		if(IIConfig.smeltableTungsten)
		{
			FurnaceRecipes.instance().addSmeltingRecipe(
					IIContent.blockOre.getStack(Ores.TUNGSTEN),
					IIContent.itemMaterialIngot.getStack(MaterialsIngot.TUNGSTEN),
					0.5F);

			FurnaceRecipes.instance().addSmeltingRecipe(
					IIContent.itemMaterialDust.getStack(MaterialsDust.TUNGSTEN),
					IIContent.itemMaterialIngot.getStack(MaterialsIngot.TUNGSTEN),
					0.5F);
		}

		if(IIConfig.smeltableAEA)
			FurnaceRecipes.instance().addSmeltingRecipe(
					IIContent.itemMaterialDust.getStack(MaterialsDust.ADVANCED_ELECTRONIC_ALLOY),
					IIContent.itemMaterialIngot.getStack(MaterialsIngot.ADVANCED_ELECTRONIC_ALLOY),
					0.5F);
	}

	public static void addArcFurnaceRecyclingRecipes()
	{
		ArcFurnaceRecipe.allowItemForRecycling(new ItemStack(IIContent.itemDrillhead, 1, OreDictionary.WILDCARD_VALUE));
		ArcFurnaceRecipe.allowItemForRecycling(new ItemStack(IIContent.itemSawblade, 1, OreDictionary.WILDCARD_VALUE));

		ArcFurnaceRecipe.allowItemForRecycling(new ItemStack(IIContent.blockMetalDecoration, 1, OreDictionary.WILDCARD_VALUE));
		ArcFurnaceRecipe.allowItemForRecycling(new ItemStack(IIContent.blockMetalDevice, 1, OreDictionary.WILDCARD_VALUE));
		ArcFurnaceRecipe.allowItemForRecycling(new ItemStack(IIContent.blockMetalFortification, 1, OreDictionary.WILDCARD_VALUE));
		ArcFurnaceRecipe.allowItemForRecycling(new ItemStack(IIContent.blockMetalFortification1, 1, OreDictionary.WILDCARD_VALUE));

	}

	public static void addAmmunitionCasingRecipes()
	{
		FillerRecipe.recipeList.clear();
		FillerRecipe.addRecipe(IIContent.itemAmmoArtillery, 160, 8000);
		FillerRecipe.addRecipe(IIContent.itemAmmoLightArtillery, 140, 6000);
		FillerRecipe.addRecipe(IIContent.itemAmmoMortar, 140, 6000);
		FillerRecipe.addRecipe(IIContent.itemAmmoAutocannon, 80, 1000);
		FillerRecipe.addRecipe(IIContent.itemAmmoMachinegun, 60, 800);
		FillerRecipe.addRecipe(IIContent.itemAmmoAssaultRifle, 55, 700);
		FillerRecipe.addRecipe(IIContent.itemAmmoSubmachinegun, 50, 600);
		FillerRecipe.addRecipe(IIContent.itemAmmoRevolver, 40, 400);

		//Projectiles
		for(ItemIIAmmoBase item : new ItemIIAmmoBase[]{IIContent.itemAmmoArtillery, IIContent.itemAmmoLightArtillery, IIContent.itemAmmoMortar,
				IIContent.itemAmmoAutocannon,
				IIContent.itemAmmoMachinegun, IIContent.itemAmmoAssaultRifle, IIContent.itemAmmoSubmachinegun})
		{
			assert item!=null;

			ItemStack casingStack = item.getCasingStack(1);
			ItemNBTHelper.setBoolean(casingStack, "ii_FilledCasing", true);

			AmmunitionWorkshopRecipe.addRecipe(
					(core, casing) -> {
						ItemStack stack = item.getStack(AmmoParts.BULLET);
						stack.deserializeNBT(core.serializeNBT());
						return stack;
					},
					new IngredientStack(item.getBulletCore("coreBrass", item.getAllowedCoreTypes()[0].getName())),
					new IngredientStack(casingStack).setUseNBT(true),
					(int)(128*item.getCaliber()),
					(int)(240+(20*item.getCaliber()))
			);
		}

		//Explosives and Mines
		for(Item item : new Item[]{IIContent.blockTripmine.itemBlock, IIContent.blockTellermine.itemBlock, IIContent.blockRadioExplosives.itemBlock, IIContent.itemNavalMine})
		{
			assert item!=null;
			IAmmo bullet = (IAmmo)item;

			AmmunitionWorkshopRecipe.addRecipe(
					(core, casing) -> {
						ItemStack stack = new ItemStack(item, 1, IIBlockTypes_Mine.MAIN.getMeta());
						stack.deserializeNBT(core.serializeNBT());
						return stack;
					},
					new IngredientStack(bullet.getBulletCore("coreBrass", bullet.getAllowedCoreTypes()[0].getName())),
					new IngredientStack(bullet.getCasingStack(1)).setUseNBT(true),
					(int)(256*bullet.getCaliber()),
					480
			);
		}

		//Grenades
		AmmunitionWorkshopRecipe.addRecipe(
				(core, casing) -> {

					ItemStack stack = IIContent.itemGrenade.getStack(AmmoParts.BULLET);
					stack.deserializeNBT(core.serializeNBT());
					return stack;
				},
				new IngredientStack(IIContent.itemGrenade.getBulletCore("coreBrass", IIContent.itemGrenade.getAllowedCoreTypes()[0].getName())),
				new IngredientStack("stickTreatedWood"), 600, 480
		);

		AmmunitionWorkshopRecipe.addRecipe(
				(core, casing) -> {
					ItemStack stack = IIContent.itemRailgunGrenade.getStack(AmmoParts.BULLET);
					stack.deserializeNBT(core.serializeNBT());
					return stack;
				},
				new IngredientStack(IIContent.itemGrenade.getBulletCore("coreBrass", IIContent.itemGrenade.getAllowedCoreTypes()[0].getName())),
				new IngredientStack("stickSteel"), 1200, 540
		);
	}

	public static void addRubberRecipes()
	{
		for(VulcanizerMolds mold : VulcanizerMolds.values())
			BlueprintCraftingRecipe.addRecipe("vulcanizer_molds",
					IIContent.itemVulcanizerMold.getStack(mold),
					new IngredientStack("plateSteel", 8),
					TOOL_CUTTERS);

		MetalPressRecipe.addRecipe(IIContent.itemMaterialPlate.getStack(MaterialsPlate.RUBBER_RAW),
				new IngredientStack("rubberRaw"),
				ApiUtils.createComparableItemStack(new ItemStack(IEContent.itemMold, 1, 0), false),
				2400
		);

		VulcanizerRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.RUBBER_BELT, 4),
				IIContent.itemVulcanizerMold.getComparableStack(VulcanizerMolds.BELT),
				new IngredientStack("plateRubberRaw", 10),
				new IngredientStack("dustVulcanizationCompound", 3),
				new IngredientStack("dustSulfur", 2),
				24000
		);

		VulcanizerRecipe.addRecipe(IIContent.itemMaterial.getStack(Materials.RUBBER_TIRE, 3),
				IIContent.itemVulcanizerMold.getComparableStack(VulcanizerMolds.TIRE),
				new IngredientStack("plateRubberRaw", 10),
				new IngredientStack("dustVulcanizationCompound", 8),
				new IngredientStack("dustSulfur", 3),
				32000
		);

		//Rubber is a slow pace industry ^^
		//Unless you build lots of coagulators, that is
		CoagulatorRecipe.addRecipe(
				IIContent.itemMaterial.getStack(Materials.NATURAL_RUBBER, 8),
				new FluidStack(IIContent.fluidLatex, 5500),
				new FluidStack(IIContent.fluidFormicAcid, 500),
				24000,
				400,
				2400
		);

	}

	public static void addMetalPressRecipes()
	{
		for(PressMolds value : PressMolds.values())
			BlueprintCraftingRecipe.addRecipe("ammo_molds",
					IIContent.itemPressMold.getStack(value),
					new IngredientStack("plateSteel", 5),
					TOOL_CUTTERS
			);
	}

	public static void addDuraluminiumRecipes()
	{
		// TODO: 07.11.2021 drones ^^

		//Not realistic at all, but it's an advanced material, hence the time and energy required
		ArcFurnaceRecipe.addRecipe(IIContent.itemMaterialIngot.getStack(MaterialsIngot.DURALUMINIUM, 6),
				new IngredientStack("ingotAluminum", 6),
				new ItemStack(Items.IRON_NUGGET),
				600, 640,
				"ingotCopper", "ingotZinc", "ingotIron", "ingotSilicon"
		);

		ArcFurnaceRecipe.addRecipe(IIContent.itemMaterialIngot.getStack(MaterialsIngot.DURALUMINIUM, 6),
				new IngredientStack("dustAluminum", 6),
				new ItemStack(Items.IRON_NUGGET),
				600, 640,
				"dustCopper", "dustZinc", "dustIron", "dustSilicon"
		);
	}

	public static void addPackerHandling()
	{
		//Items

		final ItemStack[] crates = new ItemStack[]{
				new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.CRATE.getMeta()),
				new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()),
				new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.METAL_CRATE.getMeta()),

				new ItemStack(IIContent.blockSmallCrate, 1, 0),
				new ItemStack(IIContent.blockSmallCrate, 1, 1),
				new ItemStack(IIContent.blockSmallCrate, 1, 2),
				new ItemStack(IIContent.blockSmallCrate, 1, 3),
				new ItemStack(IIContent.blockSmallCrate, 1, 4),
				new ItemStack(IIContent.blockSmallCrate, 1, 5),
		};

		PackerHandler.registerItem(
				stack -> Arrays.stream(crates).anyMatch(s -> s.isItemEqual(stack)),
				stack -> new ItemStackHandler(blusunrize.immersiveengineering.common.util.Utils.readInventory(ItemNBTHelper.getTag(stack).getTagList("inventory", 10), 27))
				{
					final ItemStack ss = stack;

					@Override
					public boolean isItemValid(int slot, @Nonnull ItemStack stack)
					{
						return IEApi.isAllowedInCrate(stack);
					}

					@Override
					protected void onContentsChanged(int slot)
					{
						super.onContentsChanged(slot);
						ItemNBTHelper.getTag(ss).setTag("inventory", blusunrize.immersiveengineering.common.util.Utils.writeInventory(this.stacks));
					}
				}
		);

		final ItemStack[] shulkerBoxes = new ItemStack[]{
				new ItemStack(Blocks.WHITE_SHULKER_BOX),
				new ItemStack(Blocks.ORANGE_SHULKER_BOX),
				new ItemStack(Blocks.MAGENTA_SHULKER_BOX),
				new ItemStack(Blocks.LIGHT_BLUE_SHULKER_BOX),
				new ItemStack(Blocks.YELLOW_SHULKER_BOX),
				new ItemStack(Blocks.LIME_SHULKER_BOX),
				new ItemStack(Blocks.PINK_SHULKER_BOX),
				new ItemStack(Blocks.GRAY_SHULKER_BOX),
				new ItemStack(Blocks.SILVER_SHULKER_BOX),
				new ItemStack(Blocks.CYAN_SHULKER_BOX),
				new ItemStack(Blocks.PURPLE_SHULKER_BOX),
				new ItemStack(Blocks.BLUE_SHULKER_BOX),
				new ItemStack(Blocks.BROWN_SHULKER_BOX),
				new ItemStack(Blocks.GREEN_SHULKER_BOX),
				new ItemStack(Blocks.RED_SHULKER_BOX)
		};

		PackerHandler.registerItem(
				stack -> Arrays.stream(shulkerBoxes).anyMatch(s -> s.isItemEqual(stack)),
				stack -> new ItemStackHandler(blusunrize.immersiveengineering.common.util.Utils.readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("BlockEntityTag").getTagList("Items", 10), 27))
				{
					final ItemStack ss = stack;

					@Override
					public boolean isItemValid(int slot, @Nonnull ItemStack stack)
					{
						return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
					}

					@Override
					protected void onContentsChanged(int slot)
					{
						super.onContentsChanged(slot);
						NBTTagCompound tag = ItemNBTHelper.getTagCompound(stack, "BlockEntityTag");
						tag.setTag("Items", blusunrize.immersiveengineering.common.util.Utils.writeInventory(this.stacks));
						ItemNBTHelper.setTagCompound(ss, "BlockEntityTag", tag);
					}
				}
		);

		PackerHandler.registerItem(
				stack -> stack.getItem()==IIContent.itemBulletMagazine,
				stack -> new ItemStackHandler(IIContent.itemBulletMagazine.readInventory(stack))
				{
					final ItemStack ss = stack;

					@Override
					public boolean isItemValid(int slot, @Nonnull ItemStack stack)
					{

						return stack.getItem()==IIContent.itemBulletMagazine.stackToSub(ss).ammo;
					}

					@Override
					public int getSlotLimit(int slot)
					{
						return 1;
					}

					@Override
					protected void onContentsChanged(int slot)
					{
						super.onContentsChanged(slot);
						IIContent.itemBulletMagazine.writeInventory(ss, stacks);
					}
				}
		);


		PackerHandler.registerItem(
				stack -> stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
				stack -> stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
		);

		//Fluids

		final ItemStack[] barrels = new ItemStack[]{
				new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.BARREL.getMeta()),
				new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.BARREL.getMeta())
		};

		PackerHandler.registerFluid(
				stack -> Arrays.stream(barrels).anyMatch(s -> s.isItemEqual(stack)),
				stack -> new FluidHandlerItemStack(stack, 12000)
				{
					@Nullable
					@Override
					public FluidStack getFluid()
					{
						NBTTagCompound tagCompound = container.getTagCompound();
						if(tagCompound==null||!tagCompound.hasKey("tank"))
							return null;
						return FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag("tank"));
					}

					@Override
					protected void setFluid(FluidStack fluid)
					{
						if(!container.hasTagCompound())
							container.setTagCompound(new NBTTagCompound());

						NBTTagCompound fluidTag = new NBTTagCompound();
						fluid.writeToNBT(fluidTag);
						container.getTagCompound().setTag("tank", fluidTag);
					}

					@Override
					protected void setContainerToEmpty()
					{
						container.getTagCompound().removeTag("tank");
					}
				}
		);

		PackerHandler.registerFluid(
				stack -> stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null),
				stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
		);
	}

	public static void addForeignOreDict()
	{
		//IE Circuit Board
		OreDictionary.registerOre("circuitBasic", new ItemStack(IEContent.itemMaterial, 1, 27));
		//IE Gun Parts
		OreDictionary.registerOre("gungripWood", new ItemStack(IEContent.itemMaterial, 1, 13));
		OreDictionary.registerOre("gunbarrelSteel", new ItemStack(IEContent.itemMaterial, 1, 14));
		OreDictionary.registerOre("gunpartRevolver", new ItemStack(IEContent.itemMaterial, 1, 15));
		OreDictionary.registerOre("gunpartHammer", new ItemStack(IEContent.itemMaterial, 1, 16));

		//Meat for production
		OreDictionary.registerOre("listAllMeatRaw", Items.PORKCHOP);
		OreDictionary.registerOre("listAllMeatRaw", Items.BEEF);
		OreDictionary.registerOre("listAllMeatRaw", Items.FISH);
		OreDictionary.registerOre("listAllMeatRaw", Items.CHICKEN);
		OreDictionary.registerOre("listAllMeatRaw", Items.RABBIT);
		OreDictionary.registerOre("listAllMeatRaw", Items.MUTTON);

		OreDictionary.registerOre("logWood", new ItemStack(IIContent.blockRubberLog));
		OreDictionary.registerOre("woodRubber", new ItemStack(IIContent.blockRubberLog));
		OreDictionary.registerOre("blockLeaves", new ItemStack(IIContent.blockRubberLeaves));

		OreDictionary.registerOre("tnt", new ItemStack(Blocks.TNT));
		OreDictionary.registerOre("materialTNT", new ItemStack(Blocks.TNT));

		OreDictionary.registerOre("leadedConcrete", new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta()));
	}
}
