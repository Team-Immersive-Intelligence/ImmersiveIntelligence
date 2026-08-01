package pl.pabilo8.immersiveintelligence.common.compat.dd;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialNugget.MaterialsNugget;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.04.2026
 * @updated 16.04.2026
 * @updated 31.07.2026
 * @ii-approved 0.3.1
 */

public class DeeperDepthsHelper extends IICompatModule
{
	public static final ResLoc RES_DD = ResLoc.of(ResLoc.root("deeperdepths"));
	private static AmmoComponent ammoComponentWindCharge;

	@Override
	public String getName()
	{
		return "deeperdepths";
	}

	@Override
	public void preInit()
	{
		addShrapnel("amethyst", IIColor.fromPackedRGB(0xba73de),
				RES_DD.with("amethyst_block"), 1, 0.08f, 0.1f);

		//Register ores for recipes
		OreDictionary.registerOre("blockBone", Blocks.BONE_BLOCK);
		OreDictionary.registerOre("blockAmethyst", Block.REGISTRY.getObject(RES_DD.with("amethyst_block")));

		//Wind charge ammo component registry
		AmmoRegistry.registerComponent(ammoComponentWindCharge = new AmmoComponentWindCharge());
	}

	@Override
	public void registerRecipes()
	{
		final Object[][] recyclingMetaBlocks = {
				{"copper_block", 9, 100, 1000, "blockCopper"},
				{"cut_copper", 9, 100, 1000, "blockCopperCut"},
				{"cut_copper_slab", 5, 100, 500, "blockCopperSlab"},
				{"chiseled_copper", 9, 100, 1000, "blockCopperChiseled"},
				{"copper_grate", 9, 60, 500, "grateGrate"},
				{"copper_lantern", 1, 60, 500, "lanternCopper"}
		};
		for(Object[] entry : recyclingMetaBlocks)
		{
			String name = String.valueOf(entry[0]);
			int amount = (int)entry[1];
			int energy = (int)entry[2];
			int time = (int)entry[3];
			OreDictionary.registerOre(String.valueOf(entry[4]), getModItem(RES_DD.with(name)));

			ArcFurnaceRecipe.addRecipe(
					new ItemStack(IEContent.itemMetal, amount, 0), entry[4],
					ItemStack.EMPTY, energy, time
			).setSpecialRecipeType("Recycling");

			new BathingRecipe(
					getModItem(RES_DD.with(name), 1), entry[4],
					new FluidStack(IIContent.fluidHydrofluoricAcid, 100),
					512, 120, false
			);
		}

		//----- Waxed and non-waxed variants -----
		final String[] waxedPrefixes = {"", "waxed_"};

		//Blocks without oxidation variants (bulb, chest, chain)
		//{name, output amount, energy, time}
		final Object[][] waxedOnlyBlocks = {
				{"copper_bulb", 7, 200, 1000},
				{"copper_chest", 8, 200, 1000},
				{"copper_chain", 1, 200, 1000}
		};

		//Blocks that have both waxed and oxidation variants
		final String[] oxidationLevels = {"", "exposed_", "weathered_", "oxidized_"};

		//{name, output amount, energy, time}
		final Object[][] oxidationBlocks = {
				{"copper_trapdoor", 3, 100, 800},
				{"lightning_rod", 3, 60, 600},
				{"cut_copper_stairs", 5, 100, 800},
				{"copper_door", 2, 100, 500},
				{"copper_bars", 2, 200, 1000}
		};

		//Base blocks that appear in Bathing recipes (all use same pattern)
		final String[] baseBlocks = {
				"copper_block", "chiseled_copper", "copper_grate",
				"cut_copper", "copper_lantern", "cut_copper_slab"
		};

		for(String waxed : waxedPrefixes)
		{
			//Recycling for bulb, chest, chain (no oxidation loop)
			for(Object[] entry : waxedOnlyBlocks)
			{
				String name = (String)entry[0];
				int amount = (int)entry[1];
				int energy = (int)entry[2];
				int time = (int)entry[3];
				Item item = Item.REGISTRY.getObject(RES_DD.with(waxed+name));
				if(item!=null)
					for(int i = 0; i < 4; i++)
						ArcFurnaceRecipe.addRecipe(
								new ItemStack(IEContent.itemMetal, amount, 0),
								new ItemStack(item, 1, i), ItemStack.EMPTY, energy, time
						).setSpecialRecipeType("Recycling");
			}

			//Recycling and Bathing for oxidation blocks
			for(String ox : oxidationLevels)
			{
				String fullPrefix = waxed+ox;

				//Recycling
				for(Object[] entry : oxidationBlocks)
				{
					String name = (String)entry[0];
					int amount = (int)entry[1];
					int energy = (int)entry[2];
					int time = (int)entry[3];
					Item item = Item.REGISTRY.getObject(RES_DD.with(fullPrefix+name));
					if(item!=null)
						ArcFurnaceRecipe.addRecipe(
								new ItemStack(IEContent.itemMetal, amount, 0),
								new ItemStack(item), ItemStack.EMPTY, energy, time
						).setSpecialRecipeType("Recycling");
				}

				//Bathing: cleaning with hydrofluoric acid (output = clean, meta 0)
				//Base blocks (no oxidation/waxed prefix in item name for input)
				if(waxed.isEmpty())
				{
					//Input: any meta? The original uses getModItem without meta → default 0.
					//Output: getModItem with meta 0 explicitly.
					for(String block : baseBlocks)
						for(int i = 1; i < 8; i++)
							new BathingRecipe(
									getModItem(RES_DD.with(block), 1),
									getModItem(RES_DD.with(block), 1, i),
									new FluidStack(IIContent.fluidHydrofluoricAcid, 100),
									512, 120, false
							);

					//Bulb, chest, chain (already obtained outside ox loop)
					//only once per waxed prefix
					if(ox.isEmpty())
						for(Object[] entry : waxedOnlyBlocks)
						{
							String name = (String)entry[0];
							Item item = Item.REGISTRY.getObject(RES_DD.with(waxed+name));
							if(item!=null)
								for(int i = 0; i < 4; i++)
									new BathingRecipe(
											new ItemStack(item, 1),
											new ItemStack(item, 1, i),
											new FluidStack(IIContent.fluidHydrofluoricAcid, 100),
											512, 120, false
									);
						}

					//Oxidation blocks (trapdoor, rod, stairs, door, bars)
					for(Object[] entry : oxidationBlocks)
					{
						String name = (String)entry[0];
						new BathingRecipe(
								getModItem(RES_DD.with(name), 1),
								getModItem(RES_DD.with(fullPrefix+name), 1),
								new FluidStack(IIContent.fluidHydrofluoricAcid, 100),
								512, 100, false
						);
					}
				}

				//Bathing: coating with sulfuric acid (actually uses hydrofluoric acid fluid)
				//Only for unwaxed → waxed conversion (waxed prefix = "")
				if(waxed.isEmpty())
				{
					//Bulb, chest, chain (only appear when ox = "")
					if(ox.isEmpty())
						for(Object[] entry : waxedOnlyBlocks)
						{
							String name = (String)entry[0];
							for(int i = 0; i < 4; i++)
								new BathingRecipe(
										getModItem(RES_DD.with(name), 1, 0),
										getModItem(RES_DD.with("waxed_"+name), 1, i),
										new FluidStack(IIContent.fluidHydrofluoricAcid, 100),
										512, 120, false
								);
						}

					//Oxidation blocks: use name‑sensitive lookups (prefix already contains oxidation)
					for(Object[] entry : oxidationBlocks)
					{
						String name = (String)entry[0];
						new BathingRecipe(
								getModItem(RES_DD.with(fullPrefix+name), 1),
								getModItem(RES_DD.with("waxed_"+fullPrefix+name), 1),
								new FluidStack(IIContent.fluidHydrofluoricAcid, 100),
								512, 100, false
						);
					}
				}
			}
		}

		//--- Additional Crusher & Precision Assembler recipes ---

		CrusherRecipe.addRecipe(new ItemStack(Blocks.SAND, 1), getModItem(RES_DD.with("amethyst_block"), 1), 524);
		CrusherRecipe.addRecipe(new ItemStack(Blocks.SAND, 1), getModItem(RES_DD.with("material"), 4, 1), 524);

		new PrecisionAssemblerRecipe(
				IIContent.itemMaterialNugget.getStack(MaterialsNugget.SILICON), ItemStack.EMPTY,
				new IngredientStack[]{new IngredientStack(getModItem(RES_DD.with("material"), 1, 1))},
				new String[]{"hammer"},
				new String[]{"hammer work main", "hammer work main", "hammer work main"},
				8192, 0.75f
		);

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
