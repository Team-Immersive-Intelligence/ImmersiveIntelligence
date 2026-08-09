package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.addShrapnel;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 16.04.2026
 * @since 11.04.2026
 */
public class ThaumcraftHelper extends IICompatModule
{
	private static AmmoCore ammoCoreThaumium;
	private static AmmoCore ammoCoreVoidmetal;
	private static AmmoComponent ammoComponentPrimordialPearl;
	private static AmmoComponent ammoComponentBottledTaint;
	private static AmmoComponent ammoComponentAlumentum;

	private static AmmoPropellant ammoPropellantAlumentum;

	@Override
	public String getName()
	{
		return "thaumcraft";
	}

	public static final ResLoc RES_TC = ResLoc.of(ResLoc.root("thaumcraft"));

	@Override
	public void preInit()
	{
		addShrapnel("crystal", IIColor.fromPackedRGB(0x372645), 3, 0.05f, 0.5f);
		//crystal_essence

		addShrapnel("thaumium", IIColor.fromPackedRGB(0x372645), 5, 0.30f, 0.0f);

		addShrapnel("voidmetal", IIColor.fromPackedRGB(0x10081a), 7, 0.30f, 0.0f);

		AmmoRegistry.registerCore(ammoCoreThaumium = new AmmoCoreThaumium());
		AmmoRegistry.registerCore(ammoCoreVoidmetal = new AmmoCoreVoidmetal());

		AmmoRegistry.registerComponent(ammoComponentPrimordialPearl = new AmmoComponentPrimordialPearl());
		AmmoRegistry.registerComponent(ammoComponentBottledTaint = new AmmoComponentBottledTaint());
		AmmoRegistry.registerComponent(ammoComponentAlumentum = new AmmoComponentAlumentum());

		AmmoRegistry.registerPropellant(ammoPropellantAlumentum = new AmmoPropellantAlumentum());

		Item nugget = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "nugget"));
		Item alumentum = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "alumentum"));

		OreDictionary.registerOre("nuggetThaumium", new ItemStack(nugget, 1, 6));
		OreDictionary.registerOre("nuggetVoid", new ItemStack(nugget, 1, 7));
		OreDictionary.registerOre("alumentum", new ItemStack(alumentum));
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
