package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

/**
 * * @author Carver (carver@iiteam.net)
 * @since 16.04.2026
 * @updated 19.04.2026
 */


public class ScapeAndRunParasitesHelper extends IICompatModule

{
	@Override
	public String getName()
	{
		return "srparasites";
	}

	public static AmmoCore AmmoCoreVileShell;
	public static AmmoCore AmmoCoreLongarmShellFrag;

	public static AmmoCore AmmoCoreBeckonShell;
	public static AmmoCore AmmoCoreBolsterShell;
	public static AmmoCore AmmoCoreDispatcherShell;
	public static AmmoCore AmmoCoreHivescrap;

	public static AmmoComponent AmmoComponentManducaterCore;
	public static AmmoComponent AmmoComponentSummonerCore;

	public static AmmoComponent AmmoComponentInfester;

	public static AmmoComponent AmmoComponentSerratedPayload;

	public static AmmoComponent AmmoComponentParasiticgas;

	public static final ResLoc RES_SRP = ResLoc.of(ResLoc.root("srparasites"));

	@Override

	public void preInit()
	{
		Item vileshell = Item.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("vile_shell")); //described as extremely resistant shell
		Item longarmshell = Item.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("ada_longarms_drop")); //two are used for the vile shell above.
		Item bolstershell = Item.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with( "ada_bolster_drop"));
		Item beckonshell = Item.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with( "beckon_drop"));
		Item dispatchershell = Item.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("dispatcher_drop"));

		Item hive_scrap = Item.REGISTRY.getObject(new ResourceLocation("srparasites", "hive_scrap"));
		//lategame resource. Obtained from meteors or Preeminent parasites or from biome. Notably hard and dangerous to obtain as it is either random RNG or having to fight Preeminents.

		OreDictionary.registerOre("vileshell", new ItemStack(vileshell));
		OreDictionary.registerOre("longarmshell", new ItemStack(longarmshell));
		OreDictionary.registerOre("bolstershell", new ItemStack(bolstershell));
		OreDictionary.registerOre("beckonshell", new ItemStack(beckonshell));
		OreDictionary.registerOre("dispatchershell", new ItemStack(dispatchershell));
		OreDictionary.registerOre("hivescrap", new ItemStack(hive_scrap));

		AmmoRegistry.registerCore(AmmoCoreVileShell = new AmmoCoreVileShell());
		AmmoRegistry.registerCore(AmmoCoreLongarmShellFrag = new AmmoCoreLongarmShellFrag());
		AmmoRegistry.registerCore(AmmoCoreBeckonShell = new AmmoCoreBeckonShell());
		AmmoRegistry.registerCore(AmmoCoreBolsterShell = new AmmoCoreBolsterShell());
		AmmoRegistry.registerCore(AmmoCoreDispatcherShell = new AmmoCoreDispatcherShell());

		AmmoRegistry.registerCore(AmmoCoreHivescrap = new AmmoCoreHivescrap());

		AmmoRegistry.registerComponent(AmmoComponentManducaterCore = new AmmoComponentManducaterCore());
		AmmoRegistry.registerComponent(AmmoComponentSummonerCore = new AmmoComponentSummonerCore());
		AmmoRegistry.registerComponent(AmmoComponentInfester = new AmmoComponentInfester());
		AmmoRegistry.registerComponent(AmmoComponentSerratedPayload = new AmmoComponentSerratedPayload());
		AmmoRegistry.registerComponent(AmmoComponentParasiticgas = new AmmoComponentParasiticgas());

//17.04.2026 TODO: hazmat protection, block placement template to make some shells spread infestation.
// 	parasitecanister (meta 0, 2 - items for parasitic bombs, separate). 		//infestremain?
//
// 16.04.2026 TODO: make it cancel application effect event of COTH and other contaminants.
// Scape and run potion id's SRPPotions: viral, coth, corrosive, conta. For "overheating" potion - make a clone of the end result code and replace radiation with protectsFromHeat
// potential: BLEED_E (bleed), CORRO_E, CONTA_E, COTH_E

	}

	@Override
	public void registerRecipes()
	{
		//turn hivesteel block into 1 steel ingot. Hivesteel is made when biome infests metal blocks, most commonly iron. Not OP as you waste 8 other ingots and already let biome progress.
		Item hivesteel = Item.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("parasite_rubble"));
		ArcFurnaceRecipe.addRecipe(new ItemStack(IEContent.itemMetal, 1, 8),
				new ItemStack(hivesteel,1,6), ItemStack.EMPTY, 4000, 800);

		//TODO: Recipes for processing the components. Wait for chemical reactor for turning meat into biodiesel.

	}

	@Override
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	//Whether the Light Engineer Armor is worn
	public static boolean gotProtection;

	@SubscribeEvent
	//24.04.2026 Carver: added hazmat+gasmask protection.

	//Cancels some of s&r effects if full set of hazmat and gasmask are on.

	public void onPotionApplicable(PotionApplicableEvent event, EntityLivingBase entity)
	{
		if(entity==null)
			return;

		if(event.getPotionEffect().getPotion().getRegistryName().equals(ResLoc.of("srparasites:coth")))
		{
			if(gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
					"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
					"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
					"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion().getRegistryName().equals(ResLoc.of("srparasites:viral")))
		{
			if(gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion().getRegistryName().equals(ResLoc.of("srparasites:corrosive")))
		{
			if(gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion().getRegistryName().equals(ResLoc.of("srparasites:conta")))
		{
			if(gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion().getRegistryName().equals(ResLoc.of("srparasites:overheating")))
		{
			if(gotProtection==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
					"heatcoat"))
			{
				event.setCanceled(true);
			}
		}
	}

	@Override
	public void postInit()
	{

	}
}
