package pl.pabilo8.immersiveintelligence.common.compat.tbl;


import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.srp.ScapeAndRunParasitesHelper;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @since 13.04.2026
 * @updated 19.04.2026
 */


public class TheBetweenlandsHelper extends IICompatModule
{
	@Override
	public String getName()
	{
		return "thebetweenlands";
	}

	public static AmmoComponent AmmoComponentCremains;
	public static AmmoComponent AmmoComponentUndyingEmbers;

	public static AmmoComponent AmmoComponentBMiddleGem;
	public static AmmoComponent AmmoComponentRMiddleGem;
	public static AmmoComponent AmmoComponentGMiddleGem;

	public static AmmoComponent AmmoComponentPyradFlame;

	public static AmmoComponent AmmoComponentLifeCrystal;
	public static AmmoComponent AmmoComponentLifeCrystalFragment;

	public static AmmoCore AmmoCoreSyrmorite;
	public static AmmoCore AmmoCoreValonite;
	public static AmmoCore AmmoCoreOctine;
	public static AmmoCore AmmoCoreAncientRemnant;

	public static AmmoPropellant AmmoPropellantTBLSulfur;

	public static final ResLoc RES_TBL = ResLoc.of(ResLoc.root("thebetweenlands"));

	//cremains gas. Color for active gas FF500000(500000). Otherwise 35435d for item.
	//Gas should be unobtainable as fluid block in survival.

	public static BlockIIFluid blockGasCremainGas;
	public static Fluid gasCremainGas;

	static
		{
			TheBetweenlandsHelper.blockGasCremainGas = (BlockIIFluid)new BlockIIFluid("cremain_gas", TheBetweenlandsHelper.gasCremainGas, Material.WATER)
					.setPotionEffects(new PotionEffect(IEPotions.flammable, 60, 0)).setTemperature(3000);
		}

