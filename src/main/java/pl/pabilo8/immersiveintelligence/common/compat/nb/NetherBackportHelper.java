package pl.pabilo8.immersiveintelligence.common.compat.nb;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 12.04.2026
 * @since 05.04.2026
 */
public class NetherBackportHelper extends IICompatModule
{
	private static AmmoCore ammoCoreNetherite;
	private static AmmoComponent ammoComponentNetherFire;

	public static final ResLoc RES_NB = ResLoc.of(ResLoc.root("nb"));

	@Override
	public String getName()
	{
		return "nb";
	}

	@Override
	public void preInit()
	{
		//12.04.26 Carver: added netherite ammo core and shrapnel

		AmmoRegistry.registerCore(ammoCoreNetherite = new AmmoCoreNetherite());
		AmmoRegistry.registerComponent(ammoComponentNetherFire = new AmmoComponentNetherFire());

		Item netherite = Item.REGISTRY.getObject(RES_NB.with("netherite_ingot"));
		Item soulLantern = Item.REGISTRY.getObject(RES_NB.with("soul_lantern"));

		OreDictionary.registerOre("ingotNetherite", new ItemStack(netherite));
		OreDictionary.registerOre("soul_lantern", new ItemStack(soulLantern));


		addShrapnel("netherite", IIColor.fromPackedRGB(0x31292a),
				RES_NB.with("netherite_block"), 8, 0.50f, 0f);

	}

	@Override
	public void registerRecipes()
	{
		//Unseen's Nether Backport (nb:) (nb:netherite_ore = ancient debris = oreNetherite) ( nb:nether_gold_ore = oreNethergold) (minecraft:magma = magma block)
		OreDictionary.registerOre("oreNetherite", Item.REGISTRY.getObject(RES_NB.with("netherite_ore")));
		OreDictionary.registerOre("oreGold", Item.REGISTRY.getObject(RES_NB.with("nether_gold_ore")));
		OreDictionary.registerOre("oreGoldNether", Item.REGISTRY.getObject(RES_NB.with("nether_gold_ore")));
		OreDictionary.registerOre("magma", Blocks.MAGMA);

		OreDictionary.registerOre("blockBasalt", Item.REGISTRY.getObject(RES_NB.with("basalt")));
		OreDictionary.registerOre("blockBasaltSmooth", Item.REGISTRY.getObject(RES_NB.with("polished_basalt")));
		OreDictionary.registerOre("blockBasaltPolished", Item.REGISTRY.getObject(RES_NB.with("smooth_basalt")));

		OreDictionary.registerOre("blockBlackstone", Item.REGISTRY.getObject(RES_NB.with("black_stone")));
		OreDictionary.registerOre("blockBlackstoneSmooth", Item.REGISTRY.getObject(RES_NB.with("polished_black_stone")));
		OreDictionary.registerOre("blockBlackstonePolished", Item.REGISTRY.getObject(RES_NB.with("smooth_black_stone")));
		OreDictionary.registerOre("blockBlackstoneGilded", Item.REGISTRY.getObject(RES_NB.with("gilded_blackstone")));
		OreDictionary.registerOre("bricksBlackstone", Item.REGISTRY.getObject(RES_NB.with("black_stone_bricks")));

		MineralMix mineralDigsite = ExcavatorHandler.addMineral("Archaic Digsite", 30, .45f, new String[]{"bricksBlackstone", "blockBlackstoneSmooth", "oreNetherite", "blockBlackstoneGilded"}, new float[]{.4f, .3f, .2f, .1f});
		MineralMix mineralLavatube = ExcavatorHandler.addMineral("Cooled Lava Tube", 30, .45f, new String[]{"magma", "blockBasaltSmooth", "obsidian"}, new float[]{.5f, .3f, .2f});
		MineralMix mineralMephitic = ExcavatorHandler.addMineral("Mephitic Quarzite", 30, .45f, new String[]{"oreQuartz", "oreGold", "dustSulfur"}, new float[]{.6f, .2f, .2f});
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

