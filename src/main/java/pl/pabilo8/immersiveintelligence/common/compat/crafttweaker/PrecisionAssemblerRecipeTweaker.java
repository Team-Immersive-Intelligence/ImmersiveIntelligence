package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".PrecisionAssembler")
@ZenRegister
public class PrecisionAssemblerRecipeTweaker
{
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, IItemStack trash, IIngredient[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
	{
		Object[] adds = null;
		if(itemInputs!=null)
		{
			adds = new Object[itemInputs.length];
			for(int i = 0; i < itemInputs.length; i++)
				adds[i] = CraftTweakerHelper.toObject(itemInputs[i]);
		}

		if(adds==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add precision assembler recipe for "+itemOutput.getDisplayName()+", no valid inputs were provided");
			return;
		}
		if(tools==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add precision assembler recipe for "+itemOutput.getDisplayName()+", no tools were provided");
			return;
		}
		if(animations==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add precision assembler recipe for "+itemOutput.getDisplayName()+", no valid animations were provided");
			return;
		}

		CraftTweakerAPI.apply(new Add(CraftTweakerHelper.toStack(itemOutput), CraftTweakerHelper.toStack(trash), adds, tools, animations, energy, timeMultiplier));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack itemOutput)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(itemOutput)));
	}

	private static class Add implements IAction
	{
		private ItemStack itemOutput;
		private ItemStack trash;
		private Object[] itemInputs;
		private String[] tools;
		private String[] animations;
		private int energy;
		private float timeMultiplier;

		public Add(ItemStack itemOutput, ItemStack trash, Object[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
		{
			this.itemOutput = itemOutput;
			this.trash = trash;
			this.itemInputs = itemInputs;
			this.tools = tools;
			this.animations = animations;
			this.energy = energy;
			this.timeMultiplier = timeMultiplier;
		}

		@Override
		public void apply()
		{
			new PrecisionAssemblerRecipe(itemOutput, trash, itemInputs, tools, animations, energy, timeMultiplier);
		}

		@Override
		public String describe()
		{
			return "Adding precision Assembler Recipe for "+itemOutput.getUnlocalizedName();
		}
	}

	private static class Remove implements IAction
	{
		List<PrecisionAssemblerRecipe> removedRecipes;
		ItemStack searched;

		public Remove(ItemStack stack)
		{
			this.searched = stack;
		}

		@Override
		public void apply()
		{
			removedRecipes = IIMultiblockRecipe.removeRecipesByFilter(PrecisionAssemblerRecipe.class, recipe ->
					recipe.output.isItemEqual(searched)
			);
		}

		@Override
		public String describe()
		{
			return "Removing precision Assembler Recipe for "+searched.getUnlocalizedName();
		}
	}

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".PrecissionAssembler")
	@ZenRegister
	public static class OldAssemblerCompat
	{
		/**
		 * Added as a compat for the old CT methods
		 */
		@ZenMethod
		public static void addRecipe(IItemStack itemOutput, IItemStack trash, IIngredient[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
		{
			PrecisionAssemblerRecipeTweaker.addRecipe(itemOutput, trash, itemInputs, tools, animations, energy, timeMultiplier);
		}

		public static void removeRecipe(IItemStack itemOutput)
		{
			PrecisionAssemblerRecipeTweaker.removeRecipe(itemOutput);
		}
	}

}