	@Override
	public void preInit()
	{
		addShrapnel("syrmorite", IIColor.fromPackedRGB(0x4a59a6),
				RES_TBL.with("syrmorite_block"), 4, 0.25f, 0f);

		addShrapnel("dentrothyst_shard_orange", IIColor.fromPackedRGB(0x523b06),
				RES_TBL.with("dentrothyst_orange"), 3, 0.15f, 0f);

		addShrapnel("dentrothyst_shard_green", IIColor.fromPackedRGB(0x065236),
				RES_TBL.with("dentrothyst_green"), 3, 0.15f, 0f);
		//dentrothyst_orange, dentrothyst_green - blocks. Items are dentrothyst_shard_

		ShrapnelHandler.addShrapnel("valonite_shard", IIColor.fromPackedRGB(0xc8b2c8),
				RES_TBL.with("valonite_block"), 7, 0.35f, 0f);

		ShrapnelHandler.addShrapnel("octine", IIColor.fromPackedRGB(0xd73900),
				RES_TBL.with("octine_block"), 5, 0.30f, 1f)
				.setFlammable(true);

		ShrapnelHandler.addShrapnel("ancient_remnant", IIColor.fromPackedRGB(0xafe5e1),
				RES_TBL.with("ancient_remnant_block"), 7, 0.30f, 0f);

		Item syrmorite_nugget = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("syrmorite_nugget"));
		Item undyingembers = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("undying_ember"));
		Item valonite = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("valonite_splinter"));
		Item octine_nugget = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "octine_nugget"));
		Item ancient_remnant = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("ancient_remnant"));

		Item sulfur = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "sulfur"));

		OreDictionary.registerOre("tsulfur", new ItemStack(sulfur));

		OreDictionary.registerOre("nuggetSyrmorite", new ItemStack(syrmorite_nugget));
		OreDictionary.registerOre("undyingembers", new ItemStack(undyingembers));
		OreDictionary.registerOre("nuggetValonite", new ItemStack(valonite));
		OreDictionary.registerOre("nuggetOctine", new ItemStack(octine_nugget));
		OreDictionary.registerOre("ancient_remnant", new ItemStack(ancient_remnant));


		Item life_crystal = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "life_crystal"));
		Item life_crystal_fragment = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("life_crystal_fragment"));

		OreDictionary.registerOre("life_crystal", new ItemStack(life_crystal));
		OreDictionary.registerOre("life_crystal_fragment", new ItemStack(life_crystal_fragment));


		AmmoCore AmmoCoreSyrmorite = new AmmoCoreSyrmorite();
		AmmoCore AmmoCoreValonite = new AmmoCoreValonite();
		AmmoCore AmmoCoreOctine = new AmmoCoreOctine();
		AmmoCore AmmoCoreAncientRemnant = new AmmoCoreOctine();

		AmmoComponent AmmoComponentCremains = new AmmoComponentCremains();
		AmmoComponent AmmoComponentUndyingEmbers = new AmmoComponentUndyingEmbers();
		AmmoComponent AmmoComponentPyradFlame = new AmmoComponentPyradFlame();

		AmmoComponent AmmoComponentBMiddleGem = new AmmoComponentBMiddleGem();
		AmmoComponent AmmoComponentRMiddleGem = new AmmoComponentBMiddleGem();
		AmmoComponent AmmoComponentGMiddleGem = new AmmoComponentBMiddleGem();

		AmmoComponent AmmoComponentLifeCrystal = new AmmoComponentLifeCrystal();
		AmmoComponent AmmoComponentLifeCrystalFragment = new AmmoComponentLifeCrystalFragment();

		AmmoPropellant AmmoPropellantTBLSulfur = new AmmoPropellantTBLSulfur();

		AmmoRegistry.registerCore(TheBetweenlandsHelper.AmmoCoreSyrmorite);
		AmmoRegistry.registerCore(TheBetweenlandsHelper.AmmoCoreValonite);
		AmmoRegistry.registerCore(TheBetweenlandsHelper.AmmoCoreOctine);
		AmmoRegistry.registerCore(TheBetweenlandsHelper.AmmoCoreAncientRemnant);

		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentCremains);
		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentUndyingEmbers);
		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentPyradFlame);

		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentBMiddleGem);
		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentRMiddleGem);
		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentGMiddleGem);

		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentLifeCrystal);
		AmmoRegistry.registerComponent(TheBetweenlandsHelper.AmmoComponentLifeCrystalFragment);

		AmmoRegistry.registerPropellant(TheBetweenlandsHelper.AmmoPropellantTBLSulfur);

		//TODO 14.04.2026: deeper integrations


		//Compat1: Electrical resist to the electrical attacks by the required meshing: lightning, lightning chiromaw, shock arrows. Have gasmask protect from shallowbreath suffocation.

		//check damage source by name and override the hurtEvent in EventHandler.
		// 15.04.2026 Complication: done by the entity using generic damage from arrows. Implementing it that way will make it immune to arrows.

		//Compat 2: Rubber boots upgrade to the LEA armor boots: will function the same as rubber boots of tbl and will allow to ignore slowdown effects of soulsand and alike. If aforementioned aspects are added, then it will function same as boots of a marsh runner that can move on water.

		//check if compat is loaded and call method from compat in ItemIILightEngineerBoots.
		//15.04.2026  Complication: Done by TBL capabilities API. Without API integration it is not easy to implement. It will go beyond the purpose of a compat.



		//Compat 3: emplacements. Gem-themed emplacement upgrades - easy and fast to code as upgrades.
		//Does not require direct integration from tbl, but more so the visuals and sounds are enough.

		//MISC: Potentially, LE armor plating from ancient remnants?

	}

	@Override
	public void registerRecipes()
	{

		//conversion of Tar to Biodiesel.

		Fluid tar = FluidRegistry.getFluid("TAR");
		Fluid fishoil = FluidRegistry.getFluid("FISH_OIL");
		Fluid shallowbreath = FluidRegistry.getFluid("SHALLOWBREATH");
		Fluid swampwater = FluidRegistry.getFluid("SWAMP_WATER");
		Fluid cleanswampwater = FluidRegistry.getFluid("CLEAN_WATER");

		RefineryRecipe.addRecipe(new FluidStack(IEContent.fluidBiodiesel, 100), new FluidStack(tar, 200),
				new FluidStack(IIContent.gasHydrogen, 50), 65);

		//Convert shallowbreath through electrolizer to something useable.
		//We assume Shallowbreath is a sentient cloud of propane, or at least consists of water and hydrogen given it is highly flammable.

		new ElectrolyzerRecipe(new FluidStack(shallowbreath, 1000),
				new FluidStack(swampwater, 100),
				FluidRegistry.getFluidStack("hydrogen", 900),
				160, 80);

		new ElectrolyzerRecipe(new FluidStack(swampwater, 750),
				FluidRegistry.getFluidStack("oxygen", 250),
				FluidRegistry.getFluidStack("hydrogen", 500),
				200, 80);

		//Chemical bath treated wood - tbl version

		String[] treated = new String[]{"_treated"};
		for(String varient2 : treated)
		{
			Item planks1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("rotten_planks"));
			Item planks2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("weedwood_planks"));
			Item planks3 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("giant_root_planks"));
			Item planks4 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "nibbletwig_planks"));
			Item planks5 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("hearthgrove_planks"));
			Item planks6 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "rubber_tree_planks"));

			new BathingRecipe(new ItemStack(Block.getBlockFromName(planks1+varient2), 12),
					new ItemStack(planks1, 8),
					new FluidStack(fishoil, 1000),
					3200, 120, false );

			new BathingRecipe(new ItemStack(Block.getBlockFromName(planks2+varient2), 12),
					new ItemStack(planks2, 8),
					new FluidStack(fishoil, 1000),
					3200, 120, false );

			new BathingRecipe(new ItemStack(Block.getBlockFromName(planks3+varient2), 12),
					new ItemStack(planks3, 8),
					new FluidStack(fishoil, 1000),
					3200, 120, false );

			new BathingRecipe(new ItemStack(Block.getBlockFromName(planks4+varient2), 12),
					new ItemStack(planks4, 8),
					new FluidStack(fishoil, 1000),
					3200, 120, false );

			new BathingRecipe(new ItemStack(Block.getBlockFromName(planks5+varient2), 12),
					new ItemStack(planks5, 8),
					new FluidStack(fishoil, 1000),
					3200, 120, false );


			new BathingRecipe(new ItemStack(planks5, 12),
					new ItemStack(planks6, 8),
					new FluidStack(fishoil, 1000),
					3200, 120, false );
		}

		//Tarred hearthgrove

		Item planks7 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("hearthgrove_log"));
		Item planks7_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "hearthgrove_log_tarred"));

		new BathingRecipe(new ItemStack(planks7_2, 12),
				new ItemStack(planks7, 8),
				new FluidStack(tar, 1000),
				3200, 120, false );

		//CRUSH the fish and make oil out of it and turn it into biodiesel

		Item fish = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "anadia"));
		Item fishremains = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "anadia_remains"));
		CrusherRecipe.addRecipe(new ItemStack(fishremains, 5),
				new ItemStack(fish, 1), 100);

		MixerRecipe.addRecipe(new FluidStack(fishoil, 400),
				new FluidStack(cleanswampwater, 400), new Object[]{"anadia_remains"}, 3400);

		RefineryRecipe.addRecipe(new FluidStack(IEContent.fluidBiodiesel, 100), new FluidStack(fishoil, 400),
				new FluidStack(IEContent.fluidEthanol, 100), 65);

		//HERBLORE

		//G R O I N D the middle gems

		Item gem1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "aqua_middle_gem"));
		Item gem1g = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "ground_aqua_middle_gem"));
		CrusherRecipe.addRecipe(new ItemStack(gem1g, 1),
				new ItemStack(gem1, 1), 100);

		Item gem2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("crimson_middle_gem"));
		Item gem2g = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("ground_crimson_middle_gem"));
		CrusherRecipe.addRecipe(new ItemStack(gem2g, 1),
				new ItemStack(gem2, 1), 100);

		Item gem3 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "green_middle_gem"));
		Item gem3g = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("ground_green_middle_gem"));
		CrusherRecipe.addRecipe(new ItemStack(gem3g, 1),
				new ItemStack(gem3, 1), 100);

		//GRINDUR the rest of herblore recipes into ground variants. Unfortunately did not find a way to use array that will output the same item, to reduce the amount of kode. Budujemo Piramid, fellas.

		String[] plsh = new String[]{"", "polished_"};
		for(String varient2 : plsh)
		{
			Item h1_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with("limestone_flux"));
			Item h1_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with(varient2+"limestone"));

			CrusherRecipe.addRecipe(new ItemStack(h1_1, 3),
					new ItemStack(h1_2, 2), 100);
		}

		Item h1_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "ground_algae"));
		Item h1_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "algae_item"));CrusherRecipe.addRecipe(new ItemStack(h1_1, 1), new ItemStack(h1_2, 1), 100);
		Item h2_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_ANGLER_TOOTH"));
		Item h2_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "ANGLER_TOOTH"));CrusherRecipe.addRecipe(new ItemStack(h2_1, 1), new ItemStack(h2_2, 1), 100);
		Item h3_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_ARROW_ARUM"));
		Item h3_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "ARROW_ARUM_LEAF"));CrusherRecipe.addRecipe(new ItemStack(h3_1, 1), new ItemStack(h3_2, 1), 100);
		Item h4_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BLACKHAT_MUSHROOM"));
		Item h4_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BLACK_HAT_MUSHROOM_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h4_1, 1), new ItemStack(h4_2, 1), 100);
		Item h5_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BLOOD_SNAIL_SHELL"));
		Item h5_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BLOOD_SNAIL_SHELL"));CrusherRecipe.addRecipe(new ItemStack(h5_1, 1), new ItemStack(h5_2, 1), 100);
		Item h6_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BLUE_EYED_GRASS"));
		Item h6_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BLUE_EYED_GRASS_FLOWERS"));CrusherRecipe.addRecipe(new ItemStack(h6_1, 1), new ItemStack(h6_2, 1), 100);
		Item h7_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BLUE_IRIS"));
		Item h7_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BLUE_IRIS_PETAL"));CrusherRecipe.addRecipe(new ItemStack(h7_1, 1), new ItemStack(h7_2, 1), 100);
		Item h8_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BOG_BEAN"));
		Item h8_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BOG_BEAN_FLOWER_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h8_1, 1), new ItemStack(h8_2, 1), 100);
		Item h9_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BONESET"));
		Item h9_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BONESET_FLOWERS"));CrusherRecipe.addRecipe(new ItemStack(h9_1, 1), new ItemStack(h9_2, 1), 100);
		Item h10_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BOTTLE_BRUSH_GRASS"));
		Item h10_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BOTTLE_BRUSH_GRASS_BLADES"));CrusherRecipe.addRecipe(new ItemStack(h10_1, 1), new ItemStack(h10_2, 1), 100);
		Item h11_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BROOM_SEDGE"));
		Item h11_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BROOM_SEDGE_LEAVES"));CrusherRecipe.addRecipe(new ItemStack(h11_1, 1), new ItemStack(h11_2, 1), 100);
		Item h12_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BULB_CAPPED_MUSHROOM"));
		Item h12_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BULB_CAPPED_MUSHROOM_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h12_1, 1), new ItemStack(h12_2, 1), 100);
		Item h13_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BUTTON_BUSH"));
		Item h13_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BUTTON_BUSH_FLOWERS"));CrusherRecipe.addRecipe(new ItemStack(h13_1, 1), new ItemStack(h13_2, 1), 100);
		Item h14_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_CARDINAL_FLOWER"));
		Item h14_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "CARDINAL_FLOWER_PETALS"));CrusherRecipe.addRecipe(new ItemStack(h14_1, 1), new ItemStack(h14_2, 1), 100);
		Item h15_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_CATTAIL"));
		Item h15_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "CATTAIL_HEAD"));CrusherRecipe.addRecipe(new ItemStack(h15_1, 1), new ItemStack(h15_2, 1), 100);
		Item h16_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_CAVE_GRASS"));
		Item h16_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "CAVE_GRASS_BLADES"));CrusherRecipe.addRecipe(new ItemStack(h16_1, 1), new ItemStack(h16_2, 1), 100);
		Item h17_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_CAVE_MOSS"));
		Item h17_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "CAVE_MOSS_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h17_1, 1), new ItemStack(h17_2, 1), 100);
		Item h18_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_COPPER_IRIS"));
		Item h18_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "COPPER_IRIS_PETALS"));CrusherRecipe.addRecipe(new ItemStack(h18_1, 1), new ItemStack(h18_2, 1), 100);
		Item h19_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_DEEP_WATER_CORAL"));
		Item h19_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "DEEP_WATER_CORAL_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h19_1, 1), new ItemStack(h19_2, 1), 100);
		Item h20_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_DRIED_SWAMP_REED"));
		Item h20_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "DRIED_SWAMP_REED"));CrusherRecipe.addRecipe(new ItemStack(h20_1, 1), new ItemStack(h20_2, 1), 100);
		Item h21_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_FLATHEAD_MUSHROOM"));
		Item h21_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "FLAT_HEAD_MUSHROOM_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h21_1, 1), new ItemStack(h21_2, 1), 100);
		Item h22_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_GOLDEN_CLUB"));
		Item h22_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GOLDEN_CLUB_FLOWER_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h22_1, 1), new ItemStack(h22_2, 1), 100);
		Item h23_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_LICHEN"));
		Item h23_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "LICHEN_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h23_1, 1), new ItemStack(h23_2, 1), 100);
		Item h24_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_MARSH_HIBISCUS"));
		Item h24_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MARSH_HIBISCUS_FLOWER"));CrusherRecipe.addRecipe(new ItemStack(h24_1, 1), new ItemStack(h24_2, 1), 100);
		Item h25_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_MARSH_MALLOW"));
		Item h25_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MARSH_MALLOW_FLOWER"));CrusherRecipe.addRecipe(new ItemStack(h25_1, 1), new ItemStack(h25_2, 1), 100);
		Item h26_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "ROUND_MARSH_MARIGOLD"));
		Item h26_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MARSH_MARIGOLD_FLOWER_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h26_1, 1), new ItemStack(h26_2, 1), 100);
		Item h27_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_MILKWEED"));
		Item h27_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MILKWEED_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h27_1, 1), new ItemStack(h27_2, 1), 100);
		Item h28_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BETWEENSTONE_PEBBLE"));
		Item h28_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BETWEENSTONE_PEBBLE"));CrusherRecipe.addRecipe(new ItemStack(h28_1, 1), new ItemStack(h28_2, 1), 100);
		Item h29_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_MIRE_CORAL"));
		Item h29_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MIRE_CORAL_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h29_1, 1), new ItemStack(h29_2, 1), 100);
		Item h30_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_MIRE_SNAIL_SHELL"));
		Item h30_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MIRE_SNAIL_SHELL"));CrusherRecipe.addRecipe(new ItemStack(h30_1, 1), new ItemStack(h30_2, 1), 100);
		Item h31_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_MOSS"));
		Item h31_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "MOSS_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h31_1, 1), new ItemStack(h31_2, 1), 100);
		Item h32_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_NETTLE"));
		Item h32_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "NETTLE_LEAF"));CrusherRecipe.addRecipe(new ItemStack(h32_1, 1), new ItemStack(h32_2, 1), 100);
		Item h33_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_PHRAGMITES"));
		Item h33_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "PHRAGMITE_STEMS"));CrusherRecipe.addRecipe(new ItemStack(h33_1, 1), new ItemStack(h33_2, 1), 100);
		Item h34_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_PICKEREL_WEED"));
		Item h34_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "PICKEREL_WEED_FLOWER"));CrusherRecipe.addRecipe(new ItemStack(h34_1, 1), new ItemStack(h34_2, 1), 100);
		Item h35_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_SHOOTS"));
		Item h35_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SHOOT_LEAVES"));CrusherRecipe.addRecipe(new ItemStack(h35_1, 1), new ItemStack(h35_2, 1), 100);
		Item h36_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_SLUDGECREEP"));
		Item h36_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SLUDGECREEP_LEAVES"));CrusherRecipe.addRecipe(new ItemStack(h36_1, 1), new ItemStack(h36_2, 1), 100);
		Item h37_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_SOFT_RUSH"));
		Item h37_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SOFT_RUSH_LEAVES"));CrusherRecipe.addRecipe(new ItemStack(h37_1, 1), new ItemStack(h37_2, 1), 100);
		Item h38_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_SUNDEW"));
		Item h38_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SUNDEW_HEAD"));CrusherRecipe.addRecipe(new ItemStack(h38_1, 1), new ItemStack(h38_2, 1), 100);
		Item h39_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_SWAMP_KELP"));
		Item h39_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SWAMP_KELP_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h39_1, 1), new ItemStack(h39_2, 1), 100);
		Item h40_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_SWAMP_GRASS_TALL"));
		Item h40_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SWAMP_TALL_GRASS_BLADES"));CrusherRecipe.addRecipe(new ItemStack(h40_1, 1), new ItemStack(h40_2, 1), 100);
		Item h41_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_ROOTS"));
		Item h41_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "TANGLED_ROOT"));CrusherRecipe.addRecipe(new ItemStack(h41_1, 1), new ItemStack(h41_2, 1), 100);
		Item h42_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GIANT_ROOT"));CrusherRecipe.addRecipe(new ItemStack(h41_1, 1), new ItemStack(h42_2, 1), 100);
		Item h43_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_WEEDWOOD_BARK"));
		Item h43_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "LOG_WEEDWOOD"));CrusherRecipe.addRecipe(new ItemStack(h43_1, 4), new ItemStack(h43_2, 1), 100);
		Item h44_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "DRY_BARK"));CrusherRecipe.addRecipe(new ItemStack(h43_1, 1), new ItemStack(h44_2, 1), 100);
		Item h45_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_HANGER"));
		Item h45_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "HANGER_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h45_1, 1), new ItemStack(h45_2, 1), 100);
		Item h46_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_WATER_WEEDS"));
		Item h46_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "WATER_WEEDS_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h46_1, 1), new ItemStack(h46_2, 1), 100);
		Item h47_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_VENUS_FLY_TRAP"));
		Item h47_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "VENUS_FLY_TRAP_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h47_1, 1), new ItemStack(h47_2, 1), 100);
		Item h48_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_VOLARPAD"));
		Item h48_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "VOLARPAD_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h48_1, 1), new ItemStack(h48_2, 1), 100);
		Item h49_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_THORNS"));
		Item h49_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "THORNS_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h49_1, 1), new ItemStack(h49_2, 1), 100);
		Item h50_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_POISON_IVY"));
		Item h50_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "POISON_IVY_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h50_1, 1), new ItemStack(h50_2, 1), 100);
		Item h51_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_PITCHER_PLANT"));
		Item h51_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "PITCHER_PLANT_TRAP"));CrusherRecipe.addRecipe(new ItemStack(h51_1, 1), new ItemStack(h51_2, 1), 100);
		Item h52_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_GENERIC_LEAF"));
		Item h52_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GENERIC_LEAF"));CrusherRecipe.addRecipe(new ItemStack(h52_1, 1), new ItemStack(h52_2, 1), 100);
		Item h53_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BLADDERWORT_FLOWER"));
		Item h53_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BLADDERWORT_FLOWER_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h53_1, 1), new ItemStack(h53_2, 1), 100);
		Item h54_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_BLADDERWORT_STALK"));
		Item h54_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "BLADDERWORT_STALK_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h54_1, 1), new ItemStack(h54_2, 1), 100);
		Item h55_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_EDGE_SHROOM"));
		Item h55_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "EDGE_SHROOM_GILLS"));CrusherRecipe.addRecipe(new ItemStack(h55_1, 1), new ItemStack(h55_2, 1), 100);
		Item h56_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_EDGE_MOSS"));
		Item h56_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "EDGE_MOSS_CLUMP"));CrusherRecipe.addRecipe(new ItemStack(h56_1, 1), new ItemStack(h56_2, 1), 100);
		Item h57_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_EDGE_LEAF"));
		Item h57_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "EDGE_LEAF_ITEM"));CrusherRecipe.addRecipe(new ItemStack(h57_1, 1), new ItemStack(h57_2, 1), 100);
		Item h58_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_ROTBULB"));
		Item h58_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "ROTBULB_STALK"));CrusherRecipe.addRecipe(new ItemStack(h58_1, 1), new ItemStack(h58_2, 1), 100);
		Item h59_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_PALE_GRASS"));
		Item h59_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "PALE_GRASS_BLADES"));CrusherRecipe.addRecipe(new ItemStack(h59_1, 1), new ItemStack(h59_2, 1), 100);
		Item h60_1 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "GROUND_STRING_ROOTS"));
		Item h60_2 = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "STRING_ROOT_FIBERS"));CrusherRecipe.addRecipe(new ItemStack(h60_1, 1), new ItemStack(h60_2, 1), 100);

		//Recipes for making ethanol in a fermenter.
		Item swampreed = Item.REGISTRY.getObject(TheBetweenlandsHelper.RES_TBL.with( "SWAMP_REED_ITEM"));

		FermenterRecipe.addRecipe(new FluidStack(IEContent.fluidEthanol, 80), ItemStack.EMPTY, h15_1, 3400);
		FermenterRecipe.addRecipe(new FluidStack(IEContent.fluidEthanol, 80), ItemStack.EMPTY, h1_1, 3400);
		FermenterRecipe.addRecipe(new FluidStack(IEContent.fluidEthanol, 80), ItemStack.EMPTY, h46_1, 3400);
		FermenterRecipe.addRecipe(new FluidStack(IEContent.fluidEthanol, 90), ItemStack.EMPTY, swampreed, 3400);

		//ExcavatorHandler.addMineral("Octine_Deposit", 15, .15f, new String[]{"oreOctine", "betweenstone"}, new float[]{.25f, .75f});
		//mineralOctine_Deposit.dimensionWhitelist = new int[]{20};
		//Todo: tbl-exclusive ore veins.
	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}
}
