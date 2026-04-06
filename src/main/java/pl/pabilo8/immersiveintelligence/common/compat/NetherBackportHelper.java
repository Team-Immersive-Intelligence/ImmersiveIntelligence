package pl.pabilo8.immersiveintelligence.common.compat;


import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;

/**
 * @author Carver (carver@iiteam.net)
 * @since 05.04.2026
 */
public class NetherBackportHelper extends IICompatModule

{
	@Override
	public String getName()
	{
		return "nb";
	}

	@Override
	public void preInit()
	{

	}

	@Override
	public void registerRecipes()
	{

		//Unseen's Nether Backport (nb:) (nb:netherite_ore = ancient debris = oreNetherite) ( nb:nether_gold_ore = oreNethergold) (minecraft:magma = magma block)

		MineralMix mineralDigsite = ExcavatorHandler.addMineral("Archaic digsite", 30, .45f, new String[]{"black_stone_bricks", "polished_black_stone", "oreNetherite", "gilded_blackstone"}, new float[]{.4f, .3f, .2f, .1f});
		MineralMix mineralLavatube = ExcavatorHandler.addMineral("Cooled lava tube", 30, .45f, new String[]{"magma", "smooth_basalt", "obsidian"}, new float[]{.5f, .3f, .2f});
		MineralMix mineralMephitic = ExcavatorHandler.addMineral("Mephitic quazite", 30, .45f, new String[]{"oreQuartz", "oreNethergold", "dustSulfur"}, new float[]{.6f, .2f, .2f});
		mineralDigsite.dimensionWhitelist = new int[]{-1};
		mineralLavatube.dimensionWhitelist = new int[]{-1};
		mineralMephitic.dimensionWhitelist = new int[]{-1};

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
