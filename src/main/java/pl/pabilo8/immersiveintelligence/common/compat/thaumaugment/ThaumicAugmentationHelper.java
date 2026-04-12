package pl.pabilo8.immersiveintelligence.common.compat.thaumaugment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.thaum.*;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

public class ThaumicAugmentationHelper extends IICompatModule

/**
 * @author Carver (carver@iiteam.net)
 * @since 12.04.2026
 */

{

	private static AmmoPropellant AmmoPropellantImpetus;
	private static AmmoComponent AmmoComponentImpetus;

	@Override
	public String getName()
	{
		return "thaumicaugmentation";
	}

	private static final ResLoc RES_TC = ResLoc.of(ResLoc.root("thaumicaugmentation"));

	@Override
	public void preInit()
	{

		AmmoPropellant AmmoPropellantImpetus = new AmmoPropellantImpetus();
		AmmoComponent AmmoComponentImpetus = new AmmoComponentImpetus();

		AmmoRegistry.registerPropellant(pl.pabilo8.immersiveintelligence.common.compat.thaumaugment.ThaumicAugmentationHelper.AmmoPropellantImpetus);
		AmmoRegistry.registerComponent(ThaumicAugmentationHelper.AmmoComponentImpetus);

		Item impetus = Item.REGISTRY.getObject(new ResourceLocation("thaumicaugmentation", "material_impetus_cell"));

		OreDictionary.registerOre("impetus", new ItemStack(impetus));
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

