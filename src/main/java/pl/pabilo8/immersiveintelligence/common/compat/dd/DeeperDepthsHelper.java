package pl.pabilo8.immersiveintelligence.common.compat.dd;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.thaum.ThaumcraftHelper;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialIngot.MaterialsIngot;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 16.04.2026
 * @since 05.04.2026
 */

public class DeeperDepthsHelper extends IICompatModule
{
	private static AmmoComponent ammoComponentWindCharge;

	@Override
	public String getName()
	{
		return "deeperdepths";
	}

	public static final ResLoc RES_DD = ResLoc.of(ResLoc.root("deeperdepths"));

	@Override
	public void preInit()
	{
		//08.04.2026 Carver: Ammo compat added.

		addShrapnel("amethyst", IIColor.fromPackedRGB(0xba73de),
				RES_DD.with("amethyst_block"), 1, 0.08f, 0.1f);

		//wind charge ammo component registry
		AmmoRegistry.registerComponent(ammoComponentWindCharge = new AmmoComponentWindCharge());

		//For recipes
		Item amethystblock1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "amethyst_block"));
		OreDictionary.registerOre("amethystblock", new ItemStack(amethystblock1));
	}

	@Override
	@SuppressWarnings("DataFlowIssue")
	public void registerRecipes()
	{
		//07.04.2026 Carver, recipe reformat
		//For item material look in IEContent.
		//For outputs: first is output, then item that is put in, then cost in time and energy

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 9, 0),
				new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "cut_copper"))),
				ItemStack.EMPTY, 100, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 5, 0),
				new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "cut_copper_slab"))),
				ItemStack.EMPTY, 100, 500);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 9, 0),
				new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "chiseled_copper"))),
				ItemStack.EMPTY, 100, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 9, 0),
				new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "copper_grate"))),
				ItemStack.EMPTY, 60, 500);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 1, 0),
				new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "copper_lantern"))),
				ItemStack.EMPTY, 60, 500);


		String[] waxed = new String[]{"", "waxed_"};
		for(String varient2 : waxed)
		{
			Item bulb1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+"copper_bulb"));
			ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 7, 0),
					new ItemStack(bulb1), ItemStack.EMPTY, 200, 1000);

			Item chest = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+"copper_chest"));
			ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 8, 0),
					new ItemStack(chest), ItemStack.EMPTY, 200, 1000);

			Item chain = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+"copper_chain"));
			ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 1, 0),
					new ItemStack(chain), ItemStack.EMPTY, 200, 1000);

			String[] variants = new String[]{"", "exposed_", "weathered_", "oxidized_"};
			for(String variant1 : variants)
			{
				Item trapdoor1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_trapdoor"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 3, 0),
						new ItemStack(trapdoor1), ItemStack.EMPTY, 100, 800);
				Item rod1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"lightning_rod"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 3, 0),
						new ItemStack(rod1), ItemStack.EMPTY, 60, 600);
				Item cut_copper_stairs = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"cut_copper_stairs"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 5, 0),
						new ItemStack(cut_copper_stairs), ItemStack.EMPTY, 100, 800);
				Item door1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_door"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 2, 0),
						new ItemStack(door1), ItemStack.EMPTY, 100, 500);
				Item bars = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_bars"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 2, 0),
						new ItemStack(bars), ItemStack.EMPTY, 200, 1000);

				//==================
				//Cleaning oxidation vvvv  (and apparently waxed state) with hydrofluoric acid
				//==================

				//no extensions, just metadata sensetive

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 0),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 0),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 0),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1, 0),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1, 0),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1, 0),
					new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);


				//more name-sensetive. Semi-sensetive.

				new BathingRecipe(new ItemStack(chest, 1, 0),
						new ItemStack(chest, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 0),
						new ItemStack(bulb1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(chain, 1, 0),
						new ItemStack(chain, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				//name-sensetive.


				new BathingRecipe(new ItemStack(trapdoor1, 1, 0),
						new ItemStack(trapdoor1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(rod1, 1, 0),
						new ItemStack(rod1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 0),
						new ItemStack(cut_copper_stairs, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(door1, 1, 0),
						new ItemStack(door1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(bars, 1, 0),
						new ItemStack(bars, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				//==================
				//Adding coating with sulfuric acid, metadata sensitive vvvv
				//==================
				//normal

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1,0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1,0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1,0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1, 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1,0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1, 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1,0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1, 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1,0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);


				//more name-sensetive. Semi-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_grate")), 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_bulb")), 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_bulb")), 1, 0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_chain")), 4),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_chain")), 1, 0), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				//name-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_cut_copper_stairs")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_stairs")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_door")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_door")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_trapdoor")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_trapdoor")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_lightning_rod")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("lightning_rod")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_bars")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_bars")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);


				//exposed

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1,1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1, 5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1,1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1, 5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1,1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1, 5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1,1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);


				//more name-sensetive. Semi-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_grate")), 1,5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_bulb")), 1,5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_bulb")), 1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_chain")), 1,5),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_chain")), 1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				//name-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_exposed_cut_copper_stairs")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("exposed_cut_copper_stairs")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_exposed_copper_door")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("exposed_copper_door")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_exposed_copper_trapdoor")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("exposed_copper_trapdoor")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_exposed_lightning_rod")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("exposed_lightning_rod")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_exposed_copper_bars")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("exposed_copper_bars")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				//wethered

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1,2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1, 6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1,2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1, 6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1,2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1, 6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1,2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);


				//more name-sensetive. Semi-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_grate")), 1,6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_bulb")), 1,6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_bulb")), 1, 2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_chain")), 1,6),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_chain")), 1, 2), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				//name-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_weathered_cut_copper_stairs")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("weathered_cut_copper_stairs")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_weathered_copper_door")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("weathered_copper_door")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_weathered_copper_trapdoor")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("weathered_copper_trapdoor")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_weathered_lightning_rod")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("weathered_lightning_rod")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_weathered_copper_bars")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("weathered_copper_bars")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				//oxidized

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_block")), 1, 3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("chiseled_copper")), 1, 3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1,3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1, 7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper")), 1,3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1, 7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_lantern")), 1,3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1, 7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("cut_copper_slab")), 1,3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);


				//more name-sensetive. Semi-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_grate")), 1,7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_grate")), 1, 3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_bulb")), 1,7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_bulb")), 1, 3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_copper_chain")), 1,7),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("copper_chain")), 1, 3), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				//name-sensetive.

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_oxidized_cut_copper_stairs")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("oxidized_cut_copper_stairs")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_oxidized_copper_door")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("oxidized_copper_door")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_oxidized_copper_trapdoor")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("oxidized_copper_trapdoor")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_oxidized_lightning_rod")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("oxidized_lightning_rod")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(Item.REGISTRY.getObject(RES_DD.with("waxed_oxidized_copper_bars")), 1),
						new ItemStack(Item.REGISTRY.getObject(RES_DD.with("oxidized_copper_bars")), 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				//==================
				//Adding coating with sulfuric acid END
				//==================

				//08.04.2026 Carver:
				//Precision assembler recipe for converting amethyst into silicon + recipe for crusher converting amethyst to sand

				Item amethystblock = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "amethyst_block"));
				Item amethystshard = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "amethyst_shard"));
				Item snad = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "sand"));

				CrusherRecipe.addRecipe(new ItemStack(snad, 1),
						new ItemStack(amethystblock, 1), 524);

				CrusherRecipe.addRecipe(new ItemStack(snad, 1),
						new ItemStack(amethystshard, 4), 524);

				new PrecisionAssemblerRecipe(
						IIContent.itemMaterialIngot.getStack(MaterialsIngot.SILICON),
						ItemStack.EMPTY,

						new IngredientStack[]{new IngredientStack("amethystblock")},

						new String[]{"hammer"},
						new String[]{"hammer work main", "hammer work main", "hammer work main"},

						14000,
						1f);

			}
		}
		//				new ItemStack(*), (IEContent.itemMetal, 1, 7), 200, 1000);    for slag (IE content)

		//deeper depths (deeperdepths:) (deeperdepths:stone refers to tuff)

		OreDictionary.registerOre("blockBone", Blocks.BONE_BLOCK);
		OreDictionary.registerOre("blockAmethyst", Item.REGISTRY.getObject(RES_DD.with("amethyst_block")));

		ExcavatorHandler.addMineral("Amethyst Crevasse", 15, .15f,
				new String[]{"blockAmethyst", "stoneCalcite", "stoneTuff"}, new float[]{.4f, .3f, .3f});
		ExcavatorHandler.addMineral("Ancient Seabed", 15, .15f,
				new String[]{"stoneCalcite", "cobblestone", "blockBone"}, new float[]{.65f, .3f, .05f});

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
