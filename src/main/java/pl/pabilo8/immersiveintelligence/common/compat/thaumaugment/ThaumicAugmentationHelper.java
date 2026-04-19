package pl.pabilo8.immersiveintelligence.common.compat.thaumaugment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 16.04.2026
 * @since 12.04.2026
 */
public class ThaumicAugmentationHelper extends IICompatModule
{
	private static AmmoPropellant ammoPropellantImpetus;
	private static AmmoComponent ammoComponentImpetus;

	@Override
	public String getName()
	{
		return "thaumicaugmentation";
	}

	private static final ResLoc RES_TC = ResLoc.of(ResLoc.root("thaumicaugmentation"));

	@Override
	public void preInit()
	{
		AmmoRegistry.registerPropellant(ammoPropellantImpetus = new AmmoPropellantImpetus());
		AmmoRegistry.registerComponent(ammoComponentImpetus = new AmmoComponentImpetus());

		Item impetus = Item.REGISTRY.getObject(RES_TC.with("material"));
		//material_impetus_cell
		OreDictionary.registerOre("impetus", new ItemStack(impetus, 1, 3));
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
