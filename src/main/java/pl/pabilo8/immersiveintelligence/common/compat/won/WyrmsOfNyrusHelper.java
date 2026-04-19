package pl.pabilo8.immersiveintelligence.common.compat.won;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @since 13.04.2026
 * @updated 19.04.2026
 */

public class WyrmsOfNyrusHelper extends IICompatModule
	
{
	@Override
	public String getName()
	{
		return "wyrmsofnyrus";
	}

	public static AmmoComponent AmmoComponentCreepshard;
	public static AmmoComponent AmmoComponentWyrmArmorFrag;
	public static AmmoComponent AmmoComponentCreepBulb;
	public static AmmoComponent AmmoComponentCorium;
	public static AmmoComponent AmmoComponentHivelight;
	public static AmmoComponent AmmoComponentCreepInfestor;
	public static AmmoCore AmmoCoreCreepedBone;
	public static AmmoCore AmmoCoreWyrmArmorFrag;
	public static AmmoCore AmmoCoreHivesteel;
	public static AmmoCore AmmoCoreBorgiplate;

	public static final ResLoc RES_WON = ResLoc.of(ResLoc.root("wyrmsofnyrus"));

	@Override
	public void preInit()
	{
		addShrapnel("creepshard", IIColor.fromPackedRGB(0xc16d28),
				RES_WON.with("hexpanels"), 5, 0.25f, 0f)
				.setDisruptsRadio(true);
				//hivepanels - also known as metal_comb

		addShrapnel("creepedbone", IIColor.fromPackedRGB(0xd7c192),
				RES_WON.with("creeplogtop"), 2, 0.35f, 0f);

		addShrapnel("creepedbone", IIColor.fromPackedRGB(0xd7c192),
				RES_WON.with("creeplogtop"), 2, 0.35f, 0f);

		ShrapnelHandler.addShrapnel("wyrm shell", IIColor.fromPackedRGB(0xe77e28),
				RES_WON.with("wyrmshellblock"), 7, 0.35f, 0f);
		//wyrmarmorfrag -  wyrmshellblock

		ShrapnelHandler.addShrapnel("hivesteel", IIColor.fromPackedRGB(0xc16d28),
				RES_WON.with("hivesteelblock"), 8, 0.45f, 0f)
				.setGoodVsUndead(true);
		//hivesteel - hivesteelblock

		ShrapnelHandler.addShrapnel("uraniumglass", IIColor.fromPackedRGB(0x97e636),
						RES_WON.with("uraniumcrystals"), 2, 0.05f, 1f)
				.setGoodVsUndead(true)
				.setDisruptsRadio(true);
		//uraniumcrystals (the block) - uraniumshard (the item)

		ShrapnelHandler.addShrapnel("borgiplate", IIColor.fromPackedRGB(0x1f0f4e),
						RES_WON.with("borgplating"), 2, 0.05f, 1f);
		//borgplating - for borgiplate. Heat-resistant metal. However, not too terribly sure of the usefullness of it as ammo core, where armor would have done better.

		Item creepedbone = Item.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with( "creepedbone"));
		OreDictionary.registerOre("creepedbone", new ItemStack(creepedbone));

		Item wyrmshell = Item.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with( "wyrmarmorfrag"));
		OreDictionary.registerOre("wyrmarmorfrag", new ItemStack(wyrmshell));

		Item hivesteel = Item.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with( "hivesteel"));
		OreDictionary.registerOre("ingotHivesteel", new ItemStack(hivesteel));

		Item borgiplate = Item.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("borgiplate"));
		OreDictionary.registerOre("borgiplate", new ItemStack(borgiplate));

		AmmoRegistry.registerComponent(AmmoComponentCreepInfestor = new AmmoComponentCreepInfestor());
		AmmoRegistry.registerComponent(AmmoComponentCreepshard = new AmmoComponentCreepshard());
		AmmoRegistry.registerComponent(AmmoComponentWyrmArmorFrag = new AmmoComponentWyrmArmorFrag());
		AmmoRegistry.registerComponent(AmmoComponentCreepBulb = new AmmoComponentCreepBulb());
		AmmoRegistry.registerComponent(AmmoComponentCorium = new AmmoComponentCorium());
		AmmoRegistry.registerComponent(AmmoComponentHivelight = new AmmoComponentHivelight());
		AmmoRegistry.registerCore(AmmoCoreWyrmArmorFrag = new AmmoCoreWyrmArmorFrag());
		AmmoRegistry.registerCore(AmmoCoreCreepedBone = new AmmoCoreCreepedBone());
		AmmoRegistry.registerCore(AmmoCoreHivesteel = new AmmoCoreHivesteel());
		AmmoRegistry.registerCore(AmmoCoreBorgiplate = new AmmoCoreBorgiplate());

// TODO 13.04.26: More in-depth compat. Advanced features that would require using wyrms' code.
		//utilize synlib as dependency API (after we update the build script). And then implement the following:
		// Wyrms have aura of IIradiation effect and be completely immune to radiation (may need a preInit meddling);
		//After certain size, wyrms that explode when dying with onFire state, should explode using II nuke explosive, adjusteable to the size in biome and effects. Like Grunts for example.
		//Possible armor plating for Light Engineer Armor.
	}

	@Override
	public void registerRecipes()
	{

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
