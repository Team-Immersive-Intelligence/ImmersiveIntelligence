package pl.pabilo8.immersiveintelligence.common.compat.ee;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.srp.AmmoCoreVileShell;
import pl.pabilo8.immersiveintelligence.common.compat.srp.ScapeAndRunParasitesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.won.*;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

public class EndExpansionHelper extends IICompatModule

/**
 * @author Carver (carver@iiteam.net)
 * @since 18.04.2026
 * @updated 18.04.2026
 * */

{
	@Override
	public String getName()
	{
		return "ee";
	}

	private static AmmoCore AmmoCoreCordium;

	private static AmmoComponent AmmoComponentPureCrystal;
	private static AmmoComponent AmmoComponentLustrianCrystal;
	private static AmmoComponent AmmoComponentVacousCrystal;
	private static AmmoComponent AmmoComponentInfusedCrystal;

	private static final ResLoc RES_EE = ResLoc.of(ResLoc.root("ee"));

	@Override
	public void preInit()
	{
		addShrapnel("lustrian_crystal", IIColor.fromPackedRGB(0xba3641),
				RES_EE.with("red_crystal"), 3, 0.05f, 0.5f)
				.setDisruptsRadio(true);
		//red_chunk
		addShrapnel("pure_crystal", IIColor.fromPackedRGB(0x9351b2),
				RES_EE.with("purple_crystal"), 3, 0.05f, 0.5f);
		//purple_chunk
		addShrapnel("vacous_crystal", IIColor.fromPackedRGB(0x0d7a1f),
				RES_EE.with("green_crystal"), 3, 0.05f, 0.5f)
				.setDisruptsRadio(true);
		//green_chunk

		Item cordium = Item.REGISTRY.getObject(new ResourceLocation("ee", "amber_ingot"));

		OreDictionary.registerOre("ingotCordium", new ItemStack(cordium));

		AmmoCore AmmoCoreCordium = new AmmoCoreCordium();

		AmmoComponent AmmoComponentPureCrystal = new AmmoComponentPureCrystal();
		AmmoComponent AmmoComponentLustrianCrystal = new AmmoComponentLustrianCrystal();
		AmmoComponent AmmoComponentVacousCrystal = new AmmoComponentVacousCrystal();
		AmmoComponent AmmoComponentInfusedCrystal = new AmmoComponentInfusedCrystal();

		AmmoRegistry.registerCore(EndExpansionHelper.AmmoCoreCordium);

		AmmoRegistry.registerComponent(EndExpansionHelper.AmmoComponentPureCrystal);
		AmmoRegistry.registerComponent(EndExpansionHelper.AmmoComponentLustrianCrystal);
		AmmoRegistry.registerComponent(EndExpansionHelper.AmmoComponentVacousCrystal);
		AmmoRegistry.registerComponent(EndExpansionHelper.AmmoComponentInfusedCrystal);
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
