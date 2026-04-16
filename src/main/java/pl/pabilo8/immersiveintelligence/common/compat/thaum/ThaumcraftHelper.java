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

public class ThaumcraftHelper extends IICompatModule

/**
 * @author Carver (carver@iiteam.net)
 * @updated 16.04.2026
 * @since 11.04.2026
 */

{
	private static AmmoCore AmmoCoreThaumium;
	private static AmmoCore AmmoCoreVoidmetal;
	private static AmmoComponent AmmoComponentPrimordialPearl;
	private static AmmoComponent AmmoComponentBottledTaint;
	private static AmmoComponent AmmoComponentAlumentum;

	private static AmmoPropellant AmmoPropellantAlumentum;

	@Override
	public String getName()
	{
		return "thaumcraft";
	}

	private static final ResLoc RES_TC = ResLoc.of(ResLoc.root("thaumcraft"));

	@Override
	public void preInit()
	{

		addShrapnel("crystal", IIColor.fromPackedRGB(0x372645),
				RES_TC.with("crystal_ordo"), 3, 0.05f, 0.5f);
		//crystal_essence

		addShrapnel("thaumium", IIColor.fromPackedRGB(0x372645),
				RES_TC.with("metal_thaumium"), 5, 0.30f, 0.0f);

		addShrapnel("voidmetal", IIColor.fromPackedRGB(0x10081a),
				RES_TC.with("metal_void"), 7, 0.30f, 0.0f);


		AmmoCore AmmoCoreThaumium = new AmmoCoreThaumium();
		AmmoCore AmmoCoreVoidmetal = new AmmoCoreVoidmetal();

		AmmoComponent AmmoComponentPrimordialPearl = new AmmoComponentPrimordialPearl();
		AmmoComponent AmmoComponentBottledTaint = new AmmoComponentPrimordialPearl();
		AmmoComponent AmmoComponentAlumentum = new AmmoComponentAlumentum();

		AmmoPropellant AmmoPropellantAlumentum = new AmmoPropellantAlumentum();

		AmmoRegistry.registerCore(ThaumcraftHelper.AmmoCoreThaumium);
		AmmoRegistry.registerCore(ThaumcraftHelper.AmmoCoreVoidmetal);

		AmmoRegistry.registerComponent(ThaumcraftHelper.AmmoComponentPrimordialPearl);
		AmmoRegistry.registerComponent(ThaumcraftHelper.AmmoComponentBottledTaint);
		AmmoRegistry.registerComponent(ThaumcraftHelper.AmmoComponentAlumentum);

		AmmoRegistry.registerPropellant(ThaumcraftHelper.AmmoPropellantAlumentum);

		Item nugget = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "nugget"));
		Item alumentum = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "alumentum"));

		OreDictionary.registerOre("nuggetThaumium", new ItemStack(nugget,1,6));
		OreDictionary.registerOre("nuggetVoid", new ItemStack(nugget,1,7));
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
