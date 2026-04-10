package pl.pabilo8.immersiveintelligence.common.compat.dd;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
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
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialIngot.MaterialsIngot;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
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

	private static final ResLoc RES_DD = ResLoc.of(ResLoc.root("deeperdepths"));

	@Override
	public void preInit()
	{
		//08.04.2026 Carver: Ammo compat added.

		addShrapnel("amethyst", IIColor.fromPackedRGB(0xba73de),
				RES_DD.with("amethyst_block"), 1, 0.08f, 0.1f);

		//wind charge ammo component registry

		AmmoRegistry.registerComponent(DeeperDepthsHelper.ammoComponentWindCharge);
		AmmoComponent ammoComponentWindCharge = new ammoComponentWindCharge();

		//For recipes
		Item amethystblock1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "amethyst_block"));
		OreDictionary.registerOre("amethystblock", new ItemStack(amethystblock1));
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
						new ItemStack(trapdoor1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(rod1, 1, 0),
						new ItemStack(rod1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 256, 80, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 0),
						new ItemStack(cutcopperslab1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 0),
						new ItemStack(cut_copper_stairs, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 110, false);

				new BathingRecipe(new ItemStack(grate1, 1, 0),
						new ItemStack(grate1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 80, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 0),
						new ItemStack(chiseledcopper1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 0),
						new ItemStack(door1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 256, 100, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 0),
						new ItemStack(bulb1, 1), new FluidStack(IIContent.fluidHydrofluoricAcid, 100), 512, 140, false);


				//Adding coating with sulfuric acid, metadata sensitive

				//normal

				new BathingRecipe(new ItemStack(bulb1, 1, 4),
						new ItemStack(ddcopper1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 4),
						new ItemStack(bulb1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 140, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 4),
						new ItemStack(trapdoor1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(rod1, 1, 4),
						new ItemStack(rod1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 80, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 4),
						new ItemStack(cutcopperslab1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 100, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 4),
						new ItemStack(cut_copper_stairs, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 110, false);

				new BathingRecipe(new ItemStack(grate1, 1, 4),
						new ItemStack(grate1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 80, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 4),
						new ItemStack(chiseledcopper1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(door1, 1, 4),
						new ItemStack(door1, 1,0), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 100, false);

				//exposed

				new BathingRecipe(new ItemStack(bulb1, 1, 5),
						new ItemStack(ddcopper1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 140, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 5),
						new ItemStack(bulb1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 160, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 5),
						new ItemStack(trapdoor1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 120, false);

				new BathingRecipe(new ItemStack(rod1, 1, 5),
						new ItemStack(rod1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 100, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 5),
						new ItemStack(cutcopperslab1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 120, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 5),
						new ItemStack(cut_copper_stairs, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 130, false);

				new BathingRecipe(new ItemStack(grate1, 1, 5),
						new ItemStack(grate1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 100, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 5),
						new ItemStack(chiseledcopper1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 140, false);

				new BathingRecipe(new ItemStack(door1, 1, 5),
						new ItemStack(door1, 1,1), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 120, false);

				//wethered

				new BathingRecipe(new ItemStack(bulb1, 1, 6),
						new ItemStack(ddcopper1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 160, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 6),
						new ItemStack(bulb1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 180, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 6),
						new ItemStack(trapdoor1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 140, false);

				new BathingRecipe(new ItemStack(rod1, 1, 6),
						new ItemStack(rod1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 140, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 6),
						new ItemStack(cutcopperslab1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 140, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 6),
						new ItemStack(cut_copper_stairs, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 150, false);

				new BathingRecipe(new ItemStack(grate1, 1, 6),
						new ItemStack(grate1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 120, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 6),
						new ItemStack(chiseledcopper1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 160, false);

				new BathingRecipe(new ItemStack(door1, 1, 6),
						new ItemStack(door1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 140, false);

				//oxidized

				new BathingRecipe(new ItemStack(bulb1, 1, 7),
						new ItemStack(ddcopper1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 180, false);

				new BathingRecipe(new ItemStack(bulb1, 1, 7),
						new ItemStack(bulb1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 200, false);

				new BathingRecipe(new ItemStack(trapdoor1, 1, 7),
						new ItemStack(trapdoor1, 1,2), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 160, false);

				new BathingRecipe(new ItemStack(rod1, 1, 7),
						new ItemStack(rod1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 160, false);

				new BathingRecipe(new ItemStack(cutcopperslab1, 1, 7),
						new ItemStack(cutcopperslab1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 160, false);

				new BathingRecipe(new ItemStack(cut_copper_stairs, 1, 7),
						new ItemStack(cut_copper_stairs, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 170, false);

				new BathingRecipe(new ItemStack(grate1, 1, 7),
						new ItemStack(grate1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 140, false);

				new BathingRecipe(new ItemStack(chiseledcopper1, 1, 7),
						new ItemStack(chiseledcopper1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 512, 180, false);

				new BathingRecipe(new ItemStack(door1, 1, 7),
						new ItemStack(door1, 1,3), new FluidStack(IIContent.fluidSulfuricAcid, 100), 256, 160, false);

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
