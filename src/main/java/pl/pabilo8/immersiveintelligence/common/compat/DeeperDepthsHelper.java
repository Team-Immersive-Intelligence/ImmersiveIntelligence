package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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

	//TODO 06.04.2026 : add oxidized, weathered, etc. varients to it. Oxidized ones are to produce slag as well.
	//	(none); exposed_; weathered_ ; oxidized_ with waxed_ coming before it too.

	{
		Item trapdoor1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "copper_trapdoor"));
		Item bulb1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "copper_bulb"));
		Item door1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "copper_door"));
		Item cutcopper1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "cut_copper"));
		Item grate1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "copper_grate"));
		Item chiseledc1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "chiseled_copper"));
		Item ccslab1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "cut_copper_slab"));
		Item ccstairs1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "cut_copper_stairs"));
		Item lightningrod = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "lightning_rod"));

		//06.04.2026 Carver
		//For item material look in IEContent.
		//For outputs: first is output, then item that is put in, then cost in time and energy

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 3, 0),
				new ItemStack(trapdoor1), ItemStack.EMPTY, 200, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 7, 0),
				new ItemStack(bulb1), ItemStack.EMPTY, 200, 1400);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 2, 0),
				new ItemStack(door1), ItemStack.EMPTY, 200, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 9, 0),
				new ItemStack(cutcopper1), ItemStack.EMPTY, 200, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 9, 0),
				new ItemStack(grate1), ItemStack.EMPTY, 200, 1000);

		//				new ItemStack(grate1), (IEContent.itemMaterial, 1, 7), 200, 1000);    for slag

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 9, 0),
				new ItemStack(chiseledc1), ItemStack.EMPTY, 200, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 5, 0),
				new ItemStack(ccslab1), ItemStack.EMPTY, 200, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 5, 0),
				new ItemStack(ccstairs1), ItemStack.EMPTY, 200, 1000);

		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMaterial, 3, 0),
				new ItemStack(lightningrod), ItemStack.EMPTY, 100, 800);

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
