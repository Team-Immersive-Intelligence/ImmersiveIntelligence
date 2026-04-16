package pl.pabilo8.immersiveintelligence.common.compat.nb;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.thaumaugment.AmmoComponentImpetus;
import pl.pabilo8.immersiveintelligence.common.compat.thaumaugment.ThaumicAugmentationHelper;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @since 05.04.2026
 * @updated 12.04.2026
 */
public class NetherBackportHelper extends IICompatModule

{

	private static AmmoCore AmmoCoreNetherite;
	private static AmmoComponent AmmoComponentNetherFire;

	private static final ResLoc RES_NB = ResLoc.of(ResLoc.root("nb"));

	@Override
	public String getName()
	{
		return "nb";

	}

	@Override
	public void preInit()
	{
		//12.04.26 Carver: added netherite ammo core and shrapnel


		AmmoComponent AmmoComponentNetherFire = new AmmoComponentNetherFire();

		AmmoCore AmmoCoreNetherite = new AmmoCoreNetherite();
		AmmoRegistry.registerCore(NetherBackportHelper.AmmoCoreNetherite);

		AmmoRegistry.registerComponent(NetherBackportHelper.AmmoComponentNetherFire);

		Item netherite = Item.REGISTRY.getObject(new ResourceLocation("nb", "netherite_ingot"));
		Item soul_lantern = Item.REGISTRY.getObject(new ResourceLocation("nb", "soul_lantern"));

		OreDictionary.registerOre("ingotNetherite", new ItemStack(netherite));
		OreDictionary.registerOre("soul_lantern", new ItemStack(soul_lantern));


		addShrapnel("netherite", IIColor.fromPackedRGB(0x31292a),
				RES_NB.with("netherite_block"), 8, 0.50f, 0f);

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

