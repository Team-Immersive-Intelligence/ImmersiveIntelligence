package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;

/**
 * @author Carver (carver@iiteam.net)
 * @since 05.04.2026
 */

public class DeeperDepthsHelper extends IICompatModule
{

	@Override
	public String getName()
	{
		return "deeperdepths";
	}

	@Override
	public void preInit()
	{

	}

	@Override
	public void registerRecipes()

	//07.04.2026 Carver, recipe reformat
	//For item material look in IEContent.
	//For outputs: first is output, then item that is put in, then cost in time and energy

	{
		String[] waxed = new String[]{"", "waxed_"};
		for(String varient2 : waxed)
		{
			String[] variants = new String[]{"", "exposed_", "weathered_", "oxidized_"};
			for(String variant1 : variants)
			{
				Item trapdoor1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_trapdoor"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 3, 0),
						new ItemStack(trapdoor1), ItemStack.EMPTY, 100, 800);
				Item rod1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"lightning_rod"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 3, 0),
						new ItemStack(rod1), ItemStack.EMPTY, 60, 600);
				Item cutcopperslab1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"cut_copper_slab"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 5, 0),
						new ItemStack(cutcopperslab1), ItemStack.EMPTY, 100, 500);
				Item cut_copper_stairs = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"cut_copper_stairs"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 5, 0),
						new ItemStack(cut_copper_stairs), ItemStack.EMPTY, 100, 800);
				Item cutcopper1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"cut_copper"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 9, 0),
						new ItemStack(cutcopper1), ItemStack.EMPTY, 100, 1000);
				Item grate1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_grate"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 9, 0),
						new ItemStack(grate1), ItemStack.EMPTY, 60, 500);
				Item chiseledcopper1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"chiseled_copper"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 9, 0),
						new ItemStack(chiseledcopper1), ItemStack.EMPTY, 100, 1000);
				Item door1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_door"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 2, 0),
						new ItemStack(door1), ItemStack.EMPTY, 100, 500);
				Item bulb1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper_bulb"));
				ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 7, 0),
						new ItemStack(bulb1), ItemStack.EMPTY, 200, 1000);

				//07.04.2026 Carver TODO: check if bath stripping oxidation layers and adding waxing works

				Item ddcopper1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", varient2+variant1+"copper"));

				//Cleaning oxidation (and apparently waxed state) with hydrofluoric acid

				new BathingRecipe(new ItemStack(ddcopper1, 1, 0),
						new ItemStack(ddcopper1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 0),
						new ItemStack(trapdoor1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(rod1, 1, 0),
						new ItemStack(rod1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 0),
						new ItemStack(cutcopperslab1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 0),
						new ItemStack(cut_copper_stairs, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(grate1, 1, 0),
						new ItemStack(grate1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 0),
						new ItemStack(chiseledcopper1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 0),
						new ItemStack(door1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 0),
						new ItemStack(bulb1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);


				//Adding coating with sulfuric acid, metadata sensitive

				//normal

				new BathingRecipe(new ItemStack(bulb1, 1, 4),
						new ItemStack(ddcopper1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 4),
						new ItemStack(bulb1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 4),
						new ItemStack(trapdoor1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(rod1, 1, 4),
						new ItemStack(rod1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 4),
						new ItemStack(cutcopperslab1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 4),
						new ItemStack(cut_copper_stairs, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(grate1, 1, 4),
						new ItemStack(grate1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 4),
						new ItemStack(chiseledcopper1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 4),
						new ItemStack(door1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				//exposed

				new BathingRecipe(new ItemStack(bulb1, 1, 5),
						new ItemStack(ddcopper1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 5),
						new ItemStack(bulb1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 5),
						new ItemStack(trapdoor1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(rod1, 1, 5),
						new ItemStack(rod1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 5),
						new ItemStack(cutcopperslab1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 5),
						new ItemStack(cut_copper_stairs, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(grate1, 1, 5),
						new ItemStack(grate1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 5),
						new ItemStack(chiseledcopper1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 5),
						new ItemStack(door1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				//wethered

				new BathingRecipe(new ItemStack(bulb1, 1, 6),
						new ItemStack(ddcopper1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 6),
						new ItemStack(bulb1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 6),
						new ItemStack(trapdoor1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(rod1, 1, 6),
						new ItemStack(rod1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 6),
						new ItemStack(cutcopperslab1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 6),
						new ItemStack(cut_copper_stairs, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(grate1, 1, 6),
						new ItemStack(grate1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 6),
						new ItemStack(chiseledcopper1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 6),
						new ItemStack(door1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				//oxidized

				new BathingRecipe(new ItemStack(bulb1, 1, 7),
						new ItemStack(ddcopper1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 7),
						new ItemStack(bulb1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 7),
						new ItemStack(trapdoor1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(rod1, 1, 7),
						new ItemStack(rod1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 7),
						new ItemStack(cutcopperslab1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 7),
						new ItemStack(cut_copper_stairs, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(grate1, 1, 7),
						new ItemStack(grate1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 7),
						new ItemStack(chiseledcopper1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 7),
						new ItemStack(door1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

			}
		}
		//				new ItemStack(*), (IEContent.itemMaterial, 1, 7), 200, 1000);    for slag (IE content)

		//deeper depths (deeperdepths:) (deeperdepths:stone refers to tuff)

		ExcavatorHandler.addMineral("Amethyst Crevasse", 15, .15f, new String[]{"amethyst_block", "calcite", "tuff"}, new float[]{.4f, .3f, .3f});
		ExcavatorHandler.addMineral("Ancient Seabed", 15, .15f, new String[]{"calcite", "cobblestone", "bone_block"}, new float[]{.65f, .3f, .05f});

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
