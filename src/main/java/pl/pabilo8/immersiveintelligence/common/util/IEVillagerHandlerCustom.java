package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.common.util.IEVillagerHandler;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

import java.lang.reflect.Field;
import java.util.List;

public class IEVillagerHandlerCustom extends IEVillagerHandler {

	// Initialize trades with instance method
	public static void initIEVillagerTrades() {
		// Call parent initialization method if needed
		IEVillagerHandler.initIEVillagerTrades();

		// Retrieve the profession and add career if it doesn't already exist
		VillagerRegistry.VillagerProfession engineerProfession = IEVillagerHandler.PROF_ENGINEER;
		if (engineerProfession != null) {
			boolean careerExists = false;

			try {
				// Access private "careers" field using reflection
				Field careersField = VillagerRegistry.VillagerProfession.class.getDeclaredField("careers");
				careersField.setAccessible(true);
				List<VillagerRegistry.VillagerCareer> careers = (List<VillagerRegistry.VillagerCareer>) careersField.get(engineerProfession);

				// Check if the "gunsmith" career already exists
				for (VillagerRegistry.VillagerCareer career : careers) {
					if ("immersiveengineering.gunsmith".equals(career.getName())) {
						careerExists = true;
						break;
					}
				}

				// Add career if it doesn't exist
				if (!careerExists) {
					VillagerRegistry.VillagerCareer careerGunsmith = new VillagerRegistry.VillagerCareer(engineerProfession, "immersiveengineering.gunsmith");

					// Add a custom trade for iron barrel replacement
					careerGunsmith.addTrade(1, new EntityVillager.ITradeList[]{
							new ReplaceSteelBarrelWithIron()
					});
				}
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	// Custom trade class for replacing steel barrel with iron barrel
	private static class ReplaceSteelBarrelWithIron implements EntityVillager.ITradeList
	{
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{
			// Retrieve OreDict items
			ItemStack steelBarrelStack = getOreDictStack("gunbarrelSteel");
			ItemStack ironBarrelStack = getOreDictStack("gunbarrelIron");

			// Check that both items exist before adding the trade
			if(!steelBarrelStack.isEmpty()&&!ironBarrelStack.isEmpty())
			{
				// Remove existing trade if it matches the trade you want to replace
				recipeList.removeIf(recipe ->
						recipe.getItemToBuy().isItemEqual(new ItemStack(Items.EMERALD, 4))&&
								recipe.getSecondItemToBuy().isItemEqual(ironBarrelStack)
				);

				// Add the new trade with iron barrel and set the price
				recipeList.add(new MerchantRecipe(
						new ItemStack(Items.EMERALD, 4), // 4 emeralds price
						ironBarrelStack
				));
			}
		}

		// Retrieve an item from the Ore Dictionary
		private ItemStack getOreDictStack(String oreDictName)
		{
			if(OreDictionary.doesOreNameExist(oreDictName)&&!OreDictionary.getOres(oreDictName).isEmpty())
			{
				return OreDictionary.getOres(oreDictName).get(0).copy();
			}
			return ItemStack.EMPTY;
		}
	}
}